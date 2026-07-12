package config;

import exceptions.ConfigurationLoaderException;

import java.io.*;
import java.util.Properties;

/**
 * Loads {@code .properties} files from the classpath and returns
 * a configured {@link Configuration} instance.
 * <p>
 * The resource path is resolved relative to the classpath root -
 * pass the path without a leading slash:
 * </p>
 *
 * <pre>{@code
 * Configuration config = ConfigurationLoader.load("application.properties");
 * Configuration challenge = ConfigurationLoader.load("challenges/login/challenge.properties");
 * }</pre>
 *
 * <p>
 * This class is stateless — all methods are static and no instantiation
 * is required or intended.
 * </p>
 */
public class ConfigurationLoader {

    /**
     * Loads a {@code .properties} file from the classpath and returns
     * a {@link Configuration} wrapping its key-value pairs.
     *
     * @param classpathResource path to the resource relative to the
     *                          classpath root, e.g.
     *                          {@code "challenges/login/challenge.properties"}
     * @return a populated {@link Configuration} instance
     * @throws ConfigurationLoaderException if the resource is null, blank,
     *                                      or not found on the classpath
     * @throws RuntimeException             if an {@link java.io.IOException}
     *                                      occurs while reading the stream
     */
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
