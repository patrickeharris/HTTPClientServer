/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import megex.serialization.BadAttributeException;
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
 * Tests for BadAttributeException class
 */
@DisplayName("BadAttributeException Tests")
public class BadAttributeExceptionTest {
    /** Default constructor for javadoc */
    public BadAttributeExceptionTest(){}
    /**
     * Tests for BadAttributeException constructors
     */
    @Nested
    @DisplayName("Tests for BadAttributeException Constructors")
    protected class ConstructorTests {
        /**
         * Tests for BadAttributeException constructor with 2 arguments
         */
        @Nested
        @DisplayName("Tests for BadAttributeException 2 argument constructor")
        protected class TwoArgConstructorTests {
            /**
             * Test successful construction of a BadAttributeException
             *
             * @param message message for exception
             * @param attribute attribute that caused exception
             */
            @ParameterizedTest(name = "message = {0} attribute = {1}")
            @ArgumentsSource(ValidBadAttributeException.class)
            @DisplayName("Successful construction of BadAttributeException")
            protected void constructorSuccess(String message, String attribute) {
                Assertions.assertDoesNotThrow(()->
                        new BadAttributeException(message, attribute));
            }

            /**
             * Tests construction of a BadAttributeException with null
             *   parameters throws a NullPointerException
             *
             * @param message message for exception
             * @param attribute attribute that caused exception
             */
            @ParameterizedTest(name = "message = {0} attribute = {1}")
            @ArgumentsSource(InvalidBadAttributeException.class)
            @DisplayName("Tests constructor null parameters")
            protected void constructorNull(String message, String attribute) {
                Assertions.assertThrows(NullPointerException.class,
                        ()-> new BadAttributeException(message, attribute));
            }
        }

        /**
         * Tests for BadAttributeExceptionTest constructor with 3 arguments
         */
        @Nested
        @DisplayName("BadAttributeExceptionTest Constructor with 3 arguments")
        protected class ThreeArgConstructorTests {
            /**
             * Test successful construction of a BadAttributeException
             *
             * @param msg message for exception
             * @param attribute attribute that caused exception
             * @param cause throwable that caused exception
             */
            @ParameterizedTest(name = "msg = {0} attribute = {1} cause = {2}")
            @ArgumentsSource(ValidBadAttribute3Arg.class)
            @DisplayName("Successful construction of a BadAttributeException")
            protected void constructorSuccess(String msg, String attribute,
                                           Throwable cause) {
                Assertions.assertDoesNotThrow(()->
                        new BadAttributeException(msg, attribute, cause));
            }

            /**
             * Tests construction of a BadAttributeException with null
             *   parameters throws a NullPointerException
             *
             * @param msg message for exception
             * @param attribute attribute that caused exception
             * @param cause throwable that caused exception
             */
            @ParameterizedTest(name = "msg = {0} attribute = {1} cause = {2}")
            @ArgumentsSource(InvalidBadAttribute3Arg.class)
            @DisplayName("Tests constructor null parameters")
            protected void constructorNull(String msg, String attribute,
                                        Throwable cause) {
                Assertions.assertThrows(NullPointerException.class,
                        ()-> new BadAttributeException(msg, attribute, cause));
            }
        }
    }

    /**
     * Tests for getAttribute method
     */
    @Nested
    @DisplayName("Tests for getAttribute")
    protected class GetAttributeTests {
        /**
         * Test successful getAttribute
         *
         * @param exception exception that was thrown
         * @param expectedAttribute expected attribute from exception
         */
        @ParameterizedTest(name = "exception = {0} expectedAttribute = {1}")
        @ArgumentsSource(ValidGetAttribute.class)
        @DisplayName("Test successful getAttribute")
        protected void getAttributeSuccess(BadAttributeException exception,
                                        String expectedAttribute) {
            Assertions.assertEquals(expectedAttribute,
                    exception.getAttribute());
        }
    }

    /**
     * Arguments for constructing valid bad attribute exception with two
     *   argument constructor
     */
    protected static class ValidBadAttributeException implements ArgumentsProvider {

        /**
         * Provides valid message and attribute
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Generic message and attribute
            list.add(Arguments.of(
                    "Message", "Attribute"
            ));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing valid bad attribute exception with three
     *   argument constructor
     */
    protected static class ValidBadAttribute3Arg implements ArgumentsProvider {

        /**
         * Provides valid message, attribute, and throwable
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Concrete throwable
            list.add(Arguments.of(
                    "Message", "Attribute", new Throwable()
            ));
            // Null throwable
            list.add(Arguments.of(
                    "Message", "Attribute", null
            ));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing invalid bad attribute exception with two
     *   argument constructor
     */
    protected static class InvalidBadAttributeException implements ArgumentsProvider {

        /**
         * Provides invalid message or attribute
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Invalid attribute
            list.add(Arguments.of(
                    "Message", null
            ));
            // Invalid message
            list.add(Arguments.of(
                    null, "Attribute"
            ));
            // Both invalid
            list.add(Arguments.of(
                    null, null
            ));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing valid bad attribute exception with three
     *   argument constructor
     */
    protected static class InvalidBadAttribute3Arg implements ArgumentsProvider {

        /**
         * Provides invalid message or attribute with throwable
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Invalid attribute with concrete throwable
            list.add(Arguments.of(
                    "Message", null, new Throwable()
            ));
            // Invalid message with concrete throwable
            list.add(Arguments.of(
                    null, "Attribute", new Throwable()
            ));
            // Both invalid with concrete throwable
            list.add(Arguments.of(
                    null, null, new Throwable()
            ));
            // Invalid attribute with null throwable
            list.add(Arguments.of(
                    "Message", null, null
            ));
            // Invalid message with null throwable
            list.add(Arguments.of(
                    null, "Attribute", null
            ));
            // Both invalid with null throwable
            list.add(Arguments.of(
                    null, null, null
            ));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing valid bad attribute exception and
     *   getting attribute
     */
    protected static class ValidGetAttribute implements ArgumentsProvider {

        /**
         * Provides valid arguments and expected attribute
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Two argument constructor
            list.add(Arguments.of(
                    new BadAttributeException("Message", "Attribute"),
                    "Attribute"
            ));
            // Three argument constructor with concrete throwable
            list.add(Arguments.of(
                    new BadAttributeException("Message", "Attribute",
                            new Throwable()), "Attribute"
            ));
            // Three argument constructor with null throwable
            list.add(Arguments.of(
                    new BadAttributeException("Message", "Attribute", null),
                    "Attribute"
            ));
            return list.stream();
        }
    }
}
