create table SPLICER_JANSSEN
(
    DRUG_CONCEPT_ID        int          null,
    SPL_ID                 varchar(50)  null,
    SET_ID                 varchar(50)  null,
    TRADE_NAME             varchar(200) null,
    SPL_DATE               date         null,
    SPL_SECTION            varchar(30)  null,
    CONDITION_CONCEPT_ID   int          null,
    CONDITION_PT           varchar(150) null,
    CONDITION_LLT          varchar(150) null,
    CONDITION_SOURCE_VALUE varchar(150) null,
    parseMethod            varchar(5)   null,
    sentenceNum            int          null,
    labdirection           varchar(150) null,
    drugfreq               varchar(150) null,
    exclude                varchar(3)   null
);

create index conc
    on SPLICER_JANSSEN (DRUG_CONCEPT_ID);

create index generic
    on SPLICER_JANSSEN (TRADE_NAME);

create index mdr
    on SPLICER_JANSSEN (CONDITION_LLT);

create index set_id
    on SPLICER_JANSSEN (SET_ID);

create index setrxn
    on SPLICER_JANSSEN (SET_ID, CONDITION_LLT);

create index src
    on SPLICER_JANSSEN (SPL_ID);