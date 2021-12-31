CREATE TABLE ${synsets_synsets.table}
(
    ${synsets_synsets.synset1id}  INTEGER NOT NULL,
    ${synsets_synsets.synset2id}  INTEGER NOT NULL,
    ${synsets_synsets.relationid} INTEGER NOT NULL,
    CONSTRAINT pk_${synsets_synsets.table} PRIMARY KEY (${synsets_synsets.synset1id}, ${synsets_synsets.synset2id}, ${synsets_synsets.relationid})
);
