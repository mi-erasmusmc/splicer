package nl.erasmusmc.mi.biosemantics.splicer;

public class PorterStemmer {


    public String stem(String str) {
        if (str.length() > 0) {
            char[] c = str.toCharArray();

            for (char value : c) {
                if (!Character.isLetter(value)) {
                    return "Invalid term";
                }
            }

            str = this.step1a(str);
            str = this.step1b(str);
            str = this.step1c(str);
            str = this.step2(str);
            str = this.step3(str);
            str = this.step4(str);
            str = this.step5a(str);
            str = this.step5b(str);
            return str;
        } else {
            return "No term entered";
        }
    }

    protected String step1a(String str) {
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

    protected String step1b(String str) {
        if (str.endsWith("eed")) {
            return this.stringMeasure(str.substring(0, str.length() - 3)) > 0 ? str.substring(0, str.length() - 1) : str;
        } else if (str.endsWith("ed") && this.containsVowel(str.substring(0, str.length() - 2))) {
            return this.step1b2(str.substring(0, str.length() - 2));
        } else {
            return str.endsWith("ing") && this.containsVowel(str.substring(0, str.length() - 3)) ? this.step1b2(str.substring(0, str.length() - 3)) : str;
        }
    }

    protected String step1b2(String str) {
        if (str.length() > 1) {
            if (!str.endsWith("at") && !str.endsWith("bl") && !str.endsWith("iz")) {
                if (this.endsWithDoubleConsonant(str) && !str.endsWith("l") && !str.endsWith("s") && !str.endsWith("z")) {
                    return str.substring(0, str.length() - 1);
                } else {
                    return this.stringMeasure(str) == 1 && this.endsWithCVC(str) ? str + "e" : str;
                }
            } else {
                return str + "e";
            }
        } else {
            return str;
        }
    }

    protected String step1c(String str) {
        if (str.length() > 1) {
            return str.endsWith("y") && this.containsVowel(str.substring(0, str.length() - 1)) ? str.substring(0, str.length() - 1) + "i" : str;
        } else {
            return str;
        }
    }

    protected String step2(String str) {
        if (str.endsWith("ational") && this.stringMeasure(str.substring(0, str.length() - 5)) > 0) {
            return str.substring(0, str.length() - 5) + "e";
        } else {
            if (str.endsWith("tional") && this.stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("enci") && this.stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("anci") && this.stringMeasure(str.substring(0, str.length() - 1)) > 0) {
                return str.substring(0, str.length() - 1) + "e";
            } else if (str.endsWith("izer") && this.stringMeasure(str.substring(0, str.length() - 1)) > 0) {
                return str.substring(0, str.length() - 1);
            } else if (str.endsWith("abli") && this.stringMeasure(str.substring(0, str.length() - 1)) > 0) {
                return str.substring(0, str.length() - 1) + "e";
            } else if (str.endsWith("alli") && this.stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("entli") && this.stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("eli") && this.stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("ousli") && this.stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2);
            } else if (str.endsWith("ization") && this.stringMeasure(str.substring(0, str.length() - 5)) > 0) {
                return str.substring(0, str.length() - 5) + "e";
            } else if (str.endsWith("ation") && this.stringMeasure(str.substring(0, str.length() - 3)) > 0) {
                return str.substring(0, str.length() - 3) + "e";
            } else if (str.endsWith("ator") && this.stringMeasure(str.substring(0, str.length() - 2)) > 0) {
                return str.substring(0, str.length() - 2) + "e";
            } else if (str.endsWith("alism") && this.stringMeasure(str.substring(0, str.length() - 3)) > 0) {
                return str.substring(0, str.length() - 3);
            } else if (str.endsWith("iveness") && this.stringMeasure(str.substring(0, str.length() - 4)) > 0) {
                return str.substring(0, str.length() - 4);
            } else if (str.endsWith("fulness") && this.stringMeasure(str.substring(0, str.length() - 4)) > 0) {
                return str.substring(0, str.length() - 4);
            } else if (str.endsWith("ousness") && this.stringMeasure(str.substring(0, str.length() - 4)) > 0) {
                return str.substring(0, str.length() - 4);
            } else if (str.endsWith("aliti") && this.stringMeasure(str.substring(0, str.length() - 3)) > 0) {
                return str.substring(0, str.length() - 3);
            } else if (str.endsWith("iviti") && this.stringMeasure(str.substring(0, str.length() - 3)) > 0) {
                return str.substring(0, str.length() - 3) + "e";
            } else {
                return str.endsWith("biliti") && this.stringMeasure(str.substring(0, str.length() - 5)) > 0 ? str.substring(0, str.length() - 5) + "le" : str;
            }
        }
    }

    protected String step3(String str) {
        if (str.endsWith("icate") && this.stringMeasure(str.substring(0, str.length() - 3)) > 0) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ative") && this.stringMeasure(str.substring(0, str.length() - 5)) > 0) {
            return str.substring(0, str.length() - 5);
        } else if (str.endsWith("alize") && this.stringMeasure(str.substring(0, str.length() - 3)) > 0) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("iciti") && this.stringMeasure(str.substring(0, str.length() - 3)) > 0) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ical") && this.stringMeasure(str.substring(0, str.length() - 2)) > 0) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("ful") && this.stringMeasure(str.substring(0, str.length() - 3)) > 0) {
            return str.substring(0, str.length() - 3);
        } else {
            return str.endsWith("ness") && this.stringMeasure(str.substring(0, str.length() - 4)) > 0 ? str.substring(0, str.length() - 4) : str;
        }
    }

    protected String step4(String str) {
        if (str.endsWith("al") && this.stringMeasure(str.substring(0, str.length() - 2)) > 1) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("ance") && this.stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("ence") && this.stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("er") && this.stringMeasure(str.substring(0, str.length() - 2)) > 1) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("ic") && this.stringMeasure(str.substring(0, str.length() - 2)) > 1) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("able") && this.stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("ible") && this.stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("ant") && this.stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ement") && this.stringMeasure(str.substring(0, str.length() - 5)) > 1) {
            return str.substring(0, str.length() - 5);
        } else if (str.endsWith("ment") && this.stringMeasure(str.substring(0, str.length() - 4)) > 1) {
            return str.substring(0, str.length() - 4);
        } else if (str.endsWith("ent") && this.stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if ((str.endsWith("sion") || str.endsWith("tion")) && this.stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ou") && this.stringMeasure(str.substring(0, str.length() - 2)) > 1) {
            return str.substring(0, str.length() - 2);
        } else if (str.endsWith("ism") && this.stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ate") && this.stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("iti") && this.stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ous") && this.stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else if (str.endsWith("ive") && this.stringMeasure(str.substring(0, str.length() - 3)) > 1) {
            return str.substring(0, str.length() - 3);
        } else {
            return str.endsWith("ize") && this.stringMeasure(str.substring(0, str.length() - 3)) > 1 ? str.substring(0, str.length() - 3) : str;
        }
    }

    protected String step5a(String str) {
        if (str.length() > 1) {
            if (this.stringMeasure(str.substring(0, str.length() - 1)) > 1 && str.endsWith("e")) {
                return str.substring(0, str.length() - 1);
            } else {
                return this.stringMeasure(str.substring(0, str.length() - 1)) == 1 && !this.endsWithCVC(str.substring(0, str.length() - 1)) && str.endsWith("e") ? str.substring(0, str.length() - 1) : str;
            }
        } else {
            return str;
        }
    }

    protected String step5b(String str) {
        if (str.length() > 1) {
            return str.endsWith("l") && this.endsWithDoubleConsonant(str) && this.stringMeasure(str.substring(0, str.length() - 1)) > 1 ? str.substring(0, str.length() - 1) : str;
        } else {
            return str;
        }
    }

    protected boolean containsVowel(String str) {
        char[] strchars = str.toCharArray();

        for (char strchar : strchars) {
            if (this.isVowel(strchar)) {
                return true;
            }
        }

        return str.indexOf(121) > -1;
    }

    public boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    protected boolean endsWithDoubleConsonant(String str) {
        char c = str.charAt(str.length() - 1);
        return c == str.charAt(str.length() - 2) && !this.containsVowel(str.substring(str.length() - 2));
    }

    protected int stringMeasure(String str) {
        int count = 0;
        boolean vowelSeen = false;
        char[] strchars = str.toCharArray();

        for (char strchar : strchars) {
            if (this.isVowel(strchar)) {
                vowelSeen = true;
            } else if (vowelSeen) {
                ++count;
                vowelSeen = false;
            }
        }

        return count;
    }

    protected boolean endsWithCVC(String str) {
        if (str.length() >= 3) {
            char c = str.charAt(str.length() - 1);
            char v = str.charAt(str.length() - 2);
            char c2 = str.charAt(str.length() - 3);
            if (c != 'w' && c != 'x' && c != 'y') {
                if (this.isVowel(c)) {
                    return false;
                } else if (!this.isVowel(v)) {
                    return false;
                } else {
                    return !this.isVowel(c2);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
