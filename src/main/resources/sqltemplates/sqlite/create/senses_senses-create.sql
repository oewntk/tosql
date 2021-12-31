CREATE TABLE ${senses_senses.table}
(
    ${senses_senses.synset1id}  INTEGER NOT NULL,
    ${senses_senses.lu1id}      INTEGER NOT NULL,
    ${senses_senses.word1id}    INTEGER NOT NULL,
    ${senses_senses.synset2id}  INTEGER NOT NULL,
    ${senses_senses.lu2id}      INTEGER NOT NULL,
    ${senses_senses.word2id}    INTEGER NOT NULL,
    ${senses_senses.relationid} INTEGER NOT NULL,
    CONSTRAINT pk_${senses_senses.table} PRIMARY KEY (${senses_senses.synset1id}, ${senses_senses.lu1id}, ${senses_senses.synset2id}, ${senses_senses.lu2id}, ${senses_senses.relationid})
);
