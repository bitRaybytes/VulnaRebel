package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/// This class' purpose is to initialize a challenge's table in the database. </br>
/// It will also read the modules database dummy data in seeds.sql and  </br>
/// The connection will be established by DatabaseManagers `getConnection()` Method.  </br> </br>
/// Executes SQL scripts
public class SchemaInitializer {

    // 1. read resources to set up database table for a challenge
    // => like "challenges/{challengeName}/schema.sql" from classpath
    // 2. Execute it
    // 3. read resources to set up dummy data for challenge
    // => like "challenges/{challengeName}/seed.sql" from classpath
    // 4. Execute it

    public SchemaInitializer(){}


    // String challengeName and Connection connection as parameters
    public BufferedReader setupChallenge() throws IOException {
        return Files.newBufferedReader(Path.of(String.valueOf(SchemaInitializer.class.getClassLoader().getResourceAsStream("challenges/login/schema.sql"))));
    }

    public static void main(String[] args) throws IOException {


        BufferedReader reader = new SchemaInitializer().setupChallenge();
        System.out.println(reader);

    }

}
