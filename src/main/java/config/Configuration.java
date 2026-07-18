package config;

import exceptions.ConfigurationException;
import html.TemplateRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Typed accessor for a {@link Properties} object loaded from a
 * {@code .properties} file.
 * <p>
 * Provides {@code getString}, {@code getInt}, and {@code getBoolean}
 * accessors with built-in key validation. All methods throw
 * {@link exceptions.ConfigurationException} on missing or malformed keys
 * rather than returning null or a silent default.
 * </p>
 *
 * <pre>{@code
 * Configuration config = new Configuration(
 *     ConfigurationLoader.load("application.properties")
 * );
 * int port = config.getInt("server.port");
 * }</pre>
 */
public class Configuration {

    private final Properties properties;


    /**
     * @param properties the populated {@link Properties} object to wrap
     * @throws ConfigurationException if {@code properties} is null or empty
     */
    public Configuration(Properties properties){
        if (properties == null){
            throw new ConfigurationException(
                    getClass().getName() +
                            ": Property might be null.");
        }

        if (properties.isEmpty()){
            throw new ConfigurationException(
                    getClass().getName() +
                            ": Property might be empty.");
        }
        this.properties = properties;
    }

    /**
     * Returns the value for the given key as a {@link String}.
     *
     * @param key the property key to look up
     * @return the property value
     * @throws ConfigurationException if the key is null, blank, or not present
     */
    public String getString(String key){
        validateKey(key);
        return properties.getProperty(key);
    }


    /**
     * Returns the value for the given key parsed as an {@code int}.
     *
     * @param key the property key to look up
     * @return the property value as an integer
     * @throws ConfigurationException if the key is missing or the value
     *                                is not a valid integer
     */
    public int getInt(String key){
        validateKey(key);
        try {
            return Integer.parseInt(properties.getProperty(key));
        }catch (NumberFormatException e){
            throw new ConfigurationException(
                    getClass().getName() +
                            ": Configuration property '" + key + "' must be a valid integer.");
        }
    }

    /**
     * Returns the value for the given key parsed as a {@code boolean}.
     * <p>
     * Delegates to {@link Boolean#parseBoolean} - only the string
     * {@code "true"} (case-insensitive) returns {@code true};
     * all other values return {@code false}.
     * </p>
     *
     * @param key the property key to look up
     * @return the property value as a boolean
     * @throws ConfigurationException if the key is null, blank, or not present
     */
    public boolean getBoolean(String key){
        validateKey(key);
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    /**
     * Returns all key-value pairs from this configuration as an
     * unmodifiable {@link Map}.
     * <p>
     * Intended for use by {@link TemplateRenderer} to replace
     * {@code {{key}}} placeholders in HTML templates.
     * </p>
     *
     * @return an unmodifiable map of all property keys and their values
     */
    public Map<String, String> entries(){
        Map<String, String> entries = new HashMap<>();

        for (Map.Entry<Object, Object> map : properties.entrySet())
            entries.put(map.getKey().toString(),map.getValue().toString());
        return Map.copyOf(entries);
    }

    private void validateKey(String key){
        if (key == null){
            throw new ConfigurationException(
                    getClass().getName() +
                            ": Configuration string cannot be null.");
        }

        if (key.isBlank()){
            throw new ConfigurationException(
                    getClass().getName() +
                            ": Configuration string cannot be empty or blank.");
        }
        validatePropertyKey(key);
    }

    private void validatePropertyKey(String key) {
        if (!properties.containsKey(key)){
            throw new ConfigurationException(
                    getClass().getName() +
                            ": Missing configuration key: '" + key + "' not exist.");
        }
    }


}
