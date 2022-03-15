ALTER TABLE ${senses_adjpositions.table} ADD KEY `k_@{senses_adjpositions.table}_@{senses_adjpositions.synsetid}` (${senses_adjpositions.synsetid});
ALTER TABLE ${senses_adjpositions.table} ADD KEY `k_@{senses_adjpositions.table}_@{senses_adjpositions.luid}`     (${senses_adjpositions.luid});
ALTER TABLE ${senses_adjpositions.table} ADD KEY `k_@{senses_adjpositions.table}_@{senses_adjpositions.wordid}`   (${senses_adjpositions.wordid});
