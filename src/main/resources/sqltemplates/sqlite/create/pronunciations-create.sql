CREATE TABLE ${pronunciations.table}
(
    ${pronunciations.pronunciationid} INTEGER     NOT NULL,
    ${pronunciations.pronunciation}   VARCHAR(50) NOT NULL,
    CONSTRAINT pk_${pronunciations.table} PRIMARY KEY (${pronunciations.pronunciationid})
);
