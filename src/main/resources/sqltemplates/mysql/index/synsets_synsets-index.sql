-- ALTER TABLE  `${synsets_synsets.table}` ADD CONSTRAINT `pk_${synsets_synsets.table}`  PRIMARY KEY                    (`${synsets_synsets.synset1id}`,`${synsets_synsets.synset2id}`,`${synsets_synsets.relationid}`);
ALTER TABLE     `${synsets_synsets.table}` ADD KEY        `k_${synsets_synsets.table}_${synsets_synsets.relationid}`    (`${synsets_synsets.relationid}`);
ALTER TABLE     `${synsets_synsets.table}` ADD KEY        `k_${synsets_synsets.table}_${synsets_synsets.synset1id}`     (`${synsets_synsets.synset1id}`);
ALTER TABLE     `${synsets_synsets.table}` ADD KEY        `k_${synsets_synsets.table}_${synsets_synsets.synset2id}`     (`${synsets_synsets.synset2id}`);
