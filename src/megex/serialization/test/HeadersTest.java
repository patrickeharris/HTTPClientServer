/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 2
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import megex.serialization.BadAttributeException;
import megex.serialization.Headers;
import megex.serialization.MessageFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.*;
import java.util.stream.Stream;

/**
 * Tests for Headers class
 */
@DisplayName("Tests for Headers class")
public class HeadersTest {
    /**
     *  Headers object to use for testing
     */
    Headers h;

    /**
     * Tests for Headers class constructor
     */
    @Nested
    @DisplayName("Tests for Headers Constructor")
    protected class ConstructorTests {
        /**
         * Tests successful construction of Headers object
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1}")
        @ArgumentsSource(ValidHeaders.class)
        @DisplayName("Tests Headers constructor valid")
        public void constructorSuccess(int streamID, boolean isEnd){
            // Constructor should not throw.
            Assertions.assertDoesNotThrow(() -> h = new Headers(streamID,
                    isEnd));
        }

        /**
         * Tests invalid construction of Headers object throws BadAttributeException
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1}")
        @ArgumentsSource(InvalidHeaders.class)
        @DisplayName("Tests Headers constructor invalid")
        public void constructorInvalid(int streamID, boolean isEnd){
            // Constructor should throw bad attribute exception.
            Assertions.assertThrows(BadAttributeException.class, () ->
                    new Headers(streamID, isEnd));
        }
    }

    /**
     * Tests for Headers class getters
     */
    @Nested
    @DisplayName("Getters Tests")
    protected class GettersTests{
        /**
         * Tests for Headers isEnd method
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1}")
        @ArgumentsSource(ValidHeaders.class)
        @DisplayName("Tests Headers ieEnd")
        public void getIsEndTest(int streamID, boolean isEnd){
            // Construction should not throw.
            Assertions.assertDoesNotThrow(() -> h = new Headers(streamID,
                    isEnd));
            // End values should be equal.
            Assertions.assertEquals(isEnd, h.isEnd());
        }

        /**
         * Tests for Headers getNames method
         */
        @ParameterizedTest(name = "headers = {0} names = {1}")
        @ArgumentsSource(GetNames.class)
        @DisplayName("Tests Headers getNames")
        public void getNamesTest(Headers headers, List<String> names){
            // Names should be equal.
            Assertions.assertArrayEquals(headers.getNames().toArray(),
                    names.toArray());
        }

        /**
         * Tests for Headers getValue method
         */
        @ParameterizedTest(name = "headers = {0} pairs = {1}")
        @ArgumentsSource(GetPairs.class)
        @DisplayName("Tests Headers getValue")
        public void getValueTest(Headers headers, Map<String, String> pairs){
            // Names should be equal.
            Assertions.assertArrayEquals(headers.getNames().toArray(),
                    pairs.keySet().toArray());
            // For each name, value should be equal.
            for(String name : headers.getNames()){
                Assertions.assertEquals(headers.getValue(name),
                        pairs.get(name));
            }
        }
    }

    @Nested
    @DisplayName("Setters Tests")
    protected class SettersTests{
        /**
         * Tests for Headers setEnd method
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1} isEndNew = {2}")
        @ArgumentsSource(SetEnd.class)
        @DisplayName("Tests Headers setEnd")
        public void setIsEndTest(int streamID, boolean isEnd,
                                 boolean isEndNew){
            // Construction should not throw.
            Assertions.assertDoesNotThrow(() -> h = new Headers(streamID,
                    isEnd));
            // Setting should not throw.
            Assertions.assertDoesNotThrow(() -> h.setEnd(isEndNew));
            // New end value should be set.
            Assertions.assertEquals(isEndNew, h.isEnd());
        }

        /**
         * Tests for Headers addValue method with valid pair
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1} pairs = {2}")
        @ArgumentsSource(SetPairs.class)
        @DisplayName("Tests Headers addValue valid")
        public void addValueSuccess(int streamID, boolean isEnd,
                                    Map<String, String> pairs){
            // Construction should not throw.
            Assertions.assertDoesNotThrow(() -> h = new Headers(streamID,
                    isEnd));
            // Adding each name, value pair should not throw.
            for(String name: pairs.keySet()) {
                Assertions.assertDoesNotThrow(() -> h.addValue(name,
                        pairs.get(name)));
            }

            // Name arrays should be equal.
            Assertions.assertArrayEquals(h.getNames().toArray(),
                    pairs.keySet().toArray());
            // For each name, values should be equal/
            for(String name : h.getNames()){
                Assertions.assertEquals(h.getValue(name), pairs.get(name));
            }
        }

        /**
         * Tests for Headers addValue method with invalid pair
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1} pairs = {2}")
        @ArgumentsSource(SetPairsInvalid.class)
        @DisplayName("Tests Headers addValue invalid")
        public void addValueFail(int streamID, boolean isEnd, Map<String,
                String> pairs){
            // Construction should not throw.
            Assertions.assertDoesNotThrow(() -> h = new Headers(streamID,
                    isEnd));
            // Adding each pair should throw exception.
            for(String name: pairs.keySet()) {
                Assertions.assertThrows(BadAttributeException.class, () ->
                        h.addValue(name, pairs.get(name)));
            }
            // No pairs should be added.
            Assertions.assertEquals(0, h.getNames().size());
        }
    }

    @ParameterizedTest(name = "streamID = {0} end = {1} pairs = {2} exp = {3}")
    @ArgumentsSource(ToString.class)
    @DisplayName("Tests Headers toString")
    public void toStringTest(int streamID, boolean end,
                             Map<String, String> pairs, String exp){
        // Construction should not throw.
        Assertions.assertDoesNotThrow(() -> h = new Headers(streamID, end));
        // Adding each pair should not throw.
        for(String name: pairs.keySet()) {
            Assertions.assertDoesNotThrow(() -> h.addValue(name,
                    pairs.get(name)));
        }

        // String should be equal.
        Assertions.assertEquals(exp, h.toString());
    }

    /**
     * Arguments for constructing valid Headers
     */
    protected static class ValidHeaders implements ArgumentsProvider {

        /**
         * Provides valid stream id and is end value
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid id, and end values.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            list.add(Arguments.of(id, end))));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing invalid Headers
     */
    protected static class InvalidHeaders implements ArgumentsProvider {

        /**
         * Provides invalid stream id and is end value
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of invalid id, and end values.
            TestConstants.INVALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            list.add(Arguments.of(id, end))));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing Headers and setting the isEnd
     */
    protected static class SetEnd implements ArgumentsProvider {

        /**
         * Provides valid stream id, is end value, and new end value
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid id, end, and new end values.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA_IS_END.stream().forEach(
                                    newEnd ->
                                                list.add(Arguments.of(
                                                        id,
                                                        end,
                                                        newEnd
                                                )))));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing Headers and getting the list of names
     */
    protected static class GetNames implements ArgumentsProvider {

        /**
         * Provides valid headers message and list of names
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid id, end, and names.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->{
                        final Headers[] h = new Headers[1];
                            Assertions.assertDoesNotThrow(() -> h[0] =
                                    new Headers(id, end));
                            TestConstants.VALID_NAMES.stream().forEach(name ->
                                TestConstants.VALID_VALUES.stream().forEach(
                                        value ->
                                        Assertions.assertDoesNotThrow(() ->
                                                h[0].addValue(name, value))
                                    ));

                            list.add(Arguments.of(
                                   h[0], TestConstants.VALID_NAMES
                            ));}));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing Headers and getting the map of name, value
     *   pairs
     */
    protected static class GetPairs implements ArgumentsProvider {

        /**
         * Provides valid headers message and map of name, value pairs
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of data id, end, and valid name value
            //   pairs.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->{
                        final Headers[] h = new Headers[1];
                        Map<String, String> pairs = new LinkedHashMap<>();
                        Assertions.assertDoesNotThrow(() -> h[0] =
                                new Headers(id, end));
                        TestConstants.VALID_NAMES.stream().forEach(name ->
                                TestConstants.VALID_VALUES.stream().forEach(
                                        value -> {
                                            Assertions.assertDoesNotThrow(() ->
                                                    h[0].addValue(name,
                                                            value));
                                            pairs.put(name, value);
                                        }));

                        list.add(Arguments.of(
                                h[0], pairs
                        ));}));
            return list.stream();
        }
    }

    protected static class SetPairs implements ArgumentsProvider {

        /**
         * Provides valid headers message and map of name, value pairs
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, and valid name value
            //   pairs.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->{
                        Map<String, String> pairs = new HashMap<>();
                        TestConstants.VALID_NAMES.stream().forEach(name ->
                                TestConstants.VALID_VALUES.stream().forEach(
                                        value ->
                                            pairs.put(name, value)
                                        ));

                        list.add(Arguments.of(
                                id, end, pairs
                        ));}));
            return list.stream();
        }
    }

    protected static class SetPairsInvalid implements ArgumentsProvider {

        /**
         * Provides valid headers message and map of name, value pairs
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, and invalid name
            //   value pairs.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->{
                        Map<String, String> pairs = new HashMap<>();
                        TestConstants.VALID_NAMES.stream().forEach(name ->
                                TestConstants.INVALID_VALUES.stream().forEach(
                                        value ->
                                                pairs.put(name, value)
                                ));

                        list.add(Arguments.of(
                                id, end, pairs
                        ));}));
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->{
                        Map<String, String> pairs = new HashMap<>();
                        TestConstants.INVALID_NAMES.stream().forEach(name ->
                                TestConstants.VALID_VALUES.stream().forEach(
                                        value ->
                                                pairs.put(name, value)
                                ));

                        list.add(Arguments.of(
                                id, end, pairs
                        ));}));
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->{
                        Map<String, String> pairs = new HashMap<>();
                        TestConstants.INVALID_NAMES.stream().forEach(name ->
                                TestConstants.INVALID_VALUES.stream().forEach(
                                        value ->
                                                pairs.put(name, value)
                                ));

                        list.add(Arguments.of(
                                id, end, pairs
                        ));}));
            return list.stream();
        }
    }

    protected static class ToString implements ArgumentsProvider {

        /**
         * Provides valid headers message and map of name, value pairs
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, name value pairs,
            //   and expected string representation.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->{
                        Map<String, String> pairs = new HashMap<>();
                        String expected = "Headers: StreamID=" + id + " isEnd="
                                + end + " (";
                        list.add(Arguments.of(
                                id, end, new HashMap<>(), expected + ")"
                        ));
                        TestConstants.VALID_NAMES.stream().forEach(name ->
                                TestConstants.VALID_VALUES.stream().forEach(
                                        value ->
                                                pairs.put(name, value)
                                ));
                        for (Map.Entry<String, String> pair :
                                pairs.entrySet()) {
                            expected += "[" + pair.getKey() + "=" +
                                    pair.getValue() + "]";
                        }
                        expected += ")";
                        list.add(Arguments.of(
                                id, end, pairs, expected
                        ));}));

            return list.stream();
        }
    }
}
