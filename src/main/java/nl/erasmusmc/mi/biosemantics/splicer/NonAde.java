package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NonAde {
    public static final String ACTIVE_MOIETY = "<activeMoiety>";
    public static final String NAME_TAG_OPEN = "<name";
    public static final String NAME_TAG_CLOSE = "</name";
    public static final String PREGNANCY_CATEGORY = "PREGNANCY CATEGORY";
    public static final String CATEGORY = "Category";
    private static final Logger log = LogManager.getLogger();
    private final Splicer splicer;
    private String shortGenDrugName = "";


    public NonAde(Splicer splicer) {
        this.splicer = splicer;
    }

    public void getGenName(String adi) {
        shortGenDrugName = "";
        String tempAdi;
        if (adi.contains(ACTIVE_MOIETY)) {
            tempAdi = adi.substring(adi.indexOf(ACTIVE_MOIETY));
            if (tempAdi.contains(NAME_TAG_OPEN) && tempAdi.indexOf(NAME_TAG_OPEN) < tempAdi.indexOf(NAME_TAG_CLOSE)) {
                tempAdi = tempAdi.substring(tempAdi.indexOf(NAME_TAG_OPEN), tempAdi.indexOf(NAME_TAG_CLOSE));
            }

            tempAdi = tempAdi.replace(NAME_TAG_OPEN, "");
            tempAdi = tempAdi.replace(ACTIVE_MOIETY, "");
            tempAdi = tempAdi.replace(">", "");
            if (tempAdi.contains("/")) {
                tempAdi = tempAdi.substring(0, tempAdi.indexOf("/"));
            }

            tempAdi = tempAdi.replaceAll("\\.", "");
            tempAdi = tempAdi.replaceAll("\\,", "");
            tempAdi = tempAdi.replaceAll("\\-", "");
            if (tempAdi.length() > 30) {
                tempAdi = tempAdi.substring(0, 30);
            }

            splicer.genDrugName = tempAdi;
            splicer.genDrugName = splicer.genDrugName.toLowerCase();
            if (splicer.genDrugName.contains(" ")) {
                shortGenDrugName = splicer.genDrugName.substring(0, splicer.genDrugName.indexOf(" "));
                shortGenDrugName = shortGenDrugName.trim();
            }
        } else if (adi.contains("<genericMedicine>")) {
            tempAdi = adi.substring(adi.indexOf("<genericMedicine>"));
            if (tempAdi.contains(NAME_TAG_OPEN) && tempAdi.indexOf(NAME_TAG_OPEN) < tempAdi.indexOf(NAME_TAG_CLOSE)) {
                tempAdi = tempAdi.substring(tempAdi.indexOf(NAME_TAG_OPEN), tempAdi.indexOf(NAME_TAG_CLOSE));
            }

            tempAdi = tempAdi.replace(NAME_TAG_OPEN, "");
            tempAdi = tempAdi.replace(">", "");
            if (tempAdi.contains("/")) {
                tempAdi = tempAdi.substring(0, tempAdi.indexOf("/"));
            }

            tempAdi = tempAdi.replace("<genericMedicine", "");
            tempAdi = tempAdi.replaceAll("\\.", "");
            tempAdi = tempAdi.replaceAll("\\,", "");
            tempAdi = tempAdi.replaceAll("\\-", "");
            if (tempAdi.length() > 30) {
                tempAdi = tempAdi.substring(0, 30);
            }

            splicer.genDrugName = tempAdi;
            splicer.genDrugName = splicer.genDrugName.toLowerCase();
            if (splicer.genDrugName.contains(" ")) {
                shortGenDrugName = splicer.genDrugName.substring(0, splicer.genDrugName.indexOf(" "));
                shortGenDrugName = shortGenDrugName.trim();
            }
        } else {
            splicer.genDrugName = "";
        }

        splicer.genDrugName = Normals.normalDrugName(splicer.genDrugName);
        shortGenDrugName = Normals.normalDrugName(shortGenDrugName);
        splicer.genDrugName = splicer.genDrugName.trim();
    }


    public void getTradeName(String adi) {
        String tempAdi;
        if (adi.contains("<manufacturedMedicine>")) {
            tempAdi = adi.substring(adi.indexOf("<manufacturedMedicine>"));
            if (tempAdi.contains(NAME_TAG_OPEN) && tempAdi.indexOf(NAME_TAG_OPEN) < tempAdi.indexOf(NAME_TAG_CLOSE)) {
                tempAdi = tempAdi.substring(tempAdi.indexOf(NAME_TAG_OPEN), tempAdi.indexOf(NAME_TAG_CLOSE));
            }

            tempAdi = tempAdi.replace(NAME_TAG_OPEN, "");
            tempAdi = tempAdi.replace(">", "");
            splicer.tradeDrugName = tempAdi.toLowerCase();
        } else if (adi.contains("<manufacturedProduct>")) {
            tempAdi = adi.substring(adi.indexOf("<manufacturedProduct>"));
            if (tempAdi.contains(NAME_TAG_OPEN) && tempAdi.indexOf(NAME_TAG_OPEN) < tempAdi.indexOf(NAME_TAG_CLOSE)) {
                tempAdi = tempAdi.substring(tempAdi.indexOf(NAME_TAG_OPEN), tempAdi.indexOf(NAME_TAG_CLOSE));
            } else if (tempAdi.contains(NAME_TAG_OPEN)) {
                tempAdi = tempAdi.substring(tempAdi.indexOf(NAME_TAG_OPEN));
            }

            tempAdi = tempAdi.replace(NAME_TAG_OPEN, "");
            tempAdi = tempAdi.replace("<suffix>", " ");
            tempAdi = tempAdi.replace("</suffix>", "");
            tempAdi = tempAdi.replace("<suffix/", "");
            tempAdi = tempAdi.replace("\"", "");
            tempAdi = tempAdi.replace(">", "");
            splicer.tradeDrugName = tempAdi.toLowerCase();
        } else {
            splicer.tradeDrugName = "";
        }

        splicer.tradeDrugName = Normals.normalDrugName(splicer.tradeDrugName);
    }

    public void getActiveMoiety(String adi) {
        shortGenDrugName = "";
        if (adi.contains(ACTIVE_MOIETY)) {
            String tempAdi = adi.substring(adi.indexOf(ACTIVE_MOIETY));
            if (tempAdi.contains(NAME_TAG_OPEN) && tempAdi.indexOf(NAME_TAG_OPEN) < tempAdi.indexOf(NAME_TAG_CLOSE)) {
                tempAdi = tempAdi.substring(tempAdi.indexOf(NAME_TAG_OPEN), tempAdi.indexOf(NAME_TAG_CLOSE));
            }

            tempAdi = tempAdi.replace(NAME_TAG_OPEN, "");
            tempAdi = tempAdi.replace(">", "");
            if (tempAdi.contains("/")) {
                tempAdi = tempAdi.substring(0, tempAdi.indexOf("/"));
            }

            tempAdi = tempAdi.replaceAll("\\.", "");
            tempAdi = tempAdi.replaceAll("\\,", "");
            tempAdi = tempAdi.replaceAll("\\-", "");
            if (tempAdi.length() > 30) {
                tempAdi = tempAdi.substring(0, 30);
            }

            splicer.activeMoietyName = tempAdi;
            splicer.activeMoietyName = splicer.activeMoietyName.toLowerCase();
        } else {
            splicer.activeMoietyName = "";
        }

        splicer.activeMoietyName = Normals.normalDrugName(splicer.activeMoietyName);
    }

    public void getPregCat(String pc) {

        String tempPc = "";
        if (pc.contains("Pregnancy Category")) {
            tempPc = pc.substring(pc.indexOf("Pregnancy Category"));
            if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.contains("Pregnancy category")) {
            tempPc = pc.substring(pc.indexOf("Pregnancy category"));
            if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.contains("Pregnancy Categories")) {
            tempPc = pc.substring(pc.indexOf("Pregnancy Categories"));
            if (tempPc.contains("(")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("("));
            } else if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.contains("Teratogenic Effects Category")) {
            tempPc = pc.substring(pc.indexOf("Teratogenic Effects Category"));
            if (tempPc.contains("(")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("("));
            } else if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.contains(PREGNANCY_CATEGORY)) {
            tempPc = pc.substring(pc.indexOf(PREGNANCY_CATEGORY));
            if (tempPc.contains("(")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("("));
            } else if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.contains("Teratogenic Effects")) {
            tempPc = pc.substring(pc.indexOf("Teratogenic Effects"));
            if (tempPc.contains(CATEGORY)) {
                tempPc = tempPc.substring(tempPc.indexOf(CATEGORY));
                if (tempPc.contains("<")) {
                    tempPc = tempPc.substring(0, tempPc.indexOf("<"));
                }

                tempPc = tempPc.replace(CATEGORY, "");
            } else if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.contains("Teratogenic effects")) {
            tempPc = pc.substring(pc.indexOf("Teratogenic effects"));
            if (tempPc.contains(CATEGORY)) {
                tempPc = tempPc.substring(tempPc.indexOf(CATEGORY));
                if (tempPc.contains("<")) {
                    tempPc = tempPc.substring(0, tempPc.indexOf("<"));
                }

                tempPc = tempPc.replace(CATEGORY, "");
            } else if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.toLowerCase().contains("category")) {
            tempPc = pc.substring(pc.toLowerCase().indexOf("category"));
            if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.toLowerCase().contains("categories")) {
            tempPc = pc.substring(pc.toLowerCase().indexOf("categories"));
            tempPc = tempPc.replaceAll("<(/?[A-z_0-9]+)(\\s+[^>]*)?(/)?(\\s+)?>(.*?)", " ");
        }

        tempPc = tempPc.replace(";", "");
        if (tempPc.contains("�")) {
            tempPc = tempPc.replace(" ", " ");
            log.debug(tempPc);
        }

        tempPc = tempPc.replace("<", "");
        tempPc = tempPc.replaceAll("\\.", "");
        tempPc = tempPc.replaceAll("(?i)Pregnancy Category", "");
        tempPc = tempPc.replaceAll("(?i)Teratogenic Effects Category", "");
        tempPc = tempPc.replaceAll("(?i)Pregnancy Categories", "");
        tempPc = tempPc.replace(PREGNANCY_CATEGORY, "");
        tempPc = tempPc.replaceAll("(?i)Teratogenic Effects", "");
        tempPc = tempPc.replaceAll("(?i)category", "");
        tempPc = tempPc.replaceAll("(?i)categories", "");
        tempPc = tempPc.trim();
        if (tempPc.contains("&")) {
            tempPc = tempPc.substring(0, tempPc.indexOf("&"));
        }

        tempPc = tempPc.replaceAll("\\)", "");
        tempPc = tempPc.replaceAll("\\]", "");
        tempPc = tempPc.replace("\"", "");
        tempPc = tempPc.replace("_", "");
        tempPc = tempPc.replace("-", "");
        tempPc = tempPc.replace("\"", "");
        tempPc = tempPc.replaceAll("\\:", "");
        tempPc = tempPc.trim();
        if (tempPc.length() >= 2) {
            tempPc = tempPc.substring(0, 1);
        }

        splicer.pregCat = tempPc;
    }

    public void getIndication(String ai) {
        String indBlob = Normals.normal2(ai);
        splicer.adeProcess.getMeddraTermsIndications(indBlob);
        splicer.adeProcess.getUniqueLCS2();
    }

}
