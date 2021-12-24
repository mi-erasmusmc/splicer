package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Numbers {


    public static void findFrequencies() {
        int numPercents = StringUtils.countMatches(F5.currentSentence, "%");
        if (numPercents == 0) {
            if (F5.italicsFlag && !F5.origSent.contains(":")) {
                italicsFreqs();
            } else {
                textFreqs(F5.origSent);
            }
        }

        if (numPercents == 1) {
            Pattern p = Pattern.compile("\\>?\\s?\\=?\\s?([0-9]+)?\\s?\\.?\\s?([0-9]+)?\\.?([0-9]+)?(\\s+)?%");
            Matcher m = p.matcher(F5.currentSentence);

            while (m.find()) {
                String tempFreq = m.group();
                tempFreq = tempFreq.replace("%", "");
                tempFreq = tempFreq.replace(" ", "");
                tempFreq = F5.compress(tempFreq);
                tempFreq = tempFreq.trim();
                PostProcess.putIntoFinals(tempFreq);
            }
        }

        if (numPercents >= 2) {
            String lowerCaseSent = F5.currentSentence.toLowerCase();
            F5.skipSent = true;
            F5.skipSent = determineDrugOrderText(lowerCaseSent);
            Matcher m;
            int perCount;
            String tempFreq;
            String ade;
            Pattern p;
            if (!F5.skipSent) {
                for (F5.count = 0; F5.count < F5.goodCount; ++F5.count) {

                    if (lowerCaseSent.contains(F5.goodWords2[F5.count])) {
                        ade = lowerCaseSent.substring(lowerCaseSent.indexOf(F5.goodWords2[F5.count]));
                    } else {
                        ade = lowerCaseSent;
                    }

                    p = Pattern.compile("\\>?\\s?\\=?\\s?([0-9]+)?\\s?\\.?\\s?([0-9]+)?\\.?([0-9]+)?(\\s+)?%");
                    m = p.matcher(ade);
                    perCount = 0;

                    while (m.find()) {
                        ++perCount;
                        if (F5.drugFirst && perCount == 1) {
                            tempFreq = m.group();
                            tempFreq = tempFreq.replace("%", "");
                            tempFreq = tempFreq.replace(" ", "");
                            tempFreq = F5.compress(tempFreq);
                            tempFreq = tempFreq.trim();
                            PostProcess.placeIntoFinal(F5.goodWords2[F5.count], F5.sMethod[F5.count], F5.sSection[F5.count], F5.countSentences + "", tempFreq, "0", "0", "0");
                            break;
                        }

                        if (!F5.drugFirst && perCount == 2) {
                            tempFreq = m.group();
                            tempFreq = tempFreq.replace("%", "");
                            tempFreq = tempFreq.replace(" ", "");
                            tempFreq = F5.compress(tempFreq);
                            tempFreq = tempFreq.trim();
                            PostProcess.placeIntoFinal(F5.goodWords2[F5.count], F5.sMethod[F5.count], F5.sSection[F5.count], F5.countSentences + "", tempFreq, "0", "0", "0");
                            break;
                        }
                    }

                    if (perCount == 0 && F5.sMethod[F5.count].contains("M1")) {
                        PostProcess.placeIntoFinal(F5.goodWords2[F5.count], F5.sMethod[F5.count], F5.sSection[F5.count], F5.countSentences + "", "unk", "0", "0", "0");
                    }
                }
            } else {
                for (F5.count = 0; F5.count < F5.goodCount; ++F5.count) {

                    if (lowerCaseSent.contains(F5.goodWords2[F5.count])) {
                        ade = lowerCaseSent.substring(lowerCaseSent.indexOf(F5.goodWords2[F5.count]));
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
                        PostProcess.placeIntoFinal(F5.goodWords2[F5.count], F5.sMethod[F5.count], F5.sSection[F5.count], F5.countSentences + "", tempFreq, "0", "0", "0");
                        break;
                    }

                    if (perCount == 0) {
                        PostProcess.placeIntoFinal(F5.goodWords2[F5.count], F5.sMethod[F5.count], F5.sSection[F5.count], F5.countSentences + "", "unk", "0", "0", "0");
                    }
                }
            }
        }

    }

    public static boolean determineDrugOrderText(String wt) {
        boolean st = true;
        if (wt.contains(F5.genDrugName)) {
            if (wt.contains("placebo")) {
                st = false;
                if (wt.indexOf(F5.genDrugName) < wt.indexOf("placebo")) {
                    F5.drugFirst = true;
                }

                if (wt.indexOf(F5.genDrugName) > wt.indexOf("placebo")) {
                    F5.drugFirst = false;
                }
            } else if (wt.contains("comparator")) {
                st = false;
                if (wt.indexOf(F5.genDrugName) < wt.indexOf("comparator")) {
                    F5.drugFirst = true;
                }

                if (wt.indexOf(F5.genDrugName) > wt.indexOf("comparator")) {
                    F5.drugFirst = false;
                }
            }
        } else if (wt.contains(F5.tradeDrugName)) {
            if (wt.contains("placebo")) {
                st = false;
                if (wt.indexOf(F5.tradeDrugName) < wt.indexOf("placebo")) {
                    F5.drugFirst = true;
                }

                if (wt.indexOf(F5.tradeDrugName) > wt.indexOf("placebo")) {
                    F5.drugFirst = false;
                }
            } else if (wt.contains("comparator")) {
                st = false;
                if (wt.indexOf(F5.tradeDrugName) < wt.indexOf("comparator")) {
                    F5.drugFirst = true;
                }

                if (wt.indexOf(F5.tradeDrugName) > wt.indexOf("comparator")) {
                    F5.drugFirst = false;
                }
            }
        }

        return st;
    }

    public static void getRunningFreq(String cs) {
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
                                        F5.runningFreq = "";
                                    } else {
                                        F5.runningFreq = "1.0";
                                    }
                                } else {
                                    F5.runningFreq = "1.0";
                                }
                            } else {
                                F5.runningFreq = "<1.0";
                            }
                        } else {
                            F5.runningFreq = "<3.0";
                        }
                    } else {
                        F5.runningFreq = "rare";
                    }
                } else {
                    F5.runningFreq = "frequent";
                }
            } else {
                F5.runningFreq = "infrequent";
            }
        } else {
            F5.runningFreq = "less frequent";
        }

    }

    public static void italicsFreqs() {
        String tempFreq;

        for (F5.count = 0; F5.count < F5.goodCount; ++F5.count) {
            if (F5.goodWords2[F5.count].contains("--i")) {
                F5.goodWords2[F5.count] = F5.goodWords2[F5.count].replace("--i", "");
                tempFreq = F5.italicsFreq;
                if (tempFreq.equals("") && !F5.runningFreq.equals("")) {
                    tempFreq = "unk";
                    if (F5.runningFreq.equals("frequent")) {
                        tempFreq = "freq";
                    } else if (F5.runningFreq.equals("less frequent")) {
                        tempFreq = "less freq";
                    } else if (F5.runningFreq.equals("infrequent")) {
                        tempFreq = "infreq";
                    } else if (F5.runningFreq.equals("rare")) {
                        tempFreq = "rare";
                    } else if (F5.runningFreq.equals("<3.0")) {
                        tempFreq = "<3.0";
                    } else if (F5.runningFreq.equals("<1.0")) {
                        tempFreq = "<1.0";
                    } else if (F5.runningFreq.equals("1.0")) {
                        tempFreq = "1.0";
                    }
                }

                PostProcess.placeIntoFinal(F5.goodWords2[F5.count], F5.sMethod[F5.count], F5.sSection[F5.count], F5.countSentences + "", tempFreq, "0", "0", "0");
            } else if (!F5.goodWords2[F5.count].contains("--i")) {
                F5.finalMedraTerms[F5.finalMedraCount] = F5.goodWords2[F5.count];
                tempFreq = F5.nonItalicsFreq;
                if (tempFreq.equals("") && !F5.runningFreq.equals("")) {
                    tempFreq = "unk";
                    switch (F5.runningFreq) {
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

                PostProcess.placeIntoFinal(F5.goodWords2[F5.count], F5.sMethod[F5.count], F5.sSection[F5.count], F5.countSentences + "", tempFreq, "0", "0", "0");
            }
        }

    }

    public static void textFreqs(String cs) {
        String rareAde = "";
        String frequentAde = "";
        String infrequentAde = "";
        cs = Normals.normal8(cs);
        cs = " " + cs + " ";
        String tempFreq;
        if (!cs.contains(" frequent :") && !cs.contains(" infrequent :") && !cs.contains(" rare :")) {
            if (!cs.contains(" frequent") && !cs.contains(" infrequent") && !cs.contains(" rare")) {
                if (!F5.runningFreq.equals("")) {
                    tempFreq = "unk";
                    switch (F5.runningFreq) {
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

                    PostProcess.putIntoFinals(tempFreq);
                } else {
                    PostProcess.putIntoFinals("unk");
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

                PostProcess.putIntoFinals(tempFreq);
            }
        } else if (cs.contains(";")) {
            F5.tempArray = cs.split("\\;");

            for (F5.count = 0; F5.count < F5.tempArray.length; ++F5.count) {
                if (F5.tempArray[F5.count].contains(" frequent :")) {
                    frequentAde = F5.tempArray[F5.count];
                }

                if (F5.tempArray[F5.count].contains(" infrequent :")) {
                    infrequentAde = F5.tempArray[F5.count];
                }

                if (F5.tempArray[F5.count].contains(" rare :")) {
                    rareAde = F5.tempArray[F5.count];
                }
            }

            tempFreq = "unk";

            for (F5.count = 0; F5.count < F5.goodCount; ++F5.count) {
                F5.finalMedraTerms[F5.finalMedraCount] = F5.goodWords2[F5.count];
                F5.finalsMethod[F5.finalMedraCount] = F5.sMethod[F5.count];
                F5.finalsSection[F5.finalMedraCount] = F5.sSection[F5.count];
                F5.sentNumArray[F5.finalMedraCount] = F5.countSentences + "";
                F5.placebo[F5.finalMedraCount] = "0";
                F5.table[F5.finalMedraCount] = "0";
                F5.ptN[F5.finalMedraCount] = "0";
                if (!frequentAde.equals("") && frequentAde.contains(F5.goodWords2[F5.count])) {
                    tempFreq = "freq";
                } else if (!infrequentAde.equals("") && infrequentAde.contains(F5.goodWords2[F5.count])) {
                    tempFreq = "infreq";
                } else if (!rareAde.equals("") && rareAde.contains(F5.goodWords2[F5.count])) {
                    tempFreq = "rare";
                }

                F5.finalFreq[F5.finalMedraCount] = tempFreq;
                ++F5.finalMedraCount;
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

            PostProcess.putIntoFinals(tempFreq);
        }

    }

    public void getItalicsNumbers() {
        String italicsTemp = F5.origSent.substring(F5.origSent.indexOf("italics"));
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
                        F5.italicsFreq = m.group();
                        F5.italicsFreq = F5.italicsFreq.replace("%", "");
                        F5.italicsFreq = F5.compress(F5.italicsFreq);
                        F5.italicsFreq = F5.italicsFreq.trim();
                        F5.italicsFreq = ">" + F5.italicsFreq;
                    }

                    if (secondPart.contains("&lt;")) {
                        p = Pattern.compile("[0-9]%");
                        m = p.matcher(italicsTemp);
                        if (m.find()) {
                            F5.nonItalicsFreq = m.group();
                            F5.nonItalicsFreq = F5.nonItalicsFreq.replace("%", "");
                            F5.nonItalicsFreq = F5.compress(F5.nonItalicsFreq);
                            F5.nonItalicsFreq = F5.nonItalicsFreq.trim();
                            F5.nonItalicsFreq = "<" + F5.nonItalicsFreq;
                        }
                    }
                } else if (firstPart.contains("&lt;")) {
                    p = Pattern.compile("[0-9]%");
                    m = p.matcher(italicsTemp);
                    if (m.find()) {
                        F5.italicsFreq = m.group();
                        F5.italicsFreq = F5.italicsFreq.replace("%", "");
                        F5.italicsFreq = F5.compress(F5.italicsFreq);
                        F5.italicsFreq = F5.italicsFreq.trim();
                        F5.italicsFreq = "<" + F5.italicsFreq;
                    }

                    if (secondPart.contains("&#8805;")) {
                        p = Pattern.compile("[0-9]%");
                        m = p.matcher(italicsTemp);
                        if (m.find()) {
                            F5.nonItalicsFreq = m.group();
                            F5.nonItalicsFreq = F5.nonItalicsFreq.replace("%", "");
                            F5.nonItalicsFreq = F5.compress(F5.nonItalicsFreq);
                            F5.nonItalicsFreq = F5.nonItalicsFreq.trim();
                            F5.nonItalicsFreq = ">" + F5.nonItalicsFreq;
                        }
                    }
                }
            }
        }

    }
}
