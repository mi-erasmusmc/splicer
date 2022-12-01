package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.erasmusmc.mi.biosemantics.splicer.Method.T1;

public class Tables {
    private static final Logger log = LogManager.getLogger();
    private final String[] freqArray = new String[10000];
    private final String[] condArray = new String[10000];
    private final Splicer splicer;
    private int maxFreqArray = 0;
    private int maxCondArray = 0;
    private boolean skipTable = true;
    private String targetReact = "";
    private String wholeTable = "";
    private String freq = "";
    private String plac = "";
    private String pattern = "";
    private String tableN = "";


    public Tables(Splicer splicer) {
        this.splicer = splicer;
    }

    public boolean determineDrugOrder(String wt) {
        boolean st = true;
        boolean primaryFound = false;
        String theGenName;
        if (!splicer.activeMoietyName.isBlank()) {
            theGenName = splicer.activeMoietyName;
        } else {
            theGenName = splicer.genDrugName;
        }

        if (wt.contains(">" + theGenName)) {
            primaryFound = true;
            if (wt.contains(">placebo")) {
                st = false;
                if (wt.indexOf(">" + theGenName) < wt.indexOf(">placebo")) {
                    splicer.drugFirst = true;
                }

                if (wt.indexOf(">" + theGenName) > wt.indexOf(">placebo")) {
                    splicer.drugFirst = false;
                }
            } else if (wt.contains(">comparator")) {
                st = false;
                if (wt.indexOf(">" + theGenName) < wt.indexOf(">comparator")) {
                    splicer.drugFirst = true;
                }

                if (wt.indexOf(">" + theGenName) > wt.indexOf(">comparator")) {
                    splicer.drugFirst = false;
                }
            }
        } else if (wt.contains(">" + splicer.tradeDrugName)) {
            primaryFound = true;
            if (wt.contains(">placebo")) {
                st = false;
                if (wt.indexOf(">" + splicer.tradeDrugName) < wt.indexOf(">placebo")) {
                    splicer.drugFirst = true;
                }

                if (wt.indexOf(">" + splicer.tradeDrugName) > wt.indexOf(">placebo")) {
                    splicer.drugFirst = false;
                }
            } else if (wt.contains(">comparator")) {
                st = false;
                if (wt.indexOf(">" + splicer.tradeDrugName) < wt.indexOf(">comparator")) {
                    splicer.drugFirst = true;
                }

                if (wt.indexOf(">" + splicer.tradeDrugName) > wt.indexOf(">comparator")) {
                    splicer.drugFirst = false;
                }
            }
        }

        if (primaryFound) {
            return st;
        } else {
            String shortGen = theGenName.substring(1);
            String shortTrade = splicer.tradeDrugName.substring(1);
            if (wt.contains(">" + shortGen)) {
                st = handlePlacebo(wt, st, shortGen);
            } else if (wt.contains(">" + shortTrade)) {
                st = handlePlacebo(wt, st, shortTrade);
            }

            return st;
        }
    }

    private boolean handlePlacebo(String wt, boolean st, String shortGen) {
        if (wt.contains(">lacebo")) {
            st = false;
            if (wt.indexOf(">" + shortGen) < wt.indexOf(">lacebo")) {
                splicer.drugFirst = true;
            }

            if (wt.indexOf(">" + shortGen) > wt.indexOf(">lacebo")) {
                splicer.drugFirst = false;
            }
        } else if (wt.contains(">omparator")) {
            st = false;
            if (wt.indexOf(">" + shortGen) < wt.indexOf(">omparator")) {
                splicer.drugFirst = true;
            }

            if (wt.indexOf(">" + shortGen) > wt.indexOf(">omparator")) {
                splicer.drugFirst = false;
            }
        }
        return st;
    }

    public void processTables(String fm) {
        maxCondArray = -1;
        freq = "";
        plac = "";
        wholeTable = fm.toLowerCase();
        wholeTable = wholeTable.replaceAll("�����", " ");
        wholeTable = wholeTable.replaceAll("����", " ");
        wholeTable = wholeTable.replaceAll("���", " ");
        wholeTable = wholeTable.replaceAll("��", " ");
        wholeTable = wholeTable.replaceAll("�", " ");
        splicer.tempArray = wholeTable.split("<tr> <td");
        if (splicer.tempArray.length < 4) {
            splicer.tempArray = wholeTable.split("<tr id=");
        }

        if (splicer.tempArray.length < 4) {
            splicer.tempArray = wholeTable.split("</tr>");
        }

        int ta;
        tableValid(wholeTable);
        ta = 1;
        if (!skipTable) {
            while (ta < splicer.tempArray.length) {
                freq = "";
                plac = "";
                String targetLine = splicer.tempArray[ta];
                getTableConditions(targetLine);
                if (maxCondArray > -1) {
                    getTableFreq(targetLine);
                    if (maxFreqArray <= 4) {
                        getTablePatterns(targetLine);
                        getFreqPlac(pattern);
                    }
                }

                maxCondArray = -1;
                ++ta;
            }
        } else {
            splicer.tableToMeddra = true;
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
            if (!splicer.genDrugName.isBlank()) {
                wholeTable = wholeTable.replaceAll(splicer.genDrugName, " ");
            }

            if (!splicer.tradeDrugName.isBlank()) {
                wholeTable = wholeTable.replaceAll(splicer.tradeDrugName, " ");
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
            wholeTable = Normals.compress(wholeTable);
            wholeTable = wholeTable.trim();
            splicer.adeProcess.getT1L1_2(wholeTable);
            splicer.adeProcess.getUniqueLCS();
            splicer.tableToMeddra = false;
        }
    }

    public void tableValid(String wt2) {
        int ta = 1;
        skipTable = true;
        skipTable = determineDrugOrder(wt2);
        tableN = "";
        if (!skipTable) {
            String wt = wt2.replaceAll("&#160;", "");
            wt = Normals.normalSpl(wt);
            log.debug("table with no tags: {}", wt);
            int nCount = 0;
            Pattern p = Pattern.compile("n(\\s+)?<?/?(\\w+)?>?(\\s+)?=(\\s+)?[0-9][0-9]?[0-9]?[0-9]?");
            Matcher m = p.matcher(wt);

            while (m.find()) {
                ++nCount;
                tableN = m.group();
                Pattern p3;
                Matcher m3;
                if (splicer.drugFirst && nCount == 1) {
                    tableN = m.group();
                    p3 = Pattern.compile("[0-9][0-9]?[0-9]?[0-9]?");
                    m3 = p3.matcher(tableN);
                    if (m3.find()) {
                        tableN = m3.group();
                        tableN = Normals.compress(tableN);
                        tableN = tableN.trim();
                        break;
                    }
                }

                if (!splicer.drugFirst && nCount == 2) {
                    tableN = m.group();
                    p3 = Pattern.compile("[0-9][0-9]?[0-9]?[0-9]?");
                    m3 = p3.matcher(tableN);
                    if (m3.find()) {
                        tableN = m3.group();
                        tableN = Normals.compress(tableN);
                        tableN = tableN.trim();
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
        for (totalCols = 0.0D; ta < splicer.tempArray.length; ++ta) {
            targetLine = splicer.tempArray[ta];
            p = Pattern.compile("(valign)");
            m = p.matcher(targetLine);
            while (m.find()) {
                totalCols++;
            }
        }

        if (totalCols == 0.0D) {
            for (ta = 1; ta < splicer.tempArray.length; ++ta) {
                targetLine = splicer.tempArray[ta];
                p = Pattern.compile("valign\\=");
                m = p.matcher(targetLine);
                while (m.find()) {
                    totalCols++;
                }
            }
        }

        if (totalCols == 0.0D) {
            for (ta = 1; ta < splicer.tempArray.length; ++ta) {
                targetLine = splicer.tempArray[ta];
                p = Pattern.compile("align\\=");
                m = p.matcher(targetLine);
                while (m.find()) {
                    totalCols++;
                }
            }
        }

        if (totalCols == 0.0D) {
            for (ta = 1; ta < splicer.tempArray.length; ++ta) {
                targetLine = splicer.tempArray[ta];
                p = Pattern.compile("\\<\\/td\\> \\<td\\>");
                m = p.matcher(targetLine);
                while (m.find()) {
                    totalCols++;
                }
            }
        }

        countColumns = splicer.tempArray.length - 1.0;
        averCols = totalCols / countColumns;
        if (averCols > 4.4D || averCols == 0.0D) {
            skipTable = true;
        }

    }

    public void getTableConditions(String tarLine) {
        Pattern p = Pattern.compile("(>|;)\\s?\\.?([0-9]+)?\\.?[0-9]+\\.?\\s?%?(\\s+)?\\(?<?");
        Matcher m = p.matcher(tarLine);
        if (m.find()) {
            targetReact = "";
            int caCount = 0;
            p = Pattern.compile(";(\\s+)?[a-zA-Z]+(\\s+)?([a-zA-Z]+)?(\\s+)?([a-zA-Z]+)?(\\s+)?([a-zA-Z]+)?<?");

            String tempCond;
            for (m = p.matcher(tarLine); m.find(); ++caCount) {
                tempCond = m.group();
                tempCond = tempCond.replace(";", "");
                tempCond = tempCond.replace("<", "");
                condArray[caCount] = tempCond;
            }

            maxCondArray = caCount - 1;
            if (maxCondArray == -1) {
                caCount = 0;
                p = Pattern.compile(">(\\s+)?[a-zA-Z]+(\\s+)?([a-zA-Z]+)?(\\s+)?([a-zA-Z]+)?(\\s+)?([a-zA-Z]+)?<?");

                for (m = p.matcher(tarLine); m.find(); ++caCount) {
                    tempCond = m.group();
                    tempCond = tempCond.replace(">", "");
                    tempCond = tempCond.replace("<", "");
                    condArray[caCount] = tempCond;
                }

                maxCondArray = caCount - 1;
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
                freqArray[faCount] = tempFreq;
                ++faCount;
            }
        }

        maxFreqArray = faCount - 1;
    }

    public void getTablePatterns(String tarLine) {
        pattern = "zocor";
        if (maxCondArray > 0 && maxFreqArray > 0) {
            if (tarLine.indexOf(freqArray[0]) > tarLine.indexOf(condArray[1])) {
                pattern = "zocor";
            }

            if (tarLine.indexOf(freqArray[0]) < tarLine.indexOf(condArray[1])) {
                pattern = "aptivus";
            }
        }

        if (maxCondArray == 0 && maxFreqArray >= 1) {
            if (!freqArray[0].contains("%") && freqArray[1].contains("%")) {
                pattern = "zemplar";
            } else if (wholeTable.contains(">n <") && wholeTable.contains(">% <")) {
                pattern = "benz";
            }
        }
    }

    public void getFreqPlac(String pat) {
        if (maxCondArray == 0) {
            targetReact = condArray[0];
            if (pat.equals("zemplar")) {
                if (splicer.drugFirst) {
                    freq = freqArray[1];
                    plac = freqArray[3];
                }

                if (!splicer.drugFirst) {
                    freq = freqArray[3];
                    plac = freqArray[1];
                }
            }

            if (pat.equals("benz")) {
                if (splicer.drugFirst) {
                    freq = freqArray[1];
                    plac = freqArray[3];
                }

                if (!splicer.drugFirst) {
                    freq = freqArray[3];
                    plac = freqArray[1];
                }
            } else {
                if (splicer.drugFirst) {
                    freq = freqArray[0];
                    plac = freqArray[1];
                }

                if (!splicer.drugFirst) {
                    freq = freqArray[1];
                    plac = freqArray[0];
                }
            }

            log.debug("targetReactions: {}   freq: {}", targetReact, freq);
            if (plac == null) {
                plac = "";
            }

            if (freq == null) {
                freq = "0";
            }

            splicer.postProcess.placeIntoFinal(targetReact, T1, splicer.currentSection, "-1", freq, plac, "1", tableN);
        }

        int mca;
        Pattern p;
        Matcher m;
        if (maxCondArray == 1) {
            if (pat.equals("zocor")) {
                if (maxFreqArray == 2) {
                    for (mca = 1; mca < maxFreqArray + 1; ++mca) {
                        targetReact = condArray[mca - 1];
                        if (splicer.drugFirst) {
                            freq = freqArray[mca - 1];
                            plac = freqArray[mca + 1];
                        }

                        if (!splicer.drugFirst) {
                            freq = freqArray[mca + 1];
                            plac = freqArray[mca - 1];
                        }

                        log.debug("pattern: {}   targetReactions: {}   freq: {}   plac: {}", pat, targetReact, freq, plac);
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
                        if (!targetReact.isBlank() && !m.find()) {
                            splicer.postProcess.placeIntoFinal(targetReact, T1, splicer.currentSection, "-1", freq, plac, "1", tableN);
                        }
                    }
                } else {
                    handleMaxFreqArrayIsOne(pat);
                }
            }

            if (pat.equals("aptivus")) {
                mca = 1;

                for (; mca <= maxFreqArray + 1; ++mca) {
                    targetReact = condArray[mca - 1];
                    if (splicer.drugFirst) {
                        freq = freqArray[0];
                        plac = freqArray[1];
                    }

                    if (!splicer.drugFirst) {
                        freq = freqArray[1];
                        plac = freqArray[0];
                    }

                    log.debug("pattern: {}   targetReactions: {}   freq: {}", pat, targetReact, freq);
                    if (plac == null) {
                        plac = "";
                    }

                    if (freq == null) {
                        freq = "0";
                    }

                    if (targetReact == null) {
                        targetReact = "";
                    }

                    splicer.postProcess.placeIntoFinal(targetReact, T1, splicer.currentSection, "-1", freq, plac, "1", tableN);
                }
            }
        }

        if (maxCondArray > 1) {
            if (pat.equals("zocor")) {
                if (maxFreqArray == 2) {
                    for (mca = 1; mca < maxFreqArray + 1; ++mca) {
                        targetReact = condArray[mca - 1];
                        if (splicer.drugFirst) {
                            freq = freqArray[mca - 1];
                            plac = freqArray[mca + 1];
                        }

                        if (!splicer.drugFirst) {
                            freq = freqArray[mca + 1];
                            plac = freqArray[mca - 1];
                        }

                        log.debug("pattern: {}   targetReactions: {}   freq: {}   plac: {}", pat, targetReact, freq, plac);
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
                        if (!targetReact.isBlank() && !m.find()) {
                            splicer.postProcess.placeIntoFinal(targetReact, T1, splicer.currentSection, "-1", freq, plac, "1", tableN);
                        }
                    }
                } else handleMaxFreqArrayIsOne(pat);
            }

            if (pat.equals("aptivus")) {
                mca = 1;
                int fromMca = -1;

                for (int fromMca2 = 0; mca <= maxFreqArray + 1; ++mca) {
                    targetReact = condArray[mca - 1];
                    if (splicer.drugFirst) {
                        if (mca == 1) {
                            freq = freqArray[mca - 1];
                            plac = freqArray[mca];
                        } else {
                            freq = freqArray[mca - fromMca];
                            plac = freqArray[mca + 1];
                        }
                    }

                    if (!splicer.drugFirst) {
                        freq = freqArray[mca - fromMca2];
                        plac = freqArray[mca - fromMca];
                    }

                    ++fromMca;
                    ++fromMca2;
                    log.debug("pattern: {}   targetReactions: {}   freq: {}", pat, targetReact, freq);
                    if (plac == null) {
                        plac = "";
                    }

                    if (freq == null) {
                        freq = "0";
                    }

                    if (targetReact == null) {
                        targetReact = "";
                    }

                    splicer.postProcess.placeIntoFinal(targetReact, T1, splicer.currentSection, "-1", freq, plac, "1", tableN);
                }
            }
        }
    }

    private void handleMaxFreqArrayIsOne(String pat) {
        int mca;
        Pattern p;
        Matcher m;
        if (maxFreqArray == 1) {
            for (mca = 1; mca < maxFreqArray + 1; ++mca) {
                targetReact = condArray[mca - 1];
                if (splicer.drugFirst) {
                    freq = freqArray[mca - 1];
                    plac = freqArray[mca];
                }

                if (!splicer.drugFirst) {
                    freq = freqArray[mca];
                    plac = freqArray[mca - 1];
                }

                log.debug("pattern: {}   targetReactions: {}   freq: {}   plac: {}", pat, targetReact, freq, plac);
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
                if (!targetReact.isBlank() && !m.find()) {
                    splicer.postProcess.placeIntoFinal(targetReact, T1, splicer.currentSection, "-1", freq, plac, "1", tableN);
                }
            }
        }
    }

}
