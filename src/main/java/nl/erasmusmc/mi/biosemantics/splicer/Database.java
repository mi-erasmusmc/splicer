package nl.erasmusmc.mi.biosemantics.splicer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
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
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    private Database() {
    }

    public static void init(String url, String user, String password) {
        log.info("Initializing database connection pool");
        config.setJdbcUrl(url + "&allowMultiQueries=true");
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(12);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
        log.info("Database connection pool initialized");
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void close() {
        ds.close();
    }

}
