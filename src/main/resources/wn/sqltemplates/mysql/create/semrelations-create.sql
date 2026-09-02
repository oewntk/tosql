CREATE TABLE ${semrelations.table} (
${semrelations.synset1id}  INT NOT NULL,
${semrelations.synset2id}  INT NOT NULL,
${semrelations.lu2id}      INT DEFAULT NULL,
${semrelations.word2id}    INT DEFAULT NULL,
${semrelations.relationid} INT NOT NULL
);
