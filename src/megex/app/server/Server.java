/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 3
 * Class: CSI 4321
 *
 ************************************************/
package megex.app.server;

import tls.TLSFactory;
import utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.*;

import static utils.LogHelper.severe;
import static utils.LogHelper.warn;

/**
 * Main class to run mock MegEx server that sends HTML data.
 */
public class Server {
    /** Max size of data server can send in one frame. */
    public static final int MAXDATASSIZE = Constants.MAX_SIZE;

    /** Interval, in ms, between server sending data messages. */
    public static final int MINDATAINTERVAL = 100;

    /** Default constructor for JavaDoc */
    public Server(){
    }

    /**
     * Main method to run MegEx server.
     *
     * @param args port to run server on, number of threads for pool, and
     *            directory for file paths
     *
     */
    public static void main(String[] args) {
        // Data Abstraction
        ExecutorService executor;
        Handler logFile;
        int port;
        int numThreads;

        // Prevent logging to console
        Constants.SERVER_LOGGER.setUseParentHandlers(false);

        // Create file handler for logging, terminate if unable to log to
        //   specified log file.
        try {
            logFile = new FileHandler(Constants.LOG);
        } catch (IOException e) {
            severe("Unable to log to file: " + Constants.LOG
                    + "! Terminating!" + e.getMessage());
            return;
        }
        // Set log to output plaintext.
        logFile.setFormatter(new SimpleFormatter());
        logFile.setLevel(Level.ALL);
        // Add file handler to logger and set appropriate level.
        Constants.SERVER_LOGGER.addHandler(logFile);
        Constants.SERVER_LOGGER.setLevel(Level.ALL);

        // Check number of arguments is correct, terminate otherwise.
        if(args.length != 3){
            severe("Incorrect parameters!" +
                    "Run as Server.java <port> <numThreads> <rootDirectory>!");
            return;
        }

        // Get arguments.
        try {
            port = Integer.parseInt(args[0]);
            numThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            severe("Incorrect parameters! " + e.getMessage());
            return;
        }
        String path = args[2];

        // Create a server socket to accept client connection requests.
        ServerSocket servSock = null;
        // Get listening socket, terminate if unable to create.
        try {
            servSock = TLSFactory.getServerListeningSocket(port,
                    Constants.KEYSTORE, Constants.KEYSTORE_PASSWORD);
        } catch (Exception e) {
            severe("Unable to create listening socket! Terminating!"
                    + e.getMessage());
            return;
        }

        // Run forever, accepting and servicing connections.
        executor = Executors.newFixedThreadPool(numThreads);
        while (true) {
            // Get a client socket and execute the protocol on a thread in the
            //   pool, terminate client connection if unable to communicate.
            try {
                Socket clntSock = TLSFactory.getServerConnectedSocket(
                        servSock);
                executor.execute(() ->
                        new ServerProtocol(path, clntSock).call());
            } catch (IOException e){
                warn("Unable to get client socket!"
                        + " Terminating connection with client! "
                        + e.getMessage());
            }
        }
    }
}
