/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 2
 * Class: CSI 4321
 *
 ************************************************/
package megex.app.client.test;

import megex.app.client.Client;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Tests for Client class
 */
public class ClientTest {
    /** Output stream for redirection */
    private static ByteArrayOutputStream o;

    /**
     * Redirect output to output stream.
     */
    @BeforeAll
    public static void redirectOutput(){
        o = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(o);
        System.setOut(p);
        System.setErr(p);
    }

    /**
     * Clear output stream.
     */
    @AfterEach
    public void closeRedirect() {
        o.reset();
    }

    /**
     * Test valid client run.
     */
    @DisplayName("Valid test")
    @Test
    public void validTest() {
        String [] test = new String[3];
        test[0] = "www.patrickeharris.tk";
        test[1] = "443";
        test[2] = "/index.html";

        Assertions.assertDoesNotThrow(() -> Client.main(test));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Settings: StreamID=0"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Window_Update:"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Headers:"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Data:"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Data:"));
    }

    /**
     * Test client run with 404 error.
     */
    @DisplayName("404 error")
    @Test
    public void notFoundTest() {
        String [] test = new String[3];
        test[0] = "www.mineplex.com";
        test[1] = "443";
        test[2] = "/three";

        Assertions.assertDoesNotThrow(() -> Client.main(test));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Settings: StreamID=0"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Window_Update:"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Headers:"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Bad status: 404"));
    }

    /**
     * Test client run with invalid message type.
     */
    @DisplayName("Invalid type")
    @Test
    public void invalidType() {
        String [] test = new String[4];
        test[0] = "www.google.com";
        test[1] = "443";
        test[2] = "/doesnotexist.html";
        test[3] = "/index.html";

        Assertions.assertDoesNotThrow(() -> Client.main(test));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Settings: StreamID=0"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Window_Update:"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received unknown type:"));
    }

    /**
     * Test client with valid and 404 error.
     */
    @DisplayName("404 following valid run")
    @Test
    public void invalidFollowingValidTest() {
        String [] test = new String[4];
        test[0] = "www.google.com";
        test[1] = "443";
        test[2] = "/index.html";
        test[3] = "/doesnotexist.html";

        Assertions.assertDoesNotThrow(() -> Client.main(test));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Settings: StreamID=0"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Window_Update:"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Headers: StreamID=1 isEnd=false"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Headers: StreamID=3 isEnd=false"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Data: StreamID=1 isEnd=false"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Received message: Data: StreamID=1 isEnd=true"));
        Assertions.assertTrue(contains(o.toByteArray(),
                "Bad status: 404"));
    }

    /**
     * Checks if byte array contains stream.
     *
     * @param b byte array to check
     * @param contains string to see if contained
     *
     * @return true if byte array contains string, false otherwise
     */
    private boolean contains(byte [] b, String contains){
        if(new String(b).contains(contains)){
            return true;
        }
        return false;
    }
}
