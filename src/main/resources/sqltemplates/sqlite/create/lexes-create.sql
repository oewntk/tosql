CREATE TABLE ${lexes.table}
(
    ${lexes.luid}        INTEGER      NOT NULL,
    ${lexes.posid}       CHARACTER(1) NOT NULL,
    ${lexes.wordid}      INTEGER      NOT NULL,
    ${lexes.casedwordid} INTEGER      NULL,
    CONSTRAINT pk_${lexes.table} PRIMARY KEY (${lexes.luid})
);
