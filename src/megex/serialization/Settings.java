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
 * Syntax for settings header and payload.
 */
public class Settings extends Message {
    /**
     * Construct Settings message
     *
     * @throws BadAttributeException
     *   if bad attribute
     */
    public Settings() throws BadAttributeException{
        // Call message constructor with appropriate code and stream id.
        super(0);
    }

    /**
     * Gets the string representation of the settings message.
     *
     * @return the string representation of the settings message.
     */
    public String toString(){
        return "Settings: StreamID=0";
    }

    /**
     * Sets the stream id for the settings message
     *
     * @param streamID the id of the stream
     *
     * @throws BadAttributeException
     *   if stream id is invalid
     */
    @Override
    public void setStreamID(int streamID) throws BadAttributeException {
        // Ensure stream id is 0.
        if(streamID != 0){
            throw new BadAttributeException("Stream ID must be 0!",
                    String.valueOf(streamID));
        }
        this.streamID = streamID;
    }

    /**
     * Encodes the given settings message
     *
     * @param messageFactory the factory to encode messages
     *
     * @return the byte array representation of the settings message
     */
    @Override
    public byte[] encode(MessageFactory messageFactory) {
        return messageFactory.encodeMsg(this);
    }

    /**
     * Checks equality with another object
     *
     * @return whether this Settings object is equal to another
     */
    @Override
    public boolean equals(Object o) {
        // Return true if object self.
        if (this == o) return true;
        // Return false if objects are different classes.
        if (o == null || getClass() != o.getClass()) return false;
        // Ensure all parameters are the same.
        Settings data1 = (Settings) o;
        return this.streamID == data1.streamID;
    }

    /**
     * Generate hash code for settings message
     *
     * @return hashcode of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.streamID);
    }

    /**
     * Gets the type of message
     *
     * @return the type of message
     */
    @Override
    public byte getCode(){
        return Constants.SETTINGS_CODE;
    }
}
