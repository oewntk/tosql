ALTER TABLE ${senses_vframes.table} ADD KEY `k_@{senses_vframes.table}_@{senses_vframes.frameid}`  (${senses_vframes.frameid});
ALTER TABLE ${senses_vframes.table} ADD KEY `k_@{senses_vframes.table}_@{senses_vframes.synsetid}` (${senses_vframes.synsetid});
ALTER TABLE ${senses_vframes.table} ADD KEY `k_@{senses_vframes.table}_@{senses_vframes.luid}`     (${senses_vframes.luid});
ALTER TABLE ${senses_vframes.table} ADD KEY `k_@{senses_vframes.table}_@{senses_vframes.wordid}`   (${senses_vframes.wordid});
