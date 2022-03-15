ALTER TABLE ${morphs.table} ADD CONSTRAINT `pk_@{morphs.table}`                 PRIMARY KEY (${morphs.morphid});
ALTER TABLE ${morphs.table} ADD CONSTRAINT `uk_@{morphs.table}_@{morphs.morph}` UNIQUE KEY  (${morphs.morph});
