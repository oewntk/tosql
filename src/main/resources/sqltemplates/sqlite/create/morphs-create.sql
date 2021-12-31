CREATE TABLE ${morphs.table}
(
    ${morphs.morphid} INTEGER     NOT NULL,
    ${morphs.morph}   VARCHAR(70) NOT NULL,
    CONSTRAINT pk_${morphs.table} PRIMARY KEY (${morphs.morphid})
);
