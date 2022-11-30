SPLICER
=======================

**A mangled version of the Structured Product Label Information Coder and ExtractoR (SPLICER)**

SPLICER is a rule-based natural language processing tool that extracts adverse reaction terms from Structured
Product Label (SPL) and maps these terms to the Medical Dictionary of Regulatory Activities (MedDRA). SPLICER uses a
series of regular expressions and algorithms to identify and extract data from SPL’s.

**Note:** This is **not** the original SPLICER code, it is code that was decompiled from a binary file and then
refactored a bit in order to make half sense.

Duke JD, Friedlin J. ADESSA: A Real-Time Decision Support Service for Delivery of Semantically Coded Adverse Drug Event
Data. AMIA Annu Symp Proc. 2010 Nov 13;2010:177-81. PMID: 21346964; PMCID: PMC3041415.

### REQUIREMENTS

- [Java 17+]("https://www.oracle.com/java/technologies/downloads/")
- Maven
- MySQL (tested with version 8)

### HOW TO RUN

- Set up a database using the .sql files in the mysql folder, this contains reference terms with which the adverse
  events
  are matched
- Place the .xml files from [DailMed](https://dailymed.nlm.nih.gov/dailymed/spl-resources-all-drug-labels.cfm) for which
  you whish to extract AEs from in the data folder, SPLICER expects this folder to be in the same location from where
  you are
  running the app, you may pass a different location as argument when running the code
- Build the code with `mvn clean package assembly:single`
- Run the code by passing a connection string `-Ddb_conn`, as well as user `-Ddb_user` and password `-Ddb_pass`for
  example: \
  `java -Ddb_conn="jdbc:mysql://localhost:3306/splicer?allowPublicKeyRetrieval=true&useSSL=false&verifyServerCertificate=false" -Ddb_user=root -Ddb_pass=root -jar target/splicer-1.0-SNAPSHOT-jar-with-dependencies.jar`

The extraction will now run for approximately 1-2 minutes per xml file, and persist the results in the SPLICER table in
your
db.

**Note**: This code has been tested on MacOS and Ubuntu machines. No testing has been done on other operating systems.
