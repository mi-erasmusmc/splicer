package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;

import static nl.erasmusmc.mi.biosemantics.splicer.Database.getConnection;

public class AdeProcess {
    public static final String MEDRA_DB_WITHOUT_REVERSE = "medra_db_without_reverse";
    public static final String MEDRA_DB_WITHOUT_REVERSE_STEMS = "medra_db_without_reverse_stems";
    private static final Logger log = LogManager.getLogger();
    static CleanUp cle = new CleanUp();
    static int testNum = 0;

    public static void getMedraTerms(String b) {
        if (!b.equals("")) {
            String origB = b;
            String q = "'";
            b = b.toLowerCase();
            b = " " + b + " ";

            String tab = MEDRA_DB_WITHOUT_REVERSE;
            String MH;

            try (Statement stmt = getConnection().createStatement()) {
                String quer = "Select Medra2 from " + tab + " where (((InStr(" + q + b + q + ", Medra2))>0))";
                ResultSet rs = stmt.executeQuery(quer);

                while (rs.next()) {
                    MH = rs.getString("Medra2");
                    if (rs.wasNull()) {
                        MH = "";
                    }

                    MH = MH.toLowerCase();
                    if (F5.tableToMedra) {
                        F5.allWords[F5.medraCount] = "T1M1 " + MH;
                        ++F5.medraCount;
                    } else {
                        F5.allWords[F5.medraCount] = "M1 " + MH;
                        ++F5.medraCount;
                    }
                }

            } catch (Exception e) {
                log.error("Got an exception getMedraTerms in query");
                log.error(e.getMessage());
            }

            testNum = F5.medraCount;
            getMedraSyns(origB);
        }

    }

    public static void getMedraTermsStop(String b) {
        String q = "'";
        log.debug("Getting MedDRA Terms Stop");
        log.debug(b);
        b = b.trim();
        b = " " + b;

        String tab = MEDRA_DB_WITHOUT_REVERSE_STEMS;
        String MH;

        try (Statement stmt = getConnection().createStatement()) {
            String quer = "Select Medra2, Stops from " + tab + " where (((InStr(" + q + b + q + ", Stops))>0))";
            ResultSet rs = stmt.executeQuery(quer);

            while (rs.next()) {
                MH = rs.getString("Medra2");
                if (rs.wasNull()) {
                    MH = "";
                }


                MH = MH.toLowerCase();
                if (F5.tableToMedra) {
                    F5.allWords[F5.medraCount] = "T1M1 " + MH;
                    ++F5.medraCount;
                } else {
                    F5.allWords[F5.medraCount] = "M1 " + MH;
                    ++F5.medraCount;
                }
            }

        } catch (Exception var15) {
            log.error("Got an exception getMedraStop query");
            log.error(var15.getMessage());
        }
    }

    public static void getMedraSyns(String b) {
        String q = "'";
        b = b.toLowerCase();

        String tab = "medsyns";

        try (Statement stmt = getConnection().createStatement()) {
            String quer;
            String f2;


            quer = "Select MDRTerm from " + tab + " where (((InStr(" + q + b + q + ", Effect))>0))";
            ResultSet rs = stmt.executeQuery(quer);

            while (rs.next()) {
                f2 = rs.getString("MDRTerm");

                f2 = f2.toLowerCase();
                if (F5.tableToMedra) {
                    F5.allWords[F5.medraCount] = "T1M1 " + f2;
                    ++F5.medraCount;
                } else {
                    F5.allWords[F5.medraCount] = "M1 " + f2;
                    ++F5.medraCount;
                }
            }
        } catch (Exception var14) {
            log.error("Got an exception getMEdraSyns in query");
            log.error(var14.getMessage());
        }
    }

    public static void getMedraTermsIndications(String b) {
        String q = "'";
        b = b.toLowerCase();
        b = " " + b + " ";

        String tab = MEDRA_DB_WITHOUT_REVERSE;
        String MH;

        try (Statement stmt = getConnection().createStatement()) {
            String quer = "Select Medra2 from " + tab + " where (((InStr(" + q + b + q + ", Medra2))>0))";
            ResultSet rs = stmt.executeQuery(quer);

            while (rs.next()) {
                MH = rs.getString("Medra2");
                if (rs.wasNull()) {
                    MH = "";
                }

                MH = MH.toLowerCase();
                boolean indClean = cle.cleanIndication(MH);
                if (indClean) {
                    F5.allWords[F5.medraCount] = MH;
                    ++F5.medraCount;
                }
            }

        } catch (Exception var14) {
            log.error("Got an exception openDb6 in query");
            log.error(var14.getMessage());
        }

        testNum = F5.medraCount;
    }

    public static void getMedraTermsEscape2(String b) {
        String origB = b;
        String q = "'";
        b = b.toLowerCase();
        b = " " + b + " ";

        String tab = MEDRA_DB_WITHOUT_REVERSE_STEMS;
        String MH;

        try (Statement stmt = getConnection().createStatement()) {
            String ste;

            String quer = "Select Medra2, Stems from " + tab + " where (((InStr(" + q + b + q + ", Stems))>0))";
            ResultSet rs = stmt.executeQuery(quer);

            while (rs.next()) {
                MH = rs.getString("Medra2");
                if (rs.wasNull()) {
                    MH = "";
                }

                ste = rs.getString("Stems");
                if (rs.wasNull()) {
                    ste = "";
                }

                MH = MH.toLowerCase();
                if (b.contains(ste)) {
                    F5.allWords3[F5.medraCount] = MH;
                    ++F5.medraCount;
                }
            }

        } catch (Exception var15) {
            log.error("Got an exception openDb6 in query");
            log.error(var15.getMessage());
        }

        testNum = F5.medraCount;
        getMedraSynsEscape(origB);
    }

    public static String getMedraSynsEscape(String b) {
        String q = "'";
        b = b.toLowerCase();

        String tab = "medsyns";

        try (Statement stmt = getConnection().createStatement()) {
            String f2;

            String quer = "Select MDRTerm from " + tab + " where (((InStr(" + q + b + q + ", Effect))>0))";

            for (ResultSet rs = stmt.executeQuery(quer); rs.next(); ++F5.medraCount) {
                f2 = rs.getString("MDRTerm");
                f2 = f2.toLowerCase();
                F5.allWords3[F5.medraCount] = f2;
            }

        } catch (Exception var14) {
            log.error("Got an exception openDb6 in query");
            log.error(var14.getMessage());
        }

        return b;
    }

    public void getList(String b) {

        F5.tempArray4 = b.split("\\:");
        if (b.contains(" : ") && F5.tempArray4.length == 1) {
            b = b.substring(b.lastIndexOf(" : "));
        }

        if (b.contains(" and ")) {
            b = b.replace(", and ", ", ");
            b = b.replace(" and ", " , ");
        }

        F5.tempArray4 = b.split(",");
        String tempMedTerm;
        if (F5.origSent.contains("Italics")) {
            tempMedTerm = F5.origSent.substring(F5.origSent.indexOf("Italics"));
            tempMedTerm = tempMedTerm.substring(tempMedTerm.indexOf(">"), tempMedTerm.indexOf("<"));
            tempMedTerm = tempMedTerm.replace(">", "");
            tempMedTerm = F5.compress(tempMedTerm);
            tempMedTerm = tempMedTerm.trim();
            F5.tempArray2 = tempMedTerm.split(",");

            String nSpl;
            for (int tCount = 0; tCount < F5.tempArray2.length; ++tCount) {
                nSpl = F5.tempArray2[tCount];
                nSpl = " " + nSpl + " ";
                nSpl = nSpl.toLowerCase();
                nSpl = Normals.normal7(nSpl);
                F5.tempArray3 = nSpl.split(" ");
                if (F5.tempArray3.length <= 3 && nSpl.length() > 4) {
                    F5.allWords[F5.medraCount] = "L1 " + nSpl + "--i";
                    ++F5.medraCount;
                }
            }

            if (F5.origSent.contains("</content>")) {
                b = F5.origSent.substring(F5.origSent.lastIndexOf("</content>"));
            }
            nSpl = Normals.normalSpl(b);
            nSpl = Normals.normal2(nSpl);
            nSpl = nSpl.trim();
            if (nSpl.startsWith(",")) {
                nSpl = nSpl.replaceFirst(",", "");
                nSpl = nSpl.trim();
            }

            F5.currentSentence = nSpl;
            log.debug("2 ******** clean sentence: {}   {} ", F5.countSentences, nSpl);
            F5.tempArray = nSpl.split(",");
        }

        F5.count = 0;

        for (; F5.count < F5.tempArray4.length; ++F5.count) {
            tempMedTerm = F5.tempArray4[F5.count];
            tempMedTerm = tempMedTerm.trim();
            if (tempMedTerm.contains("(see -")) {
                tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf("(see -"));
            }

            if (tempMedTerm.contains("href")) {
                tempMedTerm = "";
            }

            tempMedTerm = tempMedTerm.toLowerCase();
            if (F5.count == 0) {
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
            F5.tempArray3 = tempMedTerm.split(" ");
            if (F5.tempArray3.length <= 4 && tempMedTerm.length() >= 3) {
                if (tempMedTerm.startsWith("and ") && F5.count == F5.tempArray4.length - 1) {
                    tempMedTerm = tempMedTerm.replaceFirst("and ", "");
                    tempMedTerm = tempMedTerm.trim();
                    if (tempMedTerm.contains(" were ")) {
                        tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" were "));
                    } else if (tempMedTerm.contains(" are ")) {
                        tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" are "));
                    } else if (tempMedTerm.contains(" is ")) {
                        tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" is "));
                    }

                    F5.allWords[F5.medraCount] = "L1 " + tempMedTerm;
                    ++F5.medraCount;
                } else {
                    F5.allWords[F5.medraCount] = "L1 " + tempMedTerm;
                    ++F5.medraCount;
                }
            } else if (F5.tempArray3.length > 4 && tempMedTerm.length() >= 3 && F5.count == F5.tempArray4.length - 1) {
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

                    F5.allWords[F5.medraCount] = "L1 " + tempMedTerm;
                    ++F5.medraCount;
                } else {
                    getMedraTerms(tempMedTerm);
                }
            } else {
                getMedraTerms(tempMedTerm);
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

        F5.tempArray4 = b.split(",");

        for (F5.count = 0; F5.count < F5.tempArray4.length; ++F5.count) {
            String tempMedTerm = F5.tempArray4[F5.count];
            tempMedTerm = tempMedTerm.toLowerCase();
            tempMedTerm = tempMedTerm.trim();
            if (tempMedTerm.contains("(see -")) {
                tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf("(see -"));
            }

            if (tempMedTerm.contains("href")) {
                tempMedTerm = "";
            }

            if (F5.count == 0) {
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
                F5.tempArray3 = tempMedTerm.split(" ");
                if (F5.tempArray3.length <= 4 && tempMedTerm.length() >= 3) {
                    F5.allWords[F5.medraCount] = "L2 " + tempMedTerm;
                    ++F5.medraCount;
                }
            } else if (tempMedTerm.length() > 3) {
                if (F5.tempArray3.length > 4) {
                    getMedraTerms(tempMedTerm);
                } else {
                    F5.allWords[F5.medraCount] = "L2 " + tempMedTerm;
                    ++F5.medraCount;
                }
            }
        }

    }

    public void getList3(String b) {
        F5.count = 0;
        if (b.contains(" and ")) {
            b = b.replace(", and ", ", ");
            b = b.replace(" and ", " , ");
        }

        if (b.contains(",")) {
            F5.tempArray4 = b.split(",");
        } else {
            F5.tempArray4 = b.split("\\)");

            for (int j = 0; j < F5.tempArray4.length; ++j) {
                F5.tempArray4[j] = F5.tempArray4[j] + ")";
            }
        }

        for (; F5.count < F5.tempArray4.length; ++F5.count) {
            String tempMedTerm = F5.tempArray4[F5.count];
            tempMedTerm = tempMedTerm.toLowerCase();
            tempMedTerm = tempMedTerm.trim();
            if (tempMedTerm.contains("(see -")) {
                tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf("(see -"));
            }

            if (tempMedTerm.contains("href")) {
                tempMedTerm = "";
            }

            if (F5.count == 0) {
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
                F5.tempArray3 = tempMedTerm.split(" ");
                if (F5.tempArray3.length <= 3 && tempMedTerm.length() >= 3) {
                    F5.allWords[F5.medraCount] = "L3 " + tempMedTerm;
                    ++F5.medraCount;
                } else {
                    getMedraTerms(tempMedTerm);
                }
            } else {
                getMedraTerms(tempMedTerm);
            }
        }

    }

    public void getT1L1_2(String b) {

        F5.tempArray4 = b.split("\\:");
        if (b.contains(" : ") && F5.tempArray4.length == 1) {
            b = b.substring(b.lastIndexOf(" : "));
        }

        if (b.contains(" and ")) {
            b = b.replace(", and ", ", ");
            b = b.replace(" and ", " , ");
        }

        F5.tempArray4 = b.split(",");

        for (F5.count = 0; F5.count < F5.tempArray4.length; ++F5.count) {
            String tempMedTerm = F5.tempArray4[F5.count];

            if (!tempMedTerm.contains("bbbb")) {
                tempMedTerm = tempMedTerm.trim();
                if (tempMedTerm.contains("(see -")) {
                    tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf("(see -"));
                }

                if (tempMedTerm.contains("href")) {
                    tempMedTerm = "";
                }

                tempMedTerm = tempMedTerm.toLowerCase();
                if (F5.count == 0) {
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
                F5.tempArray3 = tempMedTerm.split(" ");
                if (F5.tempArray3.length <= 7 && tempMedTerm.length() >= 3) {
                    if (tempMedTerm.startsWith("and ") && F5.count == F5.tempArray4.length - 1) {
                        tempMedTerm = tempMedTerm.replaceFirst("and ", "");
                        tempMedTerm = tempMedTerm.trim();
                        if (tempMedTerm.contains(" were ")) {
                            tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" were "));
                        } else if (tempMedTerm.contains(" are ")) {
                            tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" are "));
                        } else if (tempMedTerm.contains(" is ")) {
                            tempMedTerm = tempMedTerm.substring(0, tempMedTerm.indexOf(" is "));
                        }

                        F5.allWords[F5.medraCount] = "T1L1 " + tempMedTerm;
                        ++F5.medraCount;
                    } else {
                        F5.allWords[F5.medraCount] = "T1L1 " + tempMedTerm;
                        ++F5.medraCount;
                    }
                } else if (F5.tempArray3.length > 7 && tempMedTerm.length() >= 3) {
                    getMedraTerms(tempMedTerm);
                }
            }
        }

    }

    public void getUniqueLCS() {
        F5.goodCount = 0;
        F5.count = 0;
        String thisWord;

        for (boolean skip; F5.count < F5.medraCount; ++F5.count) {
            F5.sm = "";
            F5.se = "";
            if (F5.allWords[F5.count].startsWith("M1 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("M1 ", "");
                F5.sm = "M1";
            } else if (F5.allWords[F5.count].startsWith("L1 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("L1 ", "");
                F5.sm = "L1";
            } else if (F5.allWords[F5.count].startsWith("L2 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("L2 ", "");
                F5.sm = "L2";
            } else if (F5.allWords[F5.count].startsWith("L3 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("L3 ", "");
                F5.sm = "L3";
            } else if (F5.allWords[F5.count].startsWith("T1M1 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("T1M1 ", "");
                F5.sm = "T1M1";
            } else if (F5.allWords[F5.count].startsWith("T1L1 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("T1L1 ", "");
                F5.sm = "T1L1";
            }

            if (F5.currentSection.equalsIgnoreCase("Adverse Reactions")) {
                F5.se = "Adverse Reactions";
            } else if (F5.currentSection.equalsIgnoreCase("over")) {
                F5.se = "ove";
            } else if (F5.currentSection.equalsIgnoreCase("Precautions (beta)")) {
                F5.se = "Precautions (beta)";
            } else if (F5.currentSection.equalsIgnoreCase("Warnings (beta)")) {
                F5.se = "Warnings (beta)";
            } else if (F5.currentSection.equalsIgnoreCase("Black Box (beta)")) {
                F5.se = "Black Box (beta)";
            } else {
                F5.se = F5.currentSection;
            }

            thisWord = F5.allWords[F5.count];
            thisWord = thisWord.trim();
            skip = false;

            for (int thisCount = 0; thisCount < F5.medraCount; ++thisCount) {
                String thisWord2 = F5.allWords[thisCount].trim();
                if (thisWord2.contains(thisWord) && !thisWord2.equalsIgnoreCase(thisWord)) {
                    skip = true;
                    break;
                }
            }

            if (!skip) {
                for (int goodC = 0; goodC < F5.goodCount; ++goodC) {
                    if (F5.goodWords2[goodC].equalsIgnoreCase(thisWord)) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    F5.goodWords2[F5.goodCount] = thisWord;
                    F5.sMethod[F5.goodCount] = F5.sm;
                    F5.sSection[F5.goodCount] = F5.se;
                    ++F5.goodCount;
                }
            }
        }

        for (F5.count = 0; F5.count < F5.goodCount - 1; ++F5.count) {
            log.debug("3 {}", F5.goodWords2[F5.count]);
        }

        if (F5.goodCount == 0) {
            log.debug("4 No good words found");
        }

        if (F5.goodCount > 0 && !F5.tableToMedra) {
            Numbers.findFrequencies();
            log.debug("good words below");

            for (F5.count = 0; F5.count < F5.goodCount; ++F5.count) {
                log.debug("count: {}, source: {}, good words: {}", F5.count, F5.sMethod[F5.count], F5.goodWords2[F5.count]);
            }
        } else if (F5.goodCount > 0) {
            PostProcess.putIntoFinals("");
        }

    }

    public void getUniqueLCS2() {
        F5.goodCount = 0;
        F5.count = 0;
        String thisWord = "";
        if (F5.medraCount > 0) {
            for (boolean skip; F5.count < F5.medraCount; ++F5.count) {
                F5.sm = "";
                thisWord = F5.allWords[F5.count];
                thisWord = thisWord.trim();
                skip = false;

                for (int thisCount = 0; thisCount < F5.medraCount; ++thisCount) {
                    String thisWord2 = F5.allWords[thisCount].trim();
                    if (thisWord2.contains(thisWord) && !thisWord2.equalsIgnoreCase(thisWord)) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    for (int goodC = 0; goodC < F5.goodCount; ++goodC) {
                        if (F5.goodWords2[goodC].equalsIgnoreCase(thisWord)) {
                            skip = true;
                            break;
                        }
                    }

                    if (!skip) {
                        F5.goodWords2[F5.goodCount] = thisWord;
                        F5.sMethod[F5.goodCount] = F5.sm;
                        ++F5.goodCount;
                    }
                }
            }

            for (F5.count = 0; F5.count < F5.medraCount; ++F5.count) {
                log.debug("Indications:  " + F5.allWords[F5.count]);
            }

            if (F5.goodCount == 0) {
                log.debug("No indications found");
            }

            if (F5.goodCount > 0) {
                log.debug("indications below");
                F5.count = 0;

                for (F5.indCount = 0; F5.count < F5.goodCount; ++F5.count) {
                    log.debug(F5.count + "source   " + F5.sMethod[F5.count] + " indication:   " + F5.goodWords2[F5.count]);
                    F5.indicationArray[F5.indCount] = F5.goodWords2[F5.count];
                    ++F5.indCount;
                }

                F5.maxIndCount = F5.indCount - 1;
            } else if (F5.goodCount == 0) {
                F5.maxIndCount = -1;
            }
        } else {
            F5.maxIndCount = -1;
        }

    }

    public void getUniqueLCS3() {
        F5.goodCount = 0;
        F5.count = 0;
        String thisWord;

        for (boolean skip; F5.count < F5.medraCount; ++F5.count) {
            F5.sm = "";
            F5.se = "";
            if (F5.allWords[F5.count].startsWith("M1 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("M1 ", "");
                F5.sm = "M1";
            } else if (F5.allWords[F5.count].startsWith("L1 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("L1 ", "");
                F5.sm = "L1";
            } else if (F5.allWords[F5.count].startsWith("L2 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("L2 ", "");
                F5.sm = "L2";
            } else if (F5.allWords[F5.count].startsWith("L3 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("L3 ", "");
                F5.sm = "L3";
            } else if (F5.allWords[F5.count].startsWith("T1M1 ")) {
                F5.allWords[F5.count] = F5.allWords[F5.count].replaceFirst("T1M1 ", "");
                F5.sm = "T1M1";
            }

            if (F5.currentSection.equalsIgnoreCase("Adverse Reactions")) {
                F5.se = "Adverse Reactions";
            } else if (F5.currentSection.equalsIgnoreCase("over")) {
                F5.se = "ove";
            } else if (F5.currentSection.equalsIgnoreCase("Precautions (beta)")) {
                F5.se = "Precautions (beta)";
            } else if (F5.currentSection.equalsIgnoreCase("Warnings (beta)")) {
                F5.se = "Warnings (beta)";
            } else if (F5.currentSection.equalsIgnoreCase("Black Box (beta)")) {
                F5.se = "Black Box (beta)";
            } else {
                F5.se = F5.currentSection;
            }

            thisWord = F5.allWords[F5.count];
            thisWord = thisWord.trim();
            skip = false;

            for (int thisCount = 0; thisCount < F5.medraCount; ++thisCount) {
                String thisWord2 = F5.allWords[thisCount].trim();
                if (thisWord2.contains(thisWord) && !thisWord2.equalsIgnoreCase(thisWord)) {
                    skip = true;
                    break;
                }
            }

            if (!skip) {
                for (int goodC = 0; goodC < F5.goodCount; ++goodC) {
                    if (F5.goodWords2[goodC].equalsIgnoreCase(thisWord)) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    F5.goodWords2[F5.goodCount] = thisWord;
                    F5.sMethod[F5.goodCount] = F5.sm;
                    F5.sSection[F5.goodCount] = F5.se;
                    ++F5.goodCount;
                }
            }
        }

        F5.count = 0;
        if (F5.goodCount == 0) {
            log.debug("No good words found");
        }

        if (F5.goodCount > 0 && !F5.tableToMedra) {
            for (F5.count = 0; F5.count < F5.goodCount; ++F5.count) {
                log.debug(F5.sSection[F5.count] + "........" + F5.goodWords2[F5.count]);
                PostProcess.placeIntoFinal(F5.goodWords2[F5.count], F5.sMethod[F5.count], F5.sSection[F5.count], F5.countSentences + "", "", "0", "0", "0");
            }
        }

    }

    public void getUniqueLCSEscape() {
        F5.goodCount = 0;
        String thisWord;
        int countEs = 0;

        for (boolean skip; countEs < F5.medraCount; ++countEs) {
            thisWord = F5.allWords[countEs];
            thisWord = thisWord.trim();
            skip = false;

            for (int thisCount = 0; thisCount < F5.medraCount; ++thisCount) {
                String thisWord2 = F5.allWords[thisCount].trim();
                if (thisWord2.contains(thisWord) && !thisWord2.equalsIgnoreCase(thisWord)) {
                    skip = true;
                    break;
                }
            }

            if (!skip) {
                for (int goodC = 0; goodC < F5.goodCount; ++goodC) {
                    if (F5.goodWords2[goodC].equalsIgnoreCase(thisWord)) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    F5.goodWords2[F5.goodCount] = thisWord;
                    ++F5.goodCount;
                }
            }
        }

    }

    public void findMatch(String orig) {
        orig = " " + orig + " ";
        F5.goodCount = 0;
        int countEs = 0;

        for (; countEs < F5.medraCount; ++countEs) {
            if (orig.contains(F5.allWords3[countEs])) {
                F5.allWords[F5.goodCount] = F5.allWords3[countEs].trim();
                ++F5.goodCount;
            }
        }
        F5.medraCount = F5.goodCount;
    }

}
