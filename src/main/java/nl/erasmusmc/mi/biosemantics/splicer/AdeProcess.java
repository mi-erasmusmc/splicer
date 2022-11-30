package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

import static nl.erasmusmc.mi.biosemantics.splicer.Database.*;
import static nl.erasmusmc.mi.biosemantics.splicer.Database.getConnection;
import static nl.erasmusmc.mi.biosemantics.splicer.F5.fail;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.L1;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.L2;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.L3;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.M1;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.NONE;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.T1L1;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.T1M1;

public class AdeProcess {

    private static final Logger log = LogManager.getLogger();
    private String se = "";
    private String[] tempArray3 = new String[100];
    private final String[] allWords3 = new String[1000];

    private final Splicer splicer;

    public AdeProcess(Splicer splicer) {
        this.splicer = splicer;
    }

    public void getMeddraTerms(String b) {
        if (!b.isBlank()) {
            String q = "SELECT Medra2 FROM " + TABLE_MEDDRA_DB_WITHOUT_REVERSE + " WHERE INSTR(?, Medra2)>0";
            try (var ps = getConnection().prepareStatement(q)) {
                ps.setString(1, " " + b + " ");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    var m = rs.getString("Medra2");
                    if (splicer.tableToMeddra) {
                        splicer.allWords[splicer.meddraCount] = new Word(m, T1M1);
                    } else {
                        splicer.allWords[splicer.meddraCount] = new Word(m, M1);
                    }
                    ++splicer.meddraCount;
                }
            } catch (SQLException e) {
                fail(e);
            }
            getMeddraSynonyms(b);
        }

    }

    public void getMeddraTermsStop(String b) {
        log.debug("Getting MedDRA Terms Stop {}", b);
        String q = "SELECT Medra2 FROM " + TABLE_MEDDRA_DB_WITHOUT_REVERSE_STEMS + " WHERE INSTR(?, Stops)>0";
        try (var ps = getConnection().prepareStatement(q)) {
            ps.setString(1, " " + b.trim() + " ");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                var mh = rs.getString("Medra2");
                if (splicer.tableToMeddra) {
                    splicer.allWords[splicer.meddraCount] = new Word(mh, T1M1);
                } else {
                    splicer.allWords[splicer.meddraCount] = new Word(mh, M1);
                }
                ++splicer.meddraCount;
            }

        } catch (SQLException e) {
            fail(e);
        }
    }

    public void getMeddraSynonyms(String b) {
        var q = "SELECT MDRTerm FROM " + TABLE_MEDSYNS + " WHERE INSTR(?, Effect)>0";
        try (var ps = getConnection().prepareStatement(q)) {
            ps.setString(1, b);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                var f2 = rs.getString("MDRTerm");
                if (splicer.tableToMeddra) {
                    splicer.allWords[splicer.meddraCount] = new Word(f2, T1M1);
                } else {
                    splicer.allWords[splicer.meddraCount] = new Word(f2, M1);
                }
                ++splicer.meddraCount;
            }
        } catch (SQLException e) {
            fail(e);
        }
    }

    public void getMeddraTermsIndications(String b) {
        String q = "SELECT DISTINCT Medra2 FROM " + TABLE_MEDDRA_DB_WITHOUT_REVERSE + " WHERE INSTR(?, Medra2)>0";
        try (var ps = getConnection().prepareStatement(q)) {
            ps.setString(1, " " + b + " ");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                var mh = rs.getString("Medra2");
                boolean indClean = splicer.cleanUp.cleanIndication(mh);
                if (indClean) {
                    splicer.allWords[splicer.meddraCount] = new Word(mh);
                    ++splicer.meddraCount;
                }
            }
        } catch (SQLException e) {
            fail(e);
        }
    }

    public void getMeddraTermsEscape2(String b) {
        String q = "SELECT Medra2, Stems FROM " + TABLE_MEDDRA_DB_WITHOUT_REVERSE_STEMS + " WHERE INSTR(?, Stems)>0";
        try (var ps = getConnection().prepareStatement(q)) {
            ps.setString(1, " " + b + " ");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                var mh = rs.getString("Medra2");
                var ste = rs.getString("Stems");
                if (b.contains(ste)) {
                    allWords3[splicer.meddraCount] = mh;
                    ++splicer.meddraCount;
                }
            }

        } catch (Exception e) {
            fail(e);
        }
        getMeddraSynsEscape(b);
    }

    public void getMeddraSynsEscape(String b) {
        var q = "SELECT MDRTerm FROM " + TABLE_MEDSYNS + " WHERE (((INSTR(?, Effect))>0))";
        try (var ps = getConnection().prepareStatement(q)) {
            ps.setString(1, b);
            var rs = ps.executeQuery();
            while (rs.next()){
                allWords3[splicer.meddraCount] = rs.getString("MDRTerm");
                ++splicer.meddraCount;
            }
        } catch (SQLException e) {
            fail(e);
        }
    }

    public void getList(String b) {

        splicer.tempArray4 = b.split(":");
        if (b.contains(" : ") && splicer.tempArray4.length == 1) {
            b = b.substring(b.lastIndexOf(" : "));
        }

        if (b.contains(" and ")) {
            b = b.replace(", and ", ", ");
            b = b.replace(" and ", " , ");
        }

        splicer.tempArray4 = b.split(",");
        String tempMedTerm;
        if (splicer.origSent.contains("Italics")) {
            tempMedTerm = splicer.origSent.substring(splicer.origSent.indexOf("Italics"));
            tempMedTerm = tempMedTerm.substring(tempMedTerm.indexOf(">"), tempMedTerm.indexOf("<"));
            tempMedTerm = tempMedTerm.replace(">", "");
            tempMedTerm = Normals.compress(tempMedTerm);
            tempMedTerm = tempMedTerm.trim();
            var tempArray2 = tempMedTerm.split(",");

            String nSpl;
            for (String s : tempArray2) {
                nSpl = " " + s.toLowerCase() + " ";
                nSpl = Normals.normal7(nSpl);
                tempArray3 = nSpl.split(" ");
                if (tempArray3.length <= 3 && nSpl.length() > 4) {
                    splicer.allWords[splicer.meddraCount] = new Word(nSpl + "--i", L1);
                    ++splicer.meddraCount;
                }
            }

            if (splicer.origSent.contains("</content>")) {
                b = splicer.origSent.substring(splicer.origSent.lastIndexOf("</content>"));
            }
            nSpl = Normals.normalSpl(b);
            nSpl = Normals.normal2(nSpl);
            nSpl = nSpl.trim();
            if (nSpl.startsWith(",")) {
                nSpl = nSpl.replaceFirst(",", "");
                nSpl = nSpl.trim();
            }

            splicer.currentSentence = nSpl;
            log.debug("2 ******** clean sentence: {}   {} ", splicer.countSentences, nSpl);
            splicer.tempArray = nSpl.split(",");
        }

        splicer.count = 0;

        for (; splicer.count < splicer.tempArray4.length; ++splicer.count) {
            tempMedTerm = splicer.tempArray4[splicer.count];
            tempMedTerm = tempMedTerm.trim();
            if (tempMedTerm.contains("(see -")) {
                tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf("(see -"));
            }

            if (tempMedTerm.contains("href")) {
                tempMedTerm = "";
            }

            tempMedTerm = tempMedTerm.toLowerCase();
            if (splicer.count == 0) {
                if (tempMedTerm.contains(" : ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" : "));
                } else if (tempMedTerm.contains(" were ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" were "));
                } else if (tempMedTerm.contains(" are ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" are "));
                } else if (tempMedTerm.contains(" is ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" is "));
                } else if (tempMedTerm.contains(" include ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" include "));
                }
            }

            tempMedTerm = " " + tempMedTerm + " ";
            if (tempMedTerm.contains(" : ")) {
                tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" : "));
            }

            tempMedTerm = Normals.normal7(tempMedTerm);
            tempArray3 = tempMedTerm.split(" ");
            if (tempArray3.length <= 4 && tempMedTerm.length() >= 3) {
                if (tempMedTerm.startsWith("and ") && splicer.count == splicer.tempArray4.length - 1) {
                    tempMedTerm = tempMedTerm.replaceFirst("and ", "");
                    tempMedTerm = tempMedTerm.trim();
                    if (tempMedTerm.contains(" were ")) {
                        tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" were "));
                    } else if (tempMedTerm.contains(" are ")) {
                        tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" are "));
                    } else if (tempMedTerm.contains(" is ")) {
                        tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" is "));
                    }

                }
                splicer.allWords[splicer.meddraCount] = new Word(tempMedTerm, L1);
                ++splicer.meddraCount;
            } else if (tempArray3.length > 4 && tempMedTerm.length() >= 3 && splicer.count == splicer.tempArray4.length - 1) {
                if (tempMedTerm.startsWith("and ")) {
                    tempMedTerm = tempMedTerm.replaceFirst("and ", "");
                    tempMedTerm = tempMedTerm.trim();
                    if (tempMedTerm.contains(" were ")) {
                        tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" were "));
                    } else if (tempMedTerm.contains(" are ")) {
                        tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" are "));
                    } else if (tempMedTerm.contains(" is ")) {
                        tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" is "));
                    }

                    splicer.allWords[splicer.meddraCount] = new Word(tempMedTerm, L1);
                    ++splicer.meddraCount;
                } else {
                    getMeddraTerms(tempMedTerm);
                }
            } else {
                getMeddraTerms(tempMedTerm);
            }
        }

    }

    public void getList2(String b) {
        if (b.contains(" : ")) {
            b = b.substring(b.lastIndexOf(" : "));
        }

        if (b.contains(" and ")) {
            b = b.replace(", and ", ", ");
            b = b.replace(" and ", " , ");
        }

        splicer.tempArray4 = b.split(",");

        for (splicer.count = 0; splicer.count < splicer.tempArray4.length; ++splicer.count) {
            String tempMedTerm = splicer.tempArray4[splicer.count];
            tempMedTerm = tempMedTerm.toLowerCase();
            tempMedTerm = tempMedTerm.trim();
            if (tempMedTerm.contains("(see -")) {
                tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf("(see -"));
            }

            if (tempMedTerm.contains("href")) {
                tempMedTerm = "";
            }

            if (splicer.count == 0) {
                if (tempMedTerm.contains(" : ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" : "));
                } else if (tempMedTerm.contains(" were ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" were "));
                } else if (tempMedTerm.contains(" are ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" are "));
                } else if (tempMedTerm.contains(" is ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" is "));
                }
            }

            tempMedTerm = " " + tempMedTerm + " ";
            if (tempMedTerm.contains("%") && tempMedTerm.contains("(") && tempMedTerm.contains(")") && tempMedTerm.indexOf("(") < tempMedTerm.indexOf("%") && tempMedTerm.indexOf("%") < tempMedTerm.indexOf(")")) {
                tempMedTerm = Normals.normal7(tempMedTerm);
                tempArray3 = tempMedTerm.split(" ");
                if (tempArray3.length <= 4 && tempMedTerm.length() >= 3) {
                    splicer.allWords[splicer.meddraCount] = new Word(tempMedTerm, L2);
                    ++splicer.meddraCount;
                }
            } else if (tempMedTerm.length() > 3) {
                if (tempArray3.length > 4) {
                    getMeddraTerms(tempMedTerm);
                } else {
                    splicer.allWords[splicer.meddraCount] = new Word(tempMedTerm, L2);
                    ++splicer.meddraCount;
                }
            }
        }

    }

    public void getList3(String b) {
        splicer.count = 0;
        if (b.contains(" and ")) {
            b = b.replace(", and ", ", ");
            b = b.replace(" and ", " , ");
        }

        if (b.contains(",")) {
            splicer.tempArray4 = b.split(",");
        } else {
            splicer.tempArray4 = b.split("\\)");

            for (int j = 0; j < splicer.tempArray4.length; ++j) {
                splicer.tempArray4[j] = splicer.tempArray4[j] + ")";
            }
        }

        for (; splicer.count < splicer.tempArray4.length; ++splicer.count) {
            String tempMedTerm = splicer.tempArray4[splicer.count];
            tempMedTerm = tempMedTerm.toLowerCase();
            tempMedTerm = tempMedTerm.trim();
            if (tempMedTerm.contains("(see -")) {
                tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf("(see -"));
            }

            if (tempMedTerm.contains("href")) {
                tempMedTerm = "";
            }

            if (splicer.count == 0) {
                if (tempMedTerm.contains(" : ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" : "));
                } else if (tempMedTerm.contains(" were ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" were "));
                } else if (tempMedTerm.contains(" are ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" are "));
                } else if (tempMedTerm.contains(" is ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" is "));
                }
            }

            tempMedTerm = " " + tempMedTerm + " ";
            if (tempMedTerm.contains("%") && tempMedTerm.contains("(") && tempMedTerm.contains(")") && tempMedTerm.indexOf("(") < tempMedTerm.indexOf("%") && tempMedTerm.indexOf("%") < tempMedTerm.indexOf(")")) {
                tempMedTerm = Normals.normal7(tempMedTerm);
                tempArray3 = tempMedTerm.split(" ");
                if (tempArray3.length <= 3 && tempMedTerm.length() >= 3) {
                    splicer.allWords[splicer.meddraCount] = new Word(tempMedTerm, L3);
                    ++splicer.meddraCount;
                } else {
                    getMeddraTerms(tempMedTerm);
                }
            } else {
                getMeddraTerms(tempMedTerm);
            }
        }

    }

    public void getT1L1_2(String b) {

        splicer.tempArray4 = b.split(":");
        if (b.contains(" : ") && splicer.tempArray4.length == 1) {
            b = b.substring(b.lastIndexOf(" : "));
        }

        if (b.contains(" and ")) {
            b = b.replace(", and ", ", ");
            b = b.replace(" and ", " , ");
        }

        splicer.tempArray4 = b.split(",");

        for (splicer.count = 0; splicer.count < splicer.tempArray4.length; ++splicer.count) {
            String tempMedTerm = splicer.tempArray4[splicer.count];

            if (!tempMedTerm.contains("bbbb")) {
                tempMedTerm = tempMedTerm.trim();
                if (tempMedTerm.contains("(see -")) {
                    tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf("(see -"));
                }

                if (tempMedTerm.contains("href")) {
                    tempMedTerm = "";
                }

                tempMedTerm = tempMedTerm.toLowerCase();
                if (splicer.count == 0) {
                    if (tempMedTerm.contains(" : ")) {
                        tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" : "));
                    } else if (tempMedTerm.contains(" were ")) {
                        tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" were "));
                    } else if (tempMedTerm.contains(" are ")) {
                        tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" are "));
                    } else if (tempMedTerm.contains(" is ")) {
                        tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" is "));
                    } else if (tempMedTerm.contains(" include ")) {
                        tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" include "));
                    }
                }

                tempMedTerm = " " + tempMedTerm + " ";
                if (tempMedTerm.contains(" : ")) {
                    tempMedTerm = tempMedTerm.substring(tempMedTerm.lastIndexOf(" : "));
                }

                tempMedTerm = Normals.normal7(tempMedTerm);
                tempArray3 = tempMedTerm.split(" ");
                if (tempArray3.length <= 7 && tempMedTerm.length() >= 3) {
                    if (tempMedTerm.startsWith("and ") && splicer.count == splicer.tempArray4.length - 1) {
                        tempMedTerm = tempMedTerm.replaceFirst("and ", "");
                        tempMedTerm = tempMedTerm.trim();
                        if (tempMedTerm.contains(" were ")) {
                            tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" were "));
                        } else if (tempMedTerm.contains(" are ")) {
                            tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" are "));
                        } else if (tempMedTerm.contains(" is ")) {
                            tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" is "));
                        }

                    }
                    splicer.allWords[splicer.meddraCount] = new Word(tempMedTerm, T1L1);
                    ++splicer.meddraCount;
                } else if (tempArray3.length > 7 && tempMedTerm.length() >= 3) {
                    getMeddraTerms(tempMedTerm);
                }
            }
        }

    }

    public void getUniqueLCS() {
        splicer.goodCount = 0;
        splicer.count = 0;
        String thisWord;

        for (boolean skip; splicer.count < splicer.meddraCount; ++splicer.count) {
            splicer.sm = splicer.allWords[splicer.count].method;

            if (splicer.currentSection.equalsIgnoreCase("Adverse Reactions")) {
                se = "Adverse Reactions";
            } else if (splicer.currentSection.equalsIgnoreCase("over")) {
                se = "ove";
            } else if (splicer.currentSection.equalsIgnoreCase("Precautions (beta)")) {
                se = "Precautions (beta)";
            } else if (splicer.currentSection.equalsIgnoreCase("Warnings (beta)")) {
                se = "Warnings (beta)";
            } else if (splicer.currentSection.equalsIgnoreCase("Black Box (beta)")) {
                se = "Black Box (beta)";
            } else {
                se = splicer.currentSection;
            }

            thisWord = splicer.allWords[splicer.count].word.trim();
            skip = false;

            for (int thisCount = 0; thisCount < splicer.meddraCount; ++thisCount) {
                String thisWord2 = splicer.allWords[thisCount].word.trim();
                if (thisWord2.contains(thisWord) && !thisWord2.equalsIgnoreCase(thisWord)) {
                    skip = true;
                    break;
                }
            }

            if (!skip) {
                for (int goodC = 0; goodC < splicer.goodCount; ++goodC) {
                    if (splicer.goodWords2[goodC].equalsIgnoreCase(thisWord)) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    splicer.goodWords2[splicer.goodCount] = thisWord;
                    splicer.sMethod[splicer.goodCount] = splicer.sm;
                    splicer.sSection[splicer.goodCount] = se;
                    ++splicer.goodCount;
                }
            }
        }

        for (splicer.count = 0; splicer.count < splicer.goodCount - 1; ++splicer.count) {
            log.debug("3 {}", splicer.goodWords2[splicer.count]);
        }

        if (splicer.goodCount == 0) {
            log.debug("4 No good words found");
        }

        if (splicer.goodCount > 0 && !splicer.tableToMeddra) {
            splicer.num.findFrequencies();
            log.debug("good words below");

            for (splicer.count = 0; splicer.count < splicer.goodCount; ++splicer.count) {
                log.debug("count: {}, source: {}, good words: {}", splicer.count, splicer.sMethod[splicer.count], splicer.goodWords2[splicer.count]);
            }
        } else if (splicer.goodCount > 0) {
            splicer.postProcess.putIntoFinals("");
        }

    }

    public void getUniqueLCS2() {
        splicer.goodCount = 0;
        splicer.count = 0;
        String thisWord;
        if (splicer.meddraCount > 0) {
            for (boolean skip; splicer.count < splicer.meddraCount; ++splicer.count) {
                splicer.sm = NONE;
                thisWord = splicer.allWords[splicer.count].word.trim();
                skip = false;

                for (int thisCount = 0; thisCount < splicer.meddraCount; ++thisCount) {
                    String thisWord2 = splicer.allWords[thisCount].word.trim();
                    if (thisWord2.contains(thisWord) && !thisWord2.equalsIgnoreCase(thisWord)) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    for (int goodC = 0; goodC < splicer.goodCount; ++goodC) {
                        if (splicer.goodWords2[goodC].equalsIgnoreCase(thisWord)) {
                            skip = true;
                            break;
                        }
                    }

                    if (!skip) {
                        splicer.goodWords2[splicer.goodCount] = thisWord;
                        splicer.sMethod[splicer.goodCount] = splicer.sm;
                        ++splicer.goodCount;
                    }
                }
            }

            for (splicer.count = 0; splicer.count < splicer.meddraCount; ++splicer.count) {
                log.debug("Indications:  {}", splicer.allWords[splicer.count]);
            }

            if (splicer.goodCount == 0) {
                log.debug("No indications found");
            }

            if (splicer.goodCount > 0) {
                log.debug("indications below");
                splicer.count = 0;

                for (; splicer.count < splicer.goodCount; ++splicer.count) {
                    log.debug("{} source {} indication {}  ", splicer.count, splicer.sMethod[splicer.count], splicer.goodWords2[splicer.count]);
                    splicer.indicationArray.add(splicer.goodWords2[splicer.count]);
                }
            }
        }
    }

    public void getUniqueLCS3() {
        splicer.goodCount = 0;
        splicer.count = 0;
        String thisWord;

        for (boolean skip; splicer.count < splicer.meddraCount; ++splicer.count) {
            splicer.sm = splicer.allWords[splicer.count].method;

            if (splicer.currentSection.equalsIgnoreCase("Adverse Reactions")) {
                se = "Adverse Reactions";
            } else if (splicer.currentSection.equalsIgnoreCase("over")) {
                se = "ove";
            } else if (splicer.currentSection.equalsIgnoreCase("Precautions (beta)")) {
                se = "Precautions (beta)";
            } else if (splicer.currentSection.equalsIgnoreCase("Warnings (beta)")) {
                se = "Warnings (beta)";
            } else if (splicer.currentSection.equalsIgnoreCase("Black Box (beta)")) {
                se = "Black Box (beta)";
            } else {
                se = splicer.currentSection;
            }

            thisWord = splicer.allWords[splicer.count].word.trim();
            skip = false;

            for (int thisCount = 0; thisCount < splicer.meddraCount; ++thisCount) {
                String thisWord2 = splicer.allWords[thisCount].word.trim();
                if (thisWord2.contains(thisWord) && !thisWord2.equalsIgnoreCase(thisWord)) {
                    skip = true;
                    break;
                }
            }

            if (!skip) {
                for (int goodC = 0; goodC < splicer.goodCount; ++goodC) {
                    if (splicer.goodWords2[goodC].equalsIgnoreCase(thisWord)) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    splicer.goodWords2[splicer.goodCount] = thisWord;
                    splicer.sMethod[splicer.goodCount] = splicer.sm;
                    splicer.sSection[splicer.goodCount] = se;
                    ++splicer.goodCount;
                }
            }
        }

        splicer.count = 0;
        if (splicer.goodCount == 0) {
            log.debug("No good words found");
        }

        if (splicer.goodCount > 0 && !splicer.tableToMeddra) {
            for (splicer.count = 0; splicer.count < splicer.goodCount; ++splicer.count) {
                log.debug("Section [{}] found: {}", splicer.sSection[splicer.count], splicer.goodWords2[splicer.count]);
                splicer.postProcess.placeIntoFinal(splicer.goodWords2[splicer.count], splicer.sMethod[splicer.count], splicer.sSection[splicer.count], splicer.countSentences + "", "", "0", "0", "0");
            }
        }

    }

    public void getUniqueLCSEscape() {
        splicer.goodCount = 0;
        String thisWord;
        int countEs = 0;

        for (boolean skip; countEs < splicer.meddraCount; ++countEs) {
            thisWord = splicer.allWords[countEs].word.trim();
            skip = false;

            for (int thisCount = 0; thisCount < splicer.meddraCount; ++thisCount) {
                String thisWord2 = splicer.allWords[thisCount].word.trim();
                if (thisWord2.contains(thisWord) && !thisWord2.equalsIgnoreCase(thisWord)) {
                    skip = true;
                    break;
                }
            }

            if (!skip) {
                for (int goodC = 0; goodC < splicer.goodCount; ++goodC) {
                    if (splicer.goodWords2[goodC].equalsIgnoreCase(thisWord)) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    splicer.goodWords2[splicer.goodCount] = thisWord;
                    ++splicer.goodCount;
                }
            }
        }

    }

    public void findMatch(String orig) {
        orig = " " + orig + " ";
        splicer.goodCount = 0;
        int countEs = 0;

        for (; countEs < splicer.meddraCount; ++countEs) {
            if (orig.contains(allWords3[countEs])) {
                splicer.allWords[splicer.goodCount] = new Word(allWords3[countEs].trim());
                ++splicer.goodCount;
            }
        }
        splicer.meddraCount = splicer.goodCount;
    }


}
