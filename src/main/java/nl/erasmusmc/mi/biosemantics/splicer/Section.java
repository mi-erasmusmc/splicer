package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static nl.erasmusmc.mi.biosemantics.splicer.F5.fail;

public class Section {
    private static final Logger log = LogManager.getLogger();
    private String allContInfo = "";
    private int adiInt = 0;
    private int adiInt2 = 0;
    private int adiInt3 = 0;
    private int pregInt = 0;
    private int indInt = 0;
    private int precInt = 0;
    private int overInt = 0;
    private int warnInt = 0;
    private int boxInt = 0;
    private int adeInt = 0;
    int contInt = 0;
    private final StringBuilder highValueCont = new StringBuilder();
    private final StringBuilder lowValueInd = new StringBuilder();
    private final StringBuilder lowValueCont = new StringBuilder();
    private String allWarnInfo = "";
    private String allOverInfo = "";
    private String allBoxInfo = "";
    private String allPreInfo = "";
    private String allAdeInfo = "";
    private String allIndInfo = "";
    private int pedInt = 0;
    private String allPedInfo = "";
    private final Splicer splicer;


    public Section(Splicer splicer) {
        this.splicer = splicer;
    }

    public void getSections(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                getSpecificSection(line);
                if (splicer.setId.isBlank()) {
                    getSetId(line);
                }
                if (splicer.splId.isBlank()) {
                    getSPLId(line);
                }
                if (splicer.splDate.isBlank()) {
                    getSPLDate(line);
                }
            }
        } catch (IOException e) {
            fail(e);
        }


        Arrays.stream(allIndInfo.split("\\.")).forEach(c -> {
            if (c.contains("indica")) {
                splicer.highValueInd.append(" ").append(c);
            } else {
                lowValueInd.append(" ").append(c);
            }
        });

        Arrays.stream(allContInfo.split("\\.")).forEach(c -> {
            if (c.contains("contra")) {
                highValueCont.append(" ").append(c);
            } else {
                lowValueCont.append(" ").append(c);
            }
        });

    }

    private void getSpecificSection(String lin) {
        String line = lin;
        if (adiInt2 > 0 && adiInt2 < 4) {
            splicer.allDrugInfo2 = splicer.allDrugInfo2 + " " + lin;
            ++adiInt2;
        }

        if (adiInt2 == 0 && (lin.contains("<manufacturedMedicine>") || lin.contains("<manufacturedProduct>"))) {
            splicer.allDrugInfo2 = splicer.allDrugInfo2 + " " + lin;
            ++adiInt2;
        }

        if (adiInt > 0 && adiInt < 4) {
            splicer.allDrugInfo = splicer.allDrugInfo + " " + lin;
            ++adiInt;
        }

        if (adiInt == 0 && lin.contains("<genericMedicine>")) {
            splicer.allDrugInfo = splicer.allDrugInfo + " " + lin;
            ++adiInt;
        }

        if (adiInt3 > 0 && adiInt3 < 4) {
            splicer.allActiveMoiety = splicer.allActiveMoiety + " " + lin;
            ++adiInt3;
        }

        if (adiInt3 == 0 && lin.contains("<activeMoiety>")) {
            splicer.allActiveMoiety = splicer.allActiveMoiety + " " + lin;
            ++adiInt3;
        }

        if (pregInt > 0) {
            if ((lin.toLowerCase().contains("category") || lin.toLowerCase().contains("categories")) && pregInt > 3) {
                splicer.allPregInfo = splicer.allPregInfo + " " + lin;
                log.debug("Loaded all Preg Info");
                log.debug(splicer.allPregInfo);
                pregInt = -1;
            } else if (pregInt > 20) {
                splicer.allPregInfo = splicer.allPregInfo + " " + lin;
                if (!splicer.allPregInfo.toLowerCase().contains("category") && !splicer.allPregInfo.toLowerCase().contains("categories")) {
                    splicer.allPregInfo = "";
                    pregInt = 0;
                } else {
                    pregInt = -1;
                }
            } else {
                splicer.allPregInfo = splicer.allPregInfo + " " + lin;
                ++pregInt;
            }
        }

        if (pregInt == 0) {
            if (lin.toLowerCase().contains("pregnancy category") || lin.toLowerCase().contains("pregnancy categories")
                    || lin.toLowerCase().contains("teratogenic effects category") || lin.toLowerCase().contains("teratogenic effects<")
                    || lin.toLowerCase().contains(">pregnancy<") || lin.contains("code=\"42228-7\"")
                    || lin.toLowerCase().contains("pregnancy") && lin.toLowerCase().contains("category")) {
                splicer.allPregInfo = splicer.allPregInfo + " " + lin;
                ++pregInt;
            }
        }

        if (pedInt > 0) {
            if (lin.contains("displayName=")) {
                if (lin.contains("code=\"42229-5\"")) {
                    allPedInfo = allPedInfo + " " + lin;
                    ++pedInt;
                } else {
                    pedInt = 0;
                }
            } else {
                allPedInfo = allPedInfo + " " + lin;
                ++pedInt;
            }
        }

        if (pedInt == 0 && lin.toLowerCase().contains("<code code=\"34081-0")) {
            allPedInfo = allPedInfo + " " + lin;
            ++pedInt;
        }

        if (indInt > 0) {
            if (lin.contains("displayName=")) {
                if (lin.contains("code=\"42229-5\"")) {
                    allIndInfo = allIndInfo + " " + lin;
                    ++indInt;
                } else {
                    indInt = 0;
                }
            } else {
                allIndInfo = allIndInfo + " " + lin;
                ++indInt;
            }
        }

        if (indInt == 0 && (lin.toLowerCase().contains("indications and usage") || lin.toLowerCase().contains("34067-9"))) {
            allIndInfo = allIndInfo + " " + lin;
            ++indInt;
        }

        if (precInt > 0) {
            if (lin.contains("displayName=")) {
                if (!lin.contains("code=\"42229-5\"") && !lin.contains("code=\"34072-9\"")) {
                    precInt = 0;
                } else {
                    allPreInfo = allPreInfo + " " + lin;
                    ++precInt;
                }
            } else {
                allPreInfo = allPreInfo + " " + lin;
                ++precInt;
            }
        }

        if (precInt == 0) {
            if (lin.toLowerCase().contains("section id=\"precautions\">") || lin.toLowerCase().contains("displayname=\"precautions section") || lin.contains("code=\"42232-9\"")) {
                allPreInfo = allPreInfo + " " + lin;
                ++precInt;
            }
        }

        if (overInt > 0) {
            if (lin.contains("displayName=")) {
                if (lin.contains("code=\"42229-5\"")) {
                    allOverInfo = allOverInfo + " " + lin;
                    ++overInt;
                } else {
                    overInt = 0;
                }
            } else {
                allOverInfo = allOverInfo + " " + lin;
                ++overInt;
            }
        }

        if (overInt == 0 && (lin.toLowerCase().contains("displayname=\"overdosage section\"") || lin.contains("code=\"34088-5\""))) {
            allOverInfo = allOverInfo + " " + lin;
            ++overInt;
        }

        if (warnInt > 0) {
            if (lin.contains("displayName=")) {
                if (lin.contains("code=\"42229-5\"")) {
                    allWarnInfo = allWarnInfo + " " + lin;
                    ++warnInt;
                } else {
                    warnInt = 0;
                }
            } else {
                allWarnInfo = allWarnInfo + " " + lin;
                ++warnInt;
            }
        }

        if (warnInt == 0 && (lin.toLowerCase().contains("displayname=\"warnings section\"") || lin.toLowerCase().contains("displayname=\"warnings and precautions section\"") || lin.contains("code=\"34071-1\""))) {
            allWarnInfo = allWarnInfo + " " + lin;
            ++warnInt;
        }

        if (boxInt > 0) {
            if (lin.contains("</component>")) {
                boxInt = 0;
            } else {
                allBoxInfo = allBoxInfo + " " + lin;
                ++boxInt;
            }
        }

        if (boxInt == 0 && (lin.toLowerCase().contains("displayname=\"boxed warning section\"") || lin.contains("code=\"34066-1\""))) {
            allBoxInfo = allBoxInfo + " " + lin;
            ++boxInt;
        }

        if (adeInt > 0) {
            if ((lin.contains("<code code=") || lin.contains(" code=\"")) && !lin.contains("42229-5")) {
                line = checkTableForParagraph(lin, lin);
                allAdeInfo = allAdeInfo + "\n " + line;
                splicer.fullMessage = allAdeInfo;
                adeInt = 0;
                log.debug("Loaded full message:");
                log.debug(splicer.fullMessage);
            } else {
                line = checkTableForParagraph(lin, lin);
                allAdeInfo = allAdeInfo + "\n " + line;
                ++adeInt;
            }
        }

        if (adeInt == 0 && (line.toLowerCase().contains("displayname=\"adverse reactions section\"") || line.toLowerCase().contains("<title>adverse reactions </title>") || line.toLowerCase().contains("displayname=\"adverse reactions\"") || line.toLowerCase().contains("adverse reactions</title>") || line.contains("code=\"34084-4\"")) && !line.toLowerCase().contains("life threatening")) {
            line = checkTableForParagraph(line, lin);
            allAdeInfo = allAdeInfo + " " + line;
            ++adeInt;
        }

        if (contInt > 0) {
            if (line.contains("displayName=")) {
                if (line.contains("code=\"42229-5\"")) {
                    allContInfo = allContInfo + " " + line;
                    ++contInt;
                } else {
                    contInt = 0;
                }
            } else {
                allContInfo = allContInfo + " " + line;
                ++contInt;
            }
        }

        if (contInt == 0 && (line.toLowerCase().contains("displayname=\"contraindications section") || line.contains("code=\"34070-3\""))) {
            allContInfo = allContInfo + " " + line;
            ++contInt;
        }


    }

    private void getSetId(String t) {
        if (t.contains("<setId root=")) {
            var tempSetId = t.substring(t.indexOf("\""));
            tempSetId = tempSetId.replaceAll("\"", "");
            tempSetId = tempSetId.replace("/>", "");
            tempSetId = tempSetId.trim();
            splicer.setId = tempSetId;
        }
    }

    private void getSPLId(String t) {
        if (t.contains("<id root=")) {
            String tempSPLId = t.substring(t.indexOf("\""));
            tempSPLId = tempSPLId.replace("\"", "");
            tempSPLId = tempSPLId.replace("/>", "");
            tempSPLId = tempSPLId.trim();
            splicer.splId = tempSPLId;
        }

    }

    private void getSPLDate(String t) {
        if (t.contains("<effectiveTime value=")) {
            String tempSPLDate = t.substring(t.indexOf("\""));
            tempSPLDate = tempSPLDate.replace("\"", "");
            tempSPLDate = tempSPLDate.replace("/>", "");
            tempSPLDate = tempSPLDate.trim();
            splicer.splDate = tempSPLDate;
        }
    }

    private String checkTableForParagraph(String ln, String line) {

        if (line.contains("<tbody>")) {
            splicer.gettingTable = !line.contains("</tbody>");
        } else {
            if (ln.contains("</table>")) {
                String part1 = ln.substring(0, ln.indexOf("</table>"));
                String part2 = ln.substring(ln.indexOf("</table>"));
                part2 = part2.replace("</paragraph>", " ]. ");
                ln = part1 + " " + part2;
            } else {
                ln = ln.replace("</paragraph>", " ]. ");
            }
        }

        return ln;
    }

    public void processSections() {
        splicer.currentSection = "over";
        splicer.meddraCount = 0;
        String normOver = Normals.normal2(allOverInfo).toLowerCase();
        normOver = stopOnly(normOver);
        splicer.adeProcess.getMeddraTermsStop(normOver);
        splicer.adeProcess.getUniqueLCS3();
//        splicer.currentSection = "Precautions (beta)";
//        splicer.medraCount = 0;
//        String preWarnCombo = splicer.allPreInfo + " " + splicer.allWarnInfo;
//        String normPre = Normals.normal2(preWarnCombo);
//        normPre = normPre.toLowerCase();
//        normPre = this.stopOnly(normPre);
//        AdeProcess.getMedraTermsStop(normPre);
//        splicer.adeProcess.getUniqueLCS3();
        splicer.currentSection = "Black Box (beta)";
        splicer.meddraCount = 0;
        String normBox = Normals.normal2(allBoxInfo).toLowerCase();
        log.debug("Black Box:");
        log.debug(normBox);
        normBox = stopOnly(normBox);
        splicer.adeProcess.getMeddraTerms(normBox);
        splicer.adeProcess.getUniqueLCS3();
        splicer.currentSection = "ind_HV";
        splicer.meddraCount = 0;
        String normHvi = Normals.normal2(splicer.highValueInd.toString());
        normHvi = stopOnly(normHvi);
        splicer.adeProcess.getMeddraTermsStop(normHvi);
        splicer.adeProcess.getUniqueLCS3();
        splicer.currentSection = "ind_LV";
        splicer.meddraCount = 0;
        String normLvi = Normals.normal2(lowValueInd.toString());
        normLvi = stopOnly(normLvi);
        splicer.adeProcess.getMeddraTermsStop(normLvi);
        splicer.adeProcess.getUniqueLCS3();
        splicer.currentSection = "cont_HV";
        splicer.meddraCount = 0;
        String normHvc = Normals.normal2(highValueCont.toString());
        normHvc = stopOnly(normHvc);
        splicer.adeProcess.getMeddraTermsStop(normHvc);
        splicer.adeProcess.getUniqueLCS3();
        splicer.currentSection = "cont_LV";
        splicer.meddraCount = 0;
        String normLvc = Normals.normal2(lowValueCont.toString());
        stopOnly(normLvc);
        splicer.adeProcess.getMeddraTermsStop(normHvc);
        splicer.adeProcess.getUniqueLCS3();
    }

    private static String stopOnly(String b) {
        String[] tempArray = b.toLowerCase().split(" ");
        StringBuilder allWords = new StringBuilder();

        for (String s : tempArray) {
            boolean isStopWord = Reference.stopSet().contains(s);
            if (!isStopWord) {
                allWords.append(" ").append(s);
            }
        }

        allWords = new StringBuilder(allWords.toString().trim());
        allWords.insert(0, " ");
        return allWords.toString();
    }

}
