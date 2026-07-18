package config;

import exceptions.ConfigurationException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


public class ConfigurationTest {

    @Test
    void constructor_shouldThrow_whenPropertiesAreNull() {

        assertThrows(
                ConfigurationException.class,
                () -> new Configuration(null)
        );
    }

    @Test
    void constructor_shouldThrow_whenPropertiesAreEmpty() {

        assertThrows(
                ConfigurationException.class,
                () -> new Configuration(createProperties())
        );
    }

    @Test
    void getString_shouldReturnConfiguredValue() {

        Configuration configuration = createConfiguration("application.name", "VulnaRebel");

        String value = configuration.getString("application.name");

        assertEquals("VulnaRebel", value);
    }

    @Test
    void getString_shouldThrow_whenKeyDoesNotExist() {

        Configuration configuration = createConfiguration("application.name", "VulnaRebel");

        assertThrows(
                ConfigurationException.class,
                () -> configuration.getString("server.port")
        );
    }

    @Test
    void getInt_shouldReturnConfiguredInteger() {

        Configuration configuration = createConfiguration("server.port", "8080");

        int value = configuration.getInt("server.port");

        assertEquals(8080, value);
    }

    @Test
    void getInt_shouldThrow_whenValueIsNotAnInteger() {

        Configuration configuration = createConfiguration("server.port", "eighty");

        assertThrows(
                ConfigurationException.class,
                () -> configuration.getInt("server.port")
        );
    }

    @Test
    void getBoolean_shouldReturnTrue() {

        Configuration configuration = createConfiguration("debug.enabled","true");

        assertTrue(configuration.getBoolean("debug.enabled"));
    }

    @Test
    void getBoolean_shouldReturnFalse() {

        Configuration configuration = createConfiguration("debug.enabled","false");

        assertFalse(configuration.getBoolean("debug.enabled"));
    }

    @Test
    void entries_shouldReturnAllConfiguredEntries() {

        Properties properties = new Properties();
        properties.setProperty("one", "1");
        properties.setProperty("two", "2");
        properties.setProperty("three", "3");

        Configuration configuration = new Configuration(properties);

        Map<String, String> entries = configuration.entries();

        assertEquals(3, entries.size());
        assertEquals("1", entries.get("one"));
        assertEquals("2", entries.get("two"));
        assertEquals("3", entries.get("three"));
    }

    private Properties createProperties(){
        return new Properties();
    }

    private Configuration createConfiguration(String key, String value){
        Properties properties = new Properties();
        properties.setProperty(key,value);
        return new Configuration(properties);
    }
}
