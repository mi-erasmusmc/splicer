package nl.erasmusmc.mi.biosemantics.splicer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static nl.erasmusmc.mi.biosemantics.splicer.Database.getConnection;
import static nl.erasmusmc.mi.biosemantics.splicer.F5.fail;

public class Reference {

    private static final Map<String, String> defaultLabDirection = new HashMap<>();
    private static final List<String> clean1 = new ArrayList<>();
    private static final List<String> clean2 = new ArrayList<>();
    private static final Set<String> clean3 = new HashSet<>();
    private static final List<String> filterCon = new ArrayList<>();
    private static final Set<String> stopSet = new HashSet<>();
    private static final Set<String> junkSet = new HashSet<>();
    private static final Set<String> msFilterSet = new HashSet<>();
    private static final Set<String> l2mFilterSet = new HashSet<>();
    private static final Set<String> splFilterSet = new HashSet<>();


    private Reference() {
    }

    public static void loadReferenceData() {
        loadFilters();
        loadDirectionDefaults();
        loadClean1();
        loadClean2();
        loadClean3();
        loadStopHash();
        loadJunkHash();
        getFilterContains();
    }

    private static void loadDirectionDefaults() {
        var q = "SELECT labtest, direction FROM defaultdirections";
        try (var conn = getConnection(); var stmt = conn.prepareStatement(q);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                var lab = rs.getString("LabTest");
                if (lab != null) {
                    var direction = rs.getString("Direction");
                    direction = direction == null ? "" : direction;
                    defaultLabDirection.put(lab.toLowerCase(), direction);
                }
            }

        } catch (SQLException e) {
            fail(e);
        }
    }

    private static void loadClean1() {
        try (BufferedReader in = reader("clean1.txt")) {
            String line;
            while ((line = in.readLine()) != null) {
                line = " " + line.trim().toLowerCase() + " ";
                clean1.add(line);
            }
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void loadClean2() {
        try (BufferedReader in = reader("clean2.txt")) {
            String line;
            while ((line = in.readLine()) != null) {
                line = " " + line.trim().toLowerCase() + " ";
                clean2.add(line);
            }
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void loadClean3() {
        try (BufferedReader in = reader("clean3.txt")) {
            String line;
            while ((line = in.readLine()) != null) {
                line = " " + line.trim().toLowerCase() + " ";
                clean3.add(line);
            }
        } catch (IOException e) {
            fail(e);
        }

    }

    private static void loadStopHash() {
        try (BufferedReader in = reader("stopWords.txt")) {
            String line2;
            while ((line2 = in.readLine()) != null) {
                stopSet.add(line2);
            }
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void loadJunkHash() {
        try (BufferedReader in = reader("junk.txt")) {
            String line2;
            while ((line2 = in.readLine()) != null) {
                junkSet.add(line2);
            }
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void loadFilters() {
        loadMSFilterHash();
        loadL2MFilterHash();
        loadSplTermFilterHash();
    }

    private static void loadMSFilterHash() {
        try (BufferedReader in = reader("msFilters.txt")) {
            String line2;
            while ((line2 = in.readLine()) != null) {
                msFilterSet.add(line2);
            }
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void loadL2MFilterHash() {
        try (BufferedReader in = reader("l2mFilters.txt")) {
            String line2;
            while ((line2 = in.readLine()) != null) {
                l2mFilterSet.add(line2);
            }
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void loadSplTermFilterHash() {
        try (BufferedReader in = reader("filterSPLTerms.txt")) {
            String line2;
            while ((line2 = in.readLine()) != null) {
                splFilterSet.add(line2);
            }
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void getFilterContains() {
        try (BufferedReader in = reader("filterContains.txt")) {
            String line;
            while ((line = in.readLine()) != null) {
                line = " " + line.trim().toLowerCase() + " ";
                filterCon.add(line);
            }

        } catch (IOException e) {
            fail(e);
        }
    }

    private static BufferedReader reader(String file) throws IOException {
        var ref = Reference.class.getClassLoader().getResourceAsStream(file);
        if (ref == null) {
            throw new IOException("Could not load " + file);
        }
        return new BufferedReader(new InputStreamReader(ref));
    }

    public static Map<String, String> defaultLabDirection() {
        return defaultLabDirection;
    }

    public static List<String> filterCon() {
        return filterCon;
    }

    public static Set<String> stopSet() {
        return stopSet;
    }

    public static Set<String> junkSet() {
        return junkSet;
    }

    public static Set<String> msFilterSet() {
        return msFilterSet;
    }

    public static Set<String> l2mFilterSet() {
        return l2mFilterSet;
    }

    public static Set<String> splFilterSet() {
        return splFilterSet;
    }

    public static Set<String> clean3() {
        return clean3;
    }

    public static List<String> clean2() {
        return clean2;
    }

    public static List<String> clean1() {
        return clean1;
    }


}
