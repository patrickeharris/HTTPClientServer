/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 2
 * Class: CSI 4321
 *
 ************************************************/
package megex.app.client;

import megex.serialization.*;
import tls.TLSFactory;
import utils.Constants;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * Protocol to follow for HTTPv2 standard.
 */
public class ClientProtocol {
    /** Socket for client. */
    private static Socket socket = null;
    /** Framer object to frame messages. */
    private static Framer framer = null;
    /** Deframer object to deframe messages. */
    private static Deframer deframer = null;
    /** MessageFactory object to encode and decode messages. */
    private static final MessageFactory messageFactory = new MessageFactory();
    /** Next streamID to use. */
    private static int streamID = 1;
    /** Map of streamID to path of file. */
    private static Map<Integer, String> paths = new HashMap<>();
    /** Map of streamID to file output stream. */
    private static Map<Integer, FileOutputStream> outputStreams =
            new HashMap<>();

    /** Default constructor for JavaDoc */
    public ClientProtocol(){
    }

    /**
     * Create socket to server and port
     *
     * @param server the server to connect to
     * @param port the port to connect to
     *
     * @throws Exception
     *   if server cannot be reached or IO error occurs
     */
    public void createSocket(String server, Integer port) throws Exception {
        // Make socket and get associated streams.
        socket = TLSFactory.getClientSocket(server, port);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        // Initialize framer and deframer.
        framer = new Framer(out);
        deframer = new Deframer(in);
        // Send initial preface and settings.
        out.write(Constants.PREFACE.getBytes(StandardCharsets.US_ASCII));
        framer.putFrame(messageFactory.encode(new Settings()));
    }

    /**
     * Create stream for the given server and path
     *
     * @param server the server to contact
     * @param path the file on the server to get
     *
     * @throws BadAttributeException
     *   if headers cannot be created
     * @throws IOException
     *   if frame cannot be sent
     */
    public void createStream(String server, String path)
            throws BadAttributeException, IOException {
        // Create headers message for stream.
        Headers headers = new Headers(streamID, true);
        paths.put(streamID, path.replaceAll("/", "-"));
        streamID += 2;
        headers.addValue(":method", "GET");
        headers.addValue(":path", path);
        headers.addValue(":authority", server);
        headers.addValue(":scheme", "https");
        // Send headers message.
        framer.putFrame(messageFactory.encode(headers));
    }

    /**
     * Read data from server
     *
     * @param numStreams the number of streams that were opened
     *
     * @throws IOException
     *   if file cannot be written to
     */
    public void readStreams(int numStreams) throws IOException {
        boolean isEnd;
        // Loop through for each stream.
        for(int i = 0; i < numStreams; i++){
            isEnd = false;
            // Continue reading until the end stream flag is set.
            while(!isEnd) {
                // Go to next stream if server has closed stream.
                if(socket.isInputShutdown()){
                    break;
                }
                try {
                    // Get a message.
                    Message msg = messageFactory.decode(deframer.getFrame());
                    // Determine whether message has valid stream ID.
                    if((msg.getCode() == Constants.HEADERS_CODE ||
                            msg.getCode() == Constants.DATA_CODE) &&
                            !paths.containsKey(msg.getStreamID())){
                        System.err.println("Unexpected stream ID: " + msg);
                    }
                    else {
                        System.out.println("Received message: " + msg);
                        switch (msg.getCode()) {
                            case Constants.HEADERS_CODE:
                                Headers headers = (Headers) msg;
                                // Handle if headers is end of stream.
                                if (headers.isEnd()) {
                                    isEnd = true;
                                    paths.remove(msg.getStreamID());
                                }
                                // Determine if status is valid.
                                if (headers.getNames().contains(":status") &&
                                        headers.getValue(":status").charAt(0)
                                                != '2') {
                                    System.err.println("Bad status: "
                                            + headers.getValue(":status"));
                                    isEnd = true;
                                    paths.remove(msg.getStreamID());
                                }
                                else{
                                    // Open file for output if there will be data.
                                    if(!isEnd) {
                                        FileOutputStream fileOutputStream =
                                                new FileOutputStream(paths.get(
                                                        headers.getStreamID()),
                                                        false);
                                        outputStreams.put(headers.getStreamID()
                                                , fileOutputStream);
                                    }
                                }
                                break;
                            case Constants.DATA_CODE:
                                Data data = (Data) msg;
                                // Write data to file.
                                outputStreams.get(data.getStreamID()).write(
                                        data.getData());
                                // Send window update messages if data length
                                //   is positive.
                                if (data.getData().length > 0) {
                                    framer.putFrame(messageFactory.encode(
                                            new Window_Update(0,
                                                    data.getData().length)));
                                    framer.putFrame(messageFactory.encode(
                                            new Window_Update(
                                                    data.getStreamID(),
                                                    data.getData().length)));
                                }
                                // Handle if data is end of stream.
                                if (data.isEnd()) {
                                    isEnd = true;
                                    paths.remove(data.getStreamID());
                                    outputStreams.get(
                                            data.getStreamID()).close();
                                    outputStreams.remove(data.getStreamID());
                                }
                                break;
                        }
                    }
                } catch (BadAttributeException | EOFException
                         | IllegalArgumentException e) {
                    // Info message if unknown type, error otherwise.
                    if(e.getMessage().contains("Received unknown type:")){
                        System.out.println(e.getMessage());
                    }
                    else {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Close socket
     *
     * @throws IOException
     *     if socket cannot be closed
     */
    public void closeSocket() throws IOException {
        socket.close();
    }
}
