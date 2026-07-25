package config;

import exceptions.ConfigurationLoaderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationLoaderTest {
    @Test
    void staticMethod_shouldThrow_whenClasspathIsNull() {

        assertThrows(
                ConfigurationLoaderException.class,
                ()-> ConfigurationLoader.load(null)
        );
    }

    @Test
    void staticMethod_shouldThrow_whenClasspathNotExist(){
        assertThrows(
                ConfigurationLoaderException.class,
                ()-> ConfigurationLoader.load("not/exist.properties")
        );
    }

    @Test
    void staticMethod_shouldThrow_whenClasspathIsEmpty() {
        assertThrows(
                ConfigurationLoaderException.class,
                ()-> ConfigurationLoader.load("")
        );
    }

    @Test
    void staticMethod_shouldThrow_whenClasspathIsBlank() {
        assertThrows(
                ConfigurationLoaderException.class,
                ()-> ConfigurationLoader.load("    ")
        );
    }

    @Test
    void load_shouldReturnConfiguration_whenResourceExists() {
        Configuration config = ConfigurationLoader.load("test-configurationLoader.properties");

        assertNotNull(config);
    }

    @Test
    void load_shouldReadAllProperties() {
        Configuration config = ConfigurationLoader.load("test-configurationLoader.properties");

        assertEquals("VulnaRebel", config.getString("load.name"));
        assertTrue(config.getBoolean("load.testphase"));
        assertEquals(8080, config.getInt("load.port"));
    }
}
