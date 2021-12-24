package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostProcess {
    private static final Logger log = LogManager.getLogger();
    static String test2 = "";


    private PostProcess() {
    }


    public static void placeIntoFinal(String tr, String ssm, String ssec, String sentNum, String f, String p, String t, String pn) {
        tr = tr.replaceAll("\\*", "");
        tr = F5.compress(tr);
        tr = tr.trim();
        if (p != null) {
            p = p.trim();
        } else {
            p = "-1";
        }

        f = f.trim();
        F5.finalsMethod[F5.finalMedraCount] = ssm;
        F5.finalsSection[F5.finalMedraCount] = ssec;
        F5.sentNumArray[F5.finalMedraCount] = sentNum;
        F5.finalMedraTerms[F5.finalMedraCount] = tr;
        F5.finalFreq[F5.finalMedraCount] = f;
        F5.placebo[F5.finalMedraCount] = p;
        F5.table[F5.finalMedraCount] = t;
        F5.ptN[F5.finalMedraCount] = pn;
        ++F5.finalMedraCount;
    }

    public static void putIntoFinals(String thisFrequency) {
        F5.count = 0;
        if (thisFrequency.equals("")) {
            thisFrequency = "unk";
        }

        while (F5.count < F5.goodCount) {
            F5.goodWords2[F5.count] = F5.goodWords2[F5.count].replaceAll("\\*", "");
            F5.goodWords2[F5.count] = F5.compress(F5.goodWords2[F5.count]);
            F5.goodWords2[F5.count] = F5.goodWords2[F5.count].trim();
            F5.finalMedraTerms[F5.finalMedraCount] = F5.goodWords2[F5.count];
            F5.finalsMethod[F5.finalMedraCount] = F5.sMethod[F5.count];
            F5.finalsSection[F5.finalMedraCount] = F5.sSection[F5.count];
            F5.sentNumArray[F5.finalMedraCount] = F5.countSentences + "";
            F5.finalFreq[F5.finalMedraCount] = thisFrequency;
            F5.placebo[F5.finalMedraCount] = "-1";
            F5.table[F5.finalMedraCount] = "0";
            F5.ptN[F5.finalMedraCount] = "0";
            log.debug("INTO FINALS: " + F5.finalMedraTerms[F5.finalMedraCount] + "    " + F5.finalFreq[F5.finalMedraCount] + "   " + F5.finalsMethod[F5.finalMedraCount]);
            ++F5.finalMedraCount;
            ++F5.count;
        }

    }
}
