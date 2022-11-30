package nl.erasmusmc.mi.biosemantics.splicer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Normals {

    private static final Pattern ALFA = Pattern.compile("([a-z]|[A-Z])");


    private Normals() {
    }


    public static String normalSpl(String component) {
        component = component.replaceAll("<(/?[A-z_1-9]+)(\\s+[^>]*)?(/)?(\\s+)?>(.*?)", " ");
        component = component.replaceAll(" <.+> ", "");
        component = component.replace(" —", " ");
        component = component.replace(" ≥", " ");
        component = component.replace("���", " ");
        component = component.replace("��", " ");
        component = component.replace("�", " ");
        component = component.replace("&#160", "");
        component = component.replace(" &#8217;", "");
        component = component.replace("&#8217;", "");
        component = component.replace("&#8212;", "");
        component = component.replace("&#8805;", ">=");
        component = component.replace("\t", " ");
        component = component.replaceAll("\\/", " or ");
        component = compress(component);
        component = component.trim();
        return component;
    }

    public static String normalSplTable(String component) {
        component = component.replaceAll("<(/?[A-z_0-9]+)(\\s+[^>]*)?(/)?(\\s+)?>(.*?)", " ");
        component = component.replace("&#160", "");
        component = component.replace("&#8212;", "");
        component = component.replace("&#8805;", ">=");
        component = component.replace("\t", " ");
        component = compress(component);
        component = component.trim();
        return component;
    }

    public static String normal2(String component) {
        component = component.replaceAll("\\'", "\\'\\'");
        component = component.replaceAll("\\'", "");
        component = component.replaceAll("\\,", " ,");
        component = component.replaceAll("\\.", " . ");
        component = component.replaceAll("\\^", " ");
        component = component.replaceAll("\\+", "  ");
        component = component.replaceAll("\\]", "  ");
        component = component.replaceAll("\\[", "  ");
        component = component.replaceAll("\\(", " , ");
        component = component.replaceAll("\\)", " , ");
        component = component.replaceAll("\\/", "  ");
        component = component.replaceAll("\\/", " or ");
        component = component.replace(" —", " ");
        component = component.replace("���", " ");
        component = component.replace("��", " ");
        component = component.replace("�", " ");
        component = component.replaceAll("\\{", " ");
        component = component.replaceAll("\\}", " ");
        component = component.replaceAll("\\:", " ");
        component = component.replace("%", " % ");
        component = component.replace(">", " > ");
        component = component.replace("<", " < ");
        component = component.replaceAll("\\*", " * ");
        component = component.replaceAll("^", " ");
        component = component.replace("~", " ");
        component = component.replaceAll("\\;", " ,");
        component = component.replace("percent", "%");
        component = component.replace("and", ",");
        component = component.replace(", ,", ",");
        component = component.replaceAll("\"", "");
        component = compress(component);
        component = component.trim();
        component = " " + component + " ";
        return component;
    }

    public static String normal9(String component) {
        component = component.replaceAll("\\'", "");
        component = component.replaceAll("\\,", " ,");
        component = component.replaceAll("\\.", " . ");
        component = component.replaceAll("\\^", " ");
        component = component.replaceAll("\\+", "  ");
        component = component.replaceAll("\\(", ", ");
        component = component.replaceAll("\\)", ", ");
        component = component.replaceAll("\\/", "  ");
        component = component.replace("���", " ");
        component = component.replace("��", " ");
        component = component.replace("�", " ");
        component = component.replaceAll("\\{", " ");
        component = component.replaceAll("\\}", " ");
        component = component.replaceAll("\\:", " :");
        component = component.replace("%", " % ");
        component = component.replace(">", " > ");
        component = component.replace("<", " < ");
        component = component.replaceAll("\\*", " * ");
        component = component.replaceAll("^", " ");
        component = component.replace("~", " ");
        component = component.replaceAll("\\;", " ,");
        component = compress(component);
        component = component.trim();
        return component;
    }

    public static String normal7(String component) {

        component = component.replaceAll("\\'", "");
        component = component.replaceAll("\\,", "");
        component = component.replaceAll("\\.", "");
        component = component.replaceAll("\\^", "");
        component = component.replaceAll("\\+", "");
        component = component.replaceAll("\\(", "");
        component = component.replaceAll("\\)", "");
        component = component.replaceAll("\\:", "");
        component = component.replaceAll("\\/", "");
        component = component.replace("%", "");
        component = component.replace(" were ", " ");
        component = component.replace(" is ", " ");
        component = component.replace(" including ", " ");
        component = component.replaceAll("[0-9]", " ");
        component = component.replace("placebo", " ");
        component = component.replaceAll("\\;", "");
        component = component.replace("similarly", " ");
        component = component.replace(" trial", " ");
        component = component.replace(" trials", " ");
        component = component.replace("vs", " ");
        component = component.replace(" include ", " ");
        var tempArray5 = component.split(">");
        if (component.contains(">") && tempArray5.length > 2) {
            component = component.substring(component.lastIndexOf(">"));
            component = component.replace(">", "");
        }

        component = compress(component);
        component = component.trim();
        return component;
    }

    public static String normal8(String component) {
        component = component.toLowerCase();
        component = component.replaceAll("<(/?[a-z_1-9]+)(\\s+[^>]*)?(/)?(\\s+)?>(.*?)", " ");
        component = component.replaceAll("\\'", "");
        component = component.replaceAll("\\,", " ,");
        component = component.replaceAll("\\.", " . ");
        component = component.replaceAll("\\+", "  ");
        component = component.replaceAll("\\/", "  ");
        component = component.replaceAll("\\{", " ");
        component = component.replaceAll("\\}", " ");
        component = component.replaceAll("\\:", " : ");
        component = component.replace("%", " % ");
        component = component.replace(">", " > ");
        component = component.replace("<", " < ");
        component = component.replaceAll("\\*", " * ");
        component = component.replaceAll("^", " ");
        component = component.replace("~", " ");
        component = component.replace("and", ",");
        component = compress(component);
        component = component.trim();
        return component;
    }

    public static String normalBadTable(String component) {
        component = component.toLowerCase();
        component = component.replaceAll("\\'", " ");
        component = component.replaceAll("\\,", "  ");
        component = component.replaceAll("\\.", "   ");
        component = component.replaceAll("\\+", "  ");
        component = component.replaceAll("\\/", " or ");
        component = component.replace(";", " ");
        component = component.replaceAll("\\{", " ");
        component = component.replaceAll("\\}", " ");
        component = component.replaceAll("\\-", " - ");
        component = component.replaceAll("\\:", " : ");
        component = component.replace("%", " % ");
        component = component.replace(">", " > ");
        component = component.replace("<", " < ");
        component = component.replaceAll("\\*", " * ");
        component = component.replaceAll("^", " ");
        component = component.replace("~", " ");
        component = compress(component);
        component = component.trim();
        return component;
    }

    public static String normalBadTable2(String component) {
        component = component.toLowerCase();
        component = component.replaceAll("\\'", " ");
        component = component.replaceAll("\\,", " , ");
        component = component.replaceAll("\\.", "   ");
        component = component.replaceAll("\\+", "  ");
        component = component.replaceAll("\\(", "  ");
        component = component.replaceAll("\\)", "  ");
        component = component.replaceAll("\\/", " or ");
        component = component.replace(";", " ");
        component = component.replaceAll("\\{", " ");
        component = component.replaceAll("\\}", " ");
        component = component.replaceAll("\\-", "  ");
        component = component.replaceAll("\\:", "  ");
        component = component.replace("%", "  ");
        component = component.replace(">", "  ");
        component = component.replace("<", "  ");
        component = component.replaceAll("\\*", "  ");
        component = component.replaceAll("^", " ");
        component = component.replace("~", " ");
        component = compress(component);
        component = component.trim();
        return component;
    }

    public static String normalDrugName(String component) {
        component = component.replaceAll("\\'", "");
        component = component.replaceAll("\\,", " ,");
        component = component.replaceAll("\\.", " . ");
        component = component.replaceAll("\\^", " ");
        component = component.replaceAll("\\+", "  ");
        component = component.replaceAll("\\]", "  ");
        component = component.replaceAll("\\[", "  ");
        component = component.replaceAll("\\(", " , ");
        component = component.replaceAll("\\)", " , ");
        component = component.replaceAll("\\/", "  ");
        component = component.replaceAll("\\/", "  ");
        component = component.replace("���", " ");
        component = component.replace("��", " ");
        component = component.replace("�", " ");
        component = component.replaceAll("\\,", " ");
        component = component.replaceAll("\\{", " ");
        component = component.replaceAll("\\}", " ");
        component = component.replaceAll("\\-", "");
        component = component.replaceAll("\\:", " ");
        component = component.replace("%", " % ");
        component = component.replace(">", " > ");
        component = component.replace("<", " < ");
        component = component.replaceAll("\\*", " * ");
        component = component.replaceAll("^", " ");
        component = component.replace("~", " ");
        component = component.replaceAll("\\;", " ,");
        component = compress(component);
        component = component.trim();
        component = " " + component + " ";
        return component;
    }

    public static String normalizeFilter(String component) {
        component = component.replace("âˆ’", "");
        component = component.replace("â‰¥ ", "");
        component = component.replaceAll("\\)", "");
        component = component.replaceAll("\\]", "");
        component = component.replace("--i", "");
        component = component.replace("\"", "");
        component = component.replace("- ", "");
        component = component.replace("-", "");
        component = component.replace("&lt", "");
        component = component.replace("â", "");
        component = component.replace("Â", "");
        component = compress(component);
        component = component.trim();
        return component;
    }

    public static String normalizeFilter2(String component) {
        if (component.contains(">")) {
            component = component.substring(component.indexOf(">"));
        }

        component = component.replace(">", "");
        component = component.replaceAll("\\:", "");
        component = component.replaceAll("\\]", "");
        component = component.replaceAll("\\[", "");
        component = component.replaceAll("\\=", "");
        component = component.replaceAll("\\=", "");
        component = component.replace("—", " ");
        if (component.contains("�")) {
            component = component.substring(0, component.indexOf("�"));
        }

        component = component.replace("�", " ");
        component = component.replace("&amp", " ");
        component = component.replace("�", " ");
        component = component.replace("�", " ");
        component = component.replace("\"", "");
        if (component.startsWith("-")) {
            component = component.substring(component.lastIndexOf("-"));
        }

        component = compress(component);
        component = component.trim();
        component = " " + component + " ";
        return component;
    }

    public static String compress(String ss) {
        StringBuilder dest = new StringBuilder();
        if (ss.length() == 0) {
            return ss;
        } else {
            char c = ss.charAt(0);

            for (int i = 1; i < ss.length(); ++i) {
                if (c != ' ' || ss.charAt(i) != ' ') {
                    dest.append(c);
                }

                c = ss.charAt(i);
            }

            dest.append(c);
            return dest.toString();
        }
    }

    public static String clip(String c) {
        Matcher m = ALFA.matcher(c);
        boolean mat2 = m.find();
        if (mat2) {
            String matchedLetter = m.group();
            return c.substring(c.indexOf(matchedLetter));
        } else {
            return c;
        }
    }

}
