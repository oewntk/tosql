CREATE TABLE `${synsets_synsets.table}` (
    `${synsets_synsets.synset1id}`    INT                               NOT NULL,
    `${synsets_synsets.synset2id}`    INT                               NOT NULL,
    `${synsets_synsets.relationid}`   INT                               NOT NULL,

    PRIMARY KEY                                                         (`${synsets_synsets.synset1id}`,`${synsets_synsets.synset2id}`,`${synsets_synsets.relationid}`)
    -- KEY `k_${synsets_synsets.table}_${synsets_synsets.relationid}`   (`${synsets_synsets.relationid}`),
    -- KEY `k_${synsets_synsets.table}_${synsets_synsets.synset1id}`    (`${synsets_synsets.synset1id}`),
    -- KEY `k_${synsets_synsets.table}_${synsets_synsets.synset2id}`    (`${synsets_synsets.synset2id}`)
);
