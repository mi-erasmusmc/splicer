package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static final String TABLE_MEDDRA_DB_WITHOUT_REVERSE = "medra_db_without_reverse";
    public static final String TABLE_MEDDRA_DB_WITHOUT_REVERSE_STEMS = "medra_db_without_reverse_stems";
    public static final String TABLE_MEDDRATESTS = "medratests";
    public static final String TABLE_MEDSYNS = "medsyns";
    public static final String TABLE_MEDSYNS_WITHSTEMS = "medsyns_withstems";
    public static final String TABLE_SPLICER_JANSSEN = "SPLICER_JANSSEN";
    public static final String TABLE_SWIT = "swit";
    public static final String TABLE_SYN_1 = "syn1";
    public static final String TABLE_SYN_2 = "syn2";

    private static final Logger log = LogManager.getLogger();
    private static Connection connection;

    private Database() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || !connection.isValid(3)) {
                log.info("Getting database connection details");
                String userName = System.getProperty("db_user");
                String password = System.getProperty("db_pass");
                String connectionString = System.getProperty("db_conn");
                connectionString += "&allowMultiQueries=true";
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
