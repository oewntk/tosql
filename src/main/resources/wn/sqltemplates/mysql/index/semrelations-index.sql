ALTER TABLE ${semrelations.table} ADD CONSTRAINT `pk_@{semrelations.table}` PRIMARY KEY               (${semrelations.synset1id},${semrelations.synset2id},${semrelations.relationid});
ALTER TABLE ${semrelations.table} ADD KEY        `k_@{semrelations.table}_@{semrelations.relationid}` (${semrelations.relationid});
ALTER TABLE ${semrelations.table} ADD KEY        `k_@{semrelations.table}_@{semrelations.synset1id}`  (${semrelations.synset1id});
ALTER TABLE ${semrelations.table} ADD KEY        `k_@{semrelations.table}_@{semrelations.synset2id}`  (${semrelations.synset2id});
