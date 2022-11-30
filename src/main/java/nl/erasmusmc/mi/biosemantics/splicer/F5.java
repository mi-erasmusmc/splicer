package nl.erasmusmc.mi.biosemantics.splicer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class F5 {
    private static final Logger log = LogManager.getLogger();

    private static String inputPath = "./data";

    public static void main(String[] args) {
        validateProperties();
        if (args.length > 0) {
            inputPath = args[0];
        }
        start();
        end();
    }

    private static void validateProperties() {
        Map<String, String> props = new HashMap<>(4);
        props.put("db_conn", System.getProperty("db_conn"));
        props.put("db_user", System.getProperty("db_user"));
        props.put("db_pass", System.getProperty("db_pass"));

        AtomicBoolean missingProps = new AtomicBoolean(false);
        props.forEach((k, v) -> {
            if (v == null) {
                log.error("please pass -D{}=<your{}> in order to run Splicer", k, k);
                missingProps.set(true);
            }
        });
        if (missingProps.get()) {
            System.exit(1);
        }
    }


    private static void start() {
        Reference.loadReferenceData();
        log.debug("Input path: {}", inputPath);
        File file = new File(inputPath);
        processFile(file);
    }

    private static void processFile(File file) {
        if (file.isFile()) {
            if (file.getName().endsWith(".xml")) {
                new Splicer().analyzeReport(file, false);
            }
        } else {
            File[] multipleFiles = file.listFiles();
            if (multipleFiles != null && multipleFiles.length > 0) {
                int totalFiles = multipleFiles.length;
                AtomicInteger fileCount = new AtomicInteger();
                (Arrays.asList(multipleFiles)).forEach(spl -> {
                    fileCount.getAndIncrement();
                    log.info("At file {} of {}", fileCount, totalFiles);
                    processFile(spl);
                });
            }
        }
    }

    private static void end() {
        Database.closeConnection();
        log.info("Finished splicing");
        System.exit(0);
    }

    public static void fail(Exception e) {
        log.error("Failed: {}", e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }

}
