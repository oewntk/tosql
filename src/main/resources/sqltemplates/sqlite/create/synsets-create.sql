CREATE TABLE ${synsets.table}
(
    ${synsets.synsetid}   INTEGER      NOT NULL,
    ${synsets.posid}      CHARACTER(1) NOT NULL,
    ${synsets.domainid}   INTEGER      NOT NULL,
    ${synsets.definition} TEXT,
    CONSTRAINT pk_${synsets.table} PRIMARY KEY (${synsets.synsetid})
);
