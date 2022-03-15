ALTER TABLE ${senses.table} ADD CONSTRAINT `pk_@{senses.table}`                                   PRIMARY KEY (${senses.senseid});
ALTER TABLE ${senses.table} ADD CONSTRAINT `uk_@{senses.table}_@{senses.sensekey}`                UNIQUE KEY  (${senses.sensekey});
ALTER TABLE ${senses.table} ADD CONSTRAINT `uk_@{senses.table}_@{senses.luid}_@{senses.sensekey}` UNIQUE KEY  (${senses.luid},${senses.sensekey});
ALTER TABLE ${senses.table} ADD CONSTRAINT `uk_@{senses.table}_@{senses.luid}_@{senses.synsetid}` UNIQUE KEY  (${senses.luid},${senses.synsetid});
ALTER TABLE ${senses.table} ADD KEY        `k_@{senses.table}_@{senses.luid}`                                 (${senses.luid});
ALTER TABLE ${senses.table} ADD KEY        `k_@{senses.table}_@{senses.wordid}`                               (${senses.wordid});
ALTER TABLE ${senses.table} ADD KEY        `k_@{senses.table}_@{senses.casedwordid}`                          (${senses.casedwordid});
ALTER TABLE ${senses.table} ADD KEY        `k_@{senses.table}_@{senses.synsetid}`                             (${senses.synsetid});
