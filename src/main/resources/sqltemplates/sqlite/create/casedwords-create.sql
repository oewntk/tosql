CREATE TABLE ${casedwords.table}
(
    ${casedwords.casedwordid} INTEGER NOT NULL,
    ${casedwords.wordid}      INTEGER NOT NULL,
    ${casedwords.casedword}   VARCHAR(80),
    CONSTRAINT pk_${casedwords.table} PRIMARY KEY (${casedwords.casedwordid})
);
