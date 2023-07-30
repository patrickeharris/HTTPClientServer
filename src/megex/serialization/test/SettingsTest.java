/************************************************
 *
 * Author: Patrick Harris
 * Assignment: Program 1
 * Class: CSI 4321
 *
 ************************************************/
package megex.serialization.test;

import megex.serialization.Settings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for Settings class
 */
public class SettingsTest {
    /** Default constructor for javadoc */
    public SettingsTest(){}
    /**
     * Tests for Settings constructor
     */
    @Nested
    @DisplayName("Tests for Settings Constructor")
    protected class ConstructorTests {
        /**
         * Test successful construction of a Settings object
         */
        @Test
        @DisplayName("Test successful construction of a Settings Object")
        protected void constructorSuccess() {
            Assertions.assertDoesNotThrow(() -> new Settings());
        }
    }

    /**
     * Tests for toString method
     */
    @Nested
    @DisplayName("Tests for toString")
    protected class ToStringTests {
        Settings settings;
        /**
         * Test successful toString
         */
        @Test
        @DisplayName("Test successful toString")
        protected void toStringSuccess() {
            Assertions.assertDoesNotThrow(() -> settings = new Settings());
            Assertions.assertEquals("Settings: StreamID=0",
                    settings.toString());
        }
    }
}
