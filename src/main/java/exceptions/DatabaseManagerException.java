package exceptions;

import java.sql.SQLException;

public class DatabaseManagerException extends RuntimeException{
    public DatabaseManagerException(String message, SQLException sqlErrorMsg){
        super(message, sqlErrorMsg);
    }
    public DatabaseManagerException(String message){
        super(message);
    }
}
