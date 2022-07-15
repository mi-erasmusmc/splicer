package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class F5 {
    private static final Logger log = LogManager.getLogger();

    static int count = 0;
    static int endcw;
    static File myFile;
    static String goalFile = "";
    static String[] sent = new String[10000];
    static int maxArraySentence = 0;
    static int countSentences = 0;
    static String fullMessage = "";
    static String[] allWords = new String[10000];
    static int totalReportSentences = 0;
    static String reportStart = "";
    static String reportEnd = "";
    static String[] tempArray = new String[50];
    static boolean foundValidReport = false;
    static String projectName = "";
    static String[] tempArray2 = new String[10000];
    static String allDrugInfo = "";
    static String allDrugInfo2 = "";
    static String genDrugName = "";
    static String tradeDrugName = "";
    static boolean drugFirst = false;
    static String[] freqArray = new String[10000];
    static int maxFreqArray = 0;
    static String[] condArray = new String[10000];
    static int maxCondArray = 0;
    static boolean skipTable = true;
    static int medraCount = 0;
    static int goodCount = 0;
    static String[] goodWords2 = new String[10000];
    static String[] finalMedraTerms = new String[5000];
    static int maxMedraCount = 0;
    static int finalMedraCount = 0;
    static String[] finalFreq = new String[5000];
    static String[] sMethod = new String[5000];
    static String[] finalsMethod = new String[5000];
    static String sm = "";
    static String[] sSection = new String[5000];
    static String se = "";
    static String[] finalsSection = new String[5000];
    static String currentSection = "";
    static String currentSentence = "";
    static boolean skipSent = true;
    static int tablesFound = 0;
    static String[] tempArray3 = new String[100];
    static String pregCat = "";
    static String allPregInfo = "";
    static String allIndInfo = "";
    static String allIndInfo2 = "";
    static String allPrecInfo = "";
    static String allPrecInfo2 = "";
    static String bypassTable = "off";
    static boolean processingTable = false;
    static String origSent = "";
    static String[] placebo = new String[10000];
    static String[] table = new String[10000];
    static String[] ptN = new String[10000];
    static String tableN = "";
    static boolean italicsFlag = false;
    static String italicsFreq = "";
    static String nonItalicsFreq = "";
    static String[] tempArray4 = new String[100];
    static String[] tempArray5 = new String[100];
    static boolean isList = false;
    static String runningFreq = "";
    static boolean tableToMedra = false;
    static String[] sentNumArray = new String[10000];
    static String[] indicationArray = new String[10000];
    static int indCount = 0;
    static int maxIndCount = -1;
    static PreProcess prp = new PreProcess();
    static NonAde nde = new NonAde();
    static AdeProcess ade = new AdeProcess();
    static Numbers num = new Numbers();
    static Flow flo = new Flow();
    static CleanUp cleanUp = new CleanUp();
    static Loader loader = new Loader();
    static PorterStemmer ps = new PorterStemmer();
    static Section sec = new Section();
    static String[] clean1 = new String[1000];
    static String[] clean2 = new String[1000];
    static String[] clean3 = new String[1000];
    static String[] swit = new String[1000];
    static int maxClean1 = 0;
    static int maxClean2 = 0;
    static int maxClean3 = 0;
    static int maxSwit = 0;
    static Set<String> stopSet = new HashSet<>();
    static Set<String> junkSet = new HashSet<>();
    static String medStem = "";
    static String foundMedra = "";
    static String transformFlag = "";
    static boolean medraFound = false;
    static String matchOutcome = "";
    static String allWarnInfo = "";
    static String allOverInfo = "";
    static String allBoxInfo = "";
    static String allPreInfo = "";
    static int adiInt = 0;
    static int adiInt2 = 0;
    static int adiInt3 = 0;
    static int pregInt = 0;
    static int indInt = 0;
    static int precInt = 0;
    static int overInt = 0;
    static int warnInt = 0;
    static int boxInt = 0;
    static int adeInt = 0;
    static String allAdeInfo = "";
    static boolean gettingTable = false;
    static String allOfTable = "";
    static String allContInfo = "";
    static int contInt = 0;
    static String highValueInd = "";
    static String highValueCont = "";
    static String lowValueInd = "";
    static String lowValueCont = "";
    static boolean escapeMatchOccurred = false;
    static String origTerm = "";
    static String[] defaultLab = new String[1000];
    static String[] defaultDirection = new String[1000];
    static int maxDefaultLD = 0;
    static int indCounter = 0;
    static int m1Counter = 0;
    static int junkCounter = 0;
    static int straightCounter = 0;
    static int synCounter = 0;
    static int stemMedCounter = 0;
    static int transformCounter = 0;
    static int tokenMedCounter = 0;
    static int tokenSynCounter = 0;
    static int l2mCounter = 0;
    static int labTransformCounter = 0;
    static int labCounter = 0;
    static int noMatch1Counter = 0;
    static int simCounter = 0;
    static String origMethod = "";
    static String shortGenDrugName = "";
    static String allActiveMoiety = "";
    static String activeMoietyName = "";
    static boolean passedFinalFilter = true;
    static Set<String> msFilterSet = new HashSet<>();
    static Set<String> l2mFilterSet = new HashSet<>();
    static Set<String> splFilterSet = new HashSet<>();
    static String[] filterCon = new String[1000];
    static int maxFilterCon = 0;
    static int pedInt = 0;
    static String allPedInfo = "";
    static boolean gettingPedSect = false;
    static String pedSect = "";
    static String nSpl = "";
    static String sentForList = "";
    static double commaRatio = 0.0D;
    static boolean tableOn = false;
    static String matchedMedraTerm = "";
    static String termForEscape = "";
    static String transformedEscape = "";
    static String direction = "";
    static boolean labTransformOccurred = false;
    static String quarryPath = "./out";
    static String splInputPath = "./data";
    static String inputPar = "";
    static String outputPar = "";
    static String outputPath = "";
    static String origTerm2 = "";
    static String origSPLTerm = "";
    static String setId = "";
    static boolean setIdFound = false;
    static String SPLDate = "";
    static boolean SPLDateFound = false;
    static String SPLId = "";
    static boolean SPLIdFound = false;
    static String[] allWords3 = new String[10000];


    public static void startPro() {

        loader.getFilters();
        if (!outputPath.equals("")) {
            quarryPath = outputPath;
        }

        if (inputPar.equals("")) {
            inputPar = splInputPath;
        }

        log.debug("MYSQL starting");
        Loader.loadDirectionDefaults();
        loader.getClean1();
        loader.getClean2();
        loader.getClean3();
        loader.getSwit();
        loader.loadStopHash();
        loader.loadJunkHash();
        loader.getFilterContains();
        projectName = "spl";
        log.debug("input path: {}", inputPar);
        log.debug("output path: {}", outputPath);
        File myFile2 = new File(inputPar);
        loadFile(myFile2);
        Database.closeConnection();
        log.info("Finished splicing");
        System.exit(0);
    }

    public static void loadFile(File file) {
        log.debug("file to be processed: {}", file);
        log.debug("{} is a file: {}", file, file.isFile());

        if (file.isFile()) {
            myFile = file;
            String thisFile = "" + myFile;
            log.debug("thisFile  {}", thisFile);
            goalFile = thisFile.substring(thisFile.lastIndexOf("/"));
            goalFile = goalFile.replace("/", "");
            goalFile = goalFile.replaceAll(".xml", "");
            goalFile = goalFile.trim();
            log.debug("goalFile: {}", goalFile);
            analyzeReport();

        } else {
            File[] multFiles = file.listFiles();
            int totalFiles = multFiles.length;
            AtomicInteger fileCount = new AtomicInteger();

            Arrays.asList(multFiles).forEach(spl -> {
                fileCount.getAndIncrement();
                log.info("At file {} of {}", fileCount, totalFiles);
                loadFile(spl);
            });
        }
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

    public static String portStem(String ts) {
        String s;
        if (ts.length() > 1) {
            s = ps.stem(ts.toLowerCase());
            return s;
        } else {
            return "Invalid term";
        }
    }

    public static void analyzeReport() {
        adiInt = 0;
        adiInt2 = 0;
        adiInt3 = 0;
        pregInt = 0;
        indInt = 0;
        precInt = 0;
        allDrugInfo2 = "";
        allDrugInfo = "";
        allPregInfo = "";
        allIndInfo2 = "";
        allIndInfo = "";
        allPrecInfo2 = "";
        allPrecInfo = "";
        allWarnInfo = "";
        allOverInfo = "";
        allBoxInfo = "";
        allPreInfo = "";
        allAdeInfo = "";
        allActiveMoiety = "";
        allContInfo = "";
        contInt = 0;
        highValueInd = "";
        highValueCont = "";
        lowValueInd = "";
        lowValueCont = "";
        pedInt = 0;
        allPedInfo = "";
        gettingPedSect = false;
        pedSect = "";
        fullMessage = "";
        finalMedraCount = 0;
        maxMedraCount = 0;
        goodCount = 0;
        medraCount = 0;
        sec.getSections();
        maxIndCount = -1;
        endcw = 0;
        count = 1;
        tableOn = false;
        reportStart = "34084-4";
        reportEnd = "<code code=";
        foundValidReport = true;
        flo.gatherData();
        if (Validation.alreadyExists(F5.setId)) {
            log.info("We already have {}, not splicing", F5.setId);
            return;
        }


        if (fullMessage.equals("")) {
            log.debug("{} finalMedra: NO ADES found", F5.count);
        } else {
            nde.getIndication(highValueInd);
            currentSection = "Adverse Reactions";
            processAdeSection();
        }

        gettingTable = false;
        allOfTable = "";
        if (!fullMessage.equals("")) {
            flo.postAdeProcessing();
            if (finalMedraCount == -1) {
                log.debug("{} finalMedra:   NO ADES found", F5.count);
            } else {
//                Flow.deleteFromDatabase(SPLId);

                while (count <= maxMedraCount) {
                    finalMedraTerms[count] = flo.cleanUpTerms2(finalMedraTerms[count]);
                    origSPLTerm = finalMedraTerms[count];

                    if (finalFreq[count].equals("")) {
                        finalFreq[count] = "unk";
                    }

                    flo.assignMethod();
                    boolean mappingNeeded = Flow.preMappingFilter(finalMedraTerms[count]);
                    if (mappingNeeded) {
                        matchedMedraTerm = cleanUp.mappingProcesses(finalMedraTerms[count], finalsMethod[count]);
                        transformedEscape = "";
                        termForEscape = "";
                        flo.setTransformFlag();
                        if (matchOutcome.equals("noMatch")) {
                            flo.attemptEscapeMatch();
                        }

                        if (!escapeMatchOccurred && matchOutcome.equals("noMatch")) {
                            CleanUp.similarity(termForEscape);
                        }

                        if (escapeMatchOccurred) {
                        } else if (finalMedraTerms[count] != null && finalsMethod[count] != null) {
                            if (!finalMedraTerms[count].equals("") && !finalsMethod[count].equals("")) {
                                flo.labCheck();
                                if (labTransformOccurred) {
                                    ++labTransformCounter;
                                } else {
                                    flo.finalFilter();
                                    if (passedFinalFilter) {
                                        flo.regularOutput();
                                    }
                                }
                            }
                        }
                    }
                    escapeMatchOccurred = false;
                    ++count;
                }
            }
        }
        Flow.commitToDB();
    }

    public static void processAdeSection() {
        fullMessage = fullMessage.replace("<item>", " ]. ");
        fullMessage = fullMessage.replace("\n", " ");
        fullMessage = fullMessage.replaceAll("\\.\\.\\.", ". ");
        fullMessage = fullMessage.replaceAll("\\.\\.", ". ");
        fullMessage = fullMessage.replaceAll("\\\"\\.\\\"", "");
        prp.getSentences(fullMessage);
        countSentences = 1;

        for (double halfWay = maxArraySentence / 2.0; countSentences <= maxArraySentence; ++countSentences) {
            if (countSentences >= halfWay && (sent[countSentences].toLowerCase().contains("postmarketing") || sent[countSentences].toLowerCase().contains("post-marketing"))) {
                currentSection = "Post Marketing";
            }

            italicsFlag = false;
            italicsFreq = "";
            nonItalicsFreq = "";
            medraCount = 0;

            if (sent[countSentences].contains("</table>") && !gettingTable) {
                flo.tableProcessing();
            } else if (sent[countSentences].contains("<tbody>")) {
                gettingTable = true;
            }

            if (gettingTable) {
                flo.tableProcessingAlternative();
            }

            if (sent[countSentences].length() > 4 && !sent[countSentences].contains("<table>") && !sent[countSentences].contains("</tbody>") && !gettingTable) {
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
                commaRatio = commaNumber / sentLength;
                if (nSpl.length() > 3 && sentForList.length() > 3) {
                    if (sentForList.contains(":")) {
                        flo.processSentenceWithColon();
                    } else if (commaRatio >= 0.02D) {
                        flo.processSentenceWithHighCommaRatio();
                    } else {
                        log.debug("         Likely NOT list comma ratio : {}   {}", commaRatio, nSpl);
                        AdeProcess.getMedraTerms(nSpl);
                    }

                    if (!isList) {
                        if (nSpl.contains("%") && !nSpl.contains("%)")) {
                            Numbers.getRunningFreq(sentForList);
                        } else if (!nSpl.contains("%")) {
                            Numbers.getRunningFreq(sentForList);
                        }
                    }

                    ade.getUniqueLCS();
                }
            }

            isList = false;
            bypassTable = "off";
        }

    }

    public static void main(String[] args) {
        validateProperties();


        if (args.length > 0) {
            inputPar = args[0];
        }
        if (args.length > 1) {
            outputPath = args[1];
        }
        if (args.length > 2) {
            outputPar = args[2];
        }
        startPro();
    }

    private static void validateProperties() {
        Map<String, String> props = new HashMap<>(4);
        props.put("db_conn", System.getProperty("db_conn"));
        props.put("db_user", System.getProperty("db_user"));
        props.put("db_pass", System.getProperty("db_pass"));

        AtomicBoolean missingProps = new AtomicBoolean(false);
        props.forEach((k, v) -> {
            if (v == null) {
                log.error("please pass -D{}=<your{}> in order to run the Splicer succesfully", k, k);
                missingProps.set(true);
            }
        });
        if (missingProps.get()) {
            System.exit(1);
        }
    }
}
