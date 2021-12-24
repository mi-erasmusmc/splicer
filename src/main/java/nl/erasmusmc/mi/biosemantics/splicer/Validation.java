package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Validation {

    private static final Logger log = LogManager.getLogger();

    private Validation() {
    }

    public static boolean alreadyExists(String setId) {
        try (PreparedStatement ps = Database.getConnection().prepareStatement("SELECT SET_ID FROM SPLICER_JANSSEN WHERE SET_ID = ? LIMIT 1")) {
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
