/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import megex.serialization.BadAttributeException;
import megex.serialization.Window_Update;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Tests for Window_Update class
 */
public class Window_UpdateTest {
    /** Default constructor for javadoc */
    public Window_UpdateTest(){}
    /**
     * Tests for Window_Update constructor
     */
    @Nested
    @DisplayName("Tests for Window_Update Constructor")
    protected class ConstructorTests {
        /**
         * Test successful construction of a Window_Update object
         *
         * @param streamID id of stream for message
         * @param increment increment value for window update
         */
        @ParameterizedTest(name = "streamID = {0} increment = {1}")
        @ArgumentsSource(ValidWindowUpdate.class)
        @DisplayName("Test successful construction of a Window_Update Object")
        protected void constructorSuccess(int streamID, int increment) {
            Assertions.assertDoesNotThrow(() -> new Window_Update(streamID,
                    increment));
        }

        /**
         * Tests construction of a Window_Update object with invalid
         *   parameters throws a BadAttributeException
         *
         * @param streamID id of stream for message
         * @param invalidIncrement invalid increment value for window update
         */
        @ParameterizedTest(name = "streamID = {0} invalidIncrement = {1}")
        @ArgumentsSource(InvalidWindowUpdate.class)
        @DisplayName("Tests constructor invalid parameters")
        protected void constructorInvalid(int streamID, int invalidIncrement) {
            Assertions.assertThrows(BadAttributeException.class, () ->
                    new Window_Update(streamID, invalidIncrement));
        }
    }

    /**
     * Tests for getIncrement method
     */
    @Nested
    @DisplayName("Tests for getIncrement")
    protected class GetIncrementTests {
        Window_Update window_update;

        /**
         * Test successful getIncrement
         *
         * @param streamID id of stream for message
         * @param increment increment value for window update
         */
        @ParameterizedTest(name = "streamID = {0} increment = {1}")
        @ArgumentsSource(ValidWindowUpdate.class)
        @DisplayName("Test successful getIncrement")
        protected void getIncrementSuccess(int streamID, int increment) {
            Assertions.assertDoesNotThrow(() -> window_update =
                    new Window_Update(streamID, increment));
            Assertions.assertEquals(increment, window_update.getIncrement());
        }
    }

    /**
     * Tests for setIncrement method
     */
    @Nested
    @DisplayName("Tests for setIncrement")
    protected class SetIncrementTests {
        Window_Update window_update;

        /**
         * Test successful setIncrement
         *
         * @param id id of stream for message
         * @param inc increment value for window update
         * @param newInc new increment value to set
         */
        @ParameterizedTest(name = "id = {0} inc = {1} newInc = {2}")
        @ArgumentsSource(ValidSetIncrement.class)
        @DisplayName("Test successful setIncrement")
        protected void setIncrementSuccess(int id, int inc, int newInc) {
            Assertions.assertDoesNotThrow(() -> window_update =
                    new Window_Update(id, inc));
            Assertions.assertDoesNotThrow(() -> window_update.setIncrement(
                    newInc));
            Assertions.assertEquals(newInc, window_update.getIncrement());
        }

        /**
         * Test setIncrement with invalid increment throws
         *   BadAttributeException
         *
         * @param id id of stream for message
         * @param inc increment value for window update
         * @param invalidInc new invalid increment value to set
         */
        @ParameterizedTest(name = "id = {0} inc = {1} invalidInc = {2}")
        @ArgumentsSource(InvalidSetIncrement.class)
        @DisplayName("Test setIncrement with invalid increment")
        protected void setIncrementInvalid(int id, int inc, int invalidInc) {
            Assertions.assertDoesNotThrow(() -> window_update =
                    new Window_Update(id, inc));
            Assertions.assertThrows(BadAttributeException.class, ()->
                    window_update.setIncrement(invalidInc));
        }
    }

    /**
     * Tests for toString method
     */
    @Nested
    @DisplayName("Tests for toString")
    protected class ToStringTests {
        Window_Update window_update;

        /**
         * Test successful toString
         *
         * @param id id of stream for message
         * @param increment increment value for window update
         * @param expected expected string value with given parameters
         */
        @ParameterizedTest(name = "id = {0} increment = {1} expected = {2}")
        @ArgumentsSource(ToString.class)
        @DisplayName("Test successful toString")
        protected void toStringSuccess(int id, int increment, String expected) {
            Assertions.assertDoesNotThrow(() -> window_update =
                    new Window_Update(id, increment));
            Assertions.assertEquals(expected, window_update.toString());
        }
    }


    /**
     * Arguments for constructing valid window update message
     */
    static protected class ValidWindowUpdate implements ArgumentsProvider {

        /**
         * Provides valid stream id and increment
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid stream id and increment.
            TestConstants.VALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(id ->
            TestConstants.VALID_WINDOW_UPDATE_INCREMENT.stream().forEach(inc ->
                    list.add(Arguments.of(id, inc))));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing invalid window update message
     */
    static protected class InvalidWindowUpdate implements ArgumentsProvider {

        /**
         * Provides invalid stream id or increment
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of invalid stream id and valid increment.
            TestConstants.INVALID_WINDOW_UPDATE_STREAM_ID.stream()
                    .forEach(id -> TestConstants.VALID_WINDOW_UPDATE_INCREMENT
                            .stream().forEach(inc ->
                                    list.add(Arguments.of(id, inc))));

            // Add all combinations of valid stream id and invalid increment.
            TestConstants.VALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(
                    id -> TestConstants.INVALID_WINDOW_UPDATE_INCREMENT
                            .stream().forEach(inc ->
                                    list.add(Arguments.of(id, inc))));

            // Add all combinations of invalid stream id and invalid increment.
            TestConstants.INVALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(
                    id -> TestConstants.INVALID_WINDOW_UPDATE_INCREMENT
                            .stream().forEach(inc ->
                                    list.add(Arguments.of(id, inc))));
            return list.stream();
        }
    }

    /**
     * Arguments for setting window update increment
     */
    static protected class ValidSetIncrement implements ArgumentsProvider {

        /**
         * Provides valid stream id, initial increment, and increment to set
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid stream id, increment, and
            //   increment to set.
            TestConstants.VALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(
                    id -> TestConstants.VALID_WINDOW_UPDATE_INCREMENT.stream()
                            .forEach(inc -> TestConstants
                                    .VALID_WINDOW_UPDATE_INCREMENT.stream()
                                    .forEach(newInc ->
                                            list.add(Arguments.of(
                                                    id,
                                                    inc,
                                                    newInc
                                            )))));


            return list.stream();
        }
    }

    /**
     * Arguments for setting window update increment with invalid increment
     */
    static protected class InvalidSetIncrement implements ArgumentsProvider {

        /**
         * Provides valid stream id, initial increment, and invalid increment
         *   to set
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid stream id, increment, and
            //   invalid increment to set.
            TestConstants.VALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(
                    id -> TestConstants.VALID_WINDOW_UPDATE_INCREMENT.stream()
                            .forEach(inc -> TestConstants
                                    .INVALID_WINDOW_UPDATE_INCREMENT.stream()
                                    .forEach(newInc ->
                                            list.add(Arguments.of(
                                                    id,
                                                    inc,
                                                    newInc
                                            )))));


            return list.stream();
        }
    }

    /**
     * Arguments for getting window update as a string
     */
    static protected class ToString implements ArgumentsProvider {

        /**
         * Provides valid stream id, increment, and expected string
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid stream id and increment with
            //   matching expected string.
            TestConstants.VALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(
                    id -> TestConstants.VALID_WINDOW_UPDATE_INCREMENT.stream()
                            .forEach(inc -> list.add(Arguments.of(
                                    id,
                                    inc,
                                    getString(id, inc)
                            ))));
            return list.stream();
        }

        /**
         * Makes string representation of a window update object given a
         *   stream id and increment
         *
         * @return string representation of a window update object
         */
        private String getString(int streamID, int increment){
            return "Window_Update: StreamID=" + streamID + " increment="
                    + increment;
        }
    }
}
