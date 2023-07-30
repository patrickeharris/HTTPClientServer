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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Syntax for data response header and payload.
 */
public class Response extends Message {
    /** List of host/port pairs */
    private List<Service> services;

    /**
     * Construct Response message
     */
    public Response(){
        this.services = new ArrayList<>();
    }

    /**
     * Gets the service list
     *
     * @return list of host/port pairs
     */
    public List<String> getServiceList(){
        return this.services.stream().map(Service::toString).collect(
                Collectors.toList());
    }

    /**
     * Adds a service to the list
     *
     * @param host the host ID of the service
     * @param port thr port number of the service
     *
     * @return response message with new service
     *
     * @throws IllegalArgumentException
     *    if invalid host or port
     */
    public final Response addService(String host, int port)
            throws IllegalArgumentException {
        Service service = new Service(host, port);
        // Validate adding new service will keep response within UDP payload.
        validateSize(service);
        if(!services.contains(service)) {
            services.add(service);
            // Sort by Java default string order.
            services.sort(Comparator.comparing(Service::getHost)
                    .thenComparing(Service::getPort));
        }
        return this;
    }

    /**
     * Gets string representation of response message
     *
     * @return string representation of response message
     */
    public String toString(){
        String ret = "RESPONSE ";
        for(Service service : services){
            ret = ret + "[" + service + "]";
        }
        return ret;
    }

    /**
     * Serializes the Response message
     *
     * @return byte array representation of response message
     */
    @Override
    public byte[] encode() {
        StringBuilder ret  = new StringBuilder("R ");
        // Add each service in list.
        for(Service service : this.services){
            ret.append(service).append(" ");
        }
        return ret.toString().getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Gets the op code of the Response message
     *
     * @return op code of response message
     */
    @Override
    public String getOperation() {
        return String.valueOf(Constants.OP_RESPONSE);
    }

    /**
     * Determines of this Response message is equal to another
     *
     * @param o the other object to check equality with
     *
     * @return true if this response message is equal to the other,
     *     false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(services, response.services);
    }

    /**
     * Gets hashcode of Response message
     *
     * @return hashcode of response message
     */
    @Override
    public int hashCode() {
        return Objects.hash(services);
    }

    /**
     * Determines if a new service can be added
     *
     * @param service service to be added
     *
     * @throws IllegalArgumentException
     *     if new service puts response message over maximum payload size
     */
    private void validateSize(Service service)
            throws IllegalArgumentException {
        // Get length with header and service to add.
        int payloadLength = Constants.JACK_HEADER +
                service.toString().length();
        // Add length of existing services.
        for(Service s : this.services){
            payloadLength += s.toString().length() + 1;
        }
        // Check overall size would not exceed a UDP payload.
        if(payloadLength > Constants.MAX_UDP){
            throw new IllegalArgumentException("Payload too large for UDP! "
                    + toString());
        }
    }
}
