package nl.erasmusmc.mi.biosemantics.splicer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Normals {

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
        component = F5.compress(component);
        component = component.trim();
        return component;
    }

    public static String normalSplTable(String component) {
        component = component.replaceAll("<(/?[A-z_0-9]+)(\\s+[^>]*)?(/)?(\\s+)?>(.*?)", " ");
        component = component.replace("&#160", "");
        component = component.replace("&#8212;", "");
        component = component.replace("&#8805;", ">=");
        component = component.replace("\t", " ");
        component = F5.compress(component);
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
        component = F5.compress(component);
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
        component = F5.compress(component);
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
        F5.tempArray5 = component.split(">");
        if (component.contains(">") && F5.tempArray5.length > 2) {
            component = component.substring(component.lastIndexOf(">"));
            component = component.replace(">", "");
        }

        component = F5.compress(component);
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
        component = F5.compress(component);
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
        component = F5.compress(component);
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
        component = F5.compress(component);
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
        component = F5.compress(component);
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
        component = F5.compress(component);
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

        component = F5.compress(component);
        component = component.trim();
        component = " " + component + " ";
        return component;
    }

    public static String clip(String c) {
        String anyLetter = "([a-z]|[A-Z])";
        Pattern p = Pattern.compile(anyLetter);
        Matcher m = p.matcher(c);
        boolean mat2 = m.find();
        if (mat2) {
            String matchedLetter = m.group();
            return c.substring(c.indexOf(matchedLetter));
        } else {
            return c;
        }
    }
}
