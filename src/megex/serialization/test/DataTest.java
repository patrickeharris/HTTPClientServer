/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import megex.serialization.BadAttributeException;
import megex.serialization.Data;
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
 * Tests for Data class
 */
public class DataTest {
    /** Default constructor for javadoc */
    public DataTest(){}
    /**
     * Tests for Data constructor
     */
    @Nested
    @DisplayName("Tests for Data Constructor")
   protected class ConstructorTests {
        /**
         * Test successful construction of a Data object
         *
         * @param streamID id of the stream for data message
         * @param isEnd whether data message is last of frame
         * @param data payload of data message
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1} data = {2}")
        @ArgumentsSource(ValidData.class)
        @DisplayName("Test successful construction of a Data Object")
        protected void constructorSuccess(int streamID, boolean isEnd,
                                       byte[] data) {
            Assertions.assertDoesNotThrow(() ->
                    new Data(streamID, isEnd, data));
        }

        /**
         * Tests construction of a Data object with invalid
         *   parameters throws a BadAttributeException
         *
         * @param streamID id of the stream for data message
         * @param isEnd whether data message is last of frame
         * @param data payload of data message
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1} data = {2}")
        @ArgumentsSource(InvalidData.class)
        @DisplayName("Tests constructor invalid parameters")
        protected void constructorInvalid(int streamID, boolean isEnd,
                                       byte[] data) {
            Assertions.assertThrows(BadAttributeException.class, () ->
                    new Data(streamID, isEnd, data));
        }
    }

    /**
     * Tests for isEnd method
     */
    @Nested
    @DisplayName("Tests for isEnd")
   protected class IsEndTests {
        Data dataObj;
        /**
         * Test successful isEnd
         *
         * @param streamID id of the stream for data message
         * @param isEnd whether data message is last of frame
         * @param data payload of data message
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1} data = {2}")
        @ArgumentsSource(ValidData.class)
        @DisplayName("Test successful isEnd")
        protected void isEndSuccess(int streamID, boolean isEnd, byte[] data) {
            Assertions.assertDoesNotThrow(() -> dataObj =
                    new Data(streamID, isEnd, data));
            Assertions.assertEquals(isEnd, dataObj.isEnd());
        }
    }

    /**
     * Tests for setEnd method
     */
    @Nested
    @DisplayName("Tests for setEnd")
   protected class SetEndTests {
        Data dataObj;
        /**
         * Test successful setEnd
         *
         * @param id id of the stream for data message
         * @param end whether data message is last of frame
         * @param endNew new end value for data message
         * @param data payload of data message
         */
        @ParameterizedTest(name = "id = {0} end = {1} endNew = {2} data = {3}")
        @ArgumentsSource(SetEndData.class)
        @DisplayName("Test successful setEnd")
        protected void setEndSuccess(int id, boolean end, boolean endNew,
                                  byte[] data) {
            Assertions.assertDoesNotThrow(() -> dataObj =
                    new Data(id, end, data));
            dataObj.setEnd(endNew);
            Assertions.assertEquals(endNew, dataObj.isEnd());
        }
    }

    /**
     * Tests for getData method
     */
    @Nested
    @DisplayName("Tests for getData")
   protected class GetDataTests {
        Data dataObj;
        /**
         * Test successful getDataEnd
         *
         * @param streamID id of the stream for data message
         * @param isEnd whether data message is last of frame
         * @param data payload of data message
         */
        @ParameterizedTest(name = "streamID = {0} isEnd = {1} data = {2}")
        @ArgumentsSource(ValidData.class)
        @DisplayName("Test successful getData")
        protected void getDataSuccess(int streamID, boolean isEnd, byte[] data) {
            Assertions.assertDoesNotThrow(() -> dataObj =
                    new Data(streamID, isEnd, data));
            Assertions.assertEquals(data, dataObj.getData());
        }
    }

    /**
     * Tests for setData method
     */
    @Nested
    @DisplayName("Tests for setData")
   protected class SetDataTests {
        Data dataObj;
        /**
         * Test successful setData
         *
         * @param id id of the stream for data message
         * @param end whether data message is last of frame
         * @param dat payload of data message
         * @param newDat new payload for data message
         */
        @ParameterizedTest(name = "id = {0} end = {1} dat = {2} newDat = {3}")
        @ArgumentsSource(ValidSetData.class)
        @DisplayName("Test successful setData")
        protected void setDataSuccess(int id, boolean end, byte[] dat,
                                   byte[] newDat) {
            Assertions.assertDoesNotThrow(() -> dataObj =
                    new Data(id, end, dat));
            Assertions.assertDoesNotThrow(()->dataObj.setData(newDat));
            Assertions.assertEquals(newDat, dataObj.getData());
        }

        /**
         * Test setData with invalid data throws BadAttributeException
         *
         * @param id id of the stream for data message
         * @param end whether data message is last of frame
         * @param dat payload of data message
         * @param newDat new invalid payload for data message
         */
        @ParameterizedTest(name = "id = {0} end = {1} dat = {2} newDat = {3}")
        @ArgumentsSource(InvalidSetData.class)
        @DisplayName("Test setData invalid data")
        protected void setDataInvalidData(int id, boolean end, byte[] dat,
                                       byte[] newDat) {
            Assertions.assertDoesNotThrow(() -> dataObj =
                    new Data(id, end, dat));
            Assertions.assertThrows(BadAttributeException.class, ()->
                    dataObj.setData(newDat));
        }
    }

    /**
     * Tests for toString method
     */
    @Nested
    @DisplayName("Tests for toString")
   protected class ToStringTests {
        Data dataObj;
        /**
         * Test successful toString
         *
         * @param id id of the stream for data message
         * @param end whether data message is last of frame
         * @param data payload of data message
         * @param str expected string representation of data message
         */
        @ParameterizedTest(name = "id = {0} end = {1} data = {2} str = {3}")
        @ArgumentsSource(ToString.class)
        @DisplayName("Test successful toString")
        protected void toStringSuccess(int id, boolean end, byte[] data,
                                    String str) {
            Assertions.assertDoesNotThrow(() -> dataObj =
                    new Data(id, end, data));
            Assertions.assertEquals(str, dataObj.toString());
        }
    }

    /**
     * Arguments for constructing valid data message
     */
    protected static class ValidData implements ArgumentsProvider {

        /**
         * Provides valid stream id, is end value, and data payload
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, and payload values.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA.forEach(data ->
                                    list.add(Arguments.of(id, end, data)))));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing invalid data message
     */
    protected static class InvalidData implements ArgumentsProvider {

        /**
         * Provides invalid stream id or data payload
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of invalid data id, end, and payload values.
            TestConstants.INVALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA.forEach(data ->
                                    list.add(Arguments.of(id, end, data)))));
            // Add all combinations of valid data id, end, and invalid payload
            //   values.
            TestConstants.INVALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.INVALID_DATA.forEach(data ->
                                    list.add(Arguments.of(id, end, data)))));
            // Add all combinations of valid data id, end, and invalid payload
            //   values.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.INVALID_DATA.forEach(data ->
                                    list.add(Arguments.of(id, end, data)))));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing data message and setting the isEnd
     */
    protected static class SetEndData implements ArgumentsProvider {

        /**
         * Provides valid stream id, is end value, new end value, and data
         *   payload
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, new end and payload
            //   values.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA_IS_END.stream().forEach(
                                    newEnd ->
                                    TestConstants.VALID_DATA.forEach(data ->
                                            list.add(Arguments.of(
                                                    id,
                                                    end,
                                                    newEnd,
                                                    data
                                            ))))));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing data message and setting the payload
     */
    protected static class ValidSetData implements ArgumentsProvider {

        /**
         * Provides valid stream id, is end value, data payload, and new data
         *   payload
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, payload, and new
            //   payload values.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA.stream().forEach(data ->
                                    TestConstants.VALID_DATA.forEach(newData ->
                                            list.add(Arguments.of(
                                                    id,
                                                    end,
                                                    data,
                                                    newData
                                            ))))));
            return list.stream();
        }
    }

    /**
     * Arguments for constructing data message and setting the payload to and
     *   invalid value
     */
    protected static class InvalidSetData implements ArgumentsProvider {

        /**
         * Provides valid stream id, is end value, data payload, and new
         *   invalid data payload
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, payload, and new
            //   invalid payload values.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA.stream().forEach(data ->
                                    TestConstants.INVALID_DATA.forEach(
                                            newData -> list.add(Arguments.of(
                                                    id,
                                                    end,
                                                    data,
                                                    newData
                                            ))))));
            return list.stream();
        }
    }


    /**
     * Arguments for constructing data message and expected string
     *   representation
     */
    protected static class ToString implements ArgumentsProvider {

        /**
         * Provides valid stream id, is end value, data payload, and expected
         *   string representation
         *
         * @return arguments for test
         */
        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            List<Arguments> list = new ArrayList<>();
            // Add all combinations of valid data id, end, and payload values
            //   with expected string representation of each.
            TestConstants.VALID_DATA_STREAM_ID.stream().forEach(id ->
                    TestConstants.VALID_DATA_IS_END.stream().forEach(end ->
                            TestConstants.VALID_DATA.forEach(data ->
                                    list.add(Arguments.of(id, end, data,
                                            getString(id, end, data.length)
                                    )))));
            return list.stream();
        }

        /**
         * Provides expected string representation of a data object given its
         *   parameters
         *
         * @return string representation of data object
         */
        private String getString(int streamID, boolean isEnd, int dataLength){
            return "Data: StreamID=" + streamID + " isEnd=" + isEnd +
                    " data=" + dataLength;
        }
    }
}
