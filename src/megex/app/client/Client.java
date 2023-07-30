/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 2
 * Class: CSI 4321
 *
 ************************************************/
package megex.app.client;


import megex.serialization.BadAttributeException;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Main class to run HTTPv2 protocol.
 */
public class Client {
    /** Default constructor for JavaDoc */
    public Client(){
    }

    /**
     * Main method to run HTTPv2 protocol.
     *
     * @param args server port and paths to download
     */
    public static void main(String[] args){
        // Write error and exit if not enough arguments.
        if(args.length < 3){
            System.err.println("Usage: Client.java <Server> <Port> <Paths>...");
            System.exit(1);
        }
        // Get client protocol.
        ClientProtocol cp = new ClientProtocol();
        try {
            // Crate new socket to server.
            cp.createSocket(args[0], Integer.parseInt(args[1]));
            // Create streams for each path.
            for(String path : Arrays.stream(args).skip(2).toList()){
                cp.createStream(args[0], path);
            }
            // Read from server.
            cp.readStreams(args.length - 2);
            cp.closeSocket();
        } catch (BadAttributeException e) {
            // Print error and continue for BadAttributeExceptions.
            System.err.println(e.getMessage());
        } catch (Exception e) {
            // Print error and exit for unexpected exceptions.
            System.err.println(e);
            System.exit(1);
        }
    }
}
