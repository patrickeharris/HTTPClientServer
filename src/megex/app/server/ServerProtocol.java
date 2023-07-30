/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 3
 * Class: CSI 4321
 *
 ************************************************/
package megex.app.server;

import megex.serialization.*;
import utils.Constants;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.LogHelper.log;
import static utils.LogHelper.warn;

/**
 * Protocol for handling client MegEx connections.
 */
public class ServerProtocol implements Callable<Integer> {
    /** Message factory for encoding and decoding messages */
    private static final MessageFactory mf = new MessageFactory();

    /** Root directory for files to serve */
    private final String root;

    /** Client being handled */
    private final Socket clntSock;

    /** Executor for thread to stream files */
    private final ExecutorService executor;

    /** Map of files being sent and the associated stream id */
    private static Map<Integer, InputStream> map = new HashMap<>();

    /** Framer for framing messages */
    private Framer framer;

    /**
     * Constructor for server protocol.
     *
     * @param root the directory to serve files from
     * @param clntSock the client being handled
     */
    public ServerProtocol(String root, Socket clntSock){
        this.root = root;
        this.clntSock = clntSock;
        this.executor = Executors.newSingleThreadExecutor();
    }


    /**
     * Method to handle a client socket with the HTTPv2 protocol.
     *
     * @return code for success or error
     */
    public Integer call() {
        try {
            // Hold stream id to detect duplicates.
            List<Integer> streamIDs = new ArrayList<>();
            // Get input and output stream.
            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();
            // Create framer and deframer for connection.
            framer = new Framer(out);
            Deframer deframer = new Deframer(in);
            // Send settings.
            try {
                framer.putFrame(mf.encode(new Settings()));
            }
            catch (BadAttributeException e){
            }
            // Set socket timeout.
            clntSock.setSoTimeout(Constants.TIMEOUT);
            // Check client preface, terminate client connection if incorrect.
            byte[] clientPreface = in.readNBytes(Constants.PREFACE.length());
            if (!new String(clientPreface).equals(Constants.PREFACE)) {
                warn("Bad preface: " + new String(clientPreface));
                clntSock.close();
                return 1;
            }
            // Loop until end of connection with client.
            while (!clntSock.isInputShutdown()) {
                try {
                    // Get message from client.
                    Message m = mf.decode(deframer.getFrame());
                    switch (m.getCode()) {
                        // Log data message as unexpected.
                        case Constants.DATA_CODE:
                            warn("Unexpected message: " + m);
                            break;
                        // Log settings and window update messages.
                        case Constants.SETTINGS_CODE:
                        case Constants.WINDOW_UPDATE_CODE:
                            log("Received message: " + m);
                            break;
                        case Constants.HEADERS_CODE:
                            // Get stream id from headers message.
                            int streamID = m.getStreamID();
                            // Validate stream id.
                            if (streamID <= 0 || streamID % 2 == 0) {
                                warn("Illegal stream ID: " + m);
                                break;
                            }
                            if (streamIDs.contains(streamID)) {
                                warn("Duplicate request: " + m);
                                break;
                            }
                            // Add to keep track of duplicate stream ids.
                            streamIDs.add(streamID);
                            Headers h = (Headers) m;
                            // Confirm path exists.
                            if (!h.getNames().contains(":path")) {
                                warn("No or bad path");
                                sendBadStatus(streamID, "400");
                                break;
                            }
                            // Validate path.
                            File file = new File(root
                                    + h.getValue(":path"));
                            if (!file.exists() || !file.canRead()) {
                                warn("File not found");
                                sendBadStatus(streamID, "404");
                                break;
                            }
                            if (file.isDirectory()) {
                                warn("Cannot request directory");
                                sendBadStatus(streamID, "403");
                                break;
                            }
                            // Send headers message to client with status of
                            //   200 (ok).
                            Headers headers = new Headers(streamID, false);
                            headers.addValue(":status", "200");
                            framer.putFrame(mf.encode(headers));
                            // Send data to client in parallel thread.
                            map.put(streamID, new FileInputStream(file));
                            executor.execute(this::sendFile);
                            break;
                    }
                } catch (BadAttributeException e) {
                    // Log if unable to parse.
                    warn("Unable to parse: " + e.getMessage());
                }
            }
        } catch (SocketTimeoutException e){
            // Log if client times out.
            warn("Client at " + clntSock.getRemoteSocketAddress()
                    + " has not communicated for 40 seconds!"
                    + " Terminating connection!");
        } catch (IOException e) {
            // Log if I/O problem occurs.
            warn("I/O exception occurred when communicating with client at "
                    + clntSock.getRemoteSocketAddress() +
                    "! Terminating connection! " + e.getMessage());
        } finally {
            try {
                // Terminate connection with client.
                clntSock.close();
            } catch (IOException e) {
                warn(e.getMessage());
            }
        }
        return 0;
    }

    /**
     * Method to send headers message with 404 status.
     *
     * @param streamID the stream id to send the headers message to
     * @param status status to send
     *
     * @throws BadAttributeException
     *     if headers message cannot be created
     * @throws IOException
     *     if frame cannot be sent
     */
    private void sendBadStatus(int streamID, String status)
            throws BadAttributeException, IOException {
        // Construct headers.
        Headers headers = new Headers(streamID, true);
        // Put 404 status.
        headers.addValue(":status", status);
        // Send frame and terminate connection with client.
        framer.putFrame(mf.encode(headers));
    }


    /**
     * Method to send file to a client.
     */
    private void sendFile(){
        // Initialize done to false.
        boolean done = false;
        // Loop until no more files to send.
        while(!done){
            // Loop through every stream to send a file on.
            for(int streamID : map.keySet()){
                byte [] buf;
                try {
                    // Read data to send from file.
                    buf = map.get(streamID).readNBytes(Server.MAXDATASSIZE);
                    // Send data in appropriate data message.
                    if(buf.length < Server.MAXDATASSIZE){
                        framer.putFrame(mf.encode(new Data(streamID, true,
                                buf)));
                        map.remove(streamID);
                    }
                    else{
                        framer.putFrame(mf.encode(new Data(streamID, false,
                                buf)));
                    }
                    // Sleep for minimum data interval.
                    Thread.sleep(Server.MINDATAINTERVAL);
                } catch (BadAttributeException | IOException |
                         InterruptedException e) {
                    warn(e.getMessage());

                    try {
                        map.clear();
                        clntSock.close();
                    } catch (IOException ex) {
                    }
                }
            }
            // If there are no more files to send, we are done.
            if(map.isEmpty()){
                done = true;
            }
        }
    }
}
