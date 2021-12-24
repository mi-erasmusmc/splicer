package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tables {
    private static final Logger log = LogManager.getLogger();
    static AdeProcess ade = new AdeProcess();
    static String targetReact = "";
    static String wholeTable = "";
    static String freq = "";
    static String plac = "";
    static int tt = 0;
    static int ta = 1;
    static int caCount = 0;
    static String pattern = "";

    public static boolean determineDrugOrder(String wt) {
        boolean st = true;
        boolean primaryFound = false;
        String theGenName;
        if (!F5.activeMoietyName.equalsIgnoreCase("")) {
            theGenName = F5.activeMoietyName;
        } else {
            theGenName = F5.genDrugName;
        }

        if (wt.contains(">" + theGenName)) {
            primaryFound = true;
            if (wt.contains(">placebo")) {
                st = false;
                if (wt.indexOf(">" + theGenName) < wt.indexOf(">placebo")) {
                    F5.drugFirst = true;
                }

                if (wt.indexOf(">" + theGenName) > wt.indexOf(">placebo")) {
                    F5.drugFirst = false;
                }
            } else if (wt.contains(">comparator")) {
                st = false;
                if (wt.indexOf(">" + theGenName) < wt.indexOf(">comparator")) {
                    F5.drugFirst = true;
                }

                if (wt.indexOf(">" + theGenName) > wt.indexOf(">comparator")) {
                    F5.drugFirst = false;
                }
            }
        } else if (wt.contains(">" + F5.tradeDrugName)) {
            primaryFound = true;
            if (wt.contains(">placebo")) {
                st = false;
                if (wt.indexOf(">" + F5.tradeDrugName) < wt.indexOf(">placebo")) {
                    F5.drugFirst = true;
                }

                if (wt.indexOf(">" + F5.tradeDrugName) > wt.indexOf(">placebo")) {
                    F5.drugFirst = false;
                }
            } else if (wt.contains(">comparator")) {
                st = false;
                if (wt.indexOf(">" + F5.tradeDrugName) < wt.indexOf(">comparator")) {
                    F5.drugFirst = true;
                }

                if (wt.indexOf(">" + F5.tradeDrugName) > wt.indexOf(">comparator")) {
                    F5.drugFirst = false;
                }
            }
        }

        if (primaryFound) {
            return st;
        } else {
            String shortGen = theGenName.substring(1);
            String shortTrade = F5.tradeDrugName.substring(1);
            if (wt.contains(">" + shortGen)) {
                st = handlePlacebo(wt, st, shortGen);
            } else if (wt.contains(">" + shortTrade)) {
                st = handlePlacebo(wt, st, shortTrade);
            }

            return st;
        }
    }

    private static boolean handlePlacebo(String wt, boolean st, String shortGen) {
        if (wt.contains(">lacebo")) {
            st = false;
            if (wt.indexOf(">" + shortGen) < wt.indexOf(">lacebo")) {
                F5.drugFirst = true;
            }

            if (wt.indexOf(">" + shortGen) > wt.indexOf(">lacebo")) {
                F5.drugFirst = false;
            }
        } else if (wt.contains(">omparator")) {
            st = false;
            if (wt.indexOf(">" + shortGen) < wt.indexOf(">omparator")) {
                F5.drugFirst = true;
            }

            if (wt.indexOf(">" + shortGen) > wt.indexOf(">omparator")) {
                F5.drugFirst = false;
            }
        }
        return st;
    }

    public void processTables(String fm) {
        F5.processingTable = true;

        F5.maxCondArray = -1;
        tt = 0;
        freq = "";
        plac = "";
        wholeTable = fm.toLowerCase();
        wholeTable = wholeTable.replaceAll("�����", " ");
        wholeTable = wholeTable.replaceAll("����", " ");
        wholeTable = wholeTable.replaceAll("���", " ");
        wholeTable = wholeTable.replaceAll("��", " ");
        wholeTable = wholeTable.replaceAll("�", " ");
        F5.tempArray = wholeTable.split("<tr> <td");
        if (F5.tempArray.length < 4) {
            F5.tempArray = wholeTable.split("<tr id=");
        }

        if (F5.tempArray.length < 4) {
            F5.tempArray = wholeTable.split("</tr>");
        }

        ta = 1;
        this.tableValid(wholeTable);
        ta = 1;
        if (!F5.skipTable) {
            while (ta < F5.tempArray.length) {
                freq = "";
                plac = "";
                String targetLine = F5.tempArray[ta];
                this.getTableConditions(targetLine);
                if (F5.maxCondArray > -1) {
                    this.getTableFreq(targetLine);
                    if (F5.maxFreqArray <= 4) {
                        this.getTablePatterns(targetLine);
                        this.getFreqPlac(pattern);
                    }
                }

                F5.maxCondArray = -1;
                ++ta;
            }
        } else {
            F5.tableToMedra = true;
            if (wholeTable.contains("</td> <td")) {
                wholeTable = wholeTable.replace("</td> <td", "_ </td> <td");
            } else if (wholeTable.contains("</tr")) {
                wholeTable = wholeTable.replace("</tr", "_ </tr");
            }

            wholeTable = wholeTable.replaceAll("bold\">", "bold\"> bbbb");
            wholeTable = Normals.normalSplTable(wholeTable);
            wholeTable = wholeTable.replaceAll("[0-9]?\\.?[0-9]%?", " ");
            wholeTable = Normals.normalBadTable(wholeTable);
            wholeTable = wholeTable.replaceAll("_", ",");
            wholeTable = wholeTable.replace("placebo", " ");
            if (!F5.genDrugName.equalsIgnoreCase("")) {
                wholeTable = wholeTable.replaceAll(F5.genDrugName, " ");
            }

            if (!F5.tradeDrugName.equalsIgnoreCase("")) {
                wholeTable = wholeTable.replaceAll(F5.tradeDrugName, " ");
            }

            wholeTable = wholeTable.replaceAll("bbbb", " ");
            wholeTable = wholeTable.replaceAll("_ _ _ _ _ _", "_");
            wholeTable = wholeTable.replaceAll("_ _ _ _ _", "_");
            wholeTable = wholeTable.replaceAll("_ _ _ _", "_");
            wholeTable = wholeTable.replaceAll("_ _ _", "_");
            wholeTable = wholeTable.replaceAll("_ _", "_");
            wholeTable = wholeTable.replaceAll("���", " ");
            wholeTable = wholeTable.replaceAll("��", " ");
            wholeTable = wholeTable.replaceAll("�", " ");
            wholeTable = wholeTable.replaceAll("\\/", " or ");
            wholeTable = Normals.normalBadTable2(wholeTable);
            wholeTable = F5.compress(wholeTable);
            wholeTable = wholeTable.trim();
            ade.getT1L1_2(wholeTable);
            ade.getUniqueLCS();
            F5.tableToMedra = false;
        }

        F5.processingTable = false;
    }

    public void tableValid(String wt2) {
        int ta = 1;
        F5.skipTable = true;
        F5.skipTable = determineDrugOrder(wt2);
        F5.tableN = "";
        if (!F5.skipTable) {
            String wt = wt2.replaceAll("&#160;", "");
            wt = Normals.normalSpl(wt);
            log.debug("table with no tags: {}", wt);
            int nCount = 0;
            Pattern p = Pattern.compile("n(\\s+)?<?/?(\\w+)?>?(\\s+)?=(\\s+)?[0-9][0-9]?[0-9]?[0-9]?");
            Matcher m = p.matcher(wt);

            while (m.find()) {
                ++nCount;
                F5.tableN = m.group();
                Pattern p3;
                Matcher m3;
                if (F5.drugFirst && nCount == 1) {
                    F5.tableN = m.group();
                    p3 = Pattern.compile("[0-9][0-9]?[0-9]?[0-9]?");
                    m3 = p3.matcher(F5.tableN);
                    if (m3.find()) {
                        F5.tableN = m3.group();
                        F5.tableN = F5.compress(F5.tableN);
                        F5.tableN = F5.tableN.trim();
                        break;
                    }
                }

                if (!F5.drugFirst && nCount == 2) {
                    F5.tableN = m.group();
                    p3 = Pattern.compile("[0-9][0-9]?[0-9]?[0-9]?");
                    m3 = p3.matcher(F5.tableN);
                    if (m3.find()) {
                        F5.tableN = m3.group();
                        F5.tableN = F5.compress(F5.tableN);
                        F5.tableN = F5.tableN.trim();
                    }
                }
            }
        }

        double averCols;

        String targetLine;
        Pattern p;
        Matcher m;
        double totalCols;
        double countColumns;
        for (totalCols = 0.0D; ta < F5.tempArray.length; ++ta) {
            countColumns = 0.0D;
            targetLine = F5.tempArray[ta];
            p = Pattern.compile("(valign)");

            for (m = p.matcher(targetLine); m.find(); ++countColumns) {
            }

            totalCols += countColumns;
        }

        if (totalCols == 0.0D) {
            for (ta = 1; ta < F5.tempArray.length; ++ta) {
                countColumns = 0.0D;
                targetLine = F5.tempArray[ta];
                p = Pattern.compile("valign\\=");

                for (m = p.matcher(targetLine); m.find(); ++countColumns) {
                }

                totalCols += countColumns;
            }
        }

        if (totalCols == 0.0D) {
            for (ta = 1; ta < F5.tempArray.length; ++ta) {
                countColumns = 0.0D;
                targetLine = F5.tempArray[ta];
                p = Pattern.compile("align\\=");

                for (m = p.matcher(targetLine); m.find(); ++countColumns) {
                }

                totalCols += countColumns;
            }
        }

        if (totalCols == 0.0D) {
            for (ta = 1; ta < F5.tempArray.length; ++ta) {
                countColumns = 0.0D;
                targetLine = F5.tempArray[ta];
                p = Pattern.compile("\\<\\/td\\> \\<td\\>");

                for (m = p.matcher(targetLine); m.find(); ++countColumns) {
                }

                totalCols += countColumns;
            }
        }

        countColumns = F5.tempArray.length - 1;
        averCols = totalCols / countColumns;
        if (averCols > 4.4D || averCols == 0.0D) {
            F5.skipTable = true;
        }

    }

    public void getTableConditions(String tarLine) {
        Pattern p = Pattern.compile("(>|;)\\s?\\.?([0-9]+)?\\.?[0-9]+\\.?\\s?%?(\\s+)?\\(?<?");
        Matcher m = p.matcher(tarLine);
        if (m.find()) {
            targetReact = "";
            caCount = 0;
            p = Pattern.compile(";(\\s+)?[a-zA-Z]+(\\s+)?([a-zA-Z]+)?(\\s+)?([a-zA-Z]+)?(\\s+)?([a-zA-Z]+)?<?");

            String tempCond;
            for (m = p.matcher(tarLine); m.find(); ++caCount) {
                tempCond = m.group();
                tempCond = tempCond.replace(";", "");
                tempCond = tempCond.replace("<", "");
                F5.condArray[caCount] = tempCond;
            }

            F5.maxCondArray = caCount - 1;
            if (F5.maxCondArray == -1) {
                caCount = 0;
                p = Pattern.compile(">(\\s+)?[a-zA-Z]+(\\s+)?([a-zA-Z]+)?(\\s+)?([a-zA-Z]+)?(\\s+)?([a-zA-Z]+)?<?");

                for (m = p.matcher(tarLine); m.find(); ++caCount) {
                    tempCond = m.group();
                    tempCond = tempCond.replace(">", "");
                    tempCond = tempCond.replace("<", "");
                    F5.condArray[caCount] = tempCond;
                }

                F5.maxCondArray = caCount - 1;
            }
        }

    }

    public void getTableFreq(String tarLine) {
        int faCount = 0;
        Pattern p = Pattern.compile("(>|;)\\s?\\.?([0-9]+)?\\.?[0-9]+\\.?\\s?%?(\\s+)?x?\\-?\\(?<?");
        Matcher m = p.matcher(tarLine);

        while (m.find()) {
            String tempFreq = m.group();
            if (!tempFreq.contains("x") && !tempFreq.contains("X") && !tempFreq.contains("-")) {
                tempFreq = tempFreq.replaceAll(">", "");
                tempFreq = tempFreq.replaceAll("<", "");
                tempFreq = tempFreq.replaceAll(";", "");
                tempFreq = tempFreq.replaceAll("\\(", "");
                tempFreq = tempFreq.replaceAll("%", "");
                F5.freqArray[faCount] = tempFreq;
                ++faCount;
            }
        }

        F5.maxFreqArray = faCount - 1;
    }

    public void getTablePatterns(String tarLine) {
        pattern = "zocor";
        if (F5.maxCondArray > 0 && F5.maxFreqArray > 0) {
            if (tarLine.indexOf(F5.freqArray[0]) > tarLine.indexOf(F5.condArray[1])) {
                pattern = "zocor";
            }

            if (tarLine.indexOf(F5.freqArray[0]) < tarLine.indexOf(F5.condArray[1])) {
                pattern = "aptivus";
            }
        }

        if (F5.maxCondArray == 0 && F5.maxFreqArray >= 1) {
            if (!F5.freqArray[0].contains("%") && F5.freqArray[1].contains("%")) {
                pattern = "zemplar";
            } else if (wholeTable.contains(">n <") && wholeTable.contains(">% <")) {
                pattern = "benz";
            }
        }
    }

    public void getFreqPlac(String pat) {
        if (F5.maxCondArray == 0) {
            targetReact = F5.condArray[0];
            if (pat.equals("zemplar")) {
                if (F5.drugFirst) {
                    freq = F5.freqArray[1];
                    plac = F5.freqArray[3];
                }

                if (!F5.drugFirst) {
                    freq = F5.freqArray[3];
                    plac = F5.freqArray[1];
                }
            }

            if (pat.equals("benz")) {
                if (F5.drugFirst) {
                    freq = F5.freqArray[1];
                    plac = F5.freqArray[3];
                }

                if (!F5.drugFirst) {
                    freq = F5.freqArray[3];
                    plac = F5.freqArray[1];
                }
            } else {
                if (F5.drugFirst) {
                    freq = F5.freqArray[0];
                    plac = F5.freqArray[1];
                }

                if (!F5.drugFirst) {
                    freq = F5.freqArray[1];
                    plac = F5.freqArray[0];
                }
            }

            log.debug("table: {}   targetReactions: {}   freq: {}", tt, targetReact, freq);
            if (plac == null) {
                plac = "";
            }

            if (freq == null) {
                freq = "0";
            }

            PostProcess.placeIntoFinal(targetReact, "T1", F5.currentSection, "-1", freq, plac, "1", F5.tableN);
        }

        int mca;
        Pattern p;
        Matcher m;
        if (F5.maxCondArray == 1) {
            if (pat.equals("zocor")) {
                if (F5.maxFreqArray == 2) {
                    for (mca = 1; mca < F5.maxFreqArray + 1; ++mca) {
                        targetReact = F5.condArray[mca - 1];
                        if (F5.drugFirst) {
                            freq = F5.freqArray[mca - 1];
                            plac = F5.freqArray[mca + 1];
                        }

                        if (!F5.drugFirst) {
                            freq = F5.freqArray[mca + 1];
                            plac = F5.freqArray[mca - 1];
                        }

                        log.debug("table:  " + tt + " pattern: " + pat + "   targetReactions: " + targetReact + "    freq: " + freq + "plac:  " + plac);
                        if (freq == null) {
                            freq = "0";
                        }

                        if (plac == null) {
                            plac = "";
                        }

                        if (targetReact == null) {
                            targetReact = "";
                        }

                        targetReact = targetReact.trim();
                        p = Pattern.compile("[0-9]");
                        m = p.matcher(targetReact);
                        if (!targetReact.equals("") && !m.find()) {
                            PostProcess.placeIntoFinal(targetReact, "T1", F5.currentSection, "-1", freq, plac, "1", F5.tableN);
                        }
                    }
                } else {
                    handleMaxFreqArrayIsOne(pat);
                }
            }

            if (pat.equals("aptivus")) {
                mca = 1;

                for (; mca <= F5.maxFreqArray + 1; ++mca) {
                    targetReact = F5.condArray[mca - 1];
                    if (F5.drugFirst) {
                        freq = F5.freqArray[0];
                        plac = F5.freqArray[1];
                    }

                    if (!F5.drugFirst) {
                        freq = F5.freqArray[1];
                        plac = F5.freqArray[0];
                    }

                    log.debug("table:  " + tt + " pattern: " + pat + "   targetReactions: " + targetReact + "    freq: " + freq);
                    if (plac == null) {
                        plac = "";
                    }

                    if (freq == null) {
                        freq = "0";
                    }

                    if (targetReact == null) {
                        targetReact = "";
                    }

                    PostProcess.placeIntoFinal(targetReact, "T1", F5.currentSection, "-1", freq, plac, "1", F5.tableN);
                }
            }
        }

        if (F5.maxCondArray > 1) {
            if (pat.equals("zocor")) {
                if (F5.maxFreqArray == 2) {
                    for (mca = 1; mca < F5.maxFreqArray + 1; ++mca) {
                        targetReact = F5.condArray[mca - 1];
                        if (F5.drugFirst) {
                            freq = F5.freqArray[mca - 1];
                            plac = F5.freqArray[mca + 1];
                        }

                        if (!F5.drugFirst) {
                            freq = F5.freqArray[mca + 1];
                            plac = F5.freqArray[mca - 1];
                        }

                        log.debug("table:  " + tt + " pattern: " + pat + "   targetReactions: " + targetReact + "    freq: " + freq + "plac:  " + plac);
                        if (plac == null) {
                            plac = "";
                        }

                        if (freq == null) {
                            freq = "0";
                        }

                        if (targetReact == null) {
                            targetReact = "";
                        }

                        targetReact = targetReact.trim();
                        p = Pattern.compile("[0-9]");
                        m = p.matcher(targetReact);
                        if (!targetReact.equals("") && !m.find()) {
                            PostProcess.placeIntoFinal(targetReact, "T1", F5.currentSection, "-1", freq, plac, "1", F5.tableN);
                        }
                    }
                } else handleMaxFreqArrayIsOne(pat);
            }

            if (pat.equals("aptivus")) {
                mca = 1;
                int fromMca = -1;

                for (int fromMca2 = 0; mca <= F5.maxFreqArray + 1; ++mca) {
                    targetReact = F5.condArray[mca - 1];
                    if (F5.drugFirst) {
                        if (mca == 1) {
                            freq = F5.freqArray[mca - 1];
                            plac = F5.freqArray[mca];
                        } else {
                            freq = F5.freqArray[mca - fromMca];
                            plac = F5.freqArray[mca + 1];
                        }
                    }

                    if (!F5.drugFirst) {
                        freq = F5.freqArray[mca - fromMca2];
                        plac = F5.freqArray[mca - fromMca];
                    }

                    ++fromMca;
                    ++fromMca2;
                    log.debug("table:  " + tt + " pattern: " + pat + "   targetReactions: " + targetReact + "    freq: " + freq);
                    if (plac == null) {
                        plac = "";
                    }

                    if (freq == null) {
                        freq = "0";
                    }

                    if (targetReact == null) {
                        targetReact = "";
                    }

                    PostProcess.placeIntoFinal(targetReact, "T1", F5.currentSection, "-1", freq, plac, "1", F5.tableN);
                }
            }
        }
    }

    private void handleMaxFreqArrayIsOne(String pat) {
        int mca;
        Pattern p;
        Matcher m;
        if (F5.maxFreqArray == 1) {
            for (mca = 1; mca < F5.maxFreqArray + 1; ++mca) {
                targetReact = F5.condArray[mca - 1];
                if (F5.drugFirst) {
                    freq = F5.freqArray[mca - 1];
                    plac = F5.freqArray[mca];
                }

                if (!F5.drugFirst) {
                    freq = F5.freqArray[mca];
                    plac = F5.freqArray[mca - 1];
                }

                log.debug("table:  " + tt + " pattern: " + pat + "   targetReactions: " + targetReact + "    freq: " + freq + "plac:  " + plac);
                if (plac == null) {
                    plac = "";
                }

                if (freq == null) {
                    freq = "0";
                }

                if (targetReact == null) {
                    targetReact = "";
                }

                targetReact = targetReact.trim();
                p = Pattern.compile("[0-9]");
                m = p.matcher(targetReact);
                if (!targetReact.equals("") && !m.find()) {
                    PostProcess.placeIntoFinal(targetReact, "T1", F5.currentSection, "-1", freq, plac, "1", F5.tableN);
                }
            }
        }
    }

}
