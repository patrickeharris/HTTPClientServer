/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import java.util.Arrays;
import java.util.List;

/**
 * Constant values and arrays to use as sources for tests
 */
public class TestConstants {
    /** Default constructor for javadoc */
    public TestConstants(){

    }

    /** Maximum size of a frame payload. */
    public final static int MAX_PAYLOAD_SIZE = 16384;

    /** Size of frame header. */
    public final static int HEADER_SIZE = 6;

    /** Empty message. */
    public final static byte[] MESSAGE_EMPTY = {0,0,0,0,0,0};
    /** Resulting frame from empty message. */
    public final static byte[] FRAME_EMPTY = {0,0,0,0,0,0,0,0,0};

    /** Basic message. */
    public final static byte[] MESSAGE1 = {0,0,0,0,0,0,1};
    /** Resulting frame from basic message. */
    public final static byte[] FRAME1 = {0,0,1,0,0,0,0,0,0,1};

    /** Message with only zeros. */
    public final static byte[] MESSAGE2 = {0,0,0,0,0,0,0,0,0,0,0};
    /** Resulting frame from message with only zeros. */
    public final static byte[] FRAME2 = {0,0,5,0,0,0,0,0,0,0,0,0,0,0};

    /** Multi-byte message. */
    public final static byte[] MESSAGE3 = {0,0,0,0,0,0,1,2,3,4,5};
    /** Resulting frame from multi-byte message. */
    public final static byte[] FRAME3 = {0,0,5,0,0,0,0,0,0,1,2,3,4,5};
    /** Resulting frame from three of multi-byte messages. */
    public final static byte[] FRAME3x3 = {0,0,5,0,0,0,0,0,0,1,2,3,4,5,0,0,5,
                                           0,0,0,0,0,0,1,2,3,4,5,0,0,5,0,0,0,
                                           0,0,0,1,2,3,4,5};

    /** Message with large values. */
    public final static byte[] MESSAGE4 = {0,0,0,0,0,0,127,92,-8};
    /** Resulting frame from message with large values. */
    public final static byte[] FRAME4 = {0,0,3,0,0,0,0,0,0,127,92,-8};

    /** Message with prepended zeros. */
    public final static byte[] MESSAGE5 = {0,0,0,0,0,0,0,0,0,3};
    /** Resulting frame from message with prepended zeros. */
    public final static byte[] FRAME5 = {0,0,4,0,0,0,0,0,0,0,0,0,3};

    /** Resulting output stream from frames 1, 3, and 4. */
    public final static byte[] FRAME1x3x4 = {0,0,1,0,0,0,0,0,0,1,0,0,5,0,0,
                                             0,0,0,0,1,2,3,4,5,0,0,3,0,0,0,
                                             0,0,0,127,92,-8};
    /** Resulting output stream from frames 2, 4, and 5. */
    public final static byte[] FRAME2x4x5 = {0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,
                                             0,3,0,0,0,0,0,0,127,92,-8,0,0,
                                             4,0,0,0,0,0,0,0,0,0,3};

    /** Frame that specifies too large of a payload with maximum 3-byte integer
     *   size.
     */
    public final static byte[] LARGE_FRAME1 = {-128,-128,-128,0,0,0,0,0,0,1};
    /** Frame that specifies too large of a payload with one over maximum
     *  allowed size.
     */
    public final static byte[] LARGE_FRAME2 = {0,64,1,0,0,0,0,0,0,1};

    /** Frame that specifies too large of a payload with maximum 3-byte integer
     *  size following frame with large values.
     */
    public final static byte[] MULTI_LARGE_FRAME1 = {0,0,3,0,0,0,0,0,0,127,
                                                     92,-8,-128,-128,-128,0,
                                                     0,0,0,0,0,1};
    /** Frame that specifies too large of a payload with one over maximum allowed
     *   size following frame with prepended zeros.
     */
    public final static byte[] MULTI_LARGE_FRAME2 = {0,0,4,0,0,0,0,0,0,0,0,0,
                                                     3,0,64,1,0,0,0,0,0,0,1};

    /** Frame that ends too early in the header. */
    public final static byte[] FRAME_EOF_HEADER = {0,0,5,0,0,0};
    /** Frame that ends too early in the payload. */
    public final static byte[] FRAME_EOF_PAYLOAD = {0,0,2,0,0,0,0,0,0,1};
    /** Frame that ends with no payload. */
    public final static byte[] FRAME_NO_PAYLOAD = {0,0,1,0,0,0,0,0,0};

    /** Frame that ends too early in the header after multi-byte frame. */
    public final static byte[] MULTI_FRAME_EOF_HEADER = {0,0,5,0,0,0,0,0,0,
                                                         1,2,3,4,5,0,0,5,0,
                                                         0,0};
    /** Frame that ends too early in the payload after frame with large
     * values.
     */
    public final static byte[] MULTI_FRAME_EOF_PAYLOAD = {0,0,3,0,0,0,0,0,0,
                                                          127,92,-8,0,0,2,0,
                                                          0,0,0,0,0,1};
    /** Frame that ends with no payload after frame with prepended zeros. */
    public final static byte[] MULTI_FRAME_NO_PAYLOAD = {0,0,4,0,0,0,0,0,0,
                                                         0,0,0,3,0,0,1,0,0,
                                                         0,0,0,0};

    /** Empty data message payload. */
    public final static byte [] EMPTY_DATA = new byte[0];

    /** Single byte data message payload. */
    private final static byte [] SINGLE_BYTE_DATA = {1};

    /** Multiple byte data message payload. */
    private final static byte [] MULTI_BYTE_DATA = {1, 1, 1};

    /** Multiple zero byte data message payload. */
    private final static byte [] MULTI_ZERO_DATA = {0, 0, 0};

    /** Max size data payload. */
    private final static byte [] LARGE_DATA = new byte[16384];

    /** Too large of data payload. */
    private final static byte [] TOO_LARGE_DATA = new byte[16385];

    /** Data payload with one zero byte. */
    public final static byte [] ONE_BYTE_DATA_MESSAGE = {0};

    /** Data payload with multiple zero bytes. */
    public final static byte [] MULTI_BYTE_DATA_MESSAGE = {0, 0, 0};

    /** Data payload with large values. */
    public final static byte [] MULTI_BYTE_LARGE_DATA_MESSAGE = {1, -89, 76};

    /** Data frame with one zero byte. */
    public final static byte [] ONE_BYTE_DATA_FRAME = {0, 0, 0, 0, 0, 1, 0};

    /** Data frame with multiple zero bytes. */
    public final static byte [] MULTI_BYTE_DATA_FRAME = {0, 0, 0, 0, 0, 1, 0,
                                                         0, 0};

    /** Data frame with large values. */
    public final static byte [] MULTI_BYTE_LARGE_DATA_FRAME = {0, 0, 0, 0, 0,
                                                               1, 1, -89, 76};

    /** Data frame with stream id of 0. */
    public final static byte [] ZERO_ID_DATA_FRAME = {0, 0, 0, 0, 0, 0, 0};

    /** Data frame with no payload. */
    public final static byte [] EMPTY_DATA_FRAME = {0, 0, 0, 0, 0, 1};

    /** Data frame with no payload and stream id of 0. */
    public final static byte [] ZERO_ID_EMPTY_DATA_FRAME = {0, 0, 0, 0, 0, 0};

    /** Data frame with one zero byte and isEnd flag set. */
    public final static byte [] END_ONE_BYTE_DATA_FRAME = {0, 1, 0, 0, 0,
                                                           1, 0};

    /** Data frame with multiple zero bytes and isEnd flag set. */
    public final static byte [] END_MULTI_BYTE_DATA_FRAME = {0, 1, 0, 0, 0, 1,
                                                             0, 0, 0};

    /** Data frame with large values and isEnd flag set. */
    public final static byte [] END_MULTI_BYTE_LARGE_DATA_FRAME = {0, 1, 0, 0,
                                                                   0, 1, 1,
                                                                   -89, 76};

    /** Data frame with stream id of 0 and isEnd flag set. */
    public final static byte [] END_ZERO_ID_DATA_FRAME = {0, 1, 0, 0, 0, 0, 0};

    /** Data frame with no payload and isEnd flag set. */
    public final static byte [] END_EMPTY_DATA_FRAME = {0, 1, 0, 0, 0, 1};

    /** Data frame with no payload and stream id of 0 and isEnd flag set. */
    public final static byte [] END_ZERO_ID_EMPTY_DATA_FRAME = {0, 1, 0, 0,
                                                                0, 0};
    /** Data frame with one zero byte and error flag set. */
    public final static byte [] ERROR_ONE_BYTE_DATA_FRAME = {0, 8, 0, 0,
                                                             0, 1, 0};

    /** Data frame with multiple zero bytes and error flag set. */
    public final static byte [] ERROR_MULTI_BYTE_DATA_FRAME = {0, 8, 0, 0, 0,
                                                               1, 0, 0, 0};

    /** Data frame with large values and error flag set. */
    public final static byte [] ERROR_MULTI_BYTE_LARGE_DATA_FRAME = {0, 8, 0,
                                                                     0, 0, 1,
                                                                     1, -89,
                                                                     76};

    /** Data frame with stream id of 0 and error flag set. */
    public final static byte [] ERROR_ZERO_ID_DATA_FRAME = {0, 8, 0, 0, 0,
                                                            0, 0};

    /** Data frame with no payload and error flag set. */
    public final static byte [] ERROR_EMPTY_DATA_FRAME = {0, 8, 0, 0, 0, 1};

    /** Data frame with no payload and stream id of 0 and error flag set. */
    public final static byte [] ERROR_ZERO_ID_EMPTY_DATA_FRAME = {0, 8, 0, 0,
                                                                  0, 0};

    /** Data frame with no payload and negative stream id and error flag
     * set.
     */
    public final static byte [] ERROR_NEG_ID_EMPTY_DATA_FRAME = {0, 8, 0, 0,
                                                                 0, -1};

    /** Data frame with one zero byte and isEnd and error flags set. */
    public final static byte [] ERROR_END_ONE_BYTE_DATA_FRAME = {0, 9, 0, 0,
                                                                 0, 1, 0};

    /** Data frame with multiple zero bytes and isEnd and error flags set. */
    public final static byte [] ERROR_END_MULTI_BYTE_DATA_FRAME = {0, 9, 0, 0,
                                                                   0, 1, 0, 0,
                                                                   0};

    /** Data frame with large values and isEnd and error flags set. */
    public final static byte [] ERROR_END_MULTI_BYTE_LARGE_DATA_FRAME = {0, 9,
                                                                         0, 0,
                                                                         0, 1,
                                                                         1,
                                                                         -89,
                                                                         76};

    /** Data frame with stream id of 0 and isEnd and error flags set. */
    public final static byte [] ERROR_END_ZERO_ID_DATA_FRAME = {0, 9, 0, 0, 0,
                                                                0, 0};

    /** Data frame with no payload and isEnd and error flags set. */
    public final static byte [] ERROR_END_EMPTY_DATA_FRAME = {0, 9, 0, 0, 0,
                                                              1};

    /** Data frame with no payload and stream id of 0 and isEnd and error flags
     *  set.
     */
    public final static byte [] ERROR_END_ZERO_ID_EMPTY_DATA_FRAME = {0, 9, 0,
                                                                      0, 0, 0};

    /** Data frame with no payload and negative stream id and isEnd and error
     *  flags set.
     */
    public final static byte [] ERROR_END_NEG_ID_EMPTY_DATA_FRAME = {0, 9, 0,
                                                                     0, 0, -1};


    /** Settings frame. */
    public final static byte [] ZERO_ID_EMPTY_SETTINGS_FRAME = {4, 1, 0, 0,
                                                                0, 0};

    /** Valid window frame. */
    public final static byte [] WINDOW_FRAME = {8, 0, 0, 0, 0, 1, 0, 0, 0, 1};

    /** Valid window frame with max increment. */
    public final static byte [] LARGE_WINDOW_FRAME = {8, 0, 0, 0, 0, 1,
                                                      (byte) 0x7F,
                                                      (byte) 0xFF,
                                                      (byte) 0xFF,
                                                      (byte) 0xFF};

    /** Valid window frame with stream id of 0. */
    public final static byte [] ZERO_ID_WINDOW_FRAME = {8, 0, 0, 0, 0, 0,
                                                        0, 0, 0, 1};

    /** Valid window frame with stream id of 0 and max increment. */
    public final static byte [] ZERO_ID_LARGE_WINDOW_FRAME = {8, 0, 0, 0, 0, 0,
                                                              (byte) 0x7F,
                                                              (byte) 0xFF,
                                                              (byte) 0xFF,
                                                              (byte) 0xFF};

    /** Window frame that is too short. */
    public final static byte [] SHORT_WINDOW_FRAME = {8, 0, 0, 0, 0, 1,
                                                      0, 0, 0};

    /** Valid data stream ids. */
    public final static List<Integer> VALID_DATA_STREAM_ID =
            Arrays.asList(1, Integer.MAX_VALUE);

    /** Invalid data stream ids. */
    public final static List<Integer> INVALID_DATA_STREAM_ID =
            Arrays.asList(0, -1);

    /** Valid data end values. */
    public final static List<Boolean> VALID_DATA_IS_END =
            Arrays.asList(false, true);

    /** Valid data payloads. */
    public final static List<byte []> VALID_DATA =
            Arrays.asList(EMPTY_DATA, SINGLE_BYTE_DATA, MULTI_BYTE_DATA,
                          MULTI_ZERO_DATA, LARGE_DATA, ONE_BYTE_DATA_MESSAGE);

    /** Invalid data payload. */
    public final static List<byte []> INVALID_DATA =
            Arrays.asList(TOO_LARGE_DATA);

    /** Valid stream ids for window update. */

    public final static List<Integer> VALID_WINDOW_UPDATE_STREAM_ID =
            Arrays.asList(1, Integer.MAX_VALUE, 0);

    /** Valid increments for window update */
    public final static List<Integer> VALID_WINDOW_UPDATE_INCREMENT =
            Arrays.asList(1, Integer.MAX_VALUE);

    /**Invalid stream ids for window update. */
    public final static List<Integer> INVALID_WINDOW_UPDATE_STREAM_ID =
            Arrays.asList(-1);

    /** Invalid increments for window update. */
    public final static List<Integer> INVALID_WINDOW_UPDATE_INCREMENT =
            Arrays.asList(-1, 0);

    /** Invalid frames for decoding. */
    public final static List<byte []> INVALID_DECODE =
            Arrays.asList(ZERO_ID_DATA_FRAME, ZERO_ID_EMPTY_DATA_FRAME,
                          END_ZERO_ID_DATA_FRAME, END_ZERO_ID_EMPTY_DATA_FRAME,
                          ERROR_ZERO_ID_DATA_FRAME,
                          ERROR_ZERO_ID_EMPTY_DATA_FRAME,
                          ERROR_NEG_ID_EMPTY_DATA_FRAME,
                          ERROR_END_ZERO_ID_DATA_FRAME,
                          ERROR_END_ZERO_ID_EMPTY_DATA_FRAME,
                          ERROR_END_NEG_ID_EMPTY_DATA_FRAME,
                          ERROR_ONE_BYTE_DATA_FRAME,
                          ERROR_MULTI_BYTE_DATA_FRAME,
                          ERROR_MULTI_BYTE_LARGE_DATA_FRAME,
                          ERROR_END_ONE_BYTE_DATA_FRAME,
                          ERROR_END_MULTI_BYTE_DATA_FRAME,
                          ERROR_END_MULTI_BYTE_LARGE_DATA_FRAME,
                          ERROR_EMPTY_DATA_FRAME,
                          ERROR_END_EMPTY_DATA_FRAME,
                          SHORT_WINDOW_FRAME);


    /** Valid name for headers. */
    private final static String validName1 = "name";
    /** Valid name for headers with special characters. */
    private final static String validName2 = ":2-_:'|";

    /** First invalid name for headers. */
    private final static String invalidName1 = "(";
    /** Second invalid name for headers. */
    private final static String invalidName2 = ")";
    /** Third invalid name for headers. */
    private final static String invalidName3 = ",";
    /** Fourth invalid name for headers. */
    private final static String invalidName4 = "/";
    /** Fifth invalid name for headers. */
    private final static String invalidName5 = ";";
    /** Sixth invalid name for headers. */
    private final static String invalidName6 = "<";
    /** Seventh invalid name for headers. */
    private final static String invalidName7 = "=";
    /** Eighth invalid name for headers. */
    private final static String invalidName8 = ">";
    /** Ninth invalid name for headers. */
    private final static String invalidName9 = "?";
    /** Tenth invalid name for headers. */
    private final static String invalidName10 = "@";
    /** Eleventh invalid name for headers. */
    private final static String invalidName11 = "[";
    /** Twelfth invalid name for headers. */
    private final static String invalidName12 = "\\";
    /** Thirteenth invalid name for headers. */
    private final static String invalidName13 = "]";
    /** Fourteenth invalid name for headers. */
    private final static String invalidName14 = "{";
    /** Fifteenth invalid name for headers. */
    private final static String invalidName15 = "}";
    /** Sixteenth invalid name for headers. */
    private final static String invalidName16 = "\"";
    /** Seventeenth invalid name for headers. */
    private final static String invalidName17 = " ";
    /** Eighteenth invalid name for headers. */
    private final static String invalidName18 = "\t";
    /** Nineteenth invalid name for headers. */
    private final static String invalidName19 = "\r";
    /** Twentieth invalid name for headers. */
    private final static String invalidName20 = "\n";
    /** Twenty-first invalid name for headers. */
    private final static String invalidName21 = "\0";
    /** Twenty-second invalid name for headers. */
    private final static String invalidName22 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /** Twenty-third invalid name for headers. */
    private final static String invalidName23 = "abcdefghijklMnopqrstuvwxyz";

    /** Valid value for headers. */
    private final static String validValue1 = "value";
    /** Valid value for headers with special characters. */
    private final static String validValue2 = ":2-_:'|(),/;<=>?@[\\]{}\"\t ";

    /** First invalid value for headers. */
    private final static String invalidValue1 = "\r";
    /** Second invalid value for headers. */
    private final static String invalidValue2 = "\n";
    /** Third invalid value for headers. */
    private final static String invalidValue3 = "\0";

    /** Valid names for headers. */
    public final static List<String> VALID_NAMES = Arrays.asList(validName1,
            validName2);
    /** Invalid names for headers. */
    public final static List<String> INVALID_NAMES = Arrays.asList(
            invalidName1, invalidName2, invalidName3, invalidName4,
            invalidName5, invalidName6, invalidName7, invalidName8,
            invalidName9, invalidName10, invalidName11, invalidName12,
            invalidName13, invalidName14, invalidName15, invalidName16,
            invalidName17, invalidName18, invalidName19, invalidName20,
            invalidName21, invalidName22, invalidName23);
    /** Valid values for headers. */
    public final static List<String> VALID_VALUES = Arrays.asList(validValue1,
            validValue2);
    /** Invalid values for headers. */
    public final static List<String> INVALID_VALUES = Arrays.asList(
            invalidValue1, invalidValue2, invalidValue3);


}