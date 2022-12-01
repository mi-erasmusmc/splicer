package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static nl.erasmusmc.mi.biosemantics.splicer.Method.NONE;
import static nl.erasmusmc.mi.biosemantics.splicer.Validation.alreadyExists;

public class Splicer {
    final StringBuilder highValueInd = new StringBuilder();
    private final Logger log = LogManager.getLogger();
    int count = 1;
    String[] sent = new String[10000];
    int maxArraySentence = 0;
    int countSentences = 0;
    String fullMessage = "";
    Word[] allWords = new Word[10000];
    String[] tempArray = new String[50];
    String allDrugInfo = "";
    String allDrugInfo2 = "";
    String genDrugName = "";
    String tradeDrugName = "";
    boolean drugFirst = false;
    int meddraCount = 0;
    int goodCount = 0;
    String[] goodWords2 = new String[10000];
    String[] finalMeddraTerms = new String[5000];
    int finalMeddraCount = 0;
    String[] finalFreq = new String[5000];
    Method[] sMethod = new Method[5000];
    Method[] finalsMethod = new Method[5000];
    Method sm = NONE;
    String[] sSection = new String[5000];
    String[] finalsSection = new String[5000];
    String currentSection = "";
    String currentSentence = "";
    String pregCat = "";
    String allPregInfo = "";
    String origSent = "";
    String[] placebo = new String[1000];
    String[] table = new String[1000];
    String[] ptN = new String[1000];
    boolean italicsFlag = false;
    String italicsFreq = "";
    String nonItalicsFreq = "";
    String[] tempArray4 = new String[100];
    boolean isList = false;
    boolean tableToMeddra = false;
    String[] sentNumArray = new String[10000];
    Set<String> indicationArray = new HashSet<>();
    String transformFlag = "";
    Boolean matchedOutcome = null;
    boolean gettingTable = false;
    String allOfTable = "";
    boolean escapeMatchOccurred = false;
    String origTerm = "";
    int labTransformCounter = 0;
    Method origMethod = NONE;
    String allActiveMoiety = "";
    String activeMoietyName = "";
    boolean passedFinalFilter = true;
    String nSpl = "";
    String sentForList = "";
    String matchedMeddraTerm = "";
    String termForEscape = "";
    String direction = "";
    boolean labTransformOccurred = false;
    String origTerm2 = "";
    String origSPLTerm = "";
    String setId = "";
    String splDate = "";
    String splId = "";

    PreProcess prp;
    NonAde nde;
    Numbers num;
    Flow flow;
    CleanUp cleanUp;
    AdeProcess adeProcess;
    PostProcess postProcess;
    Section section;


    public Splicer() {
        this.flow = new Flow(this);
        this.cleanUp = new CleanUp(this);
        this.adeProcess = new AdeProcess(this);
        this.nde = new NonAde(this);
        this.prp = new PreProcess(this);
        this.num = new Numbers(this);
        this.postProcess = new PostProcess(this);
        this.section = new Section(this);
    }


    public void analyzeReport(File file, boolean skipExisting) {
        section.getSections(file);
        if (skipExisting && alreadyExists(setId)) {
            log.info("We already have {}, not splicing", setId);
            return;
        }
        flow.gatherData();
        if (fullMessage.isBlank()) {
            log.debug("{} finalMeddra: NO ADES found", count);
        } else {
            nde.getIndication(highValueInd.toString());
            currentSection = "Adverse Reactions";
            processAdeSection();
        }

        gettingTable = false;
        allOfTable = "";
        if (!fullMessage.isBlank()) {
            postAdeProcessing();
            if (finalMeddraCount == -1) {
                log.debug("NO ADES found: {} final MedDRA", count);
            } else {
                flow.deleteFromDatabase(splId);

                while (count < finalMeddraCount) {
                    finalMeddraTerms[count] = flow.cleanUpTerms2(finalMeddraTerms[count]);
                    origSPLTerm = finalMeddraTerms[count];

                    if (finalFreq[count].isBlank()) {
                        finalFreq[count] = "unk";
                    }

                    flow.assignMethod();
                    boolean mappingNeeded = flow.preMappingFilter(finalMeddraTerms[count]);
                    if (mappingNeeded) {
                        matchedMeddraTerm = cleanUp.mappingProcesses(finalMeddraTerms[count], finalsMethod[count]);
                        termForEscape = "";
                        flow.setTransformFlag();

                        if (!Boolean.TRUE.equals(matchedOutcome)) {
                            flow.attemptEscapeMatch();
                        }

                        if (!escapeMatchOccurred && !Boolean.TRUE.equals(matchedOutcome)) {
                            cleanUp.similarity(termForEscape);
                        }

                        if (!escapeMatchOccurred && finalMeddraTerms[count] != null && finalsMethod[count] != null
                                && !finalMeddraTerms[count].isBlank() && !finalsMethod[count].equals(NONE)) {
                            flow.labCheck();
                            if (labTransformOccurred) {
                                ++labTransformCounter;
                            } else {
                                flow.finalFilter();
                                if (passedFinalFilter) {
                                    flow.regularOutput();
                                }
                            }
                        }
                    }
                    escapeMatchOccurred = false;
                    ++count;
                }
            }
        }
        flow.commitToDB();
    }

    public void postAdeProcessing() {
        allPregInfo = allPregInfo.replaceAll("Â ", " ");
        nde.getPregCat(allPregInfo);
        pregCat = Normals.normalBadTable(pregCat);
        if (pregCat.contains(" ")) {
            pregCat = pregCat.substring(0, pregCat.indexOf(" "));
        }

        section.processSections();
        count = 0;
        log.debug("Done post-Ade-Processing");
    }

    public void processAdeSection() {
        fullMessage = fullMessage.replace("<item>", " ]. ");
        fullMessage = fullMessage.replaceAll("\n", " ");
        fullMessage = fullMessage.replaceAll("\\.\\.\\.", ". ");
        fullMessage = fullMessage.replaceAll("\\.\\.", ". ");
        fullMessage = fullMessage.replaceAll("\\\"\\.\\\"", "");
        prp.getSentences(fullMessage);
        countSentences = 1;

        for (double halfWay = maxArraySentence / 2.0; countSentences <= maxArraySentence; ++countSentences) {
            if (countSentences >= halfWay && (sent[countSentences].toLowerCase().contains("postmarketing")
                    || sent[countSentences].toLowerCase().contains("post-marketing"))) {
                currentSection = "Post Marketing";
            }

            italicsFlag = false;
            italicsFreq = "";
            nonItalicsFreq = "";
            meddraCount = 0;

            if (sent[countSentences].contains("</table>") && !gettingTable) {
                flow.tableProcessing();
            } else if (sent[countSentences].contains("<tbody>")) {
                gettingTable = true;
            }

            if (gettingTable) {
                flow.tableProcessingAlternative();
            }

            if (sent[countSentences].length() > 4 && !sent[countSentences].contains("<table>")
                    && !sent[countSentences].contains("</tbody>") && !gettingTable) {
                origSent = sent[countSentences];
                if (italicsFlag && origSent.contains("codeSystem=")) {
                    italicsFlag = false;
                    italicsFreq = "";
                    nonItalicsFreq = "";
                }

                if (!italicsFlag && origSent.contains("italics")) {
                    italicsFlag = true;
                    num.getItalicsNumbers();
                }

                nSpl = Normals.normalSpl(sent[countSentences]);
                sentForList = Normals.normalSpl(origSent);
                nSpl = Normals.normal2(nSpl);
                sentForList = Normals.normal9(sentForList);
                currentSentence = nSpl;
                log.debug("*** clean sentence: {}   {}", countSentences, nSpl);
                tempArray = nSpl.split(",");
                double commaNumber = tempArray.length - 1.0;
                double sentLength = nSpl.length();
                double commaRatio = commaNumber / sentLength;
                if (nSpl.length() > 3 && sentForList.length() > 3) {
                    if (sentForList.contains(":")) {
                        flow.processSentenceWithColon();
                    } else if (commaRatio >= 0.02D) {
                        flow.processSentenceWithHighCommaRatio();
                    } else {
                        log.debug("Likely NOT list comma ratio : {}   {}", commaRatio, nSpl);
                        adeProcess.getMeddraTerms(nSpl);
                    }

                    if (!isList && (!nSpl.contains("%") || !nSpl.contains("%)"))) {
                        num.getRunningFreq(sentForList);
                    }
                    adeProcess.getUniqueLCS();
                }
            }
            isList = false;
        }

    }

}
