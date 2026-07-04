package config;

import exceptions.ConfigurationException;
import java.util.Properties;

/// This class uses a `ConfigurationLoader` to access the .properties file key value pairs.

public class Configuration {

    private final Properties properties;


    /// @param properties - The properties object to read from
    /// </br>
    /// Usage: </br>
    /// Configuration app = new Configuration(ConfigurationLoader.load("configFileName"));

    public Configuration(Properties properties){
        if (properties == null){
            throw new ConfigurationException(Configuration.class.getName()+": Property might be null.");
        }

        if (properties.isEmpty()){
            throw new ConfigurationException(Configuration.class.getName()+": Property might be empty.");
        }
        this.properties = properties;
    }

    public String getString(String key){
        validateKey(key);
        return properties.getProperty(key);
    }

    public int getInt(String key){
        validateKey(key);
        try {
            return Integer.parseInt(properties.getProperty(key));
        }catch (NumberFormatException e){
            throw new ConfigurationException(Configuration.class.getName()+": Configuration property '" + key + "' must be a valid integer.");
        }
    }

    public boolean getBoolean(String key){
        validateKey(key);
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    private void validateKey(String key){
        if (key == null){
            throw new ConfigurationException(Configuration.class.getName()+": Configuration string cannot be null.");
        }

        if (key.isBlank()){
            throw new ConfigurationException(Configuration.class.getName()+": Configuration string cannot be empty or blank.");
        }
        validatePropertyKey(key);
    }

    private void validatePropertyKey(String key) {
        if (!properties.containsKey(key)){
            throw new ConfigurationException(Configuration.class.getName()+": Missing configuration key: '" + key + "' not exist.");
        }
    }


}
