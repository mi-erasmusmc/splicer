package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class PreProcess {
    private static final Logger log = LogManager.getLogger();
    private final Splicer splicer;

    public PreProcess(Splicer splicer) {
        this.splicer = splicer;
    }


    public void getSentences(String extract) {
        log.debug("Getting sentences");
        log.debug("*********Pre SENTS: {}", extract);
        String nextChar = "";
        splicer.count = 0;
        int arrayCount = 1;
        int wordCount = 0;
        extract = extract.replace("<br/>", " <br/> ");
        extract = extract.replace("<p/>", " <p/> ");
        extract = extract.replace("<h3>", " <h3> ");
        extract = extract.replace("<", " <");
        extract = extract.replace("\n", " \n ");
        extract = extract.replace("&", " &");
        String thisWord;
        String lastWord = "";
        String currentSent;
        String nextWord;
        String thisChar;
        splicer.allWords = Arrays.stream(extract.split(" ")).map(Word::new).toArray(Word[]::new);


        while (wordCount <= splicer.allWords.length - 1) {
            thisWord = splicer.allWords[wordCount].word;
            if (thisWord.length() >= 1) {
                thisChar = thisWord.substring(0, 1);
            } else {
                thisChar = "";
            }

            if (!splicer.allWords[wordCount].word.contains(".")) {
                currentSent = lastWord + " " + splicer.allWords[wordCount].word;
                lastWord = currentSent;
                ++wordCount;
            } else if (!splicer.allWords[wordCount].word.endsWith("!") && !splicer.allWords[wordCount].word.endsWith("?")) {
                if (!splicer.allWords[wordCount].word.endsWith(".")) {
                    currentSent = lastWord + " " + splicer.allWords[wordCount];
                    lastWord = currentSent;
                    ++wordCount;
                } else if (splicer.allWords[wordCount].word.endsWith(".")) {
                    String regLow = "([a-z])";
                    String regCap = "([A-Z])";
                    String anyNum = "([0-9])";
                    if (wordCount + 1 <= splicer.allWords.length - 1) {
                        nextWord = splicer.allWords[wordCount + 1].word;
                        if (nextWord.length() >= 1) {
                            nextChar = nextWord.substring(0, 1);
                        } else {
                            nextChar = "";
                        }
                    }

                    String letterBeforePeriod = "";
                    if (splicer.allWords[wordCount].word.length() >= 3) {
                        letterBeforePeriod = splicer.allWords[wordCount].word.substring(splicer.allWords[wordCount].word.length() - 2, splicer.allWords[wordCount].word.length() - 1);
                    }

                    String thisWordExceptPeriod = splicer.allWords[wordCount].word.substring(0, splicer.allWords[wordCount].word.length() - 1);
                    if (!letterBeforePeriod.equals("'") && !letterBeforePeriod.equals("\"") && !letterBeforePeriod.equals(")") && !letterBeforePeriod.equals("}") && !letterBeforePeriod.equals("]")) {
                        if (thisChar.matches(regCap) && thisWord.length() < 5 && nextChar.matches(regCap)) {
                            currentSent = lastWord + " " + splicer.allWords[wordCount].word;
                            lastWord = currentSent;
                        } else if (thisWordExceptPeriod.contains(".")) {
                            currentSent = lastWord + " " + splicer.allWords[wordCount].word;
                            lastWord = currentSent;
                        } else if (thisChar.matches(regLow) && nextChar.matches(regCap)) {
                            currentSent = lastWord + " " + splicer.allWords[wordCount].word;
                            splicer.sent[arrayCount] = currentSent;
                            ++arrayCount;
                            lastWord = "";
                        } else if (thisWord.length() < 2) {
                            currentSent = lastWord + " " + splicer.allWords[wordCount].word;
                            lastWord = currentSent;
                        } else if (thisWord.matches(anyNum) && nextChar.matches(anyNum)) {
                            currentSent = lastWord + " " + splicer.allWords[wordCount].word;
                            lastWord = currentSent;
                        } else {
                            currentSent = lastWord + " " + splicer.allWords[wordCount].word;
                            splicer.sent[arrayCount] = currentSent;
                            ++arrayCount;
                            lastWord = "";
                        }
                    } else {
                        currentSent = lastWord + " " + splicer.allWords[wordCount].word;
                        splicer.sent[arrayCount] = currentSent;
                        ++arrayCount;
                        lastWord = "";
                    }

                    ++wordCount;
                }
            } else {
                currentSent = lastWord + " " + splicer.allWords[wordCount].word;
                splicer.sent[arrayCount] = currentSent;
                ++arrayCount;
                lastWord = "";
                ++wordCount;
            }
        }

        if (lastWord.length() > 3) {
            splicer.sent[arrayCount] = lastWord;
            splicer.maxArraySentence = arrayCount;
        } else {
            splicer.maxArraySentence = arrayCount - 1;
        }
    }

}
