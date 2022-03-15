ALTER TABLE ${semrelations.table} ADD CONSTRAINT lexrelationspk_@{semrelations.table}lexrelations PRIMARY KEY               (${semrelations.synset1id},${semrelations.synset2id},${semrelations.relationid});
ALTER TABLE ${semrelations.table} ADD KEY        lexrelationsk_@{semrelations.table}_@{semrelations.relationid}lexrelations (${semrelations.relationid});
ALTER TABLE ${semrelations.table} ADD KEY        lexrelationsk_@{semrelations.table}_@{semrelations.synset1id}lexrelations  (${semrelations.synset1id});
ALTER TABLE ${semrelations.table} ADD KEY        lexrelationsk_@{semrelations.table}_@{semrelations.synset2id}lexrelations  (${semrelations.synset2id});
