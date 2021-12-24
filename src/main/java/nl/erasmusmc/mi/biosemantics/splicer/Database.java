package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final Logger log = LogManager.getLogger();
    private static Connection connection;


    private Database() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || !connection.isValid(3)) {
                String userName = System.getProperty("db_user");
                String password = System.getProperty("db_pass");
                String connectionString = System.getProperty("db_conn");
                log.info("Connecting to: {}", connectionString);
                connection = DriverManager.getConnection(connectionString, userName, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
