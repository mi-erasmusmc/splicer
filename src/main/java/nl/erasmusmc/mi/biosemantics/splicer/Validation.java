package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

import static nl.erasmusmc.mi.biosemantics.splicer.Database.getConnection;

public class Validation {

    private static final Logger log = LogManager.getLogger();

    private Validation() {
    }

    public static boolean alreadyExists(String setId) {
        var q = "SELECT SET_ID FROM SPLICER_JANSSEN WHERE SET_ID = ? LIMIT 1";
        try (var conn = getConnection(); var ps = conn.prepareStatement(q)) {
            ps.setString(1, setId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return setId.equals(rs.getString(1));
            }
            return false;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
