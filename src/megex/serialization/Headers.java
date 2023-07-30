/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 2
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization;

import utils.Constants;

import java.util.*;

/**
 * Syntax for headers message header and payload.
 */
public class Headers extends Message {
    /** Holds name, value pairs in insertion order. */
    private Map<String, String> pairs = new LinkedHashMap<>();
    /** Valid characters for names. */
    private static final String validNames = "[a-z0-9!#$%&'*+\\-.:^_`|~]+";

    /** Valid characters for values. */
    private static final String validValues = "[!-~\s\t]+";

    /** Indicates whether data is last frame of the stream. */
    private boolean isEnd;

    /**
     * Construct Data with given details
     *
     * @param streamID the id of the stream for the message
     * @param isEnd whether this frame is the last of the stream
     *
     * @throws BadAttributeException
     *   if stream id is invalid
     */
    public Headers(int streamID, boolean isEnd) throws BadAttributeException {
        // Call message constructor with appropriate code and stream id.
        super(streamID);
        this.isEnd = isEnd;
    }

    /**
     * Add headers name value pair
     *
     * @param name the name of the header field
     * @param value the value of the header field
     *
     * @throws BadAttributeException
     *   if name or value contains invalid characters
     */
    public void addValue(String name, String value)
            throws BadAttributeException {
        // If name is invalid, throw error.
        if(name == null || !name.matches(validNames)){
            throw new BadAttributeException("Name contains invalid character!",
                    "name");
        }
        // If value is invalid, throw error.
        if(value == null || !value.matches(validValues)){
            throw new BadAttributeException("Value contains invalid character!"
                    , "value");
        }
        pairs.remove(name);
        pairs.put(name, value);
    }

    /**
     * Gets the header field names
     *
     * @return a set of the header field names
     */
    public Set<String> getNames(){
        return pairs.keySet();
    }

    /**
     * Gets the header field value of a specific name
     *
     * @param name the name of the header field to get the value of
     *
     * @return the value of the given header field name
     */
    public String getValue(String name){
        return pairs.get(name);
    }

    /**
     * Gets whether this is the last frame of the stream
     *
     * @return whether this is the last frame of the stream
     */
    public boolean isEnd(){
        return this.isEnd;
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
     * Gets the string representation of the headers message.
     *
     * @return the string representation of the headers message.
     */
    public String toString(){
        // Add static attributes to string.
        String ret = "Headers: StreamID=" + this.streamID + " isEnd=" +
                this.isEnd + " (";
        // Add name, value pairs to string.
        if(pairs.size() > 0) {
            for (Map.Entry<String, String> pair : pairs.entrySet()) {
                ret += "[" + pair.getKey() + "=" + pair.getValue() + "]";
            }
        }
        ret += ")";
        return ret;
    }

    /**
     * Sets the stream id for the headers message
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
            throw new BadAttributeException("Invalid message: " +
                    "Headers Stream ID of " + streamID +
                    " must be greater than 0!",
                    String.valueOf(streamID));
        }
        this.streamID = streamID;
    }

    /**
     * Encodes the given headers message
     *
     * @param messageFactory the factory to encode messages
     *
     * @return the byte array representation of the headers message
     */
    @Override
    public byte[] encode(MessageFactory messageFactory) {
        return messageFactory.encodeMsg(this);
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
        Headers headers = (Headers) o;
        return this.streamID == headers.streamID
                && isEnd == headers.isEnd
                && Objects.equals(pairs, headers.pairs);
    }

    /**
     * Generate hash code for data message
     *
     * @return hashcode of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(streamID, pairs, isEnd);
    }

    /**
     * Gets the type of message
     *
     * @return the type of message
     */
    @Override
    public byte getCode(){
        return Constants.HEADERS_CODE;
    }
}
