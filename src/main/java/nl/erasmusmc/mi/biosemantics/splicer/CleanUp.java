package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.erasmusmc.mi.biosemantics.splicer.Database.getConnection;

public class CleanUp {
    public static final String MEDRA_DB_WITHOUT_REVERSE_STEMS = "medra_db_without_reverse_stems";
    private static final Logger log = LogManager.getLogger();
    static String[] tempArray = new String[1000];
    static String[] tempArray2 = new String[1000];
    static NonAde nde = new NonAde();
    static AdeProcess ade = new AdeProcess();
    static Flow flo = new Flow();

    public static String checkJunk(String b) {
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(b);
        if (m.find()) {
            b = "";
        } else {
            p = Pattern.compile("-");
            m = p.matcher(b);

            int dashCount;
            for (dashCount = 0; m.find(); ++dashCount) {
            }

            if (dashCount > 1) {
                b = "";
            }
        }
        return b;
    }

    public static boolean checkMedraToken(String b) {
        String q = "'";
        boolean mappedMedraValid;
        b = b.trim();

        tempArray = b.split(" ");

        try (Statement stmt = getConnection().createStatement()) {
            String quer;
            String lowerMedStem;
            String spacedTerm;

            quer = "Select Medra2, Stems from " + MEDRA_DB_WITHOUT_REVERSE_STEMS + " where Stems LIKE " + q + "%" + tempArray[0] + "%" + q;
            ResultSet rs = stmt.executeQuery(quer);

            do {
                do {
                    if (!rs.next()) {
                        return false;
                    }

                    mappedMedraValid = true;
                    F5.foundMedra = rs.getString("Medra2");
                    if (rs.wasNull()) {
                        F5.foundMedra = "";
                    }

                    F5.medStem = rs.getString("Stems");
                    if (rs.wasNull()) {
                        F5.medStem = "";
                    }

                    F5.medStem = F5.medStem.toLowerCase();

                    b = b.trim();
                    lowerMedStem = F5.medStem.toLowerCase();
                    lowerMedStem = lowerMedStem.trim();
                    tempArray = b.split(" ");
                    tempArray2 = lowerMedStem.split(" ");
                    lowerMedStem = " " + lowerMedStem + " ";
                } while (tempArray.length != tempArray2.length);

                for (String s : tempArray) {
                    spacedTerm = " " + s + " ";
                    if (!lowerMedStem.contains(spacedTerm)) {
                        mappedMedraValid = false;
                        break;
                    }
                }
            } while (!mappedMedraValid);

            return true;
        } catch (Exception var16) {
            log.error("Got an exception checkMedraToken query");
            log.error(var16.getMessage());
            return false;
        }
    }

    public static boolean checkSynToken(String b) {
        String q = "'";
        boolean mappedMedraValid;
        b = b.trim();

        tempArray = b.split(" ");
        String tab = "medsyns_withstems";

        try (Statement stmt = getConnection().createStatement()) {
            String quer;
            String lowerMedStem;
            String spacedTerm;

            quer = "Select MDRTerm, EffectStems from " + tab + " where EffectStems LIKE " + q + "%" + tempArray[0] + "%" + q;
            ResultSet rs = stmt.executeQuery(quer);

            do {
                do {
                    if (!rs.next()) {
                        return false;
                    }

                    mappedMedraValid = true;
                    F5.foundMedra = rs.getString("MDRTerm");
                    if (rs.wasNull()) {
                        F5.foundMedra = "";
                    }

                    F5.medStem = rs.getString("EffectStems");
                    if (rs.wasNull()) {
                        F5.medStem = "";
                    }

                    F5.medStem = F5.medStem.toLowerCase();

                    b = b.trim();
                    lowerMedStem = F5.medStem.toLowerCase();
                    lowerMedStem = lowerMedStem.trim();
                    tempArray = b.split(" ");
                    tempArray2 = lowerMedStem.split(" ");
                    lowerMedStem = " " + lowerMedStem + " ";
                } while (tempArray.length != tempArray2.length);

                for (String s : tempArray) {
                    spacedTerm = " " + s + " ";
                    if (!lowerMedStem.contains(spacedTerm)) {
                        mappedMedraValid = false;
                        break;
                    }
                }
            } while (!mappedMedraValid);

            return true;
        } catch (Exception var16) {
            log.error("Got an exception checkSynToken query");
            log.error(var16.getMessage());
            return false;
        }
    }

    public static boolean checkMedra(String b) {
        boolean mdFound = false;
        String q = "'";
        b = b.trim();
        b = " " + b;

        try (Statement stmt = getConnection().createStatement()) {
            String quer;


            quer = "Select Medra2, Stems from " + MEDRA_DB_WITHOUT_REVERSE_STEMS + " where Stems = " + q + b + q;
            ResultSet rs = stmt.executeQuery(quer);

            StringBuilder bBuilder = new StringBuilder(b);
            while (rs.next()) {
                F5.foundMedra = rs.getString("Medra2");
                if (rs.wasNull()) {
                    F5.foundMedra = "";
                }

                F5.medStem = rs.getString("Stems");
                if (rs.wasNull()) {
                    F5.medStem = "";
                }

                F5.medStem = F5.medStem.toLowerCase();
                bBuilder.append(" ");
                if (bBuilder.toString().equalsIgnoreCase(F5.medStem)) {
                    mdFound = true;
                    break;
                }
            }
        } catch (Exception var15) {
            log.error("Got an exception checkMedra in query");
            log.error(var15.getMessage());
        }

        return mdFound;
    }

    public static boolean checkMedra2(String b) {
        boolean mdFound = false;
        b = b.trim();

        try (Statement stmt = getConnection().createStatement()) {
            String quer = "Select Medra2, Stems from " + MEDRA_DB_WITHOUT_REVERSE_STEMS + " where Medra2 = ' " + b + " '";
            ResultSet rs = stmt.executeQuery(quer);
            if (rs.next()) {
                F5.foundMedra = rs.getString("Medra2");
                if (rs.wasNull()) {
                    F5.foundMedra = "";
                }

                F5.medStem = rs.getString("Stems");
                if (rs.wasNull()) {
                    F5.medStem = "";
                }

                F5.medStem = F5.medStem.toLowerCase();
                mdFound = true;
            }

        } catch (Exception e) {
            log.error("Got an exception checkMedra2 in query");
            log.error(e.getMessage());
        }

        return mdFound;
    }

    public static String getSyn1(String b) {
        String q = "'";
        b = b.toLowerCase();
        b = " " + b + " ";

        String tab = "syn1";

        try (Statement stmt = getConnection().createStatement()) {
            String quer;
            String f1;
            String f2;


            quer = "Select Field1,Field2 from " + tab + " where (((InStr(" + q + b + q + ", Field1))>0))";
            ResultSet rs = stmt.executeQuery(quer);

            while (rs.next()) {
                f1 = rs.getString("Field1");
                if (rs.wasNull()) {
                    f1 = "";
                }

                f2 = rs.getString("Field2");
                if (rs.wasNull()) {
                    f2 = "";
                }

                if (!f1.equalsIgnoreCase("") && !f1.equalsIgnoreCase(" ")) {

                    f2 = " " + f2 + " ";
                    b = b.replaceAll(f1, f2);
                }
            }

        } catch (Exception var14) {
            log.error("Got an exception getSyn1 in query");
            log.error(var14.getMessage());
        }

        return b.trim();
    }

    public static String getSwit(String b) {
        String q = "'";
        b = b.toLowerCase();
        b = " " + b + " ";

        String tab = "swit";

        try (Statement stmt = getConnection().createStatement()) {
            String f1;
            String f2;

            String quer = "Select Field1,Field2 from " + tab + " where (((InStr(" + q + b + q + ", Field1))>0))";
            ResultSet rs = stmt.executeQuery(quer);

            while (rs.next()) {
                f1 = rs.getString("Field1");
                if (rs.wasNull()) {
                    f1 = "";
                }

                f2 = rs.getString("Field2");
                if (rs.wasNull()) {
                    f2 = "";
                }

                if (b.startsWith(f1)) {
                    b = b.replaceAll(f1, "");

                    b = b + " " + f2;
                }
            }

        } catch (Exception var14) {
            log.error("Got an exception getSwit query");
            log.error(var14.getMessage());
        }

        return b.trim();
    }

    public static String getSyn2(String b) {
        String q = "'";
        b = b.toLowerCase();
        b = " " + b + " ";
        b = b.replaceAll("\\(", "");
        b = b.replaceAll("\\)", "");
        String tab = "syn2";

        try (Statement stmt = getConnection().createStatement()) {
            String quer;
            String f1;
            String f2;

            quer = "Select Field1,Field2 from " + tab + " where (((InStr(" + q + b + q + ", Field1))>0))";
            ResultSet rs = stmt.executeQuery(quer);

            while (rs.next()) {
                f1 = rs.getString("Field1");
                if (rs.wasNull()) {
                    f1 = "";
                }

                f2 = rs.getString("Field2");
                if (rs.wasNull()) {
                    f2 = "";
                }

                f1 = f1 + " ";
                if (b.endsWith(f1)) {
                    b = b.replaceAll(f1, "");
                    b = b + " " + f2;
                }
            }

        } catch (Exception var14) {
            log.error("Got an exception getSyn2 query");
            log.error(var14.getMessage());
        }

        return b.trim();
    }

    public static String getMedSyns(String b) {
        String q = "'";
        b = b.toLowerCase();

        String tab = "medsyns";

        try (Statement stmt = getConnection().createStatement()) {
            String f1;
            String f2;

            String quer = "Select Effect ,MDRTerm from " + tab + " where Effect = " + q + b + q;
            ResultSet rs = stmt.executeQuery(quer);

            while (rs.next()) {
                f1 = rs.getString("Effect");
                if (rs.wasNull()) {
                    f1 = "";
                }

                f2 = rs.getString("MDRTerm");
                if (rs.wasNull()) {
                    f1 = "";
                }

                if (b.equalsIgnoreCase(f1)) {
                    F5.medraFound = true;
                    return f2;
                }
            }

        } catch (Exception var14) {
            log.error("Got an exception getMedSyns query");
            log.error(var14.getMessage());
        }

        return b;
    }

    public static String getLabDirection(String b, String s, String sec, String oriTerm, String md) {
        if (s == null) {
            s = "";
        }

        if (s.length() > 4 && sec.contains("Adverse Reactions")) {
            boolean isATest = checkMedraTest(b);
            boolean increased = false;
            boolean decreased = false;
            boolean abnormal = false;
            String preMed = "";
            String postMed = "";
            if (isATest) {
                ++F5.labCounter;
                s = s.toLowerCase();
                s = Normals.normalSpl(s);
                s = Normals.normal2(s);
                Pattern p;
                Matcher m;
                if (md.contains("L")) {
                    p = Pattern.compile("( increas| eleva| rise| rising| high)");
                    m = p.matcher(oriTerm);
                    if (m.find()) {
                        increased = true;
                    }

                    p = Pattern.compile("( decreas| lowered| low)");
                    m = p.matcher(oriTerm);
                    if (m.find()) {
                        decreased = true;
                    }

                    p = Pattern.compile("( abnorm| alter)");
                    m = p.matcher(oriTerm);
                    if (m.find()) {
                        abnormal = true;
                    }
                }

                if (s.contains(oriTerm)) {
                    preMed = s.substring(0, s.indexOf(oriTerm));
                    preMed = preMed.toLowerCase();
                    preMed = Normals.normalizeFilter2(preMed);
                    postMed = s.substring(s.indexOf(oriTerm));
                    postMed = postMed.toLowerCase();
                    postMed = Normals.normalizeFilter2(postMed);
                    String regConj = "( increased| eleva| rise| rising| high | decreas| lowered| low | less)";
                    Pattern pconj = Pattern.compile(regConj);
                    Matcher mconj = pconj.matcher(postMed);
                    if (mconj.find()) {
                        String matchedConcept = mconj.group();
                        postMed = postMed.substring(postMed.indexOf(matchedConcept));
                    }
                }

                p = Pattern.compile("( increas| eleva| rise| rising| high)");
                m = p.matcher(preMed);
                if (m.find()) {
                    increased = true;
                }

                p = Pattern.compile("( increas| eleva| rise| rising| high)");
                m = p.matcher(postMed);
                if (m.find()) {
                    increased = true;
                }

                p = Pattern.compile("( increas| eleva| rise| rising| high)");
                m = p.matcher(s);
                if (m.find()) {
                    increased = true;
                }

                p = Pattern.compile("( decreas| lowered| low)");
                m = p.matcher(s);
                if (m.find()) {
                    decreased = true;
                }

                p = Pattern.compile("( abnorm| alter)");
                m = p.matcher(s);
                if (m.find()) {
                    abnormal = true;
                }

                if (s.contains(" no ") || s.contains(" not ")) {
                    return "negated";
                }

                if (increased && !decreased) {
                    return "increased";
                }

                if (!increased && decreased) {
                    return "decreased";
                }

                if (increased) {
                    for (int c = 0; c <= F5.maxDefaultLD; ++c) {
                        if (b.equalsIgnoreCase(F5.defaultLab[c])) {
                            return F5.defaultDirection[c];
                        }
                    }
                    return "indeterminate";
                }

                if (abnormal) {
                    return "abnormal";
                }

                return "unstated";
            }
        }

        return "";
    }

    public static boolean checkMedraTest(String b) {
        String q = "'";
        b = b.toLowerCase();
        b = b.trim();

        String tab = "medratests";

        try (Statement stmt = getConnection().createStatement()) {
            String quer;


            quer = "Select Field1 from " + tab + " where Field1 = " + q + b + q;
            ResultSet rs = stmt.executeQuery(quer);
            if (rs.next()) {
                return true;
            }

        } catch (Exception var11) {
            log.error("Got an exception check medra test");
            log.error(var11.getMessage());
        }

        return false;
    }

    public static void similarity(String b) {
        String q = "'";
        b = b.trim();
        String shortB;
        if (b.length() > 8) {
            shortB = b.substring(2, b.length() - 2);
        } else if (b.length() > 6) {
            shortB = b.substring(1, b.length() - 1);
        } else {
            shortB = b;
        }


        try (Statement stmt = getConnection().createStatement()) {
            String quer;

            quer = "SELECT medra2, stems FROM " + MEDRA_DB_WITHOUT_REVERSE_STEMS + " WHERE medra2 LIKE " + q + "%" + shortB + "%" + q;
            ResultSet rs = stmt.executeQuery(quer);

            while (rs.next()) {
                F5.foundMedra = rs.getString("Medra2");
                if (rs.wasNull()) {
                    F5.foundMedra = "";
                }

                F5.medStem = rs.getString("Stems");
                if (rs.wasNull()) {
                    F5.medStem = "";
                }

                F5.medStem = F5.medStem.toLowerCase();
                double closenessScore = Similar.compareStrings(b.trim(), F5.foundMedra.trim());
                if (closenessScore > 0.9D) {
                    F5.origMethod = F5.finalsMethod[F5.count];
                    F5.finalsMethod[F5.count] = "MS";
                    F5.transformFlag = F5.origTerm;
                    F5.finalMedraTerms[F5.count] = F5.foundMedra;
                    F5.matchOutcome = "medraMatch";
                    ++F5.simCounter;
                    flo.labCheck();
                    if (F5.labTransformOccurred) {
                        ++F5.labTransformCounter;
                        break;
                    }

                    F5.finalMedraTerms[F5.count] = F5.finalMedraTerms[F5.count].trim();
                    flo.finalFilter();
                    if (F5.passedFinalFilter) {
                        flo.regularOutput();
                        break;
                    }
                }
            }

        } catch (Exception e) {
            log.error("Got an exception getSimilarity query");
            throw new RuntimeException(e);
        }

    }

    public static boolean transformLab2Medra(String a, String d) {
        if (d.contains("increased")) {
            F5.medraFound = checkMedra2(a + " increased");
            if (F5.medraFound) {
                concludeMatchandOuput();
                return true;
            }

            F5.medraFound = checkMedra2("blood " + a + " increased");
            if (F5.medraFound) {
                concludeMatchandOuput();
                return true;
            }
        }

        if (d.contains("decreased")) {
            F5.medraFound = checkMedra2(a + " decreased");
            if (F5.medraFound) {
                concludeMatchandOuput();
                return true;
            }

            F5.medraFound = checkMedra2("blood " + a + " decreased");
            if (F5.medraFound) {
                concludeMatchandOuput();
                return true;
            }
        }

        if (d.contains("abnormal")) {
            F5.medraFound = checkMedra2(a + " abnormal");
            if (F5.medraFound) {
                concludeMatchandOuput();
                return true;
            }

            F5.medraFound = checkMedra2("blood " + a + " abnormal");
            if (F5.medraFound) {
                concludeMatchandOuput();
                return true;
            }
        }

        return false;
    }

    public static void concludeMatchandOuput() {
        F5.matchOutcome = "medraMatch";
        F5.finalsMethod[F5.count] = "LM";
        F5.transformFlag = F5.origTerm;
        F5.finalMedraTerms[F5.count] = F5.foundMedra;
        F5.finalMedraTerms[F5.count] = F5.finalMedraTerms[F5.count].trim();
        F5.direction = "transformed";
        flo.finalFilter();
        if (F5.passedFinalFilter) {
            flo.regularOutput();
        }

    }

    public static String falseAde(String b) {
        if (F5.junkSet.contains(b)) {
            return "";
        } else {
            return b;
        }
    }

    public static boolean finalFilter() {
        Normals.normalizeFilter(F5.finalMedraTerms[F5.count]);
        String anyLetter = "([a-z]|[A-Z])";
        Pattern p = Pattern.compile(anyLetter);
        Matcher m = p.matcher(F5.finalsMethod[F5.count]);
        boolean mat2 = m.find();
        if (!mat2) {
            return false;
        } else if (F5.finalMedraTerms[F5.count].length() <= 2) {
            return false;
        } else if (F5.finalsMethod[F5.count].equals("MS") && F5.msFilterSet.contains(F5.finalMedraTerms[F5.count])) {
            return false;
        } else if (F5.finalsMethod[F5.count].equals("L2M") && F5.l2mFilterSet.contains(F5.finalMedraTerms[F5.count])) {
            return false;
        } else if (F5.l2mFilterSet.contains(F5.transformFlag)) {
            return false;
        } else if (F5.splFilterSet.contains(F5.finalMedraTerms[F5.count])) {
            return false;
        } else {
            for (int s = 0; s <= F5.maxFilterCon; ++s) {
                if (F5.finalsMethod[F5.count].contains(F5.filterCon[s])) {
                    return false;
                }
            }


            if (F5.finalsMethod[F5.count].contains("M") && Integer.parseInt(F5.sentNumArray[F5.count]) > -1) {

                String targetSent = F5.sent[Integer.parseInt(F5.sentNumArray[F5.count])];
                if (targetSent != null && targetSent.contains(F5.transformFlag)) {
                    String preMed = targetSent.substring(0, targetSent.indexOf(F5.transformFlag));
                    preMed = preMed.toLowerCase();
                    String[] tempArray = preMed.split(" ");
                    String tempPreMed;
                    if (tempArray.length >= 4) {
                        tempPreMed = tempArray[tempArray.length - 4] + " " + tempArray[tempArray.length - 3] + " " + tempArray[tempArray.length - 2] + " " + tempArray[tempArray.length - 1];
                    } else {
                        tempPreMed = preMed;
                    }

                    preMed = Normals.normalizeFilter2(tempPreMed);
                    Pattern p4 = Pattern.compile("(?i:patient('s|s)?\\s+((\\()?(\\w+)?(\\s)?(\\=)?\\s?(\\d+)?(\\))?\\s?)?with)");
                    Matcher m4 = p4.matcher(preMed);
                    return !m4.find();
                }
            }

            return true;
        }
    }

    public boolean cleanIndication(String i) {
        if (i.equalsIgnoreCase(" all ")) {
            return false;
        } else if (i.equalsIgnoreCase(" co ")) {
            return false;
        } else {
            return !i.equalsIgnoreCase(" drug therapy ");
        }
    }

    public String mappingProcesses(String ftm, String m) {
        ftm = falseAde(ftm);
        if (ftm.equalsIgnoreCase("")) {
            ++F5.junkCounter;
            return ftm;
        } else {
            F5.medraFound = false;
            F5.matchOutcome = "";
            if (m == null) {
                m = "";
            }

            boolean isIndication = nde.checkIndication(ftm);
            if (isIndication) {
                ++F5.indCounter;
                return "";
            } else if (!m.equals("M1") && !m.equals("T1M1")) {
                ftm = checkJunk(ftm);
                if (ftm.equalsIgnoreCase("")) {
                    ++F5.junkCounter;
                    return ftm;
                } else {
                    F5.medraFound = checkMedra2(ftm);
                    if (F5.medraFound) {
                        ++F5.straightCounter;
                        F5.matchOutcome = "medraMatch";
                        return F5.foundMedra;
                    } else {
                        F5.medraFound = this.checkOR(ftm);
                        if (F5.medraFound) {
                            log.debug("I CHECKED ORs and it was TRUE!");
                            ++F5.straightCounter;
                            F5.matchOutcome = "medraMatch";
                            return F5.foundMedra;
                        } else {
                            ftm = getMedSyns(ftm);
                            if (F5.medraFound) {
                                ++F5.synCounter;
                                F5.matchOutcome = "medraMatch";
                                return ftm;
                            } else {
                                String workingTerm = ftm.trim();
                                tempArray = workingTerm.split(" ");
                                StringBuilder allWords = new StringBuilder();

                                for (String s : tempArray) {
                                    boolean isStopWord = F5.stopSet.contains(s);
                                    if (!isStopWord) {
                                        String stemmedWord = F5.portStem(s);
                                        if (stemmedWord.equalsIgnoreCase("Invalid term")) {
                                            stemmedWord = s;
                                        }

                                        allWords.append(" ").append(stemmedWord);
                                    }
                                }

                                allWords = new StringBuilder(allWords.toString().trim());
                                allWords.insert(0, " ");
                                if (!allWords.toString().equalsIgnoreCase(" ")) {
                                    F5.medraFound = checkMedra(allWords.toString());
                                }

                                if (F5.medraFound) {
                                    ++F5.stemMedCounter;
                                    F5.matchOutcome = "medraMatch";
                                    return F5.foundMedra;
                                } else {
                                    ftm = this.cleanConvert(ftm);
                                    if (ftm.equalsIgnoreCase("")) {
                                        return ftm;
                                    } else {
                                        F5.medraFound = checkMedra2(ftm);
                                        if (F5.medraFound) {
                                            ++F5.transformCounter;
                                            F5.matchOutcome = "medraMatch";
                                            return F5.foundMedra;
                                        } else {
                                            if (!allWords.toString().equalsIgnoreCase(" ")) {
                                                F5.medraFound = checkMedraToken(allWords.toString());
                                            }

                                            if (F5.medraFound) {
                                                ++F5.tokenMedCounter;
                                                F5.matchOutcome = "medraMatch";
                                                return F5.foundMedra;
                                            } else {
                                                if (!allWords.toString().equalsIgnoreCase(" ")) {
                                                    F5.medraFound = checkSynToken(allWords.toString());
                                                }

                                                if (F5.medraFound) {
                                                    ++F5.tokenSynCounter;
                                                    F5.matchOutcome = "medraMatch";
                                                    return F5.foundMedra;
                                                } else {
                                                    F5.matchOutcome = "noMatch";
                                                    ++F5.noMatch1Counter;
                                                    ftm = F5.compress(ftm);
                                                    ftm = ftm.trim();
                                                    return ftm;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                ++F5.m1Counter;
                F5.matchOutcome = "medraMatch";
                return ftm;
            }
        }
    }

    public String cleanTermEscape(String ftm) {

        F5.escapeMatchOccurred = false;
        F5.medraCount = 0;
        AdeProcess.getMedraTermsEscape2(ftm);
        ade.findMatch(F5.origTerm2);
        ade.getUniqueLCSEscape();

        for (int escapeCount = 0; escapeCount < F5.goodCount; ++escapeCount) {
            log.debug(F5.goodWords2[escapeCount]);

            F5.origMethod = F5.finalsMethod[F5.count];
            F5.finalsMethod[F5.count] = "L2M";
            F5.transformFlag = F5.origTerm;
            F5.finalMedraTerms[F5.count] = F5.goodWords2[escapeCount];
            F5.matchOutcome = "medraMatch";
            ++F5.l2mCounter;
            flo.labCheck();
            if (F5.labTransformOccurred) {
                ++F5.labTransformCounter;
            }

            if (!F5.labTransformOccurred) {
                flo.finalFilter();
                if (F5.passedFinalFilter) {
                    flo.regularOutput();
                }
            } else {
                F5.escapeMatchOccurred = true;
            }
        }

        ftm = F5.compress(ftm);
        ftm = ftm.trim();
        return ftm;
    }

    public boolean checkOR(String f) {
        F5.escapeMatchOccurred = false;
        if (f.contains(" or ")) {
            tempArray = f.trim().split(" ");
            String phrase1;
            String phrase2;
            boolean phraseFound;
            if (tempArray.length == 4 && tempArray[2].equalsIgnoreCase("or")) {
                phrase1 = tempArray[0] + " " + tempArray[1];
                phrase2 = tempArray[0] + " " + tempArray[3];
                phraseFound = checkMedra2(phrase1);
                if (phraseFound) {
                    F5.escapeMatchOccurred = true;
                    F5.transformFlag = f;
                    F5.finalMedraTerms[F5.count] = phrase1;
                    F5.matchOutcome = "medraMatch";
                    flo.regularOutput();
                }

                phraseFound = checkMedra2(phrase2);
                if (phraseFound) {
                    F5.escapeMatchOccurred = true;
                    F5.finalMedraTerms[F5.count] = phrase2;
                    F5.transformFlag = f;
                    F5.matchOutcome = "medraMatch";
                    flo.regularOutput();
                }
            } else if (tempArray.length == 4 && tempArray[1].equalsIgnoreCase("or")) {
                phrase1 = tempArray[0] + " " + tempArray[3];
                phrase2 = tempArray[2] + " " + tempArray[3];
                phraseFound = checkMedra2(phrase1);
                if (phraseFound) {
                    F5.escapeMatchOccurred = true;
                    F5.transformFlag = f;
                    F5.matchOutcome = "medraMatch";
                    F5.finalMedraTerms[F5.count] = phrase1;
                    flo.regularOutput();
                }

                phraseFound = checkMedra2(phrase2);
                if (phraseFound) {
                    F5.escapeMatchOccurred = true;
                    F5.transformFlag = f;
                    F5.finalMedraTerms[F5.count] = phrase2;
                    F5.matchOutcome = "medraMatch";
                    flo.regularOutput();
                }
            }
        }

        return F5.escapeMatchOccurred;
    }

    public String clean1(String f) {
        for (int j = 0; j < F5.maxClean1; ++j) {
            if (f.endsWith(F5.clean1[j])) {
                f = f.replaceAll(F5.clean1[j], "");
            }
        }

        f = F5.compress(f);
        f = f.trim();
        return f;
    }

    public String clean2(String f) {
        for (int j = 0; j < F5.maxClean2; ++j) {
            if (f.startsWith(F5.clean2[j])) {
                f = f.replaceFirst(F5.clean2[j], "");
            }
        }

        f = F5.compress(f);
        f = f.trim();
        return f;
    }

    public String clean3(String f) {
        for (int j = 0; j < F5.maxClean3; ++j) {
            if (f.equalsIgnoreCase(F5.clean3[j])) {
                f = "";
                break;
            }
        }

        f = F5.compress(f);
        f = f.trim();
        return f;
    }

    public String cleanConvert(String ft) {
        ft = this.clean1(" " + ft + " ");
        ft = this.clean2(" " + ft + " ");
        ft = this.clean3(ft);
        ft = getSyn1(ft);
        ft = getSwit(ft);
        ft = getSyn2(ft);
        return ft;
    }

    public String finalTransform(String fm) {
        if (fm.equalsIgnoreCase("alte")) {
            return "SGPT increased";
        } else {
            return fm.equalsIgnoreCase("fatigueability") ? "fatigue" : fm;
        }
    }
}
