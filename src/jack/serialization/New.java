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
 * Syntax for new message header and payload.
 */
public class New extends Message{
    /** Host/port pair for message */
    private Service service;

    /**
     * Construct New message with given details
     *
     * @param host the host ID
     * @param port the message port
     *
     * @throws IllegalArgumentException
     *   if invalid host or port
     */
    public New(String host, int port) throws IllegalArgumentException {
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
     * @return new message with new host
     *
     * @throws IllegalArgumentException
     *    if invalid host
     */
    public New setHost(String host) throws IllegalArgumentException {
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
     * @return new message with new port
     *
     * @throws IllegalArgumentException
     *    if invalid port
     */
    public New setPort(int port) throws IllegalArgumentException {
        this.service.setPort(port);
        return this;
    }

    /**
     * Gets string representation of New message
     *
     * @return string representation of new message
     */
    public String toString(){
        return "NEW [" + this.service + "]";
    }

    /**
     * Serializes the New message
     *
     * @return byte array representation of new message
     */
    @Override
    public byte[] encode() {
        return ("N " + this.service).getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Gets the op code of the New message
     *
     * @return op code of new message
     */
    @Override
    public String getOperation() {
        return String.valueOf(Constants.OP_NEW);
    }

    /**
     * Determines of this New message is equal to another
     *
     * @param o the other object to check equality with
     *
     * @return true if this new message is equal to the other, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        New aNew = (New) o;
        return Objects.equals(service, aNew.service);
    }

    /**
     * Gets hashcode of New message
     *
     * @return hashcode of new message
     */
    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
