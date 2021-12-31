-- CREATE UNIQUE INDEX pk_${synsets_synsets.table} ON ${synsets_synsets.table}(${synsets_synsets.synset1id},${synsets_synsets.synset2id},${synsets_synsets.relationid});
CREATE INDEX k_${synsets_synsets.table}_${synsets_synsets.synset1id} ON ${synsets_synsets.table}(${synsets_synsets.synset1id});
CREATE INDEX k_${synsets_synsets.table}_${synsets_synsets.synset2id} ON ${synsets_synsets.table}(${synsets_synsets.synset2id});
