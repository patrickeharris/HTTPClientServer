/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 4
 * Class: CSI 4321
 *
 ************************************************/
package jack.serialization;

import utils.Constants;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Syntax for query message header and payload.
 */
public class Query extends Message {
    /** Search string */
    private String searchString;

    /**
     * Construct Query message with given details
     *
     * @param searchString the query serach string
     *
     * @throws IllegalArgumentException
     *   if invalid search string
     */
    public Query(String searchString) throws IllegalArgumentException {
        setSearchString(searchString);
    }

    /**
     * Gets the search string
     *
     * @return the search string
     */
    public String getSearchString(){
        return this.searchString;
    }

    /**
     * Sets the search string
     *
     * @param searchString the new search string
     *
     * @return query message with new search string
     *
     * @throws IllegalArgumentException
     *    if invalid search string
     */
    public final Query setSearchString(String searchString)
            throws IllegalArgumentException {
        // Throw error if invalid search string.
        if(searchString == null  || searchString.length() > Constants.MAX_UDP
                - Constants.JACK_HEADER || (!searchString.matches(
                        Constants.VALID_HOSTS) && !searchString.equals("*")) ||
                searchString.length() == 0){
            throw new IllegalArgumentException("Invalid search string! "
                    + searchString);
        }
        this.searchString = searchString;
        return this;
    }

    /**
     * Gets string representation of Query message
     *
     * @return string representation of query message
     */
    public String toString(){
        return "QUERY " + this.searchString;
    }

    /**
     * Encodes the query message
     *
     * @return byte array representation of query message
     */
    @Override
    public byte[] encode() {
        return ("Q " + this.getSearchString()).getBytes(
                StandardCharsets.US_ASCII);
    }

    /**
     * Gets the op code of the Query message
     *
     * @return op code of query message
     */
    @Override
    public String getOperation() {
        return String.valueOf(Constants.OP_QUERY);
    }

    /**
     * Determines of this Query message is equal to another
     *
     * @param o the other object to check equality with
     *
     * @return true if this query message is equal to the other,
     *     false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return Objects.equals(searchString, query.searchString);
    }

    /**
     * Gets hashcode of Query message
     *
     * @return hashcode of query message
     */
    @Override
    public int hashCode() {
        return Objects.hash(searchString);
    }
}
