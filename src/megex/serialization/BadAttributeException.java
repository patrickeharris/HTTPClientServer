/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization;

import java.util.Objects;

/**
 * Exception type for bad attributes in messages.
 */
public class BadAttributeException extends Exception {
    /** Value that caused the exception to be thrown. */
    protected String attribute;

    /** Version of exception. */
    private static final long serialVersionUID = 1L;

    /**
     * Construct BadAttributeException with given details
     *
     * @param message the error message
     * @param attribute the invalid attribute that caused the error
     *
     * @throws NullPointerException
     *   if given message or attribute is null
     */
    public BadAttributeException(String message, String attribute){
        // Call exception constructor.
        super(message);
        // Ensure data members are non-null.
        Objects.requireNonNull(message, "Message cannot be null!");
        Objects.requireNonNull(attribute, "Attribute cannot be null!");
        this.attribute = attribute;
    }

    /**
     * Construct BadAttributeException with given details
     *
     * @param message the error message
     * @param attribute the invalid attribute that caused the error
     * @param cause the throwable that caused the exception
     *
     * @throws NullPointerException
     *   if given message or attribute is null
     */
    public BadAttributeException(String message, String attribute,
                                 Throwable cause){
        // Call exception constructor.
        super(message, cause);
        // Ensure data members are non-null.
        Objects.requireNonNull(message, "Message cannot be null!");
        Objects.requireNonNull(attribute, "Attribute cannot be null!");
        this.attribute = attribute;
    }

    /**
     * Gets the attribute that caused the exception
     *
     * @return the attribute that caused the exception
     */
    public String getAttribute(){
        return attribute;
    }
}
