/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization;

import utils.Constants;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static utils.Utilities.byte2int;

/**
 * Takes in frames from an input stream and retrieves the message payload.
 */
public class Deframer {
    /** Input stream to read frames from. */
    protected InputStream in;

    /**
     * Construct deframer with given input stream
     *
     * @param in the input stream to retrieve frames from
     *
     * @throws NullPointerException
     *   if given input stream is null
     */
    public Deframer(InputStream in){
        this.in = Objects.requireNonNull(in, "InputStream cannot be null!");
    }

    /**
     * Retrieves and returns a header and payload from a frame read in from the
     *   input stream data member
     *
     * @return the payload of a frame that has been read in
     * @throws EOFException
     *   if given frame is too short
     * @throws IllegalArgumentException
     *   if given frame has a larger payload than what is allowed
     * @throws IOException
     *   if an IO error occurs when reading from the input stream
     */
    public byte[] getFrame() throws IOException {
        // Get size of payload.
        byte[] sizeArray = in.readNBytes(Constants.LENGTH_SIZE);
        // If size of frame is less than the set size of the 3 byte length,
        //   throw an exception.
        if(sizeArray.length < Constants.LENGTH_SIZE){
            throw new EOFException("Unable to parse: Frame is too short (" +
                    sizeArray.length + " bytes)!");
        }

        // Calculate size of payload.
        int size = byte2int(sizeArray, Constants.LENGTH_SIZE);

        // If size of payload is greater than what is allowed, throw an
        //   exception and skip bytes.
        if(size > Constants.MAX_SIZE){
            if(in.available() == Constants.HEADER_SIZE + size){
                in.skipNBytes(Constants.HEADER_SIZE + size);
            }
            throw new IllegalArgumentException("Unable to parse: " +
                    "The given length (" + size + " bytes) is greater" +
                    " than the max allowed length of 16384 bytes.");
        }

        // Read in the payload.
        byte[] payload = in.readNBytes(Constants.HEADER_SIZE + size);

        // If the payload size is less than the frame said it should be,
        //   throw an exception.
        if(payload.length < Constants.HEADER_SIZE + size){
            throw new EOFException("Unable to parse: Frame is too short (" +
                    sizeArray.length + " bytes)!");
        }

        // Return payload of frame.
        return payload;
    }


}
