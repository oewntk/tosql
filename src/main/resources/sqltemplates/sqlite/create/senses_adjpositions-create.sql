CREATE TABLE ${senses_adjpositions.table}
(
    ${senses_adjpositions.synsetid}     INTEGER      NOT NULL,
    ${senses_adjpositions.luid}         INTEGER      NOT NULL,
    ${senses_adjpositions.wordid}       INTEGER      NOT NULL,
    ${senses_adjpositions.positionid}   CHARACTER(2) NOT NULL,
    CONSTRAINT pk_${senses_adjpositions.table} PRIMARY KEY (${senses_adjpositions.synsetid}, ${senses_adjpositions.luid})
);
