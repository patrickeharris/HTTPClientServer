/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization;

import utils.Constants;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import static utils.Utilities.int2byte;

/**
 * Constructs a frame from a given message and sends it to an output stream.
 */
public class Framer {
    /** Output stream to write frames to. */
    protected OutputStream out;

    /**
     * Construct framer with given output stream
     *
     * @param out the output stream to send frames too
     *
     * @throws NullPointerException
     *   if given output stream is null
     */
    public Framer(OutputStream out){
        this.out = Objects.requireNonNull(out, "OutputStream cannot be null!");
    }

    /**
     * Creates a frame by adding the prefix length to the given message and
     *   sending the entire frame to the output stream
     *
     * @param message the payload of the frame
     *
     * @throws NullPointerException
     *   if given message is null
     * @throws IllegalArgumentException
     *   if given message is too long for frame
     * @throws IOException
     *   if an IO error occurs when writing to the output stream
     */
    public void putFrame(byte[] message) throws IOException {
        // Throw an exception if the message is null.
        Objects.requireNonNull(message, "Unable to parse: " +
                "Message cannot be null!");

        // If the size of the message is greater than what is allowed, or less
        //   than what is allowed, throw an exception.
        if(message.length > Constants.MAX_SIZE + Constants.HEADER_SIZE ||
           message.length < Constants.HEADER_SIZE){
            throw new IllegalArgumentException("Unable to parse: " +
                    "Message cannot be larger" +
                    " than 16384 bytes or smaller than 6 bytes!");
        }

        // Allocate space for the frame.
        byte [] frame = new byte[message.length + Constants.LENGTH_SIZE];

        // Convert the length of the message into a byte array and store it
        //   in the frame.
        byte[] size = int2byte(message.length - Constants.HEADER_SIZE, Constants.LENGTH_SIZE);
        System.arraycopy(size, 0, frame, 0, Constants.LENGTH_SIZE);

        // Copy message into the frame.
        System.arraycopy(message, 0, frame, Constants.LENGTH_SIZE, message.length);

        // Write the constructed frame to the output stream.
        out.write(frame);
    }

}
