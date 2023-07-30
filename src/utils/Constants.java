/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package utils;

import megex.app.server.Server;

import java.util.logging.Logger;

/**
 * Constants used by classes.
 */
public class Constants {
    /** Default constructor for javadoc */
    protected Constants(){

    }
    /** Maximum allowed size, in bytes, of frame payload. */
    public static final int MAX_SIZE = 16384;

    /** Set size, in bytes, of the length of the frame header. */
    public static final int HEADER_SIZE = 6;

    /**  Set size, in bytes, of length of frame payload. */
    public static final byte LENGTH_SIZE = 3;

    /**  Type code for a data message. */
    public static final byte DATA_CODE = 0;

    /**  Type code for a headers message. */
    public static final byte HEADERS_CODE = 1;

    /**  Type code for a settings message. */
    public static final byte SETTINGS_CODE = 4;

    /** Type code for a window update message. */
    public static final byte WINDOW_UPDATE_CODE = 8;

    /** Size of a window update payload. */
    public static final byte WINDOW_UPDATE_SIZE = 4;

    /** Size of a stream id. */
    public static final byte STREAM_ID_SIZE = 4;

    /** Start of stream id in header. */
    public static final byte STREAM_ID_OFFSET = 2;

    /** Mask to ignore reserved bit. */
    public static final byte IGNORE_RESERVED_MASK = 0x7F;

    /** Offset to flags byte. */
    public static final byte FLAGS_OFFSET = 0x1;

    /** Mask to set isEnd bit. */
    public static final byte IS_END = 0x1;

    /** Mask to set required flag. */
    public static final byte REQUIRED_FLAG = 0x4;

    /** Mask to check invalid flag. */
    public static final byte INVALID_FLAG = 0x8;

    /** Mask to check other invalid flag. */
    public static final byte INVALID_FLAG2 = 0x20;

    /** Preface to send to server. */
    public static final String PREFACE = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n";

    /** Logger instance for server. */
    public static final Logger SERVER_LOGGER = Logger.getLogger(
            Server.class.getName());

    /** Timeout of a client connection in ms. */
    public static final int TIMEOUT = 40000;

    /** File to log to. */
    public static final String LOG = "server.log";

    /** Keystore file for server. */
    public static final String KEYSTORE = "keystore";

    /** Password for keystore file for server. */
    public static final String KEYSTORE_PASSWORD = "password";

    /** Number of bytes to extend if 0x20 byte is set in headers */
    public static final int EXTENDBYTES = 5;

    /** Maximum number of bytes in a UDP payload */
    public static final int MAX_UDP = 65507;

    /** Number of bytes before jack message payload */
    public static final int JACK_HEADER = 2;

    /** Opcode for query message */
    public static final char OP_QUERY = 'Q';

    /** Opcode for response message */
    public static final char OP_RESPONSE = 'R';

    /** Opcode for new message */
    public static final char OP_NEW = 'N';

    /** Opcode for error message */
    public static final char OP_ERROR = 'E';

    /** Opcode for ack message */
    public static final char OP_ACK = 'A';

    /** Valid hosts regex */
    public static final String VALID_HOSTS = "[a-zA-Z0-9\\-.]+";

    /** Minimum valid port */
    public static final int MIN_PORT = 1;

    /** Maximum valid port */
    public static final int MAX_PORT = 65535;

    /** Minimum allowed ASCII byte */
    public static final byte MIN_BYTE = 32;

    /** Format for a response message */
    public static final String RESPONSE_FORMAT = "([a-zA-Z0-9\\-.:]+ )*";

    /** Timeout for Jack client response from server */
    public static final int JACK_TIMEOUT = 3000;

    /** Number of arguments for jack client */
    public static final int JACK_CLIENT_ARGS = 4;

    /** Error status code to exit with if error */
    public static final int BAD_STATUS = 1;

    /** Argument index of server in Jack client */
    public static final int JACK_SERVER = 0;

    /** Argument index of server port in Jack client */
    public static final int JACK_PORT = 1;

    /** Argument index of operation in Jack client */
    public static final int JACK_OP = 2;

    /** Argument index of payload in Jack client */
    public static final int JACK_PAYLOAD = 3;

    /** Character to split list of services on */
    public static final String SERVICE_SPLIT = " ";

    /** File to log to for Jack server. */
    public static final String JACK_LOG = "jack.log";
}
