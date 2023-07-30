/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package utils;

/**
 * Utility functions used by classes.
 */
public class Utilities {
    /** Default constructor for javadoc */
    protected Utilities(){

    }

    /**
     * Turns an integer into a three-byte array
     *
     * @param i the integer to convert
     * @param size the size of the byte array
     *
     * @return byte array representation of the integer
     */
    public static byte[] int2byte(int i, int size){
        // Allocate array for integer.
        byte[] ret = new byte[size];

        // Starting from lower-order byte, copy to array and shift integer.
        for(int j = size - 1; j >= 0; j--){
            ret[j] = (byte) i;
            i = i >> Byte.SIZE;
        }

        return ret;
    }

    /**
     * Convert a byte array to an integer.
     *
     * @param buf the byte array to convert
     * @param size the size of the integer in bytes
     *
     * @return the integer represented by the byte array
     */
    public static int byte2int(byte[] buf, int size){
        int ret = 0;
        // Starting with the higher order byte, add it to the integer
        //   in an unsigned manner and shift.
        for(int i = 0; i < size; i++){
            ret = ret << Byte.SIZE;
            ret += (buf[i] & 0xFF);
        }

        return ret;
    }

    /**
     * Validates that a byte array contains ASCII characters between 32 and 127
     *
     * @param bytes the byte array to check
     *
     * @return true if all bytes are good, false otherwise
     */
    public static boolean validateBytes(byte[] bytes){
        for(byte b : bytes){
            if(b < Constants.MIN_BYTE){
                return false;
            }
        }
        return true;
    }

    /**
     * Return a subset of a byte array
     *
     * @param arr the byte array to find a subset of
     * @param startByte the index to start the subset
     * @param size the number of bytes for the subset
     *
     * @return the subset of the byte array
     */
    public static byte[] arraySubset(byte[] arr, int startByte, int size) {
        byte[] data = new byte[size];
        System.arraycopy(arr, startByte, data, 0, size);
        return data;
    }
}
