package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostProcess {
    private static final Logger log = LogManager.getLogger();
    private final Splicer splicer;


    public PostProcess(Splicer splicer) {
        this.splicer = splicer;
    }


    public void placeIntoFinal(String tr, Method ssm, String ssec, String sentNum, String f, String p, String t, String pn) {
        tr = tr.replaceAll("\\*", "");
        tr = Normals.compress(tr);
        tr = tr.trim();
        if (p != null) {
            p = p.trim();
        } else {
            p = "-1";
        }

        f = f.trim();
        splicer.finalsMethod[splicer.finalMeddraCount] = ssm;
        splicer.finalsSection[splicer.finalMeddraCount] = ssec;
        splicer.sentNumArray[splicer.finalMeddraCount] = sentNum;
        splicer.finalMeddraTerms[splicer.finalMeddraCount] = tr;
        splicer.finalFreq[splicer.finalMeddraCount] = f;
        splicer.placebo[splicer.finalMeddraCount] = p;
        splicer.table[splicer.finalMeddraCount] = t;
        splicer.ptN[splicer.finalMeddraCount] = pn;
        ++splicer.finalMeddraCount;
    }

    public void putIntoFinals(String thisFrequency) {
        splicer.count = 0;
        if (thisFrequency.isBlank()) {
            thisFrequency = "unk";
        }

        while (splicer.count < splicer.goodCount) {
            splicer.goodWords2[splicer.count] = splicer.goodWords2[splicer.count].replaceAll("\\*", "");
            splicer.goodWords2[splicer.count] = Normals.compress(splicer.goodWords2[splicer.count]);
            splicer.goodWords2[splicer.count] = splicer.goodWords2[splicer.count].trim();
            splicer.finalMeddraTerms[splicer.finalMeddraCount] = splicer.goodWords2[splicer.count];
            splicer.finalsMethod[splicer.finalMeddraCount] = splicer.sMethod[splicer.count];
            splicer.finalsSection[splicer.finalMeddraCount] = splicer.sSection[splicer.count];
            splicer.sentNumArray[splicer.finalMeddraCount] = splicer.countSentences + "";
            splicer.finalFreq[splicer.finalMeddraCount] = thisFrequency;
            splicer.placebo[splicer.finalMeddraCount] = "-1";
            splicer.table[splicer.finalMeddraCount] = "0";
            splicer.ptN[splicer.finalMeddraCount] = "0";
            log.debug("INTO FINALS: " + splicer.finalMeddraTerms[splicer.finalMeddraCount] + "    " + splicer.finalFreq[splicer.finalMeddraCount] + "   " + splicer.finalsMethod[splicer.finalMeddraCount]);
            ++splicer.finalMeddraCount;
            ++splicer.count;
        }

    }
}
