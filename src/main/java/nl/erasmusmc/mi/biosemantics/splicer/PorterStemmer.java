package nl.erasmusmc.mi.biosemantics.splicer;

public class PorterStemmer {

    private PorterStemmer() {
    }


    public static String portStem(String ts) {
        if (ts.length() > 1) {
            return stem(ts.toLowerCase());
        } else {
            return "Invalid term";
        }
    }

    private static String stem(String str) {
        if (str.length() > 0) {
            char[] c = str.toCharArray();

            for (char value : c) {
                if (!Character.isLetter(value)) {
                    return "Invalid term";
                }
            }

            str = step1a(str);
            str = step1b(str);
            str = step1c(str);
            str = step2(str);
            str = step3(str);
            str = step4(str);
            str = step5a(str);
            str = step5b(str);
            return str;
        } else {
            return "No term entered";
        }
    }

    private static String step1a(String str) {
        if (str.length() > 1) {
            final String substring = str.substring(0, str.length() - 2);
            if (str.endsWith("sses")) {
                return substring;
            } else if (str.endsWith("ies")) {
                return substring;
            } else if (str.endsWith("ss")) {
                return str;
            } else {
                return str.endsWith("s") ? str.substring(0, str.length() - 1) : str;
            }
        } else {
            return str;
        }
    }

    private static String step1b(String str) {
        if (str.endsWith("eed")) {
            return stringMeasure(str.substring(0, str.length() - 3)) > 0 ? str.substring(0, str.length() - 1) : str;
        } else if (str.endsWith("ed") && containsVowel(str.substring(0, str.length() - 2))) {
            return step1b2(str.substring(0, str.length() - 2));
        } else {
            return str.endsWith("ing") && containsVowel(str.substring(0, str.length() - 3)) ? step1b2(str.substring(0, str.length() - 3)) : str;
        }
    }

    private static String step1b2(String str) {
        if (str.length() > 1) {
            if (!str.endsWith("at") && !str.endsWith("bl") && !str.endsWith("iz")) {
                if (endsWithDoubleConsonant(str) && !str.endsWith("l") && !str.endsWith("s") && !str.endsWith("z")) {
                    return str.substring(0, str.length() - 1);
                } else {
                    return stringMeasure(str) == 1 && endsWithCVC(str) ? str + "e" : str;
                }
            } else {
                return str + "e";
            }
        } else {
            return str;
        }
    }

    private static String step1c(String str) {
        if (str.length() > 1) {
            return str.endsWith("y") && containsVowel(str.substring(0, str.length() - 1)) ? str.substring(0, str.length() - 1) + "i" : str;
        } else {
            return str;
        }
    }

    private static String step2(String str) {
        if (str.endsWith("ational") && stringMeasure(str.substring(0, str.length() - 5)) > 0) {
            return str.substring(0, str.length() - 5) + "e";
        } else {
            if (str.endsWith("tional") && stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("enci") && stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("anci") && stringMeasure(str.substring(0, str.length() - 1)) > 0) {
                return str.substring(0, str.length() - 1) + "e";
            } else if (str.endsWith("izer") && stringMeasure(str.substring(0, str.length() - 1)) > 0) {
                return str.substring(0, str.length() - 1);
            } else if (str.endsWith("abli") && stringMeasure(str.substring(0, str.length() - 1)) > 0) {
                return str.substring(0, str.length() - 1) + "e";
            } else if (str.endsWith("alli") && stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("entli") && stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("eli") && stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("ousli") && stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("ization") && stringMeasure(str.substring(0, str.length() - 5)) > 0) {
                return str.substring(0, str.length() - 5) + "e";
            } else if (str.endsWith("ation") && stringMeasure(str.substring(0, str.length() - 3)) > 0) {
                return str.substring(0, str.length() - 3) + "e";
            } else if (str.endsWith("ator") && stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2) + "e";
            } else if (str.endsWith("alism") && stringMeasure(str.substring(0, str.length() - 3)) > 0) {
                return str.substring(0, str.length() - 3);
            } else if (str.endsWith("iveness") && stringMeasure(str.substring(0, str.length() - 4)) > 0) {
                return str.substring(0, str.length() - 4);
            } else if (str.endsWith("fulness") && stringMeasure(str.substring(0, str.length() - 4)) > 0) {
                return str.substring(0, str.length() - 4);
            } else if (str.endsWith("ousness") && stringMeasure(str.substring(0, str.length() - 4)) > 0) {
                return str.substring(0, str.length() - 4);
            } else if (str.endsWith("aliti") && stringMeasure(str.substring(0, str.length() - 3)) > 0) {
                return str.substring(0, str.length() - 3);
            } else if (str.endsWith("iviti") && stringMeasure(str.substring(0, str.length() - 3)) > 0) {
                return str.substring(0, str.length() - 3) + "e";
            } else {
                return str.endsWith("biliti") && stringMeasure(str.substring(0, str.length() - 5)) > 0 ? str.substring(0, str.length() - 5) + "le" : str;
            }
        }
    }

    private static String step3(String str) {
        if (str.endsWith("icate") && stringMeasure(str.substring(0, str.length() - 3)) > 0) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ative") && stringMeasure(str.substring(0, str.length() - 5)) > 0) {
            return str.substring(0, str.length() - 5);
        } else if (str.endsWith("alize") && stringMeasure(str.substring(0, str.length() - 3)) > 0) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("iciti") && stringMeasure(str.substring(0, str.length() - 3)) > 0) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ical") && stringMeasure(str.substring(0, str.length() - 2)) > 0) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("ful") && stringMeasure(str.substring(0, str.length() - 3)) > 0) {
            return str.substring(0, str.length() - 3);
        } else {
            return str.endsWith("ness") && stringMeasure(str.substring(0, str.length() - 4)) > 0 ? str.substring(0, str.length() - 4) : str;
        }
    }

    private static String step4(String str) {
        if (str.endsWith("al") && stringMeasure(str.substring(0, str.length() - 2)) > 1) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("ance") && stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("ence") && stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("er") && stringMeasure(str.substring(0, str.length() - 2)) > 1) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("ic") && stringMeasure(str.substring(0, str.length() - 2)) > 1) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("able") && stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("ible") && stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("ant") && stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ement") && stringMeasure(str.substring(0, str.length() - 5)) > 1) {
            return str.substring(0, str.length() - 5);
        } else if (str.endsWith("ment") && stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("ent") && stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if ((str.endsWith("sion") || str.endsWith("tion")) && stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ou") && stringMeasure(str.substring(0, str.length() - 2)) > 1) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("ism") && stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ate") && stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("iti") && stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ous") && stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ive") && stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else {
            return str.endsWith("ize") && stringMeasure(str.substring(0, str.length() - 3)) > 1 ? str.substring(0, str.length() - 3) : str;
        }
    }

    private static String step5a(String str) {
        if (str.length() > 1) {
            if (stringMeasure(str.substring(0, str.length() - 1)) > 1 && str.endsWith("e")) {
                return str.substring(0, str.length() - 1);
            } else {
                return stringMeasure(str.substring(0, str.length() - 1)) == 1 && !endsWithCVC(str.substring(0, str.length() - 1)) && str.endsWith("e") ? str.substring(0, str.length() - 1) : str;
            }
        } else {
            return str;
        }
    }

    private static String step5b(String str) {
        if (str.length() > 1) {
            return str.endsWith("l") && endsWithDoubleConsonant(str) && stringMeasure(str.substring(0, str.length() - 1)) > 1 ? str.substring(0, str.length() - 1) : str;
        } else {
            return str;
        }
    }

    private static boolean containsVowel(String str) {
        char[] strchars = str.toCharArray();

        for (char strchar : strchars) {
            if (isVowel(strchar)) {
                return true;
            }
        }

        return str.indexOf(121) > -1;
    }

    private static boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    private static boolean endsWithDoubleConsonant(String str) {
        char c = str.charAt(str.length() - 1);
        return c == str.charAt(str.length() - 2) && !containsVowel(str.substring(str.length() - 2));
    }

    private static int stringMeasure(String str) {
        int count = 0;
        boolean vowelSeen = false;
        char[] strchars = str.toCharArray();

        for (char strchar : strchars) {
            if (isVowel(strchar)) {
                vowelSeen = true;
            } else if (vowelSeen) {
                ++count;
                vowelSeen = false;
            }
        }

        return count;
    }

    private static boolean endsWithCVC(String str) {
        if (str.length() >= 3) {
            char c = str.charAt(str.length() - 1);
            char v = str.charAt(str.length() - 2);
            char c2 = str.charAt(str.length() - 3);
            if (c != 'w' && c != 'x' && c != 'y') {
                if (isVowel(c)) {
                    return false;
                } else if (!isVowel(v)) {
                    return false;
                } else {
                    return !isVowel(c2);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
