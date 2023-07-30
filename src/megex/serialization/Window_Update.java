/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization;

import utils.Constants;

import java.util.Objects;

/**
 * Syntax for window update header and payload.
 */
public class Window_Update extends Message {
    /** Number of octets that the sender can transmit */
    protected int increment;

    /**
     * Construct Window_Update with given details
     *
     * @param streamID the id of the stream for the message
     * @param increment number of octetes that the sender can transmit
     *
     * @throws BadAttributeException
     *   if stream id is invalid
     */
    public Window_Update(int streamID, int increment)
            throws BadAttributeException {
        // Call message constructor with appropriate code and stream id.
        super(streamID);
        // Set increment, making sure to check it is valid.
        this.setIncrement(increment);
    }

    /**
     * Gets the increment value
     *
     * @return the increment value
     */
    public int getIncrement(){
        return increment;
    }

    /**
     * Sets the increment for the window update
     *
     * @param increment the new increment value
     *
     * @throws BadAttributeException
     *   if increment value is invalid
     */
    public void setIncrement(int increment) throws BadAttributeException {
        // Ensure increment is positive.
        if(increment < 1){
            throw new BadAttributeException(
                    "Increment must be between 1 and 2^31-1!",
                    String.valueOf(increment));
        }
        this.increment = increment;
    }

    /**
     * Sets the stream id for the window update message
     *
     * @param streamID the id of the stream
     *
     * @throws BadAttributeException
     *   if stream id is invalid
     */
    @Override
    public void setStreamID(int streamID) throws BadAttributeException {
        // Ensure stream id is non-negative.
        if(streamID < 0){
            throw new BadAttributeException(
                    "Stream ID must be 0 or greater!",
                    "streamID");
        }
        this.streamID = streamID;
    }

    /**
     * Encodes the given window update message
     *
     * @param messageFactory the factory to encode messages
     *
     * @return the byte array representation of the window update message
     */
    @Override
    public byte[] encode(MessageFactory messageFactory) {
        return messageFactory.encodeMsg(this);
    }

    /**
     * Gets the string representation of the window_update message.
     *
     * @return the string representation of the window_update message.
     */
    public String toString(){
        return "Window_Update: StreamID=" + this.streamID + " increment=" +
                this.increment;
    }

    /**
     * Checks equality with another object
     *
     * @return whether this window update object is equal to another
     */
    @Override
    public boolean equals(Object o) {
        // Return true if object self.
        if (this == o) return true;
        // Return false if objects are different classes.
        if (o == null || getClass() != o.getClass()) return false;
        // Ensure all parameters are the same.
        Window_Update that = (Window_Update) o;
        return this.streamID == that.streamID &&
                increment == that.increment;
    }

    /**
     * Generate hash code for window update message
     *
     * @return hashcode of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.streamID, this.increment);
    }

    /**
     * Gets the type of message
     *
     * @return the type of message
     */
    @Override
    public byte getCode(){
        return Constants.WINDOW_UPDATE_CODE;
    }
}
