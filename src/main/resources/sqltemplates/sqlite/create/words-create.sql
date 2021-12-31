CREATE TABLE ${words.table}
(
    ${words.wordid} INTEGER     NOT NULL,
    ${words.word}   VARCHAR(80) NOT NULL,
    CONSTRAINT pk_${words.table} PRIMARY KEY (${words.wordid})
);
