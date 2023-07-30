/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 6
 * Class: CSI 4321
 *
 ************************************************/
package jack.app.server;

import jack.serialization.*;
import jack.serialization.Error;
import utils.Constants;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import static utils.LogHelper.*;

/**
 * Main class to run Jack protocol server.
 */
public class Server {
    /** List of services for server to maintain to respond to messages */
    private static List<Service> services = new ArrayList<>();
    /** Server socket to accept client connection requests. */
    private static DatagramSocket sock = null;

    /** Default constructor for JavaDoc */
    public Server(){}

    /**
     * Main method to run Jack server protocol.
     *
     * @param args server port and paths operation and payload
     */
    public static void main(String[] args){
        // Data Abstraction
        int port;
        byte[] inBuffer = new byte[Constants.MAX_UDP];

        setupLog();

        // Check number of arguments is correct, terminate otherwise.
        if(args.length != 1){
            severe("Incorrect parameters!" +
                    "Run as Server.java <port>");
            return;
        }

        // Validate port number.
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e){
            severe("Incorrect parameters! " + e.getMessage());
            return;
        }

        // Create UDP socket.
        try {
            sock = new DatagramSocket(port);
        } catch (SocketException | IllegalArgumentException e) {
            severe("Communication problem: Unable to create listening socket!"
                    + e.getMessage());
            return;
        }

        while (true) {
            DatagramPacket packet = new DatagramPacket(inBuffer,
                    inBuffer.length);
            try {
                try{
                    // Get packet from socket.
                    sock.receive(packet);
                    // Convert packet to byte array.
                    byte[] encodedMsg = Arrays.copyOfRange(packet.getData(), 0,
                            packet.getLength());
                    // Decode byte array to message.
                    Message msg = Message.decode(encodedMsg);

                    // Switch on message opcode.
                    switch (msg.getOperation().charAt(0)) {
                        // If response, error, or ack, log and send error.
                        case Constants.OP_RESPONSE:
                        case Constants.OP_ERROR:
                        case Constants.OP_ACK:
                            String error = "Unexpected message type: " + msg;
                            warn(error);
                            sendMessage(sock, packet,
                                    new Error(error).encode());
                            break;
                        // If query, handle query.
                        case Constants.OP_QUERY:
                            handleQuery((Query) msg, packet);
                            break;
                        // If new handle new.
                        case Constants.OP_NEW:
                            handleNew((New) msg, packet);
                            break;
                    }
                } catch(IllegalArgumentException e) {
                        // If illegal argument exception, log and send error.
                        String message = "Invalid message: " + e.getMessage();
                        sendMessage(sock, packet, new Error(message).encode());
                        warn(message);
                }
            } catch (IOException e) {
                // If IOException, log error.
                warn("Communication problem: " + e.getMessage());
            }
        }
    }

    /**
     * Setup logging to file.
     */
    private static void setupLog(){
        Handler logFile = null;
        // Prevent logging to console
        Constants.SERVER_LOGGER.setUseParentHandlers(false);

        // Create file handler for logging, terminate if unable to log to
        //   specified log file.
        try {
            logFile = new FileHandler(Constants.JACK_LOG);
        } catch (IOException e) {
            severe("Unable to log to file: " + Constants.JACK_LOG
                    + "! Terminating!" + e.getMessage());
            System.exit(1);
        }
        // Set log to output plaintext.
        logFile.setFormatter(new SimpleFormatter());
        logFile.setLevel(Level.ALL);
        // Add file handler to logger and set appropriate level.
        Constants.SERVER_LOGGER.addHandler(logFile);
        Constants.SERVER_LOGGER.setLevel(Level.ALL);
    }

    /**
     * Send a UDP packet.
     *
     * @param sock the socket to send the packet over
     * @param packet the packet to send
     * @param msgBytes the data for the packet
     *
     * @throws IOException
     *     if the packet has an I/O error being sent
     */
    private static void sendMessage(DatagramSocket sock,
                                    DatagramPacket packet, byte [] msgBytes)
            throws IOException {
        // Set packet's new data and length.
        packet.setData(msgBytes, 0, msgBytes.length);
        // Send packet.
        sock.send(packet);
    }

    /**
     * Handle Jack server protocol for receiving a query.
     *
     * @param q query received
     * @param packet packet received
     *
     * @throws IOException
     *     if an I/O error occurs sending a response
     */
    private static void handleQuery(Query q, DatagramPacket packet)
            throws IOException {
        // Log message.
        log(q.toString());
        String search = q.getSearchString();
        // Create response object.
        Response ret = new Response();
        // If query is wildcard, add all services to response.
        if (search.equals("*")) {
            for (Service service : services) {
                ret.addService(service.getHost(), service.getPort());
            }
        // Else, add only matching services to response.
        } else {
            for (Service service : services) {
                if (service.getHost().contains(search)) {
                    ret.addService(service.getHost(), service.getPort());
                }
            }
        }
        // Send response back.
        sendMessage(sock, packet, ret.encode());
    }

    /**
     * Handle Jack server protocol for receiving a new message.
     *
     * @param n new message received
     * @param packet packet received
     *
     * @throws IOException
     *     if an I/O error occurs sending a response
     */
    private static void handleNew(New n, DatagramPacket packet)
            throws IOException {
        // Log message.
        log(n.toString());
        String host = n.getHost();
        int msgPort = n.getPort();
        // Create service from details.
        Service add = new Service(host, msgPort);
        // Add to service list if not a duplicate.
        if (!services.contains(add)) {
            services.add(add);
        }
        // Send ACK back.
        sendMessage(sock, packet, new ACK(host, msgPort).encode());
    }
}
