create table syn2
(
    Field1 varchar(15) null,
    Field2 varchar(11) null
);

create index NewIndex18
    on syn2 (Field1);

INSERT INTO splicer.syn2 (Field1, Field2) VALUES (' disorders', 'disorder');
INSERT INTO splicer.syn2 (Field1, Field2) VALUES (' difficulties', 'difficulty');
INSERT INTO splicer.syn2 (Field1, Field2) VALUES (' smq', null);
INSERT INTO splicer.syn2 (Field1, Field2) VALUES (' nos', null);
INSERT INTO splicer.syn2 (Field1, Field2) VALUES (' l', null);
INSERT INTO splicer.syn2 (Field1, Field2) VALUES (' r', null);
INSERT INTO splicer.syn2 (Field1, Field2) VALUES (' increase', 'increased');
INSERT INTO splicer.syn2 (Field1, Field2) VALUES (' decrease', 'decreased');
INSERT INTO splicer.syn2 (Field1, Field2) VALUES (' diminished', 'impaired');