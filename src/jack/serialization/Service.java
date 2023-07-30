/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 4
 * Class: CSI 4321
 *
 ************************************************/
package jack.serialization;

import utils.Constants;

import java.util.Objects;

/**
 * Syntax for a service host/port pair.
 */
public class Service {
    /** Host ID */
    private String host;
    /** Port number */
    private int port;

    /**
     * Construct Service with given details
     *
     * @param host the host ID
     * @param port the service port
     *
     * @throws IllegalArgumentException
     *   if invalid host or port
     */
    public Service(String host, int port) throws IllegalArgumentException {
        setHost(host);
        setPort(port);
    }

    /**
     * Sets the message port
     *
     * @param port the new port number
     *
     * @throws IllegalArgumentException
     *    if invalid port
     */
    public void setPort(int port) throws IllegalArgumentException {
        // Throw error if invalid port.
        if(port < Constants.MIN_PORT || port > Constants.MAX_PORT){
            throw new IllegalArgumentException(
                    "Port must be between 1 and 65535! " + port);
        }
        // Throw error if host/port pair would be too large.
        if(this.host != null && String.valueOf(port).length() +
                this.host.length() > Constants.MAX_UDP
                - Constants.JACK_HEADER - 1){
            throw new IllegalArgumentException("Payload too large for UDP!"
                    + host + ":" + port);
        }
        this.port = port;
    }

    /**
     * Sets the message host
     *
     * @param host the new host ID
     *
     * @throws IllegalArgumentException
     *    if invalid host
     *
     * @return service with new host
     */
    public Service setHost(String host) throws IllegalArgumentException {
        // Throw error if invalid host.
        if(host == null || !host.matches(Constants.VALID_HOSTS) ||
                host.length() > Constants.MAX_UDP -
                        Constants.JACK_HEADER - 1){
            throw new IllegalArgumentException("Invalid host! " + host);
        }
        // Throw error if host/port pair would be too large.
        if(host.length() + String.valueOf(this.port).length() >
                Constants.MAX_UDP - Constants.JACK_HEADER - 1){
            throw new IllegalArgumentException("Payload too large for UDP! "
                    + host + ":" + port);
        }
        this.host = host;
        return this;
    }

    /**
     * Gets the service host
     *
     * @return the host id
     */
    public String getHost(){
        return this.host;
    }

    /**
     * Gets the service port
     *
     * @return port number
     */
    public int getPort(){
        return this.port;
    }

    /**
     * Gets string representation of service
     *
     * @return string representation of service
     */
    public String toString(){
        return this.host + ":" + this.port;
    }

    /**
     * Determines of this service is equal to another
     *
     * @param o the other object to check equality with
     *
     * @return true if this service is equal to the other, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return port == service.port && Objects.equals(host, service.host);
    }

    /**
     * Gets hashcode of service
     *
     * @return hashcode of service
     */
    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
