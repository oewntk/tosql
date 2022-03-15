CREATE TABLE ${semrelations.table}
(
    ${semrelations.synset1id}  INTEGER NOT NULL,
    ${semrelations.synset2id}  INTEGER NOT NULL,
    ${semrelations.relationid} INTEGER NOT NULL,
    CONSTRAINT pk_${semrelations.table} PRIMARY KEY (${semrelations.synset1id}, ${semrelations.synset2id}, ${semrelations.relationid})
);
