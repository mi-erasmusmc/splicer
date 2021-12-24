package nl.erasmusmc.mi.biosemantics.splicer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Similar {

    private Similar() {
    }


    public static double compareStrings(String str1, String str2) {
        List<String> pairs1 = wordLetterPairs(str1.toUpperCase());
        List<String> pairs2 = wordLetterPairs(str2.toUpperCase());
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();

        for (Object pair1 : pairs1) {
            for (int j = 0; j < pairs2.size(); ++j) {
                Object pair2 = pairs2.get(j);
                if (pair1.equals(pair2)) {
                    ++intersection;
                    pairs2.remove(j);
                    break;
                }
            }
        }

        return 2.0D * intersection / union;
    }

    private static List<String> wordLetterPairs(String str) {
        List<String> allPairs = new ArrayList<>();
        String[] words = str.split(" ");

        for (String word : words) {
            String[] pairsInWord = letterPairs(word);
            Collections.addAll(allPairs, pairsInWord);
        }

        return allPairs;
    }

    private static String[] letterPairs(String str) {
        int numPairs = str.length() == 0 ? 0 : str.length() - 1;
        String[] pairs = new String[numPairs];

        for (int i = 0; i < numPairs; ++i) {
            pairs[i] = str.substring(i, i + 2);
        }

        return pairs;
    }

}
