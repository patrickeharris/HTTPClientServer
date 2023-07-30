/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import megex.serialization.*;
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
 * Tests for Message class
 */
@DisplayName("Message Tests")
public class MessageTest {
    /** Default constructor for javadoc */
    public MessageTest(){}
    /**
     * Tests for getCode method
     */
    @Nested
    @DisplayName("Tests for getCode")
    protected class GetCodeTests {
        /**
         * Test successful getCode
         *
         * @param m message with a type code
         * @param expectedCode code expected for message m
         */
        @ParameterizedTest(name = "message = {0} expectedCode = {1}")
        @ArgumentsSource(ValidGetCode.class)
        @DisplayName("Test successful getCode")
        protected void getCodeSuccess(Message m, byte expectedCode) {
            Assertions.assertEquals(expectedCode, m.getCode());
        }
    }

    /**
     * Tests for getStreamID method
     */
    @Nested
    @DisplayName("Tests for getStreamID")
    protected class GetStreamIDTests {
        /**
         * Test successful getStreamID
         *
         * @param m message with a stream id
         * @param expectedStreamID stream id expected for message m
         */
        @ParameterizedTest(name = "message = {0} expectedStreamID = {1}")
        @ArgumentsSource(ValidGetStreamID.class)
        @DisplayName("Test successful getStreamID")
        protected void getStreamIDSuccess(Message m, int expectedStreamID) {
            Assertions.assertEquals(expectedStreamID, m.getStreamID());
        }
    }


    /**
     * Tests for setStreamID method
     */
    @Nested
    @DisplayName("Tests for setStreamID")
    protected class SetStreamIDTests {
        /**
         * Test successful setStreamID
         *
         * @param m message with a stream id
         * @param newStreamID new stream id to give to message m
         */
        @ParameterizedTest(name = "message = {0} newStreamID = {1}")
        @ArgumentsSource(ValidSetStreamID.class)
        @DisplayName("Test successful setStreamID")
        protected void setStreamIDSuccess(Message m, int newStreamID) {
            Assertions.assertDoesNotThrow(() -> m.setStreamID(newStreamID));
            Assertions.assertEquals(newStreamID, m.getStreamID());
        }

        /**
         * Test setStreamID with invalid input stream id throws
         *   BadAttributeException
         *
         * @param m message with a stream id
         * @param invalidID invalid stream id to give to message m
         */
        @ParameterizedTest(name = "message = {0} expectedCode = {1}")
        @ArgumentsSource(InvalidSetStreamID.class)
        @DisplayName("Test setStreamID with invalid id")
        protected void setStreamInvalidID(Message m, int invalidID) {
            Assertions.assertThrows(BadAttributeException.class,
                    ()-> m.setStreamID(invalidID));
        }
    }

    /**
     * Arguments for constructing valid message and expected code
     */
    static protected class ValidGetCode implements ArgumentsProvider {

        /**
         * Provides valid message and expected code
         *
         * @throws BadAttributeException
         *   if message cannot be constructed
         * @returns arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context)
                throws BadAttributeException {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, and payload values
            //   with expected data code.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA.forEach(data -> {
                                try {
                                    list.add(Arguments.of(new Data(id, end,
                                            data), (byte) 0));
                                } catch (BadAttributeException ignored) {
                                }
            })));

            // Add settings message with expected settings code.
            list.add(Arguments.of(
                    new Settings(), (byte) 4
            ));

            // Add all combinations of valid window update ids and increments
            // with expected window update code.
            TestConstants.VALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_WINDOW_UPDATE_INCREMENT.stream()
                            .forEach(inc -> {
                                try {
                                    list.add(Arguments.of(
                                            new Window_Update(id, inc),
                                            (byte) 8
                                    ));
                                } catch (BadAttributeException ignored) {
                                }
            }));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing valid message and expected stream id
     */
    static protected class ValidGetStreamID implements ArgumentsProvider {

        /**
         * Provides valid message and expected stream id
         *
         * @throws BadAttributeException
         *   if message cannot be constructed
         * @returns arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context)
                throws BadAttributeException {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, and payload values
            //   with expected stream id.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA.forEach(data -> {
                                try {
                                    list.add(Arguments.of(
                                            new Data(id, end, data),
                                            id
                                    ));
                                } catch (BadAttributeException ignored) {
                                }
            })));

            // Add settings with settings stream id.
            list.add(Arguments.of(
                    new Settings(), 0
            ));

            // Add all combinations of valid window update id and increment
            //   with expected stream id.
            TestConstants.VALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_WINDOW_UPDATE_INCREMENT.stream()
                            .forEach(inc -> {
                                try {
                                    list.add(Arguments.of(
                                            new Window_Update(id, inc),
                                            id
                                    ));
                                } catch (BadAttributeException ignored) {
                                }
            }));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing valid message and setting stream id
     */
    static protected class ValidSetStreamID implements ArgumentsProvider {

        /**
         * Provides valid message and stream id to set
         *
         * @throws BadAttributeException
         *   if message cannot be constructed
         * @returns arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context)
                throws BadAttributeException {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, and payload values
            //   with combinations of new stream id to set.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA.forEach(data ->
                                    TestConstants.VALID_DATA_STREAM_ID.stream()
                                            .forEach(newID -> {
                                                try {
                                                    list.add(Arguments.of(
                                                            new Data(
                                                                    id, end, data
                                                            ),
                                                            newID
                                                    ));
                                                } catch (BadAttributeException
                                                        ignored) {
                                                }
            }))));

            // Add settings message with stream id to set.
            list.add(Arguments.of(
                    new Settings(), 0
            ));

            // Add all combinations of valid window update ids and increments
            //   with combinations of new stream id to set.
            TestConstants.VALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_WINDOW_UPDATE_INCREMENT.stream()
                            .forEach(inc -> TestConstants
                                    .VALID_WINDOW_UPDATE_STREAM_ID.stream()
                                    .forEach(newID -> {
                                        try {
                                            list.add(Arguments.of(
                                                    new Window_Update(id, inc),
                                                    newID));
                                        } catch (BadAttributeException
                                                ignored) {
                                        }
            })));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing valid message and setting invalid stream id
     */
    static protected class InvalidSetStreamID implements ArgumentsProvider {

        /**
         * Provides valid message and invalid stream id to set
         *
         * @throws BadAttributeException
         *   if message cannot be constructed
         * @returns arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context)
                throws BadAttributeException {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, and payload values
            //   with combinations of invalid stream id to set.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA.forEach(data ->
                                    TestConstants.INVALID_DATA_STREAM_ID
                                            .stream().forEach(newID -> {
                                                try {
                                                    list.add(Arguments.of(
                                                            new Data(
                                                                    id,
                                                                    end,
                                                                    data
                                                            ),
                                                            newID
                                                    ));
                                                } catch (BadAttributeException
                                                        ignored) {
                                                }
            }))));

            // Add settings message with invalid positive stream id to set.
            list.add(Arguments.of(
                    new Settings(), 1
            ));

            // Add settings message with invalid negative stream id to set.
            list.add(Arguments.of(
                    new Settings(), -1
            ));

            // Add all combinations of valid window updates ids and increments
            //   with combinations of invalid stream id to set.
            TestConstants.VALID_WINDOW_UPDATE_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_WINDOW_UPDATE_INCREMENT.stream()
                            .forEach(inc -> TestConstants
                                    .INVALID_WINDOW_UPDATE_STREAM_ID.stream()
                                    .forEach(newID -> {
                                        try {
                                            list.add(Arguments.of(
                                                    new Window_Update(id, inc),
                                                    newID
                                            ));
                                        } catch (BadAttributeException
                                                ignored) {
                                        }
            })));
            return list.stream();
        }
    }
}
