package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Numbers {

    private final Splicer splicer;
    private String runningFreq = "";


    public Numbers(Splicer splicer) {
        this.splicer = splicer;
    }

    public void findFrequencies() {
        int numPercents = StringUtils.countMatches(splicer.currentSentence, "%");
        if (numPercents == 0) {
            if (splicer.italicsFlag && !splicer.origSent.contains(":")) {
                italicsFreqs();
            } else {
                textFreqs(splicer.origSent);
            }
        }

        if (numPercents == 1) {
            Pattern p = Pattern.compile("\\>?\\s?\\=?\\s?([0-9]+)?\\s?\\.?\\s?([0-9]+)?\\.?([0-9]+)?(\\s+)?%");
            Matcher m = p.matcher(splicer.currentSentence);

            while (m.find()) {
                String tempFreq = m.group();
                tempFreq = tempFreq.replace("%", "");
                tempFreq = tempFreq.replace(" ", "");
                tempFreq = Normals.compress(tempFreq);
                tempFreq = tempFreq.trim();
                splicer.postProcess.putIntoFinals(tempFreq);
            }
        }

        if (numPercents >= 2) {
            String lowerCaseSent = splicer.currentSentence.toLowerCase();
            boolean skipSent = true;
            skipSent = determineDrugOrderText(lowerCaseSent);
            Matcher m;
            int perCount;
            String tempFreq;
            String ade;
            Pattern p;
            if (!skipSent) {
                for (splicer.count = 0; splicer.count < splicer.goodCount; ++splicer.count) {

                    if (lowerCaseSent.contains(splicer.goodWords2[splicer.count])) {
                        ade = lowerCaseSent.substring(lowerCaseSent.indexOf(splicer.goodWords2[splicer.count]));
                    } else {
                        ade = lowerCaseSent;
                    }

                    p = Pattern.compile("\\>?\\s?\\=?\\s?([0-9]+)?\\s?\\.?\\s?([0-9]+)?\\.?([0-9]+)?(\\s+)?%");
                    m = p.matcher(ade);
                    perCount = 0;

                    while (m.find()) {
                        ++perCount;
                        if (splicer.drugFirst && perCount == 1) {
                            tempFreq = m.group();
                            tempFreq = tempFreq.replace("%", "");
                            tempFreq = tempFreq.replace(" ", "");
                            tempFreq = Normals.compress(tempFreq);
                            tempFreq = tempFreq.trim();
                            splicer.postProcess.placeIntoFinal(splicer.goodWords2[splicer.count], splicer.sMethod[splicer.count], splicer.sSection[splicer.count], splicer.countSentences + "", tempFreq, "0", "0", "0");
                            break;
                        }

                        if (!splicer.drugFirst && perCount == 2) {
                            tempFreq = m.group();
                            tempFreq = tempFreq.replace("%", "");
                            tempFreq = tempFreq.replace(" ", "");
                            tempFreq = Normals.compress(tempFreq);
                            tempFreq = tempFreq.trim();
                            splicer.postProcess.placeIntoFinal(splicer.goodWords2[splicer.count], splicer.sMethod[splicer.count], splicer.sSection[splicer.count], splicer.countSentences + "", tempFreq, "0", "0", "0");
                            break;
                        }
                    }

                    if (perCount == 0 && splicer.sMethod[splicer.count].toString().contains("M1")) {
                        splicer.postProcess.placeIntoFinal(splicer.goodWords2[splicer.count], splicer.sMethod[splicer.count], splicer.sSection[splicer.count], splicer.countSentences + "", "unk", "0", "0", "0");
                    }
                }
            } else {
                for (splicer.count = 0; splicer.count < splicer.goodCount; ++splicer.count) {

                    if (lowerCaseSent.contains(splicer.goodWords2[splicer.count])) {
                        ade = lowerCaseSent.substring(lowerCaseSent.indexOf(splicer.goodWords2[splicer.count]));
                    } else {
                        ade = "";
                    }

                    p = Pattern.compile("\\>?\\s?\\=?\\s?([0-9]+)?\\s?\\.?\\s?([0-9]+)?\\.?([0-9]+)?(\\s+)?%");
                    m = p.matcher(ade);
                    perCount = 0;

                    while (m.find()) {
                        ++perCount;
                        tempFreq = m.group();
                        tempFreq = tempFreq.replace("%", "");
                        tempFreq = tempFreq.replace(" ", "");
                        tempFreq = tempFreq.trim();
                        splicer.postProcess.placeIntoFinal(splicer.goodWords2[splicer.count], splicer.sMethod[splicer.count], splicer.sSection[splicer.count], splicer.countSentences + "", tempFreq, "0", "0", "0");
                        break;
                    }

                    if (perCount == 0) {
                        splicer.postProcess.placeIntoFinal(splicer.goodWords2[splicer.count], splicer.sMethod[splicer.count], splicer.sSection[splicer.count], splicer.countSentences + "", "unk", "0", "0", "0");
                    }
                }
            }
        }

    }

    public boolean determineDrugOrderText(String wt) {
        boolean st = true;
        if (wt.contains(splicer.genDrugName)) {
            if (wt.contains("placebo")) {
                st = false;
                if (wt.indexOf(splicer.genDrugName) < wt.indexOf("placebo")) {
                    splicer.drugFirst = true;
                }

                if (wt.indexOf(splicer.genDrugName) > wt.indexOf("placebo")) {
                    splicer.drugFirst = false;
                }
            } else if (wt.contains("comparator")) {
                st = false;
                if (wt.indexOf(splicer.genDrugName) < wt.indexOf("comparator")) {
                    splicer.drugFirst = true;
                }

                if (wt.indexOf(splicer.genDrugName) > wt.indexOf("comparator")) {
                    splicer.drugFirst = false;
                }
            }
        } else if (wt.contains(splicer.tradeDrugName)) {
            if (wt.contains("placebo")) {
                st = false;
                if (wt.indexOf(splicer.tradeDrugName) < wt.indexOf("placebo")) {
                    splicer.drugFirst = true;
                }

                if (wt.indexOf(splicer.tradeDrugName) > wt.indexOf("placebo")) {
                    splicer.drugFirst = false;
                }
            } else if (wt.contains("comparator")) {
                st = false;
                if (wt.indexOf(splicer.tradeDrugName) < wt.indexOf("comparator")) {
                    splicer.drugFirst = true;
                }

                if (wt.indexOf(splicer.tradeDrugName) > wt.indexOf("comparator")) {
                    splicer.drugFirst = false;
                }
            }
        }

        return st;
    }

    public void getRunningFreq(String cs) {
        cs = Normals.normal8(cs);
        cs = " " + cs + " ";
        if (!cs.contains(" less frequent ") && !cs.contains(" less frequently ")) {
            if (!cs.contains(" infrequent ") && !cs.contains(" infrequently ")) {
                if (!cs.contains(" frequent ") && !cs.contains(" frequently ")) {
                    if (!cs.contains(" rare ") && !cs.contains(" rarely ")) {
                        if (!cs.contains(" less than 3 . 0 ") && !cs.contains(" less than 3 %")) {
                            if (!cs.contains(" less than 1 . 0 ") && !cs.contains(" less than 1 %")) {
                                if (!cs.contains(" incidence of 1 . 0 ") && !cs.contains(" incidence of 1 % ")) {
                                    if (!cs.contains(" to 1 . 0 ") && !cs.contains(" to 1 % ")) {
                                        runningFreq = "";
                                    } else {
                                        runningFreq = "1.0";
                                    }
                                } else {
                                    runningFreq = "1.0";
                                }
                            } else {
                                runningFreq = "<1.0";
                            }
                        } else {
                            runningFreq = "<3.0";
                        }
                    } else {
                        runningFreq = "rare";
                    }
                } else {
                    runningFreq = "frequent";
                }
            } else {
                runningFreq = "infrequent";
            }
        } else {
            runningFreq = "less frequent";
        }

    }

    public void italicsFreqs() {
        String tempFreq;

        for (splicer.count = 0; splicer.count < splicer.goodCount; ++splicer.count) {
            if (splicer.goodWords2[splicer.count].contains("--i")) {
                splicer.goodWords2[splicer.count] = splicer.goodWords2[splicer.count].replace("--i", "");
                tempFreq = splicer.italicsFreq;
                if (tempFreq.isBlank() && !runningFreq.isBlank()) {
                    tempFreq = "unk";
                    if (runningFreq.equals("frequent")) {
                        tempFreq = "freq";
                    } else if (runningFreq.equals("less frequent")) {
                        tempFreq = "less freq";
                    } else if (runningFreq.equals("infrequent")) {
                        tempFreq = "infreq";
                    } else if (runningFreq.equals("rare")) {
                        tempFreq = "rare";
                    } else if (runningFreq.equals("<3.0")) {
                        tempFreq = "<3.0";
                    } else if (runningFreq.equals("<1.0")) {
                        tempFreq = "<1.0";
                    } else if (runningFreq.equals("1.0")) {
                        tempFreq = "1.0";
                    }
                }

                splicer.postProcess.placeIntoFinal(splicer.goodWords2[splicer.count], splicer.sMethod[splicer.count], splicer.sSection[splicer.count], splicer.countSentences + "", tempFreq, "0", "0", "0");
            } else if (!splicer.goodWords2[splicer.count].contains("--i")) {
                splicer.finalMeddraTerms[splicer.finalMeddraCount] = splicer.goodWords2[splicer.count];
                tempFreq = splicer.nonItalicsFreq;
                if (tempFreq.isBlank() && !runningFreq.isBlank()) {
                    tempFreq = "unk";
                    switch (runningFreq) {
                        case "frequent":
                            tempFreq = "freq";
                            break;
                        case "less frequent":
                            tempFreq = "less freq";
                            break;
                        case "infrequent":
                            tempFreq = "infreq";
                            break;
                        case "rare":
                            tempFreq = "rare";
                            break;
                        case "<3.0":
                            tempFreq = "<3.0";
                            break;
                        case "<1.0":
                            tempFreq = "<1.0";
                            break;
                        case "1.0":
                            tempFreq = "1.0";
                            break;
                    }
                }

                splicer.postProcess.placeIntoFinal(splicer.goodWords2[splicer.count], splicer.sMethod[splicer.count], splicer.sSection[splicer.count], splicer.countSentences + "", tempFreq, "0", "0", "0");
            }
        }

    }

    public void textFreqs(String cs) {
        String rareAde = "";
        String frequentAde = "";
        String infrequentAde = "";
        cs = Normals.normal8(cs);
        cs = " " + cs + " ";
        String tempFreq;
        if (!cs.contains(" frequent :") && !cs.contains(" infrequent :") && !cs.contains(" rare :")) {
            if (!cs.contains(" frequent") && !cs.contains(" infrequent") && !cs.contains(" rare")) {
                if (!runningFreq.isBlank()) {
                    tempFreq = "unk";
                    switch (runningFreq) {
                        case "frequent":
                            tempFreq = "freq";
                            break;
                        case "less frequent":
                            tempFreq = "less freq";
                            break;
                        case "infrequent":
                            tempFreq = "infreq";
                            break;
                        case "rare":
                            tempFreq = "rare";
                            break;
                        case "<3.0":
                            tempFreq = "<3.0";
                            break;
                        case "<1.0":
                            tempFreq = "<1.0";
                            break;
                        case "1.0":
                            tempFreq = "1.0";
                            break;
                    }

                    splicer.postProcess.putIntoFinals(tempFreq);
                } else {
                    splicer.postProcess.putIntoFinals("unk");
                }
            } else {
                tempFreq = "unk";
                if (cs.contains(" frequent") && cs.indexOf(" frequent") < cs.length() / 3) {
                    tempFreq = "freq";
                } else if (cs.contains(" infrequent") && cs.indexOf(" infrequent") < cs.length() / 3) {
                    tempFreq = "infreq";
                } else if (cs.contains(" rare") && cs.indexOf(" rare") < cs.length() / 3) {
                    tempFreq = "rare";
                }

                splicer.postProcess.putIntoFinals(tempFreq);
            }
        } else if (cs.contains(";")) {
            splicer.tempArray = cs.split("\\;");

            for (splicer.count = 0; splicer.count < splicer.tempArray.length; ++splicer.count) {
                if (splicer.tempArray[splicer.count].contains(" frequent :")) {
                    frequentAde = splicer.tempArray[splicer.count];
                }

                if (splicer.tempArray[splicer.count].contains(" infrequent :")) {
                    infrequentAde = splicer.tempArray[splicer.count];
                }

                if (splicer.tempArray[splicer.count].contains(" rare :")) {
                    rareAde = splicer.tempArray[splicer.count];
                }
            }

            tempFreq = "unk";

            for (splicer.count = 0; splicer.count < splicer.goodCount; ++splicer.count) {
                splicer.finalMeddraTerms[splicer.finalMeddraCount] = splicer.goodWords2[splicer.count];
                splicer.finalsMethod[splicer.finalMeddraCount] = splicer.sMethod[splicer.count];
                splicer.finalsSection[splicer.finalMeddraCount] = splicer.sSection[splicer.count];
                splicer.sentNumArray[splicer.finalMeddraCount] = splicer.countSentences + "";
                splicer.placebo[splicer.finalMeddraCount] = "0";
                splicer.table[splicer.finalMeddraCount] = "0";
                splicer.ptN[splicer.finalMeddraCount] = "0";
                if (!frequentAde.isBlank() && frequentAde.contains(splicer.goodWords2[splicer.count])) {
                    tempFreq = "freq";
                } else if (!infrequentAde.isBlank() && infrequentAde.contains(splicer.goodWords2[splicer.count])) {
                    tempFreq = "infreq";
                } else if (!rareAde.isBlank() && rareAde.contains(splicer.goodWords2[splicer.count])) {
                    tempFreq = "rare";
                }

                splicer.finalFreq[splicer.finalMeddraCount] = tempFreq;
                ++splicer.finalMeddraCount;
            }
        } else {
            tempFreq = "unk";
            if (cs.contains(" frequent :")) {
                tempFreq = "freq";
            } else if (cs.contains(" infrequent :")) {
                tempFreq = "infreq";
            } else if (cs.contains(" rare :")) {
                tempFreq = "rare";
            }

            splicer.postProcess.putIntoFinals(tempFreq);
        }

    }

    public void getItalicsNumbers() {
        String italicsTemp = splicer.origSent.substring(splicer.origSent.indexOf("italics"));
        if (italicsTemp.contains("%")) {
            String firstPart = italicsTemp.substring(0, italicsTemp.indexOf("%"));
            String secondPart = italicsTemp.substring(italicsTemp.indexOf("%"));
            if (firstPart.contains("italics")) {
                Pattern p;
                Matcher m;
                if (firstPart.contains("&#8805;")) {
                    p = Pattern.compile("[0-9]%");
                    m = p.matcher(italicsTemp);
                    if (m.find()) {
                        splicer.italicsFreq = m.group();
                        splicer.italicsFreq = splicer.italicsFreq.replace("%", "");
                        splicer.italicsFreq = Normals.compress(splicer.italicsFreq);
                        splicer.italicsFreq = splicer.italicsFreq.trim();
                        splicer.italicsFreq = ">" + splicer.italicsFreq;
                    }

                    if (secondPart.contains("&lt;")) {
                        p = Pattern.compile("[0-9]%");
                        m = p.matcher(italicsTemp);
                        if (m.find()) {
                            splicer.nonItalicsFreq = m.group();
                            splicer.nonItalicsFreq = splicer.nonItalicsFreq.replace("%", "");
                            splicer.nonItalicsFreq = Normals.compress(splicer.nonItalicsFreq);
                            splicer.nonItalicsFreq = splicer.nonItalicsFreq.trim();
                            splicer.nonItalicsFreq = "<" + splicer.nonItalicsFreq;
                        }
                    }
                } else if (firstPart.contains("&lt;")) {
                    p = Pattern.compile("[0-9]%");
                    m = p.matcher(italicsTemp);
                    if (m.find()) {
                        splicer.italicsFreq = m.group();
                        splicer.italicsFreq = splicer.italicsFreq.replace("%", "");
                        splicer.italicsFreq = Normals.compress(splicer.italicsFreq);
                        splicer.italicsFreq = splicer.italicsFreq.trim();
                        splicer.italicsFreq = "<" + splicer.italicsFreq;
                    }

                    if (secondPart.contains("&#8805;")) {
                        p = Pattern.compile("[0-9]%");
                        m = p.matcher(italicsTemp);
                        if (m.find()) {
                            splicer.nonItalicsFreq = m.group();
                            splicer.nonItalicsFreq = splicer.nonItalicsFreq.replace("%", "");
                            splicer.nonItalicsFreq = Normals.compress(splicer.nonItalicsFreq);
                            splicer.nonItalicsFreq = splicer.nonItalicsFreq.trim();
                            splicer.nonItalicsFreq = ">" + splicer.nonItalicsFreq;
                        }
                    }
                }
            }
        }

    }
}
