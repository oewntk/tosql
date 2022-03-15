CREATE UNIQUE INDEX `pk_@{lexes_morphs.table}` ON ${lexes_morphs.table} (${lexes_morphs.morphid},${lexes_morphs.luid},${lexes_morphs.posid});
CREATE INDEX `k_@{lexes_morphs.table}_@{lexes_morphs.morphid}` ON ${lexes_morphs.table} (${lexes_morphs.morphid});
CREATE INDEX `k_@{lexes_morphs.table}_@{lexes_morphs.luid}` ON ${lexes_morphs.table} (${lexes_morphs.luid});
CREATE INDEX `k_@{lexes_morphs.table}_@{lexes_morphs.wordid}` ON ${lexes_morphs.table} (${lexes_morphs.wordid});
