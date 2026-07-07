package config;

import exceptions.ConfigurationLoaderException;

import java.io.*;
import java.util.Properties;

/// This class is for loading the configuration files.</br>

public class ConfigurationLoader {

    public static Configuration load(String classpathResource){
        validateResource(classpathResource);
        Properties properties = new Properties();
        try(java.io.InputStream in =
                            ConfigurationLoader.class
                                    .getResourceAsStream("/"+ classpathResource)
        ){
            if (in == null){
                throw new ConfigurationLoaderException(
                        ConfigurationLoader.class.getName()+
                                ": Configure file '" + classpathResource + "' not found.");
            }
            properties.load(in);
            return new Configuration(properties);
        } catch (IOException e) {
            throw new ConfigurationLoaderException(
                    ConfigurationLoader.class.getName()+
                            ": Failure in InputStream occurred ",e);
        }
    }

    private static void validateResource(String classpathResource){
        if (classpathResource == null){
            throw new ConfigurationLoaderException(
                    ConfigurationLoader.class.getName()+
                            ": Classpath to resource file cannot be null.");
        }

        if (classpathResource.isBlank()){
            throw new ConfigurationLoaderException(
                    ConfigurationLoader.class.getName()+
                            "Classpath to resource file cannot be empty or blank.");
        }
    }
}
