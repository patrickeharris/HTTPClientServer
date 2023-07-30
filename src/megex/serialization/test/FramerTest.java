/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import megex.serialization.Framer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Tests for Framer class
 */
@DisplayName("Framer Tests")
public class FramerTest {
    /** Default constructor for javadoc */
    public FramerTest(){}
    /**
     * Tests for Framer constructor
     */
    @Nested
    @DisplayName("Tests for Framer Constructor")
    protected class ConstructorTests {
        /**
         * Test successful construction of a Framer object
         */
        @Test
        @DisplayName("Test successful construction of a Framer")
        protected void constructorSuccess() {
            // Test constructor does not throw any errors with valid output
            //   stream.
            OutputStream out = new ByteArrayOutputStream();
            Assertions.assertDoesNotThrow(() -> new Framer(out));
        }

        /**
         * Tests construction of a Framer object with a null OutputStream
         *   throws a NullPointerException
         */
        @Test
        @DisplayName("Test constructor null output stream")
        protected void constructorNull() {
            // Test when passing in a null output stream that the constructor
            //   throws a NullPointerException.
            Assertions.assertThrows(NullPointerException.class,
                    () -> new Framer(null));
        }
    }

    /**
     * Tests for putFrame method
     */
    @Nested
    @DisplayName("Tests for putFrame()")
    protected class PutFrameTests {
        // Output stream to put frames in.
        ByteArrayOutputStream out;
        // Framer to convert messages to frames.
        Framer framer;

        /**
         * Initialize new output stream and framer for each test.
         */
        @BeforeEach
        protected void initFramer(){
            out = new ByteArrayOutputStream();
            framer = new Framer(out);
        }

        /**
         * Closes output stream after using it
         *
         * @throws IOException
         *   if an IO error occurs closing the stream
         */
        @AfterEach
        protected void closeStream() throws IOException {
            out.close();
        }

        /**
         * Test successful construction of a frame and that the frame is added
         *   to the OutputStream, including an empty frame.
         *
         * @param message the payload of the frame to be constructed
         * @param expected the expected frame to be constructed with the
         *                 given payload
         */
        @DisplayName("Test successful frame construction")
        @ParameterizedTest(name = "message = {0} expected = {1}")
        @ArgumentsSource(ValidMessage.class)
        protected void putFrameSuccess(byte [] message, byte [] expected) {
            // Tests frame is successfully constructed with given message
            //   and matches expected.
            Assertions.assertDoesNotThrow(() -> framer.putFrame(message));
            Assertions.assertArrayEquals(expected, out.toByteArray());
        }

        /**
         * Tests when the OutputStream is closed before writing the frame to it
         *   that an IOException is thrown
         *
         * @param message the payload of the frame to be constructed
         * @throws IOException
         *   if an IO error occurs when making or closing the output stream
         */
        @DisplayName("Tests throwing IO Exception when stream is closed")
        @ParameterizedTest(name = "message = {0}")
        @ArgumentsSource(ValidMessage.class)
        protected void putFrameIO(byte[] message) throws IOException {
            // Construct output stream that can be closed.
            PipedInputStream in = new PipedInputStream();
            OutputStream invalidOut = new PipedOutputStream(in);
            // Close output stream before writing.
            invalidOut.close();
            Framer framer = new Framer(invalidOut);
            // Tests when writing to closed output stream that an IOException
            //   is thrown.
            Assertions.assertThrows(IOException.class,
                    () -> framer.putFrame(message));
        }

        /**
         * Tests when a message is too long to fit in a frame that an
         *   IllegalArgumentException is thrown
         *
         * @param message the payload of the frame to be constructed
         */
        @DisplayName("Tests throwing IllegalArgument when message too long")
        @ParameterizedTest(name = "message = {0}")
        @ArgumentsSource(LargeMessage.class)
        protected void putFrameTooLong(byte [] message) {
            // Tests when a message is too long that an
            //   IllegalArgumentException is thrown.
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> framer.putFrame(message));
        }

        /**
         * Tests when a payload is null that a NullPointerException is thrown
         */
        @Test
        @DisplayName("Tests throwing NullPointerException with null message")
        protected void putFrameNull() {
            // Tests when a message is null that a NullPointerException is
            //   thrown.
            Assertions.assertThrows(NullPointerException.class,
                    () -> framer.putFrame(null));
        }

        /**
         * Tests successful generation of multiple frames to an output stream
         *
         * @param message1 first payload to frame
         * @param message2 second payload to frame
         * @param message3 third payload to frame
         * @param expected expected frames
         */
        @DisplayName("Test multiple successful frame constructions")
        @ParameterizedTest(name = "message1 = {0} message2 = {1} " +
                "message3 = {2} expected = {3}")
        @ArgumentsSource(MultipleValidMessages.class)
        protected void putMultiFrameSuccess(byte [] message1, byte[] message2,
                                         byte [] message3, byte [] expected) {
            // Tests construction of multiple frames from messages results in
            //   correct output stream.
            Assertions.assertDoesNotThrow(() -> framer.putFrame(message1));
            Assertions.assertDoesNotThrow(() -> framer.putFrame(message2));
            Assertions.assertDoesNotThrow(() -> framer.putFrame(message3));
            Assertions.assertArrayEquals(expected, out.toByteArray());
        }

        /**
         * Tests successful generation of frames after a NullPointerException
         *   is thrown
         *
         * @param message payload to frame
         * @param expected expected frame
         */
        @DisplayName("Test successful frame construction after null message")
        @ParameterizedTest(name = "message = {0} expected = {1}")
        @ArgumentsSource(ValidMessage.class)
        protected void putFrameMultipleNull(byte [] message, byte [] expected) {
            // Tests first null message throws NullPointerException and
            //   following message is constructed into a frame and matches
            //   the expected frame.
            Assertions.assertThrows(NullPointerException.class,
                    () -> framer.putFrame(null));
            Assertions.assertDoesNotThrow(() -> framer.putFrame(message));
            Assertions.assertArrayEquals(expected, out.toByteArray());
        }

        /**
         * Tests successful generation of frames after an
         *   IllegalArgumentException is thrown
         *
         * @param message1 invalid payload
         * @param message2 payload to frame
         * @param expected expected frame
         */
        @DisplayName("Tests successful frame construction after bad message")
        @ParameterizedTest(name = "message1 = {0} message2 = {1}" +
                           " expected = {2}")
        @ArgumentsSource(MultipleMessagesWithLarge.class)
        protected void putFrameMultipleTooLong(byte [] message1, byte [] message2,
                                            byte [] expected) {
            // Tests first message that is too long throws
            //   IllegalArgumentException and following message is correctly
            //   constructed into a frame and matches expected frame.
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                framer.putFrame(message1);
            });
            Assertions.assertDoesNotThrow(() -> framer.putFrame(message2));
            Assertions.assertArrayEquals(expected, out.toByteArray());
        }
    }

    /**
     * Arguments for constructing valid frames
     */
    static protected class ValidMessage implements ArgumentsProvider {
        /**
         * Provides a valid message and the expected frame
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // One-byte message.
            list.add(Arguments.of(TestConstants.MESSAGE1,
                                  TestConstants.FRAME1));
            // Message with all zeros.
            list.add(Arguments.of(TestConstants.MESSAGE2,
                                  TestConstants.FRAME2));
            // Multi-byte message.
            list.add(Arguments.of(TestConstants.MESSAGE3,
                                  TestConstants.FRAME3));
            // Message with large values.
            list.add(Arguments.of(TestConstants.MESSAGE4,
                                  TestConstants.FRAME4));
            // Message with prepended zeros.
            list.add(Arguments.of(TestConstants.MESSAGE5,
                                  TestConstants.FRAME5));
            // Frame from empty message.
            list.add(Arguments.of(TestConstants.MESSAGE_EMPTY,
                                  TestConstants.FRAME_EMPTY));
            return list.stream();
        }
    }

    /**
     * Messages too large for a frame
     */
    static protected class LargeMessage implements ArgumentsProvider {
        /**
         * Provides messages that are too large to be a frame payload
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Message that is just over size allowed.
            list.add(Arguments.of(
                    new byte[TestConstants.MAX_PAYLOAD_SIZE +
                            TestConstants.HEADER_SIZE + 1]));
            // Message that is way over size allowed.
            list.add(Arguments.of(
                    new byte[TestConstants.MAX_PAYLOAD_SIZE +
                            TestConstants.HEADER_SIZE +  1000]));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing multiple valid frames for the same framer
     */
    static protected class MultipleValidMessages implements ArgumentsProvider {
        /**
         * Provides valid messages and the expected frames in the output stream
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Multi-byte message three times.
            list.add(Arguments.of(
                    TestConstants.MESSAGE3,
                    TestConstants.MESSAGE3,
                    TestConstants.MESSAGE3,
                    TestConstants.FRAME3x3
            ));
            // One-byte message, multi-byte message and message with large
            //   values.
            list.add(Arguments.of(
                    TestConstants.MESSAGE1,
                    TestConstants.MESSAGE3,
                    TestConstants.MESSAGE4,
                    TestConstants.FRAME1x3x4
            ));
            // Message with all zeros, message with large values, and message
            //   with prepended zeros.
            list.add(Arguments.of(
                    TestConstants.MESSAGE2,
                    TestConstants.MESSAGE4,
                    TestConstants.MESSAGE5,
                    TestConstants.FRAME2x4x5
            ));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing multiple valid frame after
     *   IllegalArgumentException is thrown
     */
    static protected class MultipleMessagesWithLarge implements ArgumentsProvider {
        /**
         * Provides invalid message, valid message, and the expected frame
         *   to see in the output stream
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Message that is just over size allowed followed by message with
            //   large values.
            list.add(Arguments.of(
                    new byte[TestConstants.MAX_PAYLOAD_SIZE +
                    TestConstants.HEADER_SIZE + 1],
                    TestConstants.MESSAGE4,
                    TestConstants.FRAME4
            ));
            // Message that is way over size allowed followed by message with
            //   prepended zeros.
            list.add(Arguments.of(
                    new byte[TestConstants.MAX_PAYLOAD_SIZE +
                    TestConstants.HEADER_SIZE + 1000],
                    TestConstants.MESSAGE5,
                    TestConstants.FRAME5
            ));
            return list.stream();
        }
    }

}
