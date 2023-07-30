/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 3
 * Class: CSI 4321
 *
 ************************************************/
package megex.app.server;

import megex.serialization.*;
import tls.TLSFactory;
import utils.Utilities;

import javax.net.ssl.SSLHandshakeException;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Main class to run mock MegEx server that sends HTML data.
 */
public class SimpleServer {
    /** Message factory to encode and decode messages */
    private static MessageFactory mf;
    /** HTML data to send to clients */
    private static final String data = "<html><head></head><body>" +
            "<h1>hi</h1></body></html>";

    /** Default constructor for JavaDoc */
    public SimpleServer(){
    }

    /**
     * Main method to run mock MegEx server.
     *
     * @param args unused (needed for main method)
     *
     * @throws Exception
     *   if server listening socket can not be created or IOException occurs
     */
    public static void main(String[] args) throws Exception {
        // Create message factory.
        mf = new MessageFactory();
        // Create a server socket to accept client connection requests.
        ServerSocket servSock = TLSFactory.getServerListeningSocket(8080,
                "keystore", "password");

        // Run forever, accepting and servicing connections.
        while (true) {
            try {
                // Get client connection
                Socket clntSock = TLSFactory.getServerConnectedSocket(
                        servSock);
                InputStream in = clntSock.getInputStream();
                OutputStream out = clntSock.getOutputStream();
                System.out.println("Handling client at: " +
                        clntSock.getLocalAddress());
                // Create framer and deframer for connection.
                Framer framer = new Framer(out);
                Deframer deframer = new Deframer(in);
                // Send settings and window update.
                framer.putFrame(new byte[]{4, 0, 0, 0, 0, 0});
                framer.putFrame(mf.encode(new Window_Update(0, 983041)));
                // Skip client preface.
                in.skipNBytes(24);

                // Initialize end of connection with client to false.
                boolean isEnd = false;
                // Loop until end of connection with client.
                while (!isEnd) {
                    try {
                        // Get frame.
                        byte[] msgBytes = deframer.getFrame();
                        // Check if headers message received from client.
                        if (msgBytes[0] == 1) {
                            // Get stream id from headers message.
                            int streamID = Utilities.byte2int(Arrays.copyOfRange(
                                    msgBytes, 2, 6), 4);
                            // Send headers message to client with status of 200
                            //   (ok).
                            Headers headers = new Headers(streamID, false);
                            headers.addValue(":status", "200");
                            framer.putFrame(mf.encode(headers));
                            // Send HTML data to client.
                            framer.putFrame(mf.encode(new Data(streamID, true,
                                    data.getBytes(StandardCharsets.UTF_8))));
                            // Set end of connection with client to true.
                            isEnd = true;
                        }
                    } catch (BadAttributeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                // Close connection with client.
                clntSock.close();
            } catch(SSLHandshakeException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
