package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.erasmusmc.mi.biosemantics.splicer.Database.TABLE_MEDDRATESTS;
import static nl.erasmusmc.mi.biosemantics.splicer.Database.TABLE_MEDDRA_DB_WITHOUT_REVERSE_STEMS;
import static nl.erasmusmc.mi.biosemantics.splicer.Database.TABLE_MEDSYNS;
import static nl.erasmusmc.mi.biosemantics.splicer.Database.TABLE_MEDSYNS_WITHSTEMS;
import static nl.erasmusmc.mi.biosemantics.splicer.Database.TABLE_SWIT;
import static nl.erasmusmc.mi.biosemantics.splicer.Database.TABLE_SYN_1;
import static nl.erasmusmc.mi.biosemantics.splicer.Database.TABLE_SYN_2;
import static nl.erasmusmc.mi.biosemantics.splicer.Database.getConnection;
import static nl.erasmusmc.mi.biosemantics.splicer.F5.fail;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.L2M;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.LM;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.M1;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.MS;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.NONE;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.T1M1;

public class CleanUp {
    private static final Logger log = LogManager.getLogger();


    private final Splicer splicer;
    private String[] tempArray = new String[1000];
    private String[] tempArray2 = new String[1000];
    private boolean meddraFound = false;
    private String foundMeddra = "";
    private String medStem = "";


    public CleanUp(Splicer splicer) {
        this.splicer = splicer;
    }

    private boolean isJunk(String b) {
        Pattern p = Pattern.compile("\\d");
        Matcher m = p.matcher(b);
        if (m.find()) {
            return true;
        } else {
            p = Pattern.compile("-");
            m = p.matcher(b);
            int dashCount = 0;
            while (m.find()) {
                dashCount++;
            }
            return dashCount > 1;
        }
    }

    private boolean checkMeddraToken(String b) {
        boolean mappedMedraValid;
        b = b.trim();
        tempArray = b.split(" ");
        var q = "Select Medra2, Stems from " + TABLE_MEDDRA_DB_WITHOUT_REVERSE_STEMS + " where Stems LIKE ?";
        try (var conn = getConnection(); var stmt = conn.prepareStatement(q)) {
            stmt.setString(1, "%" + tempArray[0] + "%");
            ResultSet rs = stmt.executeQuery();

            String lowerMedStem;
            String spacedTerm;

            do {
                do {
                    if (!rs.next()) {
                        return false;
                    }

                    mappedMedraValid = true;
                    foundMeddra = rs.getString("Medra2");
                    if (rs.wasNull()) {
                        foundMeddra = "";
                    }

                    medStem = rs.getString("Stems");
                    if (rs.wasNull()) {
                        medStem = "";
                    }


                    b = b.trim();
                    lowerMedStem = medStem;
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
        } catch (SQLException e) {
            fail(e);
            return false;
        }
    }

    private boolean checkSynToken(String b) {
        boolean mappedMedraValid;
        tempArray = b.trim().split(" ");

        var q = "Select MDRTerm, EffectStems from " + TABLE_MEDSYNS_WITHSTEMS + " where EffectStems LIKE ?";

        try (var conn = getConnection(); var stmt = conn.prepareStatement(q)) {
            stmt.setString(1, "%" + tempArray[0] + "%");
            ResultSet rs = stmt.executeQuery();

            String lowerMedStem;
            String spacedTerm;


            do {
                do {
                    if (!rs.next()) {
                        return false;
                    }

                    mappedMedraValid = true;
                    foundMeddra = rs.getString("MDRTerm");
                    if (rs.wasNull()) {
                        foundMeddra = "";
                    }

                    medStem = rs.getString("EffectStems");
                    if (rs.wasNull()) {
                        medStem = "";
                    }

                    medStem = medStem.toLowerCase();

                    b = b.trim();
                    lowerMedStem = medStem.toLowerCase();
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
        } catch (SQLException e) {
            fail(e);
            return false;
        }
    }

    private boolean checkMeddra(String b) {
        var q = "Select Medra2, Stems from " + TABLE_MEDDRA_DB_WITHOUT_REVERSE_STEMS + " where Stems = ? limit 1";
        try (var conn = getConnection(); var ps = conn.prepareStatement(q)) {
            ps.setString(1, " " + b.trim() + " ");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                foundMeddra = rs.getString("Medra2");
                medStem = rs.getString("Stems");
                return true;
            }
        } catch (SQLException e) {
            fail(e);
        }
        return false;
    }

    private boolean checkMeddra2(String b) {
        String q = "Select Medra2, Stems from " + TABLE_MEDDRA_DB_WITHOUT_REVERSE_STEMS + " where Medra2 = ?";
        try (var conn = getConnection(); var stmt = conn.prepareStatement(q)) {
            stmt.setString(1, " " + b.trim() + " ");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                foundMeddra = rs.getString("Medra2");
                medStem = rs.getString("Stems");
                return true;
            }
        } catch (SQLException e) {
            fail(e);
        }
        return false;
    }

    private String getSyn1(String b) {

        var q = "Select Field1,Field2 from " + TABLE_SYN_1 + " where (((InStr(?, Field1))>0))";
        try (var conn = getConnection(); var stmt = conn.prepareStatement(q)) {
            stmt.setString(1, " " + b + " ");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                var f1 = rs.getString("Field1");
                if (rs.wasNull()) {
                    f1 = "";
                }

                var f2 = rs.getString("Field2");
                if (rs.wasNull()) {
                    f2 = "";
                }

                if (!f1.isBlank() && !f1.equalsIgnoreCase(" ")) {
                    f2 = " " + f2 + " ";
                    b = b.replace(f1, f2);
                }
            }

        } catch (SQLException e) {
            fail(e);
        }

        return b.trim();
    }

    private String getSwit(String b) {
        String q = "Select Field1,Field2 from " + TABLE_SWIT + " where (((InStr(?, Field1))>0))";

        try (var conn = getConnection(); var stmt = conn.prepareStatement(q)) {
            stmt.setString(1, " " + b + " ");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                var f1 = rs.getString("Field1");
                if (rs.wasNull()) {
                    f1 = "";
                }

                var f2 = rs.getString("Field2");
                if (rs.wasNull()) {
                    f2 = "";
                }

                if (b.startsWith(f1)) {
                    b = b.replace(f1, "");

                    b = b + " " + f2;
                }
            }

        } catch (Exception e) {
            fail(e);
        }

        return b.trim();
    }

    private String getSyn2(String b) {
        b = " " + b + " ";
        b = b.replaceAll("\\(", "")
                .replaceAll("\\)", "");

        var q = "Select Field1, Field2 from " + TABLE_SYN_2 + " where InStr(?, Field1)>0";
        try (var conn = getConnection(); var stmt = conn.prepareStatement(q)) {
            stmt.setString(1, b);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                var f1 = rs.getString("Field1");
                if (rs.wasNull()) {
                    f1 = "";
                }

                var f2 = rs.getString("Field2");
                if (rs.wasNull()) {
                    f2 = "";
                }

                f1 = f1 + " ";
                if (b.endsWith(f1)) {
                    b = b.replace(f1, "");
                    b = b + " " + f2;
                }
            }

        } catch (Exception e) {
            fail(e);
        }

        return b.trim();
    }

    private String getMedSyns(String b) {
        var q = "Select MDRTerm from " + TABLE_MEDSYNS + " where Effect = ? limit 1";
        try (var conn = getConnection(); var ps = conn.prepareStatement(q)) {
            ps.setString(1, b);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                meddraFound = true;
                return rs.getString("MDRTerm");
            }
        } catch (SQLException e) {
            fail(e);
        }
        return b;
    }

    public String getLabDirection(String b, String s, String sec, String oriTerm, Method md) {
        if (s == null || (s.length() < 5 && !sec.contains("Adverse Reactions"))) {
            return "";
        } else if (isAMeddraTest(b)) {
            boolean increased = false;
            boolean decreased = false;
            boolean abnormal = false;
            String preMed = "";
            String postMed = "";
            s = s.toLowerCase();
            s = Normals.normalSpl(s);
            s = Normals.normal2(s);
            Pattern p;
            Matcher m;
            if (md.toString().contains("L")) {
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

            if (increased && decreased) {
                return Reference.defaultLabDirection().getOrDefault(b.toLowerCase(), abnormal ? "abnormal" : "indeterminate");
            }

            if (abnormal) {
                return "abnormal";
            }

            return "unstated";
        }
        return "";
    }


    private boolean isAMeddraTest(String b) {
        var q = "Select Field1 from " + TABLE_MEDDRATESTS + " where Field1 = ? limit 1";
        try (var conn = getConnection(); var ps = conn.prepareStatement(q)) {
            ps.setString(1, b.trim());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            fail(e);
        }
        return false;
    }

    public void similarity(String b) {
        b = b.trim();
        String shortB;
        if (b.length() > 8) {
            shortB = b.substring(2, b.length() - 2);
        } else if (b.length() > 6) {
            shortB = b.substring(1, b.length() - 1);
        } else {
            shortB = b;
        }

        var q = "SELECT medra2, stems FROM " + TABLE_MEDDRA_DB_WITHOUT_REVERSE_STEMS + " WHERE medra2 LIKE ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(q)) {
            ps.setString(1, +'%' + shortB + '%');
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                foundMeddra = rs.getString("Medra2");
                if (rs.wasNull()) {
                    foundMeddra = "";
                }

                medStem = rs.getString("Stems");
                if (rs.wasNull()) {
                    medStem = "";
                }

                medStem = medStem.toLowerCase();
                double closenessScore = Similar.compareStrings(b.trim(), foundMeddra.trim());
                if (closenessScore > 0.9D) {
                    splicer.origMethod = splicer.finalsMethod[splicer.count];
                    splicer.finalsMethod[splicer.count] = MS;
                    splicer.transformFlag = splicer.origTerm;
                    splicer.finalMeddraTerms[splicer.count] = foundMeddra;
                    splicer.matchedOutcome = true;
                    splicer.flow.labCheck();
                    if (splicer.labTransformOccurred) {
                        ++splicer.labTransformCounter;
                        break;
                    }

                    splicer.finalMeddraTerms[splicer.count] = splicer.finalMeddraTerms[splicer.count].trim();
                    splicer.flow.finalFilter();
                    if (splicer.passedFinalFilter) {
                        splicer.flow.regularOutput();
                        break;
                    }
                }
            }

        } catch (Exception e) {
            fail(e);
        }

    }

    public boolean transformLab2Medra(String a, String d) {
        if (d.contains("increased")) {
            meddraFound = checkMeddra2(a + " increased");
            if (meddraFound) {
                concludeMatchAndOutput();
                return true;
            }

            meddraFound = checkMeddra2("blood " + a + " increased");
            if (meddraFound) {
                concludeMatchAndOutput();
                return true;
            }
        }

        if (d.contains("decreased")) {
            meddraFound = checkMeddra2(a + " decreased");
            if (meddraFound) {
                concludeMatchAndOutput();
                return true;
            }

            meddraFound = checkMeddra2("blood " + a + " decreased");
            if (meddraFound) {
                concludeMatchAndOutput();
                return true;
            }
        }

        if (d.contains("abnormal")) {
            meddraFound = checkMeddra2(a + " abnormal");
            if (meddraFound) {
                concludeMatchAndOutput();
                return true;
            }

            meddraFound = checkMeddra2("blood " + a + " abnormal");
            if (meddraFound) {
                concludeMatchAndOutput();
                return true;
            }
        }

        return false;
    }

    private void concludeMatchAndOutput() {
        splicer.matchedOutcome = true;
        splicer.finalsMethod[splicer.count] = LM;
        splicer.transformFlag = splicer.origTerm;
        splicer.finalMeddraTerms[splicer.count] = foundMeddra;
        splicer.finalMeddraTerms[splicer.count] = splicer.finalMeddraTerms[splicer.count].trim();
        splicer.direction = "transformed";
        splicer.flow.finalFilter();
        if (splicer.passedFinalFilter) {
            splicer.flow.regularOutput();
        }

    }

    private boolean falseAde(String b) {
        return Reference.junkSet().contains(b);
    }

    public boolean finalFilter() {
        Normals.normalizeFilter(splicer.finalMeddraTerms[splicer.count]);
        var m = splicer.finalsMethod[splicer.count];
        if (m == null || m.equals(NONE)) {
            return false;
        } else if (splicer.finalMeddraTerms[splicer.count].length() <= 2) {
            return false;
        } else if (splicer.finalsMethod[splicer.count].equals(MS) && Reference.msFilterSet().contains(splicer.finalMeddraTerms[splicer.count])) {
            return false;
        } else if (splicer.finalsMethod[splicer.count].equals(L2M) && Reference.l2mFilterSet().contains(splicer.finalMeddraTerms[splicer.count])) {
            return false;
        } else if (Reference.l2mFilterSet().contains(splicer.transformFlag)) {
            return false;
        } else if (Reference.splFilterSet().contains(splicer.finalMeddraTerms[splicer.count])) {
            return false;
        } else if (Reference.filterCon().stream().anyMatch(fc -> splicer.finalsMethod[splicer.count].toString().contains(fc))) {
            return false;
        }

        if (splicer.finalsMethod[splicer.count].toString().contains("M") && Integer.parseInt(splicer.sentNumArray[splicer.count]) > -1) {

            String targetSent = splicer.sent[Integer.parseInt(splicer.sentNumArray[splicer.count])];
            if (targetSent != null && targetSent.contains(splicer.transformFlag)) {
                String preMed = targetSent.substring(0, targetSent.indexOf(splicer.transformFlag));
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


    public boolean cleanIndication(String i) {
        if (i.equalsIgnoreCase(" all ")) {
            return false;
        } else if (i.equalsIgnoreCase(" co ")) {
            return false;
        } else {
            return !i.equalsIgnoreCase(" drug therapy ");
        }
    }

    public String mappingProcesses(String ftm, Method method) {
        if (ftm.isBlank() || falseAde(ftm)) {
            return ftm;
        } else {
            meddraFound = false;
            splicer.matchedOutcome = null;
            if (method == null) {
                method = NONE;
            }

            boolean isIndication = splicer.indicationArray.contains(ftm);
            if (isIndication) {
                return "";
            } else if (!method.equals(M1) && !method.equals(T1M1)) {
                if (isJunk(ftm)) {
                    return "";
                } else {
                    meddraFound = checkMeddra2(ftm);
                    if (meddraFound) {
                        splicer.matchedOutcome = true;
                        return foundMeddra;
                    } else {
                        meddraFound = this.checkOR(ftm);
                        if (meddraFound) {
                            log.debug("I CHECKED ORs and it was TRUE!");
                            splicer.matchedOutcome = true;
                            return foundMeddra;
                        } else {
                            ftm = getMedSyns(ftm);
                            if (meddraFound) {
                                splicer.matchedOutcome = true;
                                return ftm;
                            } else {
                                String workingTerm = ftm.trim();
                                tempArray = workingTerm.split(" ");
                                StringBuilder allWords = new StringBuilder();
                                for (String s : tempArray) {
                                    boolean isStopWord = Reference.stopSet().contains(s);
                                    if (!isStopWord) {
                                        String stemmedWord = PorterStemmer.portStem(s);
                                        if (stemmedWord.equalsIgnoreCase("Invalid term")) {
                                            stemmedWord = s;
                                        }
                                        allWords.append(" ").append(stemmedWord);
                                    }
                                }

                                allWords = new StringBuilder(allWords.toString().trim());
                                allWords.insert(0, " ");
                                if (!allWords.toString().equals(" ")) {
                                    meddraFound = checkMeddra(allWords.toString());
                                }

                                if (meddraFound) {
                                    splicer.matchedOutcome = true;
                                    return foundMeddra;
                                } else {
                                    ftm = cleanConvert(ftm);
                                    if (ftm.isBlank()) {
                                        return ftm;
                                    } else {
                                        meddraFound = checkMeddra2(ftm);
                                        if (meddraFound) {
                                            splicer.matchedOutcome = true;
                                            return foundMeddra;
                                        } else {
                                            if (!allWords.toString().equalsIgnoreCase(" ")) {
                                                meddraFound = checkMeddraToken(allWords.toString());
                                            }

                                            if (meddraFound) {
                                                splicer.matchedOutcome = true;
                                                return foundMeddra;
                                            } else {
                                                if (!allWords.toString().equalsIgnoreCase(" ")) {
                                                    meddraFound = checkSynToken(allWords.toString());
                                                }

                                                if (meddraFound) {
                                                    splicer.matchedOutcome = true;
                                                    return foundMeddra;
                                                } else {
                                                    splicer.matchedOutcome = false;
                                                    ftm = Normals.compress(ftm);
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
                splicer.matchedOutcome = true;
                return ftm;
            }
        }
    }

    public String cleanTermEscape(String ftm) {

        splicer.escapeMatchOccurred = false;
        splicer.meddraCount = 0;
        splicer.adeProcess.getMeddraTermsEscape2(ftm);
        splicer.adeProcess.findMatch(splicer.origTerm2);
        splicer.adeProcess.getUniqueLCSEscape();

        for (int escapeCount = 0; escapeCount < splicer.goodCount; ++escapeCount) {
            log.debug(splicer.goodWords2[escapeCount]);

            splicer.origMethod = splicer.finalsMethod[splicer.count];
            splicer.finalsMethod[splicer.count] = L2M;
            splicer.transformFlag = splicer.origTerm;
            splicer.finalMeddraTerms[splicer.count] = splicer.goodWords2[escapeCount];
            splicer.matchedOutcome = true;
            splicer.flow.labCheck();
            if (splicer.labTransformOccurred) {
                ++splicer.labTransformCounter;
            }

            if (!splicer.labTransformOccurred) {
                splicer.flow.finalFilter();
                if (splicer.passedFinalFilter) {
                    splicer.flow.regularOutput();
                }
            } else {
                splicer.escapeMatchOccurred = true;
            }
        }

        ftm = Normals.compress(ftm);
        ftm = ftm.trim();
        return ftm;
    }

    private boolean checkOR(String f) {
        splicer.escapeMatchOccurred = false;
        if (f.contains(" or ")) {
            tempArray = f.trim().split(" ");
            String phrase1;
            String phrase2;
            boolean phraseFound;
            if (tempArray.length == 4 && tempArray[2].equalsIgnoreCase("or")) {
                phrase1 = tempArray[0] + " " + tempArray[1];
                phrase2 = tempArray[0] + " " + tempArray[3];
                phraseFound = checkMeddra2(phrase1);
                if (phraseFound) {
                    splicer.escapeMatchOccurred = true;
                    splicer.transformFlag = f;
                    splicer.finalMeddraTerms[splicer.count] = phrase1;
                    splicer.matchedOutcome = true;
                    splicer.flow.regularOutput();
                }

                phraseFound = checkMeddra2(phrase2);
                if (phraseFound) {
                    splicer.escapeMatchOccurred = true;
                    splicer.finalMeddraTerms[splicer.count] = phrase2;
                    splicer.transformFlag = f;
                    splicer.matchedOutcome = true;
                    splicer.flow.regularOutput();
                }
            } else if (tempArray.length == 4 && tempArray[1].equalsIgnoreCase("or")) {
                phrase1 = tempArray[0] + " " + tempArray[3];
                phrase2 = tempArray[2] + " " + tempArray[3];
                phraseFound = checkMeddra2(phrase1);
                if (phraseFound) {
                    splicer.escapeMatchOccurred = true;
                    splicer.transformFlag = f;
                    splicer.matchedOutcome = true;
                    splicer.finalMeddraTerms[splicer.count] = phrase1;
                    splicer.flow.regularOutput();
                }

                phraseFound = checkMeddra2(phrase2);
                if (phraseFound) {
                    splicer.escapeMatchOccurred = true;
                    splicer.transformFlag = f;
                    splicer.finalMeddraTerms[splicer.count] = phrase2;
                    splicer.matchedOutcome = true;
                    splicer.flow.regularOutput();
                }
            }
        }

        return splicer.escapeMatchOccurred;
    }

    private String clean1(String f) {
        for (String c : Reference.clean1()) {
            if (f.endsWith(c)) {
                f = f.replace(c, "");
            }
        }
        return Normals.compress(f).trim();
    }

    private String clean2(String f) {
        for (String c : Reference.clean2()) {
            if (f.startsWith(c)) {
                f = f.replaceFirst(c, "");
            }
        }
        return Normals.compress(f).trim();
    }

    private String clean3(String f) {
        if (Reference.clean3().contains(f.toLowerCase())) {
            return "";
        }
        f = Normals.compress(f);
        return f.trim();
    }

    private String cleanConvert(String ft) {
        ft = clean1(" " + ft + " ");
        ft = clean2(" " + ft + " ");
        ft = clean3(ft);
        ft = getSyn1(ft);
        ft = getSwit(ft);
        return getSyn2(ft);
    }


}
