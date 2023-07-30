/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 4
 * Class: CSI 4321
 *
 ************************************************/
package jack.serialization;

import utils.Constants;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Syntax for ack message header and payload.
 */
public class ACK extends Message {
    /** Host/port pair for message */
    private Service service;

    /**
     * Construct ACK message with given details
     *
     * @param host the host ID
     * @param port the message port
     *
     * @throws IllegalArgumentException
     *   if invalid host or port
     */
    public ACK(String host, int port) throws IllegalArgumentException {
        this.service = new Service(host, port);
    }

    /**
     * Gets the message host
     *
     * @return the host id
     */
    public String getHost(){
        return this.service.getHost();
    }

    /**
     * Sets the message host
     *
     * @param host the host ID
     *
     * @return ack message with new host
     *
     * @throws IllegalArgumentException
     *    if invalid host
     */
    public ACK setHost(String host) throws IllegalArgumentException {
        this.service = this.service.setHost(host);
        return this;
    }

    /**
     * Gets the message port
     *
     * @return message port
     */
    public int getPort(){
        return this.service.getPort();
    }

    /**
     * Sets the message port
     *
     * @param port the message port
     *
     * @return ack message with new port
     *
     * @throws IllegalArgumentException
     *    if invalid port
     */
    public ACK setPort(int port) throws IllegalArgumentException {
        this.service.setPort(port);
        return this;
    }

    /**
     * Gets string representation of ACK message
     *
     * @return string representation of ack message
     */
    public String toString(){
        return "ACK [" + this.service + "]";
    }

    /**
     * Serializes the ACK message
     *
     * @return byte array representation of ack message
     */
    @Override
    public byte[] encode() {
        return ("A " + this.service).getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Gets the op code of the ACK message
     *
     * @return op code of ack message
     */
    @Override
    public String getOperation() {
        return String.valueOf(Constants.OP_ACK);
    }

    /**
     * Determines of this ACK message is equal to another
     *
     * @param o the other object to check equality with
     *
     * @return true if this ack message is equal to the other, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ACK ack = (ACK) o;
        return Objects.equals(service, ack.service);
    }

    /**
     * Gets hashcode of ACK message
     *
     * @return hashcode of ack message
     */
    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
