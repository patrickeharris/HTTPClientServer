/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import megex.serialization.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Tests for MessageFactory class
 */
public class MessageFactoryTest {
    /** Default constructor for javadoc */
    public MessageFactoryTest(){}
    /**
     * Tests for MessageFactory constructors
     */
    @Nested
    @DisplayName("Tests for MessageFactory Constructors")
    protected class ConstructorTests {
        /**
         * Test successful construction of a MessageFactory object
         */
        @Test
        @DisplayName("Test successful construction of a MessageFactory Object")
        protected void constructorSuccess() {
            Assertions.assertDoesNotThrow(() -> new MessageFactory());
        }
    }

    /**
     * Tests for decode method
     */
    @Nested
    @DisplayName("Tests for decode")
    protected class DecodeTests {
        MessageFactory messageFactory;
        /**
         * Test successful decode
         *
         * @param msgBytes byte array to decode
         * @param expectedMsg message object from byte array
         */
        @ParameterizedTest(name = "msgBytes = {0} expectedMsg = {1}")
        @ArgumentsSource(Valid.class)
        @DisplayName("Test successful decode")
        protected void decodeSuccess(byte[] msgBytes, Message expectedMsg) {
            messageFactory = new MessageFactory();
            Assertions.assertEquals(expectedMsg, Assertions.assertDoesNotThrow(
                    () -> messageFactory.decode(msgBytes)));
        }

        /**
         * Test decode with null message bytes throws NullPointerException
         */
        @Test
        @DisplayName("Test decode null message")
        protected void decodeNull() {
            messageFactory = new MessageFactory();
            Assertions.assertThrows(NullPointerException.class,
                    ()-> messageFactory.decode(null));
        }

        /**
         * Test decode with validation failure throws BadAttributeException
         *
         * @param invalidMsgBytes invalid frame to decode
         */
        @ParameterizedTest(name = "invalidMsgBytes = {0}")
        @ArgumentsSource(InvalidDecode.class)
        @DisplayName("Test decode validation failure")
        protected void decodeValidationFailure(byte[] invalidMsgBytes) {
            messageFactory = new MessageFactory();
            Assertions.assertThrows(BadAttributeException.class,
                    ()-> messageFactory.decode(invalidMsgBytes));
        }
    }

    /**
     * Tests for encode method
     */
    @Nested
    @DisplayName("Tests for encode")
    protected class EncodeTests {
        MessageFactory messageFactory;
        /**
         * Test successful encode
         *
         * @param expectedMsgBytes expected frame
         * @param msg message to encode
         */
        @ParameterizedTest(name = "expectedMsgBytes = {0} msg = {1} ")
        @ArgumentsSource(Valid.class)
        @DisplayName("Test successful encode")
        protected void encodeSuccess(byte[] expectedMsgBytes, Message msg) {
            messageFactory = new MessageFactory();
            Assertions.assertArrayEquals(expectedMsgBytes,
                    messageFactory.encode(msg));
        }

        /**
         * Test decode with null message throws NullPointerException
         */
        @Test
        @DisplayName("Test encode null message")
        protected void encodeNull() {
            messageFactory = new MessageFactory();
            Assertions.assertThrows(NullPointerException.class,
                    ()-> messageFactory.encode(null));
        }
    }

    /**
     * Arguments for valid encoding and decoding
     */
    static protected class Valid implements ArgumentsProvider {

        /**
         * Provides valid frame and message object
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context)
                throws BadAttributeException {
            List<Arguments> list = new ArrayList<>();
            // One byte data message.
            list.add(Arguments.of(
                    TestConstants.ONE_BYTE_DATA_FRAME,
                    new Data(1, false, TestConstants.ONE_BYTE_DATA_MESSAGE)
            ));
            // Multiple byte data message.
            list.add(Arguments.of(
                    TestConstants.MULTI_BYTE_DATA_FRAME,
                    new Data(1, false, TestConstants.MULTI_BYTE_DATA_MESSAGE)
            ));
            // Multiple byte data message with large payload values.
            list.add(Arguments.of(
                    TestConstants.MULTI_BYTE_LARGE_DATA_FRAME,
                    new Data(1, false,
                            TestConstants.MULTI_BYTE_LARGE_DATA_MESSAGE)
            ));
            // Data message with no payload.
            list.add(Arguments.of(
                    TestConstants.EMPTY_DATA_FRAME,
                    new Data(1, false, TestConstants.EMPTY_DATA)
            ));
            // One byte data message with isEnd flag set.
            list.add(Arguments.of(
                    TestConstants.END_ONE_BYTE_DATA_FRAME,
                    new Data(1, true, TestConstants.ONE_BYTE_DATA_MESSAGE)
            ));
            // Multiple byte data message with isEnd flag set.
            list.add(Arguments.of(
                    TestConstants.END_MULTI_BYTE_DATA_FRAME,
                    new Data(1, true, TestConstants.MULTI_BYTE_DATA_MESSAGE)
            ));
            // Multiple byte data message with large payload values and isEnd flag set.
            list.add(Arguments.of(
                    TestConstants.END_MULTI_BYTE_LARGE_DATA_FRAME,
                    new Data(1, true,
                            TestConstants.MULTI_BYTE_LARGE_DATA_MESSAGE)
            ));
            // Data message with no payload and isEnd flag set.
            list.add(Arguments.of(
                    TestConstants.END_EMPTY_DATA_FRAME,
                    new Data(1, true, TestConstants.EMPTY_DATA)
            ));
            // Settings frame.
            list.add(Arguments.of(
                    TestConstants.ZERO_ID_EMPTY_SETTINGS_FRAME,
                    new Settings()
            ));
            // Window frame.
            list.add(Arguments.of(
                    TestConstants.WINDOW_FRAME,
                    new Window_Update(1, 1)
            ));
            // Window frame with max increment.
            list.add(Arguments.of(
                    TestConstants.LARGE_WINDOW_FRAME,
                    new Window_Update(1, Integer.MAX_VALUE)
            ));
            // Window frame with stream id of 0.
            list.add(Arguments.of(
                    TestConstants.ZERO_ID_WINDOW_FRAME,
                    new Window_Update(0, 1)
            ));
            // Window frame with stream id of 0 and max increment.
            list.add(Arguments.of(
                    TestConstants.ZERO_ID_LARGE_WINDOW_FRAME,
                    new Window_Update(0, Integer.MAX_VALUE)
            ));
            return list.stream();
        }
    }

    /**
     * Arguments for invalid decoding
     */
    static protected class InvalidDecode implements ArgumentsProvider {

        /**
         * Provides invalid frames
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // add all invalid frames.
            TestConstants.INVALID_DECODE.stream().forEach(decode ->
                    list.add(Arguments.of(decode)));
            return list.stream();
        }
    }



}
