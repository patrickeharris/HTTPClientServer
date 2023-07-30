/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization;

/**
 * Syntax for frame header and payload.
 */
public abstract class Message {
    /** Stream ID of message */
    protected int streamID;

    /**
     * Construct Message with the given details
     *
     * @param streamID the id of the stream
     *
     * @throws BadAttributeException
     *   if stream id is invalid
     */
    protected Message(int streamID) throws BadAttributeException {
        // Calls appropriate set stream id method.
        this.setStreamID(streamID);
    }

    /**
     * Gets the type of message
     *
     * @return the type of message
     */
    public abstract byte getCode();

    /**
     * Gets the id of the stream for the message
     *
     * @return the id of the stream
     */
    public int getStreamID(){
        return streamID;
    }

    /**
     * Sets the stream id for the message
     *
     * @param streamID the id of the stream
     *
     * @throws BadAttributeException
     *   if stream id is invalid
     */
    public abstract void setStreamID(int streamID)
            throws BadAttributeException;

    /**
     * Encodes the given message
     *
     * @param messageFactory the factory to encode messages
     *
     * @return the byte array representation of the message
     */
    public abstract byte [] encode(MessageFactory messageFactory);
}
