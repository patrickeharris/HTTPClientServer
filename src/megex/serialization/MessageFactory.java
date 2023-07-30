/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization;

import com.twitter.hpack.Decoder;
import com.twitter.hpack.Encoder;
import utils.Constants;
import utils.Utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static utils.Utilities.byte2int;

/**
 * Encodes and decodes messages.
 */
public class MessageFactory {
    /** Character encoding for name, value pairs */
    private static final Charset CHARENC = StandardCharsets.US_ASCII;
    /** Static decoder for whole lifecycle */
    private static Decoder decoder;
    /** Default constructor for javadoc */
    public MessageFactory(){
    }

    /**
     * Decode the byte array into a message
     *
     * @param msgBytes byte array to decode
     *
     * @return decoded message
     * @throws NullPointerException
     *   if msgBytes is null
     * @throws BadAttributeException
     *   if msgBytes is invalid message
     */
    public Message decode(byte [] msgBytes)
            throws BadAttributeException, NullPointerException{
        // Data Abstraction
        Message m = null;
        // Ensure byte array is non-null.
        Objects.requireNonNull(msgBytes, "Invalid message: " +
                "Message bytes cannot be null!");
        // Ensure frame meets minimum size.
        validateSize(msgBytes, Constants.HEADER_SIZE, false);
        // Get stream ID from byte array.
        byte [] streamIDArr = Utilities.arraySubset(msgBytes,
                Constants.STREAM_ID_OFFSET, Constants.STREAM_ID_SIZE);
        streamIDArr[0] &= Constants.IGNORE_RESERVED_MASK;
        int streamID = byte2int(streamIDArr, Constants.STREAM_ID_SIZE);

        // Initialize isEnd to false.
        boolean isEnd = false;

        boolean flagSet = false;

        // Switch based on message type.
        switch (msgBytes[0]){
            // For data code, construct data message.
            case Constants.DATA_CODE:
                // Error if error flag is set.
                if((msgBytes[Constants.FLAGS_OFFSET] & Constants.INVALID_FLAG)
                        == Constants.INVALID_FLAG){
                    throw new BadAttributeException("Invalid message: " +
                            "Data error flag is set!",
                            String.valueOf(msgBytes[Constants.FLAGS_OFFSET]));
                }
                // Set isEnd flag if necessary.
                if((msgBytes[Constants.FLAGS_OFFSET] & Constants.IS_END)
                        == Constants.IS_END){
                    isEnd = true;
                }
                // Construct data message.
                m = new Data(streamID, isEnd, Utilities.arraySubset(msgBytes,
                        Constants.HEADER_SIZE,
                        msgBytes.length - Constants.HEADER_SIZE));
                break;

            // For settings code, construct settings message.
            case Constants.SETTINGS_CODE:
                if(streamID != 0){
                    throw new BadAttributeException("Invalid message: "
                            + "Stream id must be 0!",
                            String.valueOf(streamID));
                }
                m = new Settings();
                break;

            // For window update code, construct window update message.
            case Constants.WINDOW_UPDATE_CODE:
                // Throw error if payload size is too small
                validateSize(msgBytes, Constants.HEADER_SIZE
                        + Constants.WINDOW_UPDATE_SIZE, true);
                // Get increment value from byte array.
                byte [] incrementArr = Utilities.arraySubset(msgBytes,
                        Constants.HEADER_SIZE,
                        Constants.WINDOW_UPDATE_SIZE);
                incrementArr[0] &= Constants.IGNORE_RESERVED_MASK;
                int increment = byte2int(incrementArr,
                        Constants.WINDOW_UPDATE_SIZE);
                // Constructs window update message.
                m = new Window_Update(streamID, increment);
                break;

            case Constants.HEADERS_CODE:
                // Throw error if too large.
                if(msgBytes.length > Constants.MAX_SIZE){
                    throw new BadAttributeException(
                            "Invalid message: Payload too large!",
                            String.valueOf(msgBytes.length));
                }
                // Set isEnd flag if necessary.
                if((msgBytes[Constants.FLAGS_OFFSET] & Constants.IS_END)
                        == Constants.IS_END){
                    isEnd = true;
                }
                // Throw error if required bit is not set.
                if((msgBytes[Constants.FLAGS_OFFSET] & Constants.REQUIRED_FLAG)
                        != Constants.REQUIRED_FLAG){
                    throw new BadAttributeException(
                            "Invalid message: Required headers flag not set!",
                            String.valueOf(msgBytes[Constants.FLAGS_OFFSET]));
                }
                // Throw error if invalid flags are set.
                if((msgBytes[Constants.FLAGS_OFFSET] & Constants.INVALID_FLAG)
                        == Constants.INVALID_FLAG){
                    throw new BadAttributeException(
                            "Invalid message: Invalid headers flag set!"
                            , String.valueOf(msgBytes[Constants.FLAGS_OFFSET]));
                }
                if(
                        (msgBytes[Constants.FLAGS_OFFSET]
                                & Constants.INVALID_FLAG2)
                                == Constants.INVALID_FLAG2){
                    flagSet = true;
                }
                // Create decoder if needed.
                if(decoder == null) {
                    decoder = new Decoder(Constants.MAX_SIZE, Constants.MAX_SIZE);
                }
                // Create objects for decoding headers.
                ByteArrayInputStream in = new ByteArrayInputStream(
                        Utilities.arraySubset(msgBytes,
                                flagSet ? Constants.HEADER_SIZE
                                        + Constants.EXTENDBYTES :
                                        Constants.HEADER_SIZE,
                                flagSet ? msgBytes.length
                                        - Constants.HEADER_SIZE
                                        - Constants.EXTENDBYTES
                                        : msgBytes.length
                                        - Constants.HEADER_SIZE));
                Headers h = new Headers(streamID, isEnd);
                Map<String, String> pairs = new LinkedHashMap<>();
                // Decode headers name, value pairs.
                try {
                    decoder.decode(in, (name, value, sensitive) ->
                            pairs.put(b2s(name), b2s(value)));
                } catch (IOException ignored) {
                }
                // Add pairs to headers message.
                for (Map.Entry<String, String> entry : pairs.entrySet()) {
                    h.addValue(entry.getKey(), entry.getValue());
                }
                decoder.endHeaderBlock();
                m = h;
                break;
            default:
                // Throw exception if invalid type.
                throw new BadAttributeException(
                        "Received unknown type: " + msgBytes[0],
                        Byte.toString(msgBytes[0]));
        }

        return m;
    }

    /**
     * Ensure message has correct size
     *
     * @param msgBytes the message to check
     * @param size the size to check against
     * @param strict whether size is strict or minimum
     *
     * @throws BadAttributeException
     *   if msg is wrong size
     */
    private void validateSize(byte[] msgBytes, int size, boolean strict)
            throws BadAttributeException {
        if(msgBytes.length < size){
            if(!strict || (strict && msgBytes.length != size)){
                throw new BadAttributeException(
                        "Invalid message: message is too small!",
                        String.valueOf(size));
            }
        }
    }

    /**
     * Encode the byte array into a message
     *
     * @param msg the message to encode
     *
     * @return encoded byte array
     * @throws NullPointerException
     *   if msg is null
     */
    public byte[] encode(Message msg){
        // Ensure message is non-null.
        Objects.requireNonNull(msg, "Message cannot be null!");
        // Call message-specific encode method.
        byte [] msgBytes = msg.encode(this);
        // Set message code.
        msgBytes[0] = msg.getCode();
        // Make sure reserved bit is unset.
        msgBytes[Constants.FLAGS_OFFSET] &= Constants.IGNORE_RESERVED_MASK;
        // Set stream id in byte array.
        System.arraycopy(Utilities.int2byte(msg.getStreamID(),
                Constants.STREAM_ID_SIZE),
                0,
                msgBytes,
                Constants.STREAM_ID_OFFSET,
                Constants.STREAM_ID_SIZE);
        return msgBytes;
    }

    /**
     * Convert the byte array into a string
     *
     * @param b the byte array to convert
     *
     * @return the string representation of the bytes
     */
    private static String b2s(byte[] b) {
        return new String(b, CHARENC);
    }

    /**
     * Encode the byte array into a data message
     *
     * @param data the message to encode
     *
     * @return encoded byte array
     * @throws NullPointerException
     *   if msg is null
     */
    public byte[] encodeMsg(Data data) {
        // Allocate space for byte array.
        byte [] frame = new byte[data.getData().length +
                Constants.HEADER_SIZE];
        // Set isEnd flag.
        if(data.isEnd()){
            frame[Constants.FLAGS_OFFSET] = Constants.IS_END;
        }
        // Copy data payload into byte array.
        System.arraycopy(data.getData(), 0, frame, Constants.HEADER_SIZE,
                data.getData().length);
        return frame;
    }

    /**
     * Encode the byte array into a settings message
     *
     * @param settings the message to encode
     *
     * @return encoded byte array
     * @throws NullPointerException
     *   if msg is null
     */
    public byte[] encodeMsg(Settings settings) {
        // Allocate space for byte array.
        byte [] frame = new byte[Constants.HEADER_SIZE];
        // Set flags to 1 for settings.
        frame[Constants.FLAGS_OFFSET] = 1;
        return frame;
    }

    /**
     * Encode the byte array into a window update message
     *
     * @param window_update the message to encode
     *
     * @return encoded byte array
     * @throws NullPointerException
     *   if msg is null
     */
    public byte[] encodeMsg(Window_Update window_update) {
        // Allocate space for byte array.
        byte [] frame = new byte[Constants.HEADER_SIZE +
                Constants.WINDOW_UPDATE_SIZE];
        // Copy increment into byte array.
        System.arraycopy(Utilities.int2byte(window_update.getIncrement(),
                        Constants.STREAM_ID_SIZE),
                0, frame,
                Constants.HEADER_SIZE,
                Constants.WINDOW_UPDATE_SIZE);
        return frame;
    }

    /**
     * Encode the byte array into a headers message
     *
     * @param headers the message to encode
     *
     * @return encoded byte array
     * @throws NullPointerException
     *   if msg is null
     */
    public byte[] encodeMsg(Headers headers) {
        Encoder encoder = new Encoder(Constants.MAX_SIZE);
        // Encode each name, value pair.
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        headers.getNames().stream().forEach((name) -> {
            try {
                encoder.encodeHeader(out,
                        name.getBytes(StandardCharsets.US_ASCII),
                        headers.getValue(name)
                                .getBytes(StandardCharsets.US_ASCII),
                        false);
            } catch (IOException ignored) {
            }
        });
        // Create array for headers message.
        byte [] frame = new byte[out.size() + Constants.HEADER_SIZE];
        // Copy name value pairs.
        System.arraycopy(out.toByteArray(), 0, frame,
                Constants.HEADER_SIZE, out.size());
        // Set isEnd flag.
        if(headers.isEnd()){
            frame[Constants.FLAGS_OFFSET] |= Constants.IS_END;
        }
        // Set required flag.
        frame[Constants.FLAGS_OFFSET] |= Constants.REQUIRED_FLAG;
        return frame;
    }

}
