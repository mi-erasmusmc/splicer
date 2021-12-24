create table swit
(
    Field1 varchar(19) null,
    Field2 varchar(13) null
);

create index NewIndex15
    on swit (Field1);

INSERT INTO splicer.swit (Field1, Field2) VALUES (' increased', 'increased');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' decreased', 'decreased');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' elevated', 'increased');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' elevations of', 'increased');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' increases in', 'increased');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' increase in', 'increased');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' impaired', 'impaired');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' irregular', 'irregular');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' reduced', 'reduced');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' decreased', 'decreased');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' decrease in', 'decreased');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' disorder of the', 'disorder');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' disorder of', 'disorder');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' disorders of the', 'disorder');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' disorders of', 'disorder');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' disorder', 'disorder');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' pain in', 'pain');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' psoriaform', 'psoriaform');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' psoriasiform', 'psoriasiform');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' pain', 'pain');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' disturbance of', 'disturbance');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' rash', 'rash');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' abnormal', 'abnormal');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' swollen', 'swelling');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' aggravation of', 'aggravated');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' aggravated', 'aggravated');
INSERT INTO splicer.swit (Field1, Field2) VALUES (' worsening of', 'aggravated');