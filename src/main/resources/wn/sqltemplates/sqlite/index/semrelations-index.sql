CREATE UNIQUE INDEX `pk_@{semrelations.table}` ON ${semrelations.table} (${semrelations.synset1id},${semrelations.synset2id},${semrelations.relationid});
CREATE INDEX `k_@{semrelations.table}_@{semrelations.relationid}` ON ${semrelations.table} (${semrelations.relationid});
CREATE INDEX `k_@{semrelations.table}_@{semrelations.synset1id}` ON ${semrelations.table} (${semrelations.synset1id});
CREATE INDEX `k_@{semrelations.table}_@{semrelations.synset2id}` ON ${semrelations.table} (${semrelations.synset2id});
