package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NonAde {
    private static final Logger log = LogManager.getLogger();
    static AdeProcess ade = new AdeProcess();


    public NonAde() {
    }

    public void getGenName(String adi) {
        F5.shortGenDrugName = "";
        String tempAdi;
        if (adi.contains("<activeMoiety>")) {
            tempAdi = adi.substring(adi.indexOf("<activeMoiety>"));
            if (tempAdi.contains("<name") && tempAdi.indexOf("<name") < tempAdi.indexOf("</name")) {
                tempAdi = tempAdi.substring(tempAdi.indexOf("<name"), tempAdi.indexOf("</name"));
            }

            tempAdi = tempAdi.replace("<name", "");
            tempAdi = tempAdi.replace("<activeMoiety>", "");
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

            F5.genDrugName = tempAdi;
            F5.genDrugName = F5.genDrugName.toLowerCase();
            if (F5.genDrugName.contains(" ")) {
                F5.shortGenDrugName = F5.genDrugName.substring(0, F5.genDrugName.indexOf(" "));
                F5.shortGenDrugName = F5.shortGenDrugName.trim();
            }
        } else if (adi.contains("<genericMedicine>")) {
            tempAdi = adi.substring(adi.indexOf("<genericMedicine>"));
            if (tempAdi.contains("<name") && tempAdi.indexOf("<name") < tempAdi.indexOf("</name")) {
                tempAdi = tempAdi.substring(tempAdi.indexOf("<name"), tempAdi.indexOf("</name"));
            }

            tempAdi = tempAdi.replace("<name", "");
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

            F5.genDrugName = tempAdi;
            F5.genDrugName = F5.genDrugName.toLowerCase();
            if (F5.genDrugName.contains(" ")) {
                F5.shortGenDrugName = F5.genDrugName.substring(0, F5.genDrugName.indexOf(" "));
                F5.shortGenDrugName = F5.shortGenDrugName.trim();
            }
        } else {
            F5.genDrugName = "";
        }

        F5.genDrugName = Normals.normalDrugName(F5.genDrugName);
        F5.shortGenDrugName = Normals.normalDrugName(F5.shortGenDrugName);
        F5.genDrugName = F5.genDrugName.trim();
    }


    public void getTradeName(String adi) {
        String tempAdi;
        if (adi.contains("<manufacturedMedicine>")) {
            tempAdi = adi.substring(adi.indexOf("<manufacturedMedicine>"));
            if (tempAdi.contains("<name") && tempAdi.indexOf("<name") < tempAdi.indexOf("</name")) {
                tempAdi = tempAdi.substring(tempAdi.indexOf("<name"), tempAdi.indexOf("</name"));
            }

            tempAdi = tempAdi.replace("<name", "");
            tempAdi = tempAdi.replace(">", "");
            F5.tradeDrugName = tempAdi.toLowerCase();
        } else if (adi.contains("<manufacturedProduct>")) {
            tempAdi = adi.substring(adi.indexOf("<manufacturedProduct>"));
            if (tempAdi.contains("<name") && tempAdi.indexOf("<name") < tempAdi.indexOf("</name")) {
                tempAdi = tempAdi.substring(tempAdi.indexOf("<name"), tempAdi.indexOf("</name"));
            } else if (tempAdi.contains("<name")) {
                tempAdi = tempAdi.substring(tempAdi.indexOf("<name"));
            }

            tempAdi = tempAdi.replace("<name", "");
            tempAdi = tempAdi.replace("<suffix>", " ");
            tempAdi = tempAdi.replace("</suffix>", "");
            tempAdi = tempAdi.replace("<suffix/", "");
            tempAdi = tempAdi.replace("\"", "");
            tempAdi = tempAdi.replace(">", "");
            F5.tradeDrugName = tempAdi.toLowerCase();
        } else {
            F5.tradeDrugName = "";
        }

        F5.tradeDrugName = Normals.normalDrugName(F5.tradeDrugName);
    }

    public void getActiveMoiety(String adi) {
        F5.shortGenDrugName = "";
        if (adi.contains("<activeMoiety>")) {
            String tempAdi = adi.substring(adi.indexOf("<activeMoiety>"));
            if (tempAdi.contains("<name") && tempAdi.indexOf("<name") < tempAdi.indexOf("</name")) {
                tempAdi = tempAdi.substring(tempAdi.indexOf("<name"), tempAdi.indexOf("</name"));
            }

            tempAdi = tempAdi.replace("<name", "");
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

            F5.activeMoietyName = tempAdi;
            F5.activeMoietyName = F5.activeMoietyName.toLowerCase();
        } else {
            F5.activeMoietyName = "";
        }

        F5.activeMoietyName = Normals.normalDrugName(F5.activeMoietyName);
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
        } else if (pc.contains("PREGNANCY CATEGORY")) {
            tempPc = pc.substring(pc.indexOf("PREGNANCY CATEGORY"));
            if (tempPc.contains("(")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("("));
            } else if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.contains("Teratogenic Effects")) {
            tempPc = pc.substring(pc.indexOf("Teratogenic Effects"));
            if (tempPc.contains("Category")) {
                tempPc = tempPc.substring(tempPc.indexOf("Category"));
                if (tempPc.contains("<")) {
                    tempPc = tempPc.substring(0, tempPc.indexOf("<"));
                }

                tempPc = tempPc.replace("Category", "");
            } else if (tempPc.contains("<")) {
                tempPc = tempPc.substring(0, tempPc.indexOf("<"));
            }
        } else if (pc.contains("Teratogenic effects")) {
            tempPc = pc.substring(pc.indexOf("Teratogenic effects"));
            if (tempPc.contains("Category")) {
                tempPc = tempPc.substring(tempPc.indexOf("Category"));
                if (tempPc.contains("<")) {
                    tempPc = tempPc.substring(0, tempPc.indexOf("<"));
                }

                tempPc = tempPc.replace("Category", "");
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
        tempPc = tempPc.replaceAll("PREGNANCY CATEGORY", "");
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

        F5.pregCat = tempPc;
    }

    public void getIndication(String ai) {
        String indBlob = Normals.normal2(ai);
        AdeProcess.getMedraTermsIndications(indBlob);
        ade.getUniqueLCS2();
    }

    public boolean checkIndication(String ta) {
        boolean wasFound = false;
        if (F5.maxIndCount > -1) {
            for (int i = 0; i <= F5.maxIndCount; ++i) {
                if (F5.indicationArray[i].equalsIgnoreCase(ta)) {
                    wasFound = true;
                    break;
                }
            }
        }
        return wasFound;
    }

}
