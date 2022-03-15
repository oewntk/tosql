CREATE UNIQUE INDEX `pk_@{morphs.table}` ON ${morphs.table} (${morphs.morphid});
CREATE UNIQUE INDEX `uk_@{morphs.table}_@{morphs.morph}` ON ${morphs.table} (${morphs.morph});
