/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 4
 * Class: CSI 4321
 *
 ************************************************/
package jack.serialization;

import utils.Constants;
import utils.Utilities;

import java.nio.charset.StandardCharsets;

/**
 * Interface for message types.
 */
public abstract class Message {

    /**
     * Construct Message for JavaDoc
     */
    protected Message(){
    }

    /**
     * Decodes a message
     *
     * @param msgBytes the serialized message
     *
     * @return the decoded message
     *
     * @throws IllegalArgumentException
     *   if invalid message or problem decoding
     */
    public static Message decode(byte[] msgBytes)
            throws IllegalArgumentException {
        // Throw error if invalid.
        if(msgBytes == null || msgBytes.length > Constants.MAX_UDP ||
                msgBytes.length < Constants.JACK_HEADER ||
                msgBytes[1] != ' ' || !Utilities.validateBytes(msgBytes)){
            throw new IllegalArgumentException("Invalid message to decode!");
        }

        // Get op code and payload.
        Message ret = null;
        char op = (char) msgBytes[0];
        String payload = new String(Utilities.arraySubset(msgBytes,
                Constants.JACK_HEADER,
                msgBytes.length - Constants.JACK_HEADER),
                StandardCharsets.US_ASCII);

        switch (op){
            case Constants.OP_QUERY:
                ret = new Query(payload);
                break;
            case Constants.OP_RESPONSE:
                ret = createResponse(payload);
                break;
            case Constants.OP_NEW:
                // Get host and port from pair.
                ret = new New(getPart(payload, 0),
                        Integer.parseInt(getPart(payload, 1)));
                break;
            case Constants.OP_ACK:
                // Get host and port from pair.
                ret = new ACK(getPart(payload, 0),
                        Integer.parseInt(getPart(payload, 1)));
                break;
            case Constants.OP_ERROR:
                ret = new Error(payload);
                break;
            default:
                // Throw error for invalid op codes.
                throw new IllegalArgumentException(
                        "Message to decode has invalid op code! "
                                + op + " " + payload);
        }
        return ret;
    }

    /**
     * Serializes a message
     *
     * @return byte array representation of message
     */
    public abstract byte[] encode();

    /**
     * Gets the op code of a message
     *
     * @return op code of message
     */
    public abstract String getOperation();

    /**
     * Ensures a colon exists in a string
     *
     * @param pair string to check
     *
     * @throws IllegalArgumentException
     *   if no colon is found
     */
    private static void validateColon(String pair)
            throws IllegalArgumentException {
        // If there is no colon, or not enough characters for host/port pair,
        //   throw error.
        if(!pair.contains(":") || pair.length() < 3){
            throw new IllegalArgumentException(
                    "Message to decode has invalid payload! " + pair);
        }
    }

    /**
     * Gets a host or port from a string
     *
     * @param pair the host/port pair
     * @param part which part of the pair to get
     *
     * @return the host or port requested
     *
     * @throws IllegalArgumentException
     *   if no colon in pair
     */
    private static String getPart(String pair, int part)
            throws IllegalArgumentException {
        // Ensure there is a colon and return the part before or after the
        //   colon.
        validateColon(pair);
        String[] parts = pair.split(":");
        // Ensure there is stuff before and after the colon
        if(parts.length != 2){
            throw new IllegalArgumentException("Invalid host port pair!");
        }
        return parts[part];
    }

    /**
     * Validates response payload matches format from specification
     *
     * @param payload the response payload
     *
     * @throws IllegalArgumentException
     *   if payload is invalid
     */
    private static void validateResponse(String payload){
        // Validate response has correct format.
        if(!payload.matches(Constants.RESPONSE_FORMAT)){
            throw new IllegalArgumentException(
                    "Invalid response message! R " + payload);
        }
    }

    /**
     * Creates a response message from a payload
     *
     * @param payload the response payload
     *
     * @return the reponse message object
     *
     * @throws IllegalArgumentException
     *   if invalid host or port
     */
    private static Message createResponse(String payload){
        validateResponse(payload);
        Response response = new Response();
        // Get list of host/port pairs.
        String [] services = payload.split(Constants.SERVICE_SPLIT);
        // Add each host/port pair.
        for(String service : services){
            if(service.length() > 0){
                response.addService(getPart(service, 0),
                        Integer.parseInt(getPart(service, 1)));
            }
        }
        return response;
    }

}
