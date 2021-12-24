CREATE TABLE defaultdirections
(
    labtest   VARCHAR(255) NULL,
    direction VARCHAR(255) NULL
);

CREATE INDEX newindex1
    ON defaultdirections (labtest);

INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Absolute neutrophil count', 'decreased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Alanine aminotransferase', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Alkaline phosphatase', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Amylase', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Blood creatinine', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Creatinine', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Fasting blood glucose', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('GGT', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Glycosylated hemoglobin', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Lactate dehydrogenase', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Liver function tests', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Serum amylase', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Serum bilirubin', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('SGOT', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('SGPT', 'increased');
INSERT INTO splicer.defaultdirections (labtest, direction)
VALUES ('Transaminases', 'increased');