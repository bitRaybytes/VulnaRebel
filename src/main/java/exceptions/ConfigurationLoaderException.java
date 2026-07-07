package exceptions;

import java.io.IOException;

public class ConfigurationLoaderException extends RuntimeException {
    public ConfigurationLoaderException(String message){
        super(message);
    }

    public ConfigurationLoaderException(String message, IOException e) {
        super(message,e);
    }
}
