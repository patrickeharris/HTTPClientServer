/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 4
 * Class: CSI 4321
 *
 ************************************************/
package jack.serialization;

import utils.Constants;
import utils.Utilities;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Syntax for error message header and payload.
 */
public class Error extends Message {
    /** Error message */
    private String errorMessage;

    /**
     * Construct Error message with given details
     *
     * @param errorMessage the error message
     *
     * @throws IllegalArgumentException
     *   if invalid error message
     */
    public Error(String errorMessage) throws IllegalArgumentException {
        setErrorMessage(errorMessage);
    }

    /**
     * Gets the error message
     *
     * @return the error message
     */
    public String getErrorMessage(){
        return this.errorMessage;
    }

    /**
     * Sets the error message
     *
     * @param errorMessage the error message
     *
     * @return error message with new error
     *
     * @throws IllegalArgumentException
     *    if invalid error message
     */
    public final Error setErrorMessage(String errorMessage)
            throws IllegalArgumentException {
        // Throw error if invalid error message.
        if(errorMessage == null || errorMessage.length() > Constants.MAX_UDP
                - Constants.JACK_HEADER || !Utilities.validateBytes(
                        errorMessage.getBytes(StandardCharsets.US_ASCII)) ||
                errorMessage.length() == 0){
            throw new IllegalArgumentException("Invalid error message! "
                    + errorMessage);
        }
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * Gets string representation of Error message
     *
     * @return string representation of error message
     */
    public String toString(){
        return "ERROR " + this.errorMessage;
    }

    /**
     * Serializes the Error message
     *
     * @return byte array representation of error message
     */
    @Override
    public byte[] encode() {
        return ("E " + this.errorMessage).getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Gets the op code of the Error message
     *
     * @return op code of error message
     */
    @Override
    public String getOperation() {
        return String.valueOf(Constants.OP_ERROR);
    }

    /**
     * Determines of this Error message is equal to another
     *
     * @param o the other object to check equality with
     *
     * @return true if this error message is equal to the other,
     *     false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error error = (Error) o;
        return Objects.equals(errorMessage, error.errorMessage);
    }

    /**
     * Gets hashcode of Error message
     *
     * @return hashcode of error message
     */
    @Override
    public int hashCode() {
        return Objects.hash(errorMessage);
    }
}
