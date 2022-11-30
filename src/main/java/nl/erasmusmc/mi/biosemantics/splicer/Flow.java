package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.erasmusmc.mi.biosemantics.splicer.Database.TABLE_SPLICER_JANSSEN;
import static nl.erasmusmc.mi.biosemantics.splicer.Database.getConnection;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.L1;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.L2;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.L3;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.M;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.T1L1;
import static nl.erasmusmc.mi.biosemantics.splicer.Method.T1M1;

public class Flow {
    private static final Logger log = LogManager.getLogger();
    private final Tables tab;
    private final Splicer splicer;
    private StringBuilder allUpdateStatements;
    private int updateStatementsCount = 0;

    public Flow(Splicer splicer) {
        this.splicer = splicer;
        this.tab = new Tables(splicer);
        this.allUpdateStatements = new StringBuilder();
    }

    public void sendToDatabase() {
        String sect = splicer.finalsSection[splicer.count];

        if (Objects.equals(sect, "Precautions (beta)") || Objects.equals(sect, "Adverse Reactions")
                || Objects.equals(sect, "Post Marketing") || Objects.equals(sect, "Warnings (beta)")
                || Objects.equals(sect, "Black Box (beta)")) {


            String quer1;
            String quer2;
            String allQuer;

            if (Boolean.FALSE.equals(splicer.matchedOutcome)) {
                log.debug("{} does not match a MedDRA term", splicer.origSPLTerm);
                return;
            }

            quer1 = "INSERT INTO " + TABLE_SPLICER_JANSSEN + " (SPL_ID, SET_ID, TRADE_NAME, SPL_DATE, SPL_SECTION, CONDITION_SOURCE_VALUE, CONDITION_LLT, parseMethod, sentenceNum, labdirection, drugfreq) VALUES(";
            quer2 = qtd(splicer.splId) + "," + qtd(splicer.setId) + "," + qtd(splicer.tradeDrugName.trim()) + "," +
                    qtd(splicer.splDate) + "," + qtd(splicer.finalsSection[splicer.count]) + "," +
                    qtd(splicer.origSPLTerm) + "," + qtd(splicer.finalMeddraTerms[splicer.count]) + "," +
                    qtd(splicer.finalsMethod[splicer.count].toString()) + "," + qtd(splicer.sentNumArray[splicer.count]) + "," +
                    qtd(splicer.direction) + "," + qtd(splicer.finalFreq[splicer.count]) + "); ";
            allQuer = quer1 + quer2;
            log.debug("Inserting into table {}, {} - {}", TABLE_SPLICER_JANSSEN, splicer.tradeDrugName, splicer.finalMeddraTerms[splicer.count]);
            allUpdateStatements.append(allQuer);
            updateStatementsCount++;
            if (updateStatementsCount > 50) {
                commitToDB();
            }
        }

    }

    public void deleteFromDatabase(String b) {
        var q = "DELETE FROM " + TABLE_SPLICER_JANSSEN + " WHERE SPL_ID = ?";
        try (var stmt = getConnection().prepareStatement(q)) {
            stmt.setString(1, b);
            int c = stmt.executeUpdate();
            log.debug("Delete from DB: {} rows deleted with query: {} ", c, q);
        } catch (Exception e) {
            log.error("Got an exception in when deleting from db");
            log.error(e.getMessage());
        }

    }

    public boolean preMappingFilter(String f) {
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

    public void commitToDB() {
        String query = allUpdateStatements.toString();
        if (!query.isBlank()) {
            try (Statement stmt = getConnection().createStatement()) {
                log.info("Committing matches to database");
                stmt.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                allUpdateStatements = new StringBuilder();
                updateStatementsCount = 0;
            }
        } else {
            log.info("Nothing to commit");
        }
    }

    public void gatherData() {
        splicer.nde.getGenName(splicer.allDrugInfo);
        splicer.nde.getTradeName(splicer.allDrugInfo2);
        splicer.nde.getActiveMoiety(splicer.allActiveMoiety);
    }

    public void tableProcessing() {
        String theTable = splicer.sent[splicer.countSentences].substring(0, splicer.sent[splicer.countSentences].indexOf("</table>"));
        tab.processTables(theTable);
        String postTable = splicer.sent[splicer.countSentences].substring(splicer.sent[splicer.countSentences].indexOf("</table>"));
        splicer.goodCount = 0;
        splicer.meddraCount = 0;
        if (postTable.length() > 4) {
            postTable = postTable.replace("</table>", "");
            splicer.sent[splicer.countSentences] = postTable;
        }

    }

    public void tableProcessingAlternative() {
        splicer.allOfTable = splicer.allOfTable + splicer.sent[splicer.countSentences];
        if (splicer.sent[splicer.countSentences].contains("</tbody>")) {
            String theTable = splicer.allOfTable;
            tab.processTables(theTable);
            splicer.gettingTable = false;
            splicer.allOfTable = "";
        }

    }

    public void processSentenceWithColon() {
        String sentPart2;
        splicer.tempArray4 = splicer.sentForList.split("\\:");
        if (splicer.tempArray4.length == 1) {
            sentPart2 = splicer.sentForList.substring(splicer.sentForList.indexOf(":"));
            sentPart2 = sentPart2.replaceAll("\\:", " ");
        } else {
            sentPart2 = splicer.sentForList;
        }


        if (splicer.nSpl.contains("%") && splicer.nSpl.contains("(") && splicer.nSpl.contains(")") && splicer.nSpl.indexOf("(") < splicer.nSpl.indexOf("%")
                && splicer.nSpl.indexOf("%") < splicer.nSpl.indexOf(")") && splicer.tempArray.length > 4) {
            splicer.adeProcess.getList2(splicer.sentForList);
            splicer.isList = true;
        } else if (splicer.nSpl.contains("%") && splicer.nSpl.contains("(") && splicer.nSpl.contains(")") && splicer.tempArray.length <= 4) {
            splicer.adeProcess.getList3(splicer.sentForList);
            splicer.isList = true;
        } else {
            splicer.adeProcess.getList(sentPart2);
            splicer.isList = true;
        }

    }

    public void processSentenceWithHighCommaRatio() {
        if (!splicer.nSpl.contains("%") && splicer.tempArray.length > 2) {
            splicer.adeProcess.getList(splicer.sentForList);
            splicer.isList = true;
        } else if (splicer.nSpl.contains("%") && splicer.nSpl.contains("(") && splicer.nSpl.contains(")") && splicer.nSpl.indexOf("(") < splicer.nSpl.indexOf("%") && splicer.nSpl.indexOf("%") < splicer.nSpl.indexOf(")") && splicer.tempArray.length > 4) {
            splicer.adeProcess.getList2(splicer.sentForList);
            splicer.isList = true;
        } else if (splicer.nSpl.contains("%") && splicer.nSpl.contains("(") && splicer.nSpl.contains(")") && splicer.tempArray.length <= 4) {
            splicer.adeProcess.getList3(splicer.sentForList);
            splicer.isList = true;
        } else {
            splicer.adeProcess.getMeddraTerms(splicer.nSpl);
        }

    }

    public void assignMethod() {
        if (splicer.finalMeddraTerms[splicer.count].startsWith("M ")) {
            splicer.finalMeddraTerms[splicer.count] = splicer.finalMeddraTerms[splicer.count].replaceFirst("M ", "");
            splicer.sm = M;
        } else if (splicer.finalMeddraTerms[splicer.count].startsWith("L1 ")) {
            splicer.finalMeddraTerms[splicer.count] = splicer.finalMeddraTerms[splicer.count].replaceFirst("L1 ", "");
            splicer.sm = L1;
        } else if (splicer.finalMeddraTerms[splicer.count].startsWith("L2 ")) {
            splicer.finalMeddraTerms[splicer.count] = splicer.finalMeddraTerms[splicer.count].replaceFirst("L2 ", "");
            splicer.sm = L2;
        } else if (splicer.finalMeddraTerms[splicer.count].startsWith("L3 ")) {
            splicer.finalMeddraTerms[splicer.count] = splicer.finalMeddraTerms[splicer.count].replaceFirst("L3 ", "");
            splicer.sm = L3;
        } else if (splicer.finalMeddraTerms[splicer.count].startsWith("T1M1 ")) {
            splicer.finalMeddraTerms[splicer.count] = splicer.finalMeddraTerms[splicer.count].replaceFirst("T1M1 ", "");
            splicer.sm = T1M1;
        } else if (splicer.finalMeddraTerms[splicer.count].startsWith("T1L1 ")) {
            splicer.finalMeddraTerms[splicer.count] = splicer.finalMeddraTerms[splicer.count].replaceFirst("T1L1 ", "");
            splicer.sm = T1L1;
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

        all = new StringBuilder(Normals.compress(all.toString()));
        all = new StringBuilder(all.toString().trim());
        return all.toString();
    }

    public void setTransformFlag() {
        splicer.matchedMeddraTerm = splicer.matchedMeddraTerm.trim();
        if (splicer.matchedMeddraTerm.equalsIgnoreCase(splicer.finalMeddraTerms[splicer.count])) {
            splicer.transformFlag = splicer.finalMeddraTerms[splicer.count];
            splicer.termForEscape = splicer.finalMeddraTerms[splicer.count];
        } else {
            splicer.transformFlag = splicer.origTerm;
            splicer.matchedMeddraTerm = splicer.matchedMeddraTerm.trim();
            splicer.termForEscape = splicer.matchedMeddraTerm;
            splicer.finalMeddraTerms[splicer.count] = splicer.matchedMeddraTerm;
        }
    }

    public void attemptEscapeMatch() {
        splicer.origTerm2 = splicer.finalMeddraTerms[splicer.count];
        splicer.tempArray = splicer.termForEscape.split(" ");
        StringBuilder allWords2 = new StringBuilder();

        for (int j = 0; j < splicer.tempArray.length; ++j) {
            boolean isStopWord = Reference.stopSet().contains(splicer.tempArray[j]);
            if (!isStopWord) {
                String stemmedWord = PorterStemmer.portStem(splicer.tempArray[j]);
                if (stemmedWord.equalsIgnoreCase("Invalid term")) {
                    stemmedWord = splicer.tempArray[j];
                }

                allWords2.append(" ").append(stemmedWord);
            }
        }

        splicer.termForEscape = allWords2.toString();
        splicer.termForEscape = splicer.termForEscape.trim();
        splicer.cleanUp.cleanTermEscape(splicer.termForEscape);
    }

    public void labCheck() {
        int sentNumForTest;
        String sentForTest;
        if (Integer.parseInt(splicer.sentNumArray[splicer.count]) > -1) {
            sentNumForTest = Integer.parseInt(splicer.sentNumArray[splicer.count]);
            sentForTest = splicer.sent[sentNumForTest];
            if (sentForTest == null) {
                sentForTest = "";
            }
        } else {
            sentForTest = " ";
        }

        if (splicer.origMethod.toString().contains("T")) {
            sentForTest = splicer.origTerm;
        }

        splicer.direction = splicer.cleanUp.getLabDirection(splicer.finalMeddraTerms[splicer.count], sentForTest, splicer.finalsSection[splicer.count], splicer.transformFlag, splicer.finalsMethod[splicer.count]);
        splicer.labTransformOccurred = false;
        if (splicer.direction.equals("increased") || splicer.direction.equals("decreased") || splicer.direction.equals("abnormal")) {
            splicer.labTransformOccurred = splicer.cleanUp.transformLab2Medra(splicer.finalMeddraTerms[splicer.count], splicer.direction);
        }

    }

    public void regularOutput() {
        splicer.escapeMatchOccurred = true;
        sendToDatabase();
    }

    public void finalFilter() {
        splicer.transformFlag = Normals.clip(splicer.transformFlag);
        splicer.finalMeddraTerms[splicer.count] = Normals.clip(splicer.finalMeddraTerms[splicer.count]);
        splicer.passedFinalFilter = splicer.cleanUp.finalFilter();
        Normals.normalizeFilter(splicer.finalMeddraTerms[splicer.count]);
        splicer.transformFlag = Normals.normalizeFilter(splicer.transformFlag);
        splicer.finalMeddraTerms[splicer.count] = finalTransform(splicer.finalMeddraTerms[splicer.count]);
    }

    private String finalTransform(String fm) {
        if (fm.equalsIgnoreCase("alte")) {
            return "SGPT increased";
        } else {
            return fm.equalsIgnoreCase("fatigueability") ? "fatigue" : fm;
        }
    }

    private static String qtd(String s) {
        return "'" + s + "'";
    }

}
