package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Section {
    private static final Logger log = LogManager.getLogger();
    static String line = "";
    static AdeProcess ade = new AdeProcess();
    static String[] tempArray5 = new String[1000];

    public void getSections() {
        try {
            FileReader fr = new FileReader(F5.myFile);
            BufferedReader br = new BufferedReader(fr);
            F5.setIdFound = false;
            F5.setId = "";
            F5.SPLIdFound = false;
            F5.SPLId = "";
            F5.SPLDateFound = false;
            F5.SPLDate = "";

            while ((line = br.readLine()) != null) {

                this.getSpecificSection(line);
                if (!F5.setIdFound) {
                    this.getSetId(line);
                }

                if (!F5.SPLIdFound) {
                    this.getSPLId(line);
                }

                if (!F5.SPLDateFound) {
                    this.getSPLDate(line);
                }
            }

            fr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        F5.lowValueInd = "";
        tempArray5 = F5.allIndInfo.split("\\.");

        int c;
        for (c = 0; c < tempArray5.length; ++c) {
            if (tempArray5[c].contains("indica")) {
                F5.highValueInd = F5.highValueInd + " " + tempArray5[c];
            } else {
                F5.lowValueInd = F5.lowValueInd + " " + tempArray5[c];
            }
        }

        F5.lowValueCont = "";
        tempArray5 = F5.allContInfo.split("\\.");

        for (c = 0; c < tempArray5.length; ++c) {
            if (tempArray5[c].contains("contra")) {
                F5.highValueCont = F5.highValueCont + " " + tempArray5[c];
            } else {
                F5.lowValueCont = F5.lowValueCont + " " + tempArray5[c];
            }
        }
    }

    public void getSpecificSection(String lin) {
        String line = lin;
        if (F5.adiInt2 > 0 && F5.adiInt2 < 4) {
            F5.allDrugInfo2 = F5.allDrugInfo2 + " " + lin;
            ++F5.adiInt2;
        }

        if (F5.adiInt2 == 0 && (lin.contains("<manufacturedMedicine>") || lin.contains("<manufacturedProduct>"))) {
            F5.allDrugInfo2 = F5.allDrugInfo2 + " " + lin;
            ++F5.adiInt2;
        }

        if (F5.adiInt > 0 && F5.adiInt < 4) {
            F5.allDrugInfo = F5.allDrugInfo + " " + lin;
            ++F5.adiInt;
        }

        if (F5.adiInt == 0 && lin.contains("<genericMedicine>")) {
            F5.allDrugInfo = F5.allDrugInfo + " " + lin;
            ++F5.adiInt;
        }

        if (F5.adiInt3 > 0 && F5.adiInt3 < 4) {
            F5.allActiveMoiety = F5.allActiveMoiety + " " + lin;
            ++F5.adiInt3;
        }

        if (F5.adiInt3 == 0 && lin.contains("<activeMoiety>")) {
            F5.allActiveMoiety = F5.allActiveMoiety + " " + lin;
            ++F5.adiInt3;
        }

        if (F5.pregInt > 0) {
            if ((lin.toLowerCase().contains("category") || lin.toLowerCase().contains("categories")) && F5.pregInt > 3) {
                F5.allPregInfo = F5.allPregInfo + " " + lin;
                log.debug("Loaded all Preg Info");
                log.debug(F5.allPregInfo);
                F5.pregInt = -1;
            } else if (F5.pregInt > 20) {
                F5.allPregInfo = F5.allPregInfo + " " + lin;
                if (!F5.allPregInfo.toLowerCase().contains("category") && !F5.allPregInfo.toLowerCase().contains("categories")) {
                    F5.allPregInfo = "";
                    F5.pregInt = 0;
                } else {
                    F5.pregInt = -1;
                }
            } else {
                F5.allPregInfo = F5.allPregInfo + " " + lin;
                ++F5.pregInt;
            }
        }

        if (F5.pregInt == 0) {
            if (lin.toLowerCase().contains("pregnancy category") || lin.toLowerCase().contains("pregnancy categories") || lin.toLowerCase().contains("teratogenic effects category") || lin.toLowerCase().contains("teratogenic effects<") || lin.toLowerCase().contains(">pregnancy<") || lin.contains("code=\"42228-7\"") || lin.toLowerCase().contains("pregnancy") && lin.toLowerCase().contains("category")) {
                F5.allPregInfo = F5.allPregInfo + " " + lin;
                ++F5.pregInt;
            }
        }

        if (F5.pedInt > 0) {
            if (lin.contains("displayName=")) {
                if (lin.contains("code=\"42229-5\"")) {
                    F5.allPedInfo = F5.allPedInfo + " " + lin;
                    ++F5.pedInt;
                } else {
                    F5.pedInt = 0;
                    F5.gettingPedSect = false;
                }
            } else {
                F5.allPedInfo = F5.allPedInfo + " " + lin;
                ++F5.pedInt;
            }
        }

        if (F5.pedInt == 0 && lin.toLowerCase().contains("<code code=\"34081-0")) {
            F5.allPedInfo = F5.allPedInfo + " " + lin;
            ++F5.pedInt;
            F5.gettingPedSect = true;
        }

        if (F5.indInt > 0) {
            if (lin.contains("displayName=")) {
                if (lin.contains("code=\"42229-5\"")) {
                    F5.allIndInfo = F5.allIndInfo + " " + lin;
                    ++F5.indInt;
                } else {
                    F5.indInt = 0;
                }
            } else {
                F5.allIndInfo = F5.allIndInfo + " " + lin;
                ++F5.indInt;
            }
        }

        if (F5.indInt == 0 && (lin.toLowerCase().contains("indications and usage") || lin.toLowerCase().contains("34067-9"))) {
            F5.allIndInfo = F5.allIndInfo + " " + lin;
            ++F5.indInt;
        }

        if (F5.precInt > 0) {
            if (lin.contains("displayName=")) {
                if (!lin.contains("code=\"42229-5\"") && !lin.contains("code=\"34072-9\"")) {
                    F5.precInt = 0;
                } else {
                    F5.allPreInfo = F5.allPreInfo + " " + lin;
                    ++F5.precInt;
                }
            } else {
                F5.allPreInfo = F5.allPreInfo + " " + lin;
                ++F5.precInt;
            }
        }

        if (F5.precInt == 0) {
            if (lin.toLowerCase().contains("section id=\"precautions\">") || lin.toLowerCase().contains("displayname=\"precautions section") || lin.contains("code=\"42232-9\"")) {
                F5.allPreInfo = F5.allPreInfo + " " + lin;
                ++F5.precInt;
            }
        }

        if (F5.overInt > 0) {
            if (lin.contains("displayName=")) {
                if (lin.contains("code=\"42229-5\"")) {
                    F5.allOverInfo = F5.allOverInfo + " " + lin;
                    ++F5.overInt;
                } else {
                    F5.overInt = 0;
                }
            } else {
                F5.allOverInfo = F5.allOverInfo + " " + lin;
                ++F5.overInt;
            }
        }

        if (F5.overInt == 0 && (lin.toLowerCase().contains("displayname=\"overdosage section\"") || lin.contains("code=\"34088-5\""))) {
            F5.allOverInfo = F5.allOverInfo + " " + lin;
            ++F5.overInt;
        }

        if (F5.warnInt > 0) {
            if (lin.contains("displayName=")) {
                if (lin.contains("code=\"42229-5\"")) {
                    F5.allWarnInfo = F5.allWarnInfo + " " + lin;
                    ++F5.warnInt;
                } else {
                    F5.warnInt = 0;
                }
            } else {
                F5.allWarnInfo = F5.allWarnInfo + " " + lin;
                ++F5.warnInt;
            }
        }

        if (F5.warnInt == 0 && (lin.toLowerCase().contains("displayname=\"warnings section\"") || lin.toLowerCase().contains("displayname=\"warnings and precautions section\"") || lin.contains("code=\"34071-1\""))) {
            F5.allWarnInfo = F5.allWarnInfo + " " + lin;
            ++F5.warnInt;
        }

        if (F5.boxInt > 0) {
            if (lin.contains("</component>")) {
                F5.boxInt = 0;
            } else {
                F5.allBoxInfo = F5.allBoxInfo + " " + lin;
                ++F5.boxInt;
            }
        }

        if (F5.boxInt == 0 && (lin.toLowerCase().contains("displayname=\"boxed warning section\"") || lin.contains("code=\"34066-1\""))) {
            F5.allBoxInfo = F5.allBoxInfo + " " + lin;
            ++F5.boxInt;
        }

        if (F5.adeInt > 0) {
            if ((lin.contains("<code code=") || lin.contains(" code=\"")) && !lin.contains("42229-5")) {
                line = this.checkTableForParagraph(lin);
                F5.allAdeInfo = F5.allAdeInfo + "\n " + line;
                F5.fullMessage = F5.allAdeInfo;
                F5.adeInt = 0;
                log.debug("Loaded full message");
                log.debug(F5.fullMessage);
            } else {
                line = this.checkTableForParagraph(lin);
                F5.allAdeInfo = F5.allAdeInfo + "\n " + line;
                ++F5.adeInt;
            }
        }

        if (F5.adeInt == 0 && (line.toLowerCase().contains("displayname=\"adverse reactions section\"") || line.toLowerCase().contains("<title>adverse reactions </title>") || line.toLowerCase().contains("displayname=\"adverse reactions\"") || line.toLowerCase().contains("adverse reactions</title>") || line.contains("code=\"34084-4\"")) && !line.toLowerCase().contains("life threatening")) {
            line = this.checkTableForParagraph(line);
            F5.allAdeInfo = F5.allAdeInfo + " " + line;
            ++F5.adeInt;
        }

        if (F5.contInt > 0) {
            if (line.contains("displayName=")) {
                if (line.contains("code=\"42229-5\"")) {
                    F5.allContInfo = F5.allContInfo + " " + line;
                    ++F5.contInt;
                } else {
                    F5.contInt = 0;
                }
            } else {
                F5.allContInfo = F5.allContInfo + " " + line;
                ++F5.contInt;
            }
        }

        if (F5.contInt == 0 && (line.toLowerCase().contains("displayname=\"contraindications section") || line.contains("code=\"34070-3\""))) {
            F5.allContInfo = F5.allContInfo + " " + line;
            ++F5.contInt;
        }


    }

    public void getSetId(String t) {
        String tempSetId = "";
        if (t.contains("<setId root=")) {
            tempSetId = t.substring(t.indexOf("\""));
            tempSetId = tempSetId.replaceAll("\"", "");
            tempSetId = tempSetId.replace("/>", "");
            tempSetId = tempSetId.trim();
            F5.setId = tempSetId;
            F5.setIdFound = true;
        }

    }

    public void getSPLId(String t) {
        String tempSPLId = "";
        if (t.contains("<id root=")) {
            tempSPLId = t.substring(t.indexOf("\""));
            tempSPLId = tempSPLId.replaceAll("\"", "");
            tempSPLId = tempSPLId.replace("/>", "");
            tempSPLId = tempSPLId.trim();
            F5.SPLId = tempSPLId;
            F5.SPLIdFound = true;
        }

    }

    public void getSPLDate(String t) {
        String tempSPLDate = "";
        if (t.contains("<effectiveTime value=")) {
            tempSPLDate = t.substring(t.indexOf("\""));
            tempSPLDate = tempSPLDate.replaceAll("\"", "");
            tempSPLDate = tempSPLDate.replace("/>", "");
            tempSPLDate = tempSPLDate.trim();
            F5.SPLDate = tempSPLDate;
            F5.SPLDateFound = true;
        }

    }

    public String checkTableForParagraph(String ln) {

        if (line.contains("<tbody>")) {
            F5.gettingTable = true;
        }

        if (F5.gettingTable) {
            if (line.contains("</tbody>")) {
                F5.gettingTable = false;
            }
        } else {
            if (!ln.contains("</table>")) {
                ln = ln.replace("</paragraph>", " ]. ");
            } else if (ln.contains("</table>")) {
                String part1 = ln.substring(0, ln.indexOf("</table>"));
                String part2 = ln.substring(ln.indexOf("</table>"));
                part2 = part2.replace("</paragraph>", " ]. ");
                ln = part1 + " " + part2;
            }
        }

        return ln;
    }

    public void processSections() {
        F5.currentSection = "over";
        F5.medraCount = 0;
        String normOver = Normals.normal2(F5.allOverInfo);
        normOver = normOver.toLowerCase();
        normOver = this.stopOnly(normOver);
        AdeProcess.getMedraTermsStop(normOver);
        ade.getUniqueLCS3();
//        F5.currentSection = "Precautions (beta)";
//        F5.medraCount = 0;
//        String preWarnCombo = F5.allPreInfo + " " + F5.allWarnInfo;
//        String normPre = Normals.normal2(preWarnCombo);
//        normPre = normPre.toLowerCase();
//        normPre = this.stopOnly(normPre);
//        AdeProcess.getMedraTermsStop(normPre);
//        ade.getUniqueLCS3();
        F5.currentSection = "Black Box (beta)";
        F5.medraCount = 0;
        String normBox = Normals.normal2(F5.allBoxInfo);
        log.debug(normBox);
        normBox = this.stopOnly(normBox);
        AdeProcess.getMedraTermsStop(normBox);
        ade.getUniqueLCS3();
        F5.currentSection = "ind_HV";
        F5.medraCount = 0;
        String normHvi = Normals.normal2(F5.highValueInd);
        normHvi = this.stopOnly(normHvi);
        AdeProcess.getMedraTermsStop(normHvi);
        ade.getUniqueLCS3();
        F5.currentSection = "ind_LV";
        F5.medraCount = 0;
        String normLvi = Normals.normal2(F5.lowValueInd);
        normLvi = this.stopOnly(normLvi);
        AdeProcess.getMedraTermsStop(normLvi);
        ade.getUniqueLCS3();
        F5.currentSection = "cont_HV";
        F5.medraCount = 0;
        String normHvc = Normals.normal2(F5.highValueCont);
        normHvc = this.stopOnly(normHvc);
        AdeProcess.getMedraTermsStop(normHvc);
        ade.getUniqueLCS3();
        F5.currentSection = "cont_LV";
        F5.medraCount = 0;
        String normLvc = Normals.normal2(F5.lowValueCont);
        this.stopOnly(normLvc);
        AdeProcess.getMedraTermsStop(normHvc);
        ade.getUniqueLCS3();
    }

    public String stopOnly(String b) {
        b = b.toLowerCase();
        String[] tempArray = b.split(" ");
        StringBuilder allWords = new StringBuilder();

        for (String s : tempArray) {
            boolean isStopWord = F5.stopSet.contains(s);
            if (!isStopWord) {
                allWords.append(" ").append(s);
            }
        }

        allWords = new StringBuilder(allWords.toString().trim());
        allWords.insert(0, " ");
        return allWords.toString();
    }

}
