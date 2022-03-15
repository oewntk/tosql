CREATE TABLE ${lexrelations.table}
(
    ${lexrelations.synset1id}  INTEGER NOT NULL,
    ${lexrelations.lu1id}      INTEGER NOT NULL,
    ${lexrelations.word1id}    INTEGER NOT NULL,
    ${lexrelations.synset2id}  INTEGER NOT NULL,
    ${lexrelations.lu2id}      INTEGER NOT NULL,
    ${lexrelations.word2id}    INTEGER NOT NULL,
    ${lexrelations.relationid} INTEGER NOT NULL,
    CONSTRAINT pk_${lexrelations.table} PRIMARY KEY (${lexrelations.synset1id}, ${lexrelations.lu1id}, ${lexrelations.synset2id}, ${lexrelations.lu2id}, ${lexrelations.relationid})
);
