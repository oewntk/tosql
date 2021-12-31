CREATE TABLE ${senses.table}
(
    ${senses.luid}        INTEGER      NOT NULL,
    ${senses.sensekey}    VARCHAR(100) NOT NULL,
    ${senses.wordid}      INTEGER      NOT NULL,
    ${senses.casedwordid} INTEGER      NULL,
    ${senses.synsetid}    INTEGER      NOT NULL,
    ${senses.senseid}     INTEGER      NOT NULL,
    ${senses.sensenum}    INTEGER      NULL,
    ${senses.lexid}       INTEGER      NOT NULL,
    ${senses.tagcount}    INTEGER      NULL,
    CONSTRAINT pk_${senses.table} PRIMARY KEY (${senses.senseid})
    -- CONSTRAINT pk_${senses.table} PRIMARY KEY (${senses.sensekey})
    -- CONSTRAINT pk_${senses.table} PRIMARY KEY (${senses.luid}, ${senses.synsetid})
);
