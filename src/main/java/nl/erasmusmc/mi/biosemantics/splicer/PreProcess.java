package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreProcess {
    private static final Logger log = LogManager.getLogger();


    public void getSentences(String extract) {
        log.debug("Getting sentences");
        log.debug("*********Pre SENTS: {}", extract);
        String nextChar = "";
        F5.count = 0;
        F5.maxArraySentence = 0;
        int arrayCount = 1;
        int wordCount = 0;
        extract = extract.replace("<br/>", " <br/> ");
        extract = extract.replace("<p/>", " <p/> ");
        extract = extract.replace("<h3>", " <h3> ");
        extract = extract.replace("<", " <");
        extract = extract.replace("\n", " \n ");
        extract = extract.replace("&", " &");
        String thisWord = "";
        String lastWord = "";
        String currentSent = "";
        String nextWord = "";
        String thisChar = "";
        F5.allWords = extract.split(" ");

        while (wordCount <= F5.allWords.length - 1) {
            thisWord = F5.allWords[wordCount];
            if (thisWord.length() >= 1) {
                thisChar = thisWord.substring(0, 1);
            } else {
                thisChar = "";
            }

            if (!F5.allWords[wordCount].contains(".")) {
                currentSent = lastWord + " " + F5.allWords[wordCount];
                lastWord = currentSent;
                ++wordCount;
            } else if (!F5.allWords[wordCount].endsWith("!") && !F5.allWords[wordCount].endsWith("?")) {
                if (!F5.allWords[wordCount].endsWith(".")) {
                    currentSent = lastWord + " " + F5.allWords[wordCount];
                    lastWord = currentSent;
                    ++wordCount;
                } else if (F5.allWords[wordCount].endsWith(".")) {
                    String regLow = "([a-z])";
                    String regCap = "([A-Z])";
                    String anyNum = "([0-9])";
                    if (wordCount + 1 <= F5.allWords.length - 1) {
                        nextWord = F5.allWords[wordCount + 1];
                        if (nextWord.length() >= 1) {
                            nextChar = nextWord.substring(0, 1);
                        } else {
                            nextChar = "";
                        }
                    }

                    String letterBeforePeriod = "";
                    if (F5.allWords[wordCount].length() >= 3) {
                        letterBeforePeriod = F5.allWords[wordCount].substring(F5.allWords[wordCount].length() - 2, F5.allWords[wordCount].length() - 1);
                    }

                    String thisWordExceptPeriod = F5.allWords[wordCount].substring(0, F5.allWords[wordCount].length() - 1);
                    if (!letterBeforePeriod.equals("'") && !letterBeforePeriod.equals("\"") && !letterBeforePeriod.equals(")") && !letterBeforePeriod.equals("}") && !letterBeforePeriod.equals("]")) {
                        if (thisChar.matches(regCap) && thisWord.length() < 5 && nextChar.matches(regCap)) {
                            currentSent = lastWord + " " + F5.allWords[wordCount];
                            lastWord = currentSent;
                        } else if (thisWordExceptPeriod.contains(".")) {
                            currentSent = lastWord + " " + F5.allWords[wordCount];
                            lastWord = currentSent;
                        } else if (thisChar.matches(regLow) && nextChar.matches(regCap)) {
                            currentSent = lastWord + " " + F5.allWords[wordCount];
                            F5.sent[arrayCount] = currentSent;
                            ++arrayCount;
                            lastWord = "";
                        } else if (thisWord.length() < 2) {
                            currentSent = lastWord + " " + F5.allWords[wordCount];
                            lastWord = currentSent;
                        } else if (thisWord.matches(anyNum) && nextChar.matches(anyNum)) {
                            currentSent = lastWord + " " + F5.allWords[wordCount];
                            lastWord = currentSent;
                        } else {
                            currentSent = lastWord + " " + F5.allWords[wordCount];
                            F5.sent[arrayCount] = currentSent;
                            ++arrayCount;
                            lastWord = "";
                        }
                    } else {
                        currentSent = lastWord + " " + F5.allWords[wordCount];
                        F5.sent[arrayCount] = currentSent;
                        ++arrayCount;
                        lastWord = "";
                    }

                    ++wordCount;
                }
            } else {
                currentSent = lastWord + " " + F5.allWords[wordCount];
                F5.sent[arrayCount] = currentSent;
                ++arrayCount;
                lastWord = "";
                ++wordCount;
            }
        }

        if (lastWord.length() > 3) {
            F5.sent[arrayCount] = lastWord;
            F5.maxArraySentence = arrayCount;
        } else {
            F5.maxArraySentence = arrayCount - 1;
        }

        F5.totalReportSentences += F5.maxArraySentence;
    }

}
