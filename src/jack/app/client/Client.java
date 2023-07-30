/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 5
 * Class: CSI 4321
 *
 ************************************************/
package jack.app.client;

import jack.serialization.*;
import jack.serialization.Error;
import utils.Constants;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Main class to run Jack protocol client.
 */
public class Client {
    /** Default constructor for JavaDoc */
    public Client(){}

    /**
     * Main method to run Jack protocol.
     *
     * @param args server port and paths operation and payload
     */
    public static void main(String [] args){
        // Write error and exit if not enough arguments.
        if(args.length < Constants.JACK_CLIENT_ARGS){
            System.err.println("Bad parameters: Usage: Client.java <Server>" +
                    " <Port> <Op> <payload>");
            System.exit(Constants.BAD_STATUS);
        }

        try {
            // Get server.
            InetAddress server = InetAddress.getByName(
                    args[Constants.JACK_SERVER]);
            // Get port.
            int port = Integer.parseInt(args[Constants.JACK_PORT]);
            // Validate port.
            if(port < Constants.MIN_PORT || port > Constants.MAX_PORT){
                throw new IllegalArgumentException(
                        "Invalid port! Must be between 1 and 65535!");
            }
            DatagramSocket sock = new DatagramSocket();
            // Set timeout for retransmission.
            sock.setSoTimeout(Constants.JACK_TIMEOUT);
            // Attempt to connect to server.
            sock.connect(server, port);
            // Validate message to send.
            Message msg = Message.decode((args[Constants.JACK_OP] + " " +
                    args[Constants.JACK_PAYLOAD]).getBytes(
                            StandardCharsets.US_ASCII));
            // Send message.
            sendMessage(sock, msg.encode());
            // Receive reply from server.
            receiveMessage(sock, msg, server, 0);
        } catch ( IllegalArgumentException | UnknownHostException e) {
            // Print error message and terminate.
            System.err.println("Bad parameters: " + e.getMessage());
            System.exit(Constants.BAD_STATUS);
        } catch (IOException e) {
            // Print error message and terminate.
            System.err.println("Communication problem: " + e);
            System.exit(Constants.BAD_STATUS);
        }

    }

    /**
     * Method to send Jack message over UDP socket.
     *
     * @param sock socket to send message over
     * @param msgBytes message to send
     *
     * @throws IOException
     *     if message cannot be sent
     */
    private static void sendMessage(DatagramSocket sock, byte [] msgBytes)
            throws IOException {
        DatagramPacket packet = new DatagramPacket(msgBytes, msgBytes.length);
        sock.send(packet);
    }

    /**
     * Method to get message from UDP socket.
     *
     * @param sock the socket to receive message from
     * @param sent the message that was sent to the server
     * @param server the server a message is expected from
     * @param tryNum the number of times retransmission has been attempted
     *
     * @throws IOException
     *     if I/O error occurs
     */
    private static void receiveMessage(DatagramSocket sock, Message sent,
                                       InetAddress server, int tryNum)
            throws IOException {
        Message msg = null;
        try {
            // Get packet from server.
            DatagramPacket packet = new DatagramPacket(
                    new byte[Constants.MAX_UDP], Constants.MAX_UDP);
            sock.receive(packet);
            // If packet from another server, print error and attempt to
            //   receive again.
            if(!packet.getAddress().equals(server)){
                System.err.println("Unexpected message source: "
                        + packet.getAddress());
                receiveMessage(sock, sent, server, tryNum);
                return;
            }
            // Convert packet to byte array.
            byte[] encodedMsg = Arrays.copyOfRange(packet.getData(), 0,
                    packet.getLength());
            // Decode byte array to message.
            msg = Message.decode(encodedMsg);
        } catch(SocketTimeoutException e){
            // If timeout and under maximum retransmissions, retransmit message
            //   and attempt to receive reply.
            if(tryNum < 3){
                sendMessage(sock, sent.encode());
                receiveMessage(sock, sent, server, tryNum + 1);
                return;
            }
            // If maximum number of retransmissions, throw exception leading to
            //   termination.
            else{
                throw new IOException("Socket timeout after maximum number" +
                        " of retransmissions!");
            }
        } catch(IllegalArgumentException e){
            // If invalid message received, print error and attempt to receive
            //   again.
            System.err.println("Invalid message: " + e.getMessage());
            receiveMessage(sock, sent, server, tryNum);
            return;
        }

        // Switch on message opcode.
        switch(msg.getOperation().charAt(0)){
            // For query and new messages, print that message was unexpected
            //   and attempt to receive again.
            case Constants.OP_QUERY:
            case Constants.OP_NEW:
                System.err.println("Unexpected message type");
                receiveMessage(sock, sent, server, tryNum);
                break;
            // For error message, print error and terminate.
            case Constants.OP_ERROR:
                System.err.println(((Error) msg).getErrorMessage());
                System.exit(Constants.BAD_STATUS);
                break;
            // For response message, if a query was sent, print response and
            //   return. Otherwise, print error and terminate.
            case Constants.OP_RESPONSE:
                if(sent.getOperation().charAt(0) == Constants.OP_QUERY){
                    System.out.println(msg);
                    return;
                }
                else{
                    System.err.println("Unexpected Response");
                    receiveMessage(sock, sent, server, tryNum);
                }
                break;
            // For ack message, if a new message was sent and the ack's host
            //   and port matches the new's host and port, print ack and
            //   return. Otherwise, print error and terminate.
            case Constants.OP_ACK:
                if(sent.getOperation().charAt(0) == Constants.OP_NEW &&
                        ((ACK) msg).getHost().equals(((New) sent).getHost()) &&
                        ((ACK) msg).getPort() == ((New) sent).getPort()){
                    System.out.println(msg);
                    return;
                }
                else{
                    System.err.println("Unexpected ACK");
                    receiveMessage(sock, sent, server, tryNum);
                }
                break;
        }
    }
}
