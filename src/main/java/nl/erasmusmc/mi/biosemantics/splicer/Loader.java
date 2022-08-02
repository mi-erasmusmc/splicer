package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import static nl.erasmusmc.mi.biosemantics.splicer.Database.getConnection;

public class Loader {

    private static final Logger log = LogManager.getLogger();

    public static void loadDirectionDefaults() {
        int dd = 0;
        String f1;
        String f2;

        try (Statement stmt = getConnection().createStatement()) {

            String query = "SELECT labtest,direction FROM defaultdirections";

            for (ResultSet rs = stmt.executeQuery(query); rs.next(); ++dd) {
                f1 = rs.getString("LabTest");
                if (rs.wasNull()) {
                    f1 = "";
                }

                f2 = rs.getString("Direction");
                if (rs.wasNull()) {
                    f2 = "";
                }

                f1 = f1.toLowerCase();
                F5.defaultLab[dd] = f1;
                F5.defaultDirection[dd] = f2;
                log.debug("loading default-directions: {}   {}", f1, f2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Disconnected from database");
        }
    }

    public void getClean1() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("clean1.txt")))) {
            String line2;

            int rowCount;
            for (rowCount = 0; (line2 = in.readLine()) != null; ++rowCount) {
                log.debug(line2);
                line2 = " " + line2.trim().toLowerCase();
                F5.clean1[rowCount] = line2;
                if (!F5.clean1[rowCount].equals("")) {
                    F5.clean1[rowCount] = F5.clean1[rowCount] + " ";
                }
            }

            F5.maxClean1 = rowCount - 1;
        } catch (Exception var4) {
            log.error("Error opening getReportVariables file");
        }
    }

    public void getClean2() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("clean2.txt")))) {
            String line2;

            int rowCount;
            for (rowCount = 0; (line2 = in.readLine()) != null; ++rowCount) {
                log.debug(line2);
                line2 = line2.trim().toLowerCase();
                F5.clean2[rowCount] = line2;
                if (!F5.clean2[rowCount].equals("")) {
                    F5.clean2[rowCount] = " " + F5.clean2[rowCount] + " ";
                }
            }

            F5.maxClean2 = rowCount - 1;
        } catch (Exception var4) {
            log.error("Error opening getReportVariables file");
        }
    }

    public void getClean3() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("clean3.txt")))) {
            String line2;

            int rowCount;
            for (rowCount = 0; (line2 = in.readLine()) != null; ++rowCount) {
                log.debug(line2);
                line2 = line2.trim().toLowerCase();
                F5.clean3[rowCount] = line2;
            }

            F5.maxClean3 = rowCount - 1;
        } catch (Exception var4) {
            log.error("Error opening getReportVariables file");
        }

    }

    public void getSwit() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("swit.txt")))) {
            String line2;

            int rowCount;
            for (rowCount = 0; (line2 = in.readLine()) != null; ++rowCount) {
                log.debug(line2);
                F5.swit[rowCount] = line2;
                if (!F5.swit[rowCount].equals("")) {
                    F5.swit[rowCount] = " " + F5.swit[rowCount] + " ";
                }
            }

            F5.maxSwit = rowCount - 1;
        } catch (Exception var4) {
            log.error("Error opening getReportVariables file");
        }

    }

    public void loadStopHash() {
        log.debug("loading stops");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("stopWords.txt")))) {
            F5.stopSet = new HashSet<>();
            String line2;

            while ((line2 = in.readLine()) != null) {
                log.debug("loading stops: {}", line2);
                F5.stopSet.add(line2);
            }
        } catch (Exception var3) {
            log.error("No stopWord file");
        }
        log.debug("{} stops items loaded", F5.stopSet.size());
    }

    public void loadJunkHash() {
        log.debug("Loading junk");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("junk.txt")))) {
            F5.junkSet = new HashSet<>();
            String line2;

            while ((line2 = in.readLine()) != null) {
                log.debug("loading junk: {}", line2);
                F5.junkSet.add(line2);
            }
        } catch (Exception e) {
            log.error("No junkWord file");
            e.printStackTrace();
        }
        log.debug("{} junk items loaded", F5.junkSet.size());
    }

    public void getFilters() {
        this.loadMSFilterHash();
        this.loadL2MFilterHash();
        this.loadSplTermFilterHash();
    }

    public void loadMSFilterHash() {
        log.debug("Loading MS Filters");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("msFilters.txt")))) {
            F5.msFilterSet = new HashSet<>();
            String line2;

            while ((line2 = in.readLine()) != null) {
                log.debug("loading msFilters: {}", line2);
                F5.msFilterSet.add(line2);
            }
        } catch (Exception var3) {
            log.error("No msFilters file");
            var3.printStackTrace();
        }
        log.debug("Loaded {} MS Filers", F5.msFilterSet.size());
    }

    public void loadL2MFilterHash() {
        log.debug("Loading L2M Filter Hash");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("l2mFilters.txt")))) {
            F5.l2mFilterSet = new HashSet<>();
            String line2;

            while ((line2 = in.readLine()) != null) {
                log.debug("loading l2mFilters: {}", line2);
                F5.l2mFilterSet.add(line2);
            }
        } catch (Exception var3) {
            log.error("No L2MFilters file");
            var3.printStackTrace();
        }
        log.debug("Loaded {} L2M Filter Hash", F5.l2mFilterSet.size());
    }

    public void loadSplTermFilterHash() {
        log.debug("Loading Splicer Term Filter Hash");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("filterSPLTerms.txt")))) {
            F5.splFilterSet = new HashSet<>();
            String line2;

            while ((line2 = in.readLine()) != null) {
                log.debug("loading splFilters: {}", line2);
                F5.splFilterSet.add(line2);
            }
        } catch (Exception var3) {
            log.error("No splicer Term Filter Hash file");
            var3.printStackTrace();
        }
        log.debug("Loaded {} spl term filters", F5.splFilterSet.size());
    }

    public void getFilterContains() {
        log.debug("Loading filter contains");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("filterContains.txt")))) {
            String line2;

            int rowCount;
            for (rowCount = 0; (line2 = in.readLine()) != null; ++rowCount) {
                log.debug("filterCons  {} ", line2);
                F5.filterCon[rowCount] = line2;
            }

            F5.maxFilterCon = rowCount - 1;
        } catch (Exception var4) {
            log.error("Error opening getReportVariables file");
        }
        log.debug("Loaded {}filtercons", F5.filterCon.length);
    }
}
