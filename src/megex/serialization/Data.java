/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization;

import utils.Constants;

import java.util.Arrays;
import java.util.Objects;

/**
 * Syntax for data message header and payload.
 */
public class Data extends Message {
    /** Indicates whether data is last frame of the stream. */
    protected boolean isEnd;
    /** Payload of data message. */
    protected byte [] data;

    /** Message to throw if stream id is invalid. */
    private static final String throwMessage = "Invalid message: " +
            "Data Stream ID of must be greater than 0";

    /**
     * Construct Data with given details
     *
     * @param streamID the id of the stream for the message
     * @param isEnd whether this frame is the last of the stream
     * @param data the payload of the data message
     *
     * @throws BadAttributeException
     *   if stream id or data is invalid
     */
    public Data(int streamID, boolean isEnd, byte[] data)
            throws BadAttributeException {
        // Call message constructor with appropriate code and stream id.
        super(streamID);
        this.isEnd = isEnd;
        // Set data, making sure to check it is valid.
        this.setData(data);
    }

    /**
     * Gets the data payload
     *
     * @return the data payload
     */
    public byte[] getData(){
        return data;
    }

    /**
     * Gets whether this is the last frame of the stream
     *
     * @return whether this is the last frame of the stream
     */
    public boolean isEnd(){
        return isEnd;
    }

    /**
     * Sets the data payload
     *
     * @param data the new data payload
     *
     * @throws BadAttributeException
     *   if data value is invalid
     */
    public void setData(byte [] data) throws BadAttributeException {
        // Ensure data is non-null and not too large.
        if(data == null || data.length > Constants.MAX_SIZE){
            throw new BadAttributeException("Invalid message: " +
                    "Data payload is invalid!", "data");
        }
        this.data = data;
    }

    /**
     * Sets whether this is the last frame of the payload
     *
     * @param end whether this is the last frame of the payload
     */
    public void setEnd(boolean end){
        this.isEnd = end;
    }

    /**
     * Sets the stream id for the data message
     *
     * @param streamID the id of the stream
     *
     * @throws BadAttributeException
     *   if stream id is invalid
     */
    @Override
    public void setStreamID(int streamID) throws BadAttributeException {
        // Ensure stream id is positive.
        if(streamID <= 0){
            throw new BadAttributeException(throwMessage,
                    String.valueOf(streamID));
        }
        this.streamID = streamID;
    }

    /**
     * Encodes the given data message
     *
     * @param messageFactory the factory to encode messages
     *
     * @return the byte array representation of the data message
     */
    @Override
    public byte[] encode(MessageFactory messageFactory) {
        return messageFactory.encodeMsg(this);
    }

    /**
     * Gets the string representation of the data message.
     *
     * @return the string representation of the data message.
     */
    public String toString(){
        return "Data: StreamID="+ this.streamID + " isEnd=" + this.isEnd +
                " data=" + this.data.length;
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
        Data data1 = (Data) o;
        return this.streamID == data1.streamID &&
                isEnd == data1.isEnd && Arrays.equals(data, data1.data);
    }

    /**
     * Generate hash code for data message
     *
     * @return hashcode of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.streamID, this.isEnd,
                Arrays.hashCode(this.data));
    }

    /**
     * Gets the type of message
     *
     * @return the type of message
     */
    @Override
    public byte getCode(){
        return Constants.DATA_CODE;
    }
}
