package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Statement;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.erasmusmc.mi.biosemantics.splicer.Database.getConnection;

public class Flow {
    public static final String SPLICER_JANSSEN = "SPLICER_JANSSEN";
    private static final Logger log = LogManager.getLogger();
    static Tables tab = new Tables();
    static NonAde nde = new NonAde();
    static AdeProcess ade = new AdeProcess();
    static CleanUp cle = new CleanUp();
    static Section sec = new Section();

    public static void sendToDatabase() {
        String q = "'";

        try (Statement stmt = getConnection().createStatement()) {
            String targetTable;
            String quer1;
            String quer2;
            String allQuer;

            targetTable = SPLICER_JANSSEN;
            if (!Objects.equals(F5.matchOutcome, "medraMatch")) {
                log.debug("I am NOT a MATCH");
                return;
            }

            quer1 = "INSERT INTO " + targetTable + " (SPL_ID, SET_ID, TRADE_NAME, SPL_DATE, SPL_SECTION, CONDITION_SOURCE_VALUE, CONDITION_LLT, parseMethod, sentenceNum, labdirection, drugfreq) VALUES(";
            quer2 = q + F5.SPLId + q + "," + q + F5.setId + q + "," + q + F5.tradeDrugName.trim() + q + "," + q + F5.SPLDate + q + "," + q + F5.finalsSection[F5.count] + q + "," + q + F5.origSPLTerm + q + "," + q + F5.finalMedraTerms[F5.count] + q + "," + q + F5.finalsMethod[F5.count] + q + "," + q + F5.sentNumArray[F5.count] + q + "," + q + F5.direction + q + "," + q + F5.finalFreq[F5.count] + q + ")";
            allQuer = quer1 + quer2;
            log.debug("Inserting into table {}, {} - {}", targetTable, F5.tradeDrugName, F5.finalMedraTerms[F5.count]);
            log.debug("DB insert query:  {}", allQuer);
            String sect = F5.finalsSection[F5.count];
            if (Objects.equals(sect, "Precautions (beta)") || Objects.equals(sect, "Adverse Reactions") || Objects.equals(sect, "Post Marketing") || Objects.equals(sect, "Warnings (beta)") || Objects.equals(sect, "Black Box (beta)")) {
                stmt.executeUpdate(allQuer);
            }

        } catch (Exception var11) {
            log.error("Got an exception inserting into db");
            log.error(var11.getMessage());
        }

    }

    public static void deleteFromDatabase(String b) {
        String q = "'";

        try (Statement stmt = getConnection().createStatement()) {
            String quer;

            quer = "DELETE FROM " + SPLICER_JANSSEN + " where SPL_ID = " + q + b + q;
            int c = stmt.executeUpdate(quer);
            log.debug("Delete from DB: {} rows deleted with query: {} ", c, quer);
        } catch (Exception e) {
            log.error("Got an exception in when deleting from db");
            log.error(e.getMessage());
        }

    }

    public static boolean preMappingFilter(String f) {
        String anyLetter = "([a-z]|[A-Z])";
        Pattern p = Pattern.compile(anyLetter);
        Matcher m = p.matcher(f);
        boolean mat2 = m.find();
        if (!mat2) {
            return false;
        } else {
            return f.length() > 2;
        }
    }

    public void gatherData() {
        nde.getGenName(F5.allDrugInfo);
        nde.getTradeName(F5.allDrugInfo2);
        nde.getActiveMoiety(F5.allActiveMoiety);
    }

    public void tableProcessing() {
        String theTable = F5.sent[F5.countSentences].substring(0, F5.sent[F5.countSentences].indexOf("</table>"));
        ++F5.tablesFound;
        tab.processTables(theTable);
        String postTable = F5.sent[F5.countSentences].substring(F5.sent[F5.countSentences].indexOf("</table>"));
        F5.goodCount = 0;
        F5.medraCount = 0;
        if (postTable.length() > 4) {
            postTable = postTable.replace("</table>", "");
            F5.sent[F5.countSentences] = postTable;
        }

    }

    public void tableProcessingAlternative() {
        F5.allOfTable = F5.allOfTable + F5.sent[F5.countSentences];
        if (F5.sent[F5.countSentences].indexOf("</tbody>") > -1) {
            String theTable = F5.allOfTable;
            ++F5.tablesFound;
            tab.processTables(theTable);
            F5.gettingTable = false;
            F5.allOfTable = "";
        }

    }

    public void processSentenceWithColon() {
        String sentPart2;
        F5.tempArray4 = F5.sentForList.split("\\:");
        if (F5.tempArray4.length == 1) {
            sentPart2 = F5.sentForList.substring(F5.sentForList.indexOf(":"));
            sentPart2 = sentPart2.replaceAll("\\:", " ");
        } else {
            sentPart2 = F5.sentForList;
        }

        if (!F5.nSpl.contains("%")) {
            ade.getList(sentPart2);
            F5.isList = true;
        } else if (F5.nSpl.contains("%") && F5.nSpl.contains("(") && F5.nSpl.contains(")") && F5.nSpl.indexOf("(") < F5.nSpl.indexOf("%") && F5.nSpl.indexOf("%") < F5.nSpl.indexOf(")") && F5.tempArray.length > 4) {
            ade.getList2(F5.sentForList);
            F5.isList = true;
        } else if (F5.nSpl.contains("%") && F5.nSpl.contains("(") && F5.nSpl.contains(")") && F5.tempArray.length <= 4) {
            ade.getList3(F5.sentForList);
            F5.isList = true;
        } else {
            ade.getList(sentPart2);
            F5.isList = true;
        }

    }

    public void processSentenceWithHighCommaRatio() {
        log.debug("                                  Likely a list comma ratio : " + F5.commaRatio + "   " + F5.nSpl);
        if (!F5.nSpl.contains("%") && F5.tempArray.length > 2) {
            ade.getList(F5.sentForList);
            F5.isList = true;
        } else if (!F5.nSpl.contains("%") && F5.tempArray.length > 2 && F5.sentForList.contains(":")) {
            ade.getList(F5.sentForList);
            F5.isList = true;
        } else if (F5.nSpl.contains("%") && F5.nSpl.contains("(") && F5.nSpl.contains(")") && F5.nSpl.indexOf("(") < F5.nSpl.indexOf("%") && F5.nSpl.indexOf("%") < F5.nSpl.indexOf(")") && F5.tempArray.length > 4) {
            ade.getList2(F5.sentForList);
            F5.isList = true;
        } else if (F5.nSpl.contains("%") && F5.nSpl.contains("(") && F5.nSpl.contains(")") && F5.tempArray.length <= 4) {
            ade.getList3(F5.sentForList);
            F5.isList = true;
        } else {
            AdeProcess.getMedraTerms(F5.nSpl);
        }

    }

    public void postAdeProcessing() {
        F5.tableOn = false;
        F5.allPregInfo = F5.allPregInfo.replaceAll("Â ", " ");
        nde.getPregCat(F5.allPregInfo);
        F5.pregCat = Normals.normalBadTable(F5.pregCat);
        if (F5.pregCat.contains(" ")) {
            F5.pregCat = F5.pregCat.substring(0, F5.pregCat.indexOf(" "));
        }

        sec.processSections();
        F5.maxMedraCount = F5.finalMedraCount - 1;
        F5.count = 0;
        log.debug(F5.count + "FFFFFFFFFFFFFFFFFFFFFF  finalMedra Terms Below:   ");
    }

    public void assignMethod() {
        if (F5.finalMedraTerms[F5.count].startsWith("M ")) {
            F5.finalMedraTerms[F5.count] = F5.finalMedraTerms[F5.count].replaceFirst("M ", "");
            F5.sm = "M";
        } else if (F5.finalMedraTerms[F5.count].startsWith("L1 ")) {
            F5.finalMedraTerms[F5.count] = F5.finalMedraTerms[F5.count].replaceFirst("L1 ", "");
            F5.sm = "L1";
        } else if (F5.finalMedraTerms[F5.count].startsWith("L2 ")) {
            F5.finalMedraTerms[F5.count] = F5.finalMedraTerms[F5.count].replaceFirst("L2 ", "");
            F5.sm = "L2";
        } else if (F5.finalMedraTerms[F5.count].startsWith("L3 ")) {
            F5.finalMedraTerms[F5.count] = F5.finalMedraTerms[F5.count].replaceFirst("L3 ", "");
            F5.sm = "L3";
        } else if (F5.finalMedraTerms[F5.count].startsWith("T1M1 ")) {
            F5.finalMedraTerms[F5.count] = F5.finalMedraTerms[F5.count].replaceFirst("T1M1 ", "");
            F5.sm = "T1M1";
        } else if (F5.finalMedraTerms[F5.count].startsWith("T1L1 ")) {
            F5.finalMedraTerms[F5.count] = F5.finalMedraTerms[F5.count].replaceFirst("T1L1 ", "");
            F5.sm = "T1L1";
        }

    }

    public String cleanUpTerms2(String t) {
        StringBuilder all = new StringBuilder();
        char[] ch = t.toCharArray();

        for (char c : ch) {
            String anyLetter = "([a-z]|[A-Z]|\\s)";
            Pattern p = Pattern.compile(anyLetter);
            Matcher m = p.matcher(c + "");
            boolean mat2 = m.find();
            if (mat2) {
                all.append(c);
            } else {
                all.append(" ");
            }
        }

        all = new StringBuilder(F5.compress(all.toString()));
        all = new StringBuilder(all.toString().trim());
        return all.toString();
    }

    public void setTransformFlag() {
        F5.matchedMedraTerm = F5.matchedMedraTerm.trim();
        if (F5.matchedMedraTerm.equalsIgnoreCase(F5.finalMedraTerms[F5.count])) {
            F5.transformFlag = F5.finalMedraTerms[F5.count];
            F5.termForEscape = F5.finalMedraTerms[F5.count];
        } else if (!F5.matchedMedraTerm.equalsIgnoreCase(F5.finalMedraTerms[F5.count])) {
            F5.transformFlag = F5.origTerm;
            F5.matchedMedraTerm = F5.matchedMedraTerm.trim();
            F5.termForEscape = F5.matchedMedraTerm;
            F5.finalMedraTerms[F5.count] = F5.matchedMedraTerm;
        }

    }

    public void attemptEscapeMatch() {
        F5.origTerm2 = F5.finalMedraTerms[F5.count];
        F5.tempArray = F5.termForEscape.split(" ");
        StringBuilder allWords2 = new StringBuilder();

        for (int j = 0; j < F5.tempArray.length; ++j) {
            boolean isStopWord = F5.stopSet.contains(F5.tempArray[j]);
            if (!isStopWord) {
                String stemmedWord = F5.portStem(F5.tempArray[j]);
                if (stemmedWord.equalsIgnoreCase("Invalid term")) {
                    stemmedWord = F5.tempArray[j];
                }

                allWords2.append(" ").append(stemmedWord);
            }
        }

        F5.termForEscape = allWords2.toString();
        F5.termForEscape = F5.termForEscape.trim();
        F5.transformedEscape = cle.cleanTermEscape(F5.termForEscape);
    }

    public void labCheck() {
        int sentNumForTest;
        String sentForTest;
        if (Integer.parseInt(F5.sentNumArray[F5.count]) > -1) {
            sentNumForTest = Integer.parseInt(F5.sentNumArray[F5.count]);
            sentForTest = F5.sent[sentNumForTest];
            if (sentForTest == null) {
                sentForTest = "";
            }
        } else {
            sentForTest = " ";
        }

        if (F5.origMethod.contains("T")) {
            sentForTest = F5.origTerm;
        }

        F5.direction = CleanUp.getLabDirection(F5.finalMedraTerms[F5.count], sentForTest, F5.finalsSection[F5.count], F5.transformFlag, F5.finalsMethod[F5.count]);
        F5.labTransformOccurred = false;
        if (F5.direction.equals("increased") || F5.direction.equals("decreased") || F5.direction.equals("abnormal")) {
            F5.labTransformOccurred = CleanUp.transformLab2Medra(F5.finalMedraTerms[F5.count], F5.direction);
        }

    }

    public void regularOutput() {
        F5.escapeMatchOccurred = true;
        sendToDatabase();
    }

    public void finalFilter() {
        F5.transformFlag = Normals.clip(F5.transformFlag);
        F5.finalMedraTerms[F5.count] = Normals.clip(F5.finalMedraTerms[F5.count]);
        F5.passedFinalFilter = CleanUp.finalFilter();
        Normals.normalizeFilter(F5.finalMedraTerms[F5.count]);
        F5.transformFlag = Normals.normalizeFilter(F5.transformFlag);
        F5.finalMedraTerms[F5.count] = cle.finalTransform(F5.finalMedraTerms[F5.count]);
    }

}
