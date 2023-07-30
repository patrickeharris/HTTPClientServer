/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import megex.serialization.Deframer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Tests for Deframer class
 */
@DisplayName("Deframer Tests")
public class DeframerTest {
    /** Default constructor for javadoc */
    public DeframerTest(){}
    /**
     * Tests for Deframer constructor
     */
    @Nested
    @DisplayName("Tests for Deframer Constructor")
    protected class ConstructorTests {
        /**
         * Test successful construction of a Deframer object
         */
        @Test
        @DisplayName("Test successful construction of a Deframer")
        protected void constructorSuccess() {
            // Initialize input stream with a valid frame.
            byte[] array = TestConstants.FRAME1;
            InputStream in = new ByteArrayInputStream(array);

            // Test constructor does not throw any errors.
            Assertions.assertDoesNotThrow(() -> new Deframer(in));
        }

        /**
         * Tests construction of a Deframer object with a null
         *   InputStream throws a NullPointerException
         */
        @Test
        @DisplayName("Tests constructor null input stream")
        protected void constructorNull() {
            // Test when passing in a null input stream that the constructor
            //   throws a NullPointerException.
            Assertions.assertThrows(NullPointerException.class,
                    () -> new Deframer(null));
        }
    }

    /**
     * Tests for getFrame method
     */
    @Nested
    @DisplayName("Tests for getFrame()")
    protected class GetFrameTests {
        // Input stream to read frames from.
        InputStream in;
        // Deframer to convert frame to message.
        Deframer deframer;

        /**
         * Closes input stream after using it
         *
         * @throws IOException
         *   if an IO error occurs closing the stream
         */
        @AfterEach
        protected void closeStream() throws IOException {
            in.close();
        }

        /**
         * Test successful retrieval of a frame from an InputStream
         *
         * @param frame the given frame to deconstruct
         * @param expectedMessage the payload expected to retrieve from the
         *                        given frame
         *
         * @throws IOException
         *   if an IO error occurs when reading from the input stream
         */
        @DisplayName("Test successful message retrieval")
        @ParameterizedTest(name = "frame = {0} expectedMessage = {1}")
        @ArgumentsSource(ValidFrame.class)
        protected void getFrameSuccess(byte [] frame, byte [] expectedMessage)
                                    throws IOException {
            in = new ByteArrayInputStream(frame);
            deframer = new Deframer(in);

            // Tests message from frame in input stream matches expected
            //   message.
            Assertions.assertArrayEquals(expectedMessage, deframer.getFrame());
        }

        /**
         * Test successful retrieval of multiple frames from an InputStream
         *
         * @param frame the given frame to deconstruct
         * @param expectedMessage1 the first payload expected to retrieve from
         *                         the given frame
         * @param expectedMessage2 the second payload expected to retrieve from
         *                         the given frame
         * @param expectedMessage3 the third payload expected to retrieve from
         *                         the given frame
         *
         * @throws IOException
         *   if an IO error occurs when reading from the input stream
         */
        @DisplayName("Test successful retrieval of multiple messages")
        @ParameterizedTest(name = "frame = {0} expectedMessage1 = {1}" +
                " expectedMessage2 = {2} expectedMessage3 = {3}")
        @ArgumentsSource(ValidMultiFrames.class)
        protected void getMultipleFrameSuccess(byte [] frame,
                                            byte [] expectedMessage1,
                                            byte [] expectedMessage2,
                                            byte [] expectedMessage3)
                                            throws IOException {
            in = new ByteArrayInputStream(frame);
            deframer = new Deframer(in);
            // Tests all messages from frames in input stream match expected
            //   message.
            Assertions.assertArrayEquals(expectedMessage1, deframer.getFrame());
            Assertions.assertArrayEquals(expectedMessage2, deframer.getFrame());
            Assertions.assertArrayEquals(expectedMessage3, deframer.getFrame());
        }

        /**
         * Tests when a frame terminates early in the header or payload that an
         *   EOFException is thrown
         *
         * @param frame the given frame to deconstruct
         */
        @DisplayName("Tests short frames throw EOFException")
        @ParameterizedTest(name = "frame = {0}")
        @ArgumentsSource(SmallFrame.class)
        protected void getFrameEOF(byte [] frame) {
            in = new ByteArrayInputStream(frame);
            deframer = new Deframer(in);
            // Tests frames that are too short throw EOFException.
            Assertions.assertThrows(EOFException.class,
                    () -> deframer.getFrame());
        }

        /**
         * Test successful retrieval of a frame and a thrown EOFException
         *   from a frame that terminates early in the same InputStream
         *
         * @param frame the given frame to deconstruct
         * @param expectedMessage the payload expected to retrieve from the
         *                        given frame
         *
         * @throws IOException
         *   if an IO error occurs when reading from the input stream
         */
        @DisplayName("Test short message following successful retrieval")
        @ParameterizedTest(name = "frame = {0} expectedMessage = {1}")
        @ArgumentsSource(MultiFrameEOF.class)
        protected void getMultipleFrameSuccessEOFHeader(byte [] frame,
                                                     byte [] expectedMessage)
                                                     throws IOException {
            in = new ByteArrayInputStream(frame);
            deframer = new Deframer(in);
            // Tests first frame matches message and following short frame
            //   throws EOFException.
            Assertions.assertArrayEquals(expectedMessage, deframer.getFrame());
            Assertions.assertThrows(EOFException.class,
                    () -> deframer.getFrame());
        }

        /**
         * Tests when the InputStream is closed before reading that an
         *   IOException is thrown
         *
         * @param frame the given frame to deconstruct
         *
         * @throws IOException
         *   if an IO error occurs closing the input stream
         */
        @DisplayName("Tests closing stream before reading throws IOException")
        @ParameterizedTest(name = "frame = {0}")
        @ArgumentsSource(ValidFrame.class)
        protected void getFrameIO(byte [] frame) throws IOException {
            // Construct an input stream that can be closed to throw exception.
            in = new ByteArrayInputStream(frame);
            InputStream invalidIn = new BufferedInputStream(in);
            // Close input stream before reading and tests that reading
            //   throws an IOException.
            invalidIn.close();
            deframer = new Deframer(invalidIn);
            Assertions.assertThrows(IOException.class,
                    () -> deframer.getFrame());
        }

        /**
         * Tests when a frame has a payload size that is too large that an
         *   IllegalArgumentException is thrown
         *
         * @param frame the given frame to deconstruct
         */
        @DisplayName("Tests get IllegalArgumentException with large payload")
        @ParameterizedTest(name = "frame = {0}")
        @ArgumentsSource(LargeFrame.class)
        protected void getFrameTooLarge(byte [] frame) {
            in = new ByteArrayInputStream(frame);
            deframer = new Deframer(in);
            // Tests frame with too large of a payload throws and
            //   IllegalArgumentException.
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> deframer.getFrame());
        }

        /**
         * Test successful retrieval of a frame and a thrown
         *   IllegalArgumentException from a frame with too
         *   large of a payload in the same InputStream
         *
         * @param frame the given frame to deconstruct
         * @param expectedMessage the payload expected to retrieve from the
         *                        given frame
         *
         * @throws IOException
         *   if an IO error occurs when reading from the input stream
         */
        @DisplayName("Test illegal argument after successful retrieval")
        @ParameterizedTest(name = "frame = {0} expectedMessage = {1}")
        @ArgumentsSource(MultiFrameTooLarge.class)
        protected void getMultipleFrameSuccessTooLarge(byte [] frame,
                                                    byte [] expectedMessage)
                                                    throws IOException {
            in = new ByteArrayInputStream(frame);
            deframer = new Deframer(in);
            // Tests first frame matches message and following frame with too
            //   large of a payload throws an IllegalArgumentException.
            Assertions.assertArrayEquals(expectedMessage, deframer.getFrame());
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> deframer.getFrame());
        }
    }

    /**
     * Arguments for deconstructing valid frames
     */
    static protected class ValidFrame implements ArgumentsProvider {
        /**
         * Provides a valid frame and the expected message
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // One-byte message.
            list.add(Arguments.of(TestConstants.FRAME1,
                                  TestConstants.MESSAGE1));
            // Message with all zeros.
            list.add(Arguments.of(TestConstants.FRAME2,
                                  TestConstants.MESSAGE2));
            // Multi-byte message.
            list.add(Arguments.of(TestConstants.FRAME3,
                                  TestConstants.MESSAGE3));
            // Message with large values.
            list.add(Arguments.of(TestConstants.FRAME4,
                                  TestConstants.MESSAGE4));
            // Message with prepended zeros.
            list.add(Arguments.of(TestConstants.FRAME5,
                                  TestConstants.MESSAGE5));
            // Frame from empty message.
            list.add(Arguments.of(TestConstants.FRAME_EMPTY,
                                  TestConstants.MESSAGE_EMPTY));
            return list.stream();
        }
    }

    /**
     * Arguments for deconstructing multiple valid frames for the same deframer
     */
    static protected class ValidMultiFrames implements ArgumentsProvider {
        /**
         * Provides valid frames and the expected messages
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Multi-byte message three times.
            list.add(Arguments.of(
                    TestConstants.FRAME3x3,
                    TestConstants.MESSAGE3,
                    TestConstants.MESSAGE3,
                    TestConstants.MESSAGE3
            ));
            // One-byte message, multi-byte message and message with large
            //   values.
            list.add(Arguments.of(
                    TestConstants.FRAME1x3x4,
                    TestConstants.MESSAGE1,
                    TestConstants.MESSAGE3,
                    TestConstants.MESSAGE4
            ));
            // Message with all zeros, message with large values, and message
            //   with prepended zeros.
            list.add(Arguments.of(
                    TestConstants.FRAME2x4x5,
                    TestConstants.MESSAGE2,
                    TestConstants.MESSAGE4,
                    TestConstants.MESSAGE5
            ));
            return list.stream();
        }
    }

    /**
     * Frames with too large of a payload
     */
    static protected class LargeFrame implements ArgumentsProvider {
        /**
         * Provides frames that have a payload larger than the maximum allowed
         *   size
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Frame with maximum three-byte payload size that is too large.
            list.add(Arguments.of(TestConstants.LARGE_FRAME1));
            // Frame that has payload size just above what is allowed.
            list.add(Arguments.of(TestConstants.LARGE_FRAME2));
            return list.stream();
        }
    }

    /**
     * Frames that end early
     */
    static protected class SmallFrame implements ArgumentsProvider {
        /**
         * Provides frames that are shorter than expected
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Frame that ends early in the header.
            list.add(Arguments.of(TestConstants.FRAME_EOF_HEADER));
            // Frame that ends early in the payload.
            list.add(Arguments.of(TestConstants.FRAME_EOF_PAYLOAD));
            // Frame that has no payload.
            list.add(Arguments.of(TestConstants.FRAME_NO_PAYLOAD));
            return list.stream();
        }
    }

    /**
     * Frames that end early after valid frames
     */
    static protected class MultiFrameEOF implements ArgumentsProvider {
        /**
         * Provides frames that are shorter than expected after valid frames
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Frame that ends early in the header following multi-byte frame.
            list.add(Arguments.of(TestConstants.MULTI_FRAME_EOF_HEADER,
                                  TestConstants.MESSAGE3));
            // Frame that ends early in the payload following frame with large
            //   values.
            list.add(Arguments.of(TestConstants.MULTI_FRAME_EOF_PAYLOAD,
                    TestConstants.MESSAGE4));
            // Frame with no payload following message with prepended zeros.
            list.add(Arguments.of(TestConstants.MULTI_FRAME_NO_PAYLOAD,
                    TestConstants.MESSAGE5));
            return list.stream();
        }
    }

    /**
     * Frames with too large of a payload after valid frames
     */
    static protected class MultiFrameTooLarge implements ArgumentsProvider {
        /**
         * Provides frames that have a payload larger than the maximum allowed
         *   size after valid frames
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Frame with maximum three-byte payload size that is too large
            //   following frame with large values.
            list.add(Arguments.of(TestConstants.MULTI_LARGE_FRAME1,
                                  TestConstants.MESSAGE4));
            // Frame that has payload size just above what is allowed following
            //   frame with prepended zeros.
            list.add(Arguments.of(TestConstants.MULTI_LARGE_FRAME2,
                                  TestConstants.MESSAGE5));
            return list.stream();
        }
    }
}
