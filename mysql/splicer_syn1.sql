create table syn1
(
    Field1 varchar(23) null,
    Field2 varchar(52) null
);

create index NewIndex16
    on syn1 (Field1);

create index NewIndex17
    on syn1 (Field2);

INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' injection-site', 'local');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' increase in', 'increased');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' increases in', 'increased');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' .', null);
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' hiccoughs', 'hiccups');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' intellectual ability', 'intelligence');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' liver enzymes', 'liver function test');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' liver enzyme', 'liver function test');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' liver transaminases', 'liver function test');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' liver test', 'liver function test');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' maculapapular', 'maculopapular');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' mask-like', 'Masked');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' disorders', 'disorder');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' difficulties', 'difficulty');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' hodgkin', 'hodgkins');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' parethesias', 'paresthesias');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' rashes', 'rash');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' prolongation', 'prolonged');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' {tabs}		', null);
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' vulvar', 'vulval');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' &#8221', null);
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' ecg', 'electrocardiogram');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' cramping', 'cramps');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' leukocytes', 'leukocyte count');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' neutrophils', 'neutrophil count');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' white blood cells', 'white blood cell count');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' lymphocytes', 'lyphocyte count');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' worsened', 'aggravated');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' impaired', 'impaired');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' diminished', 'impaired');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' decresed', 'decreased');
INSERT INTO splicer.syn1 (Field1, Field2) VALUES (' injection site', 'local');