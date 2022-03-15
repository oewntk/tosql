CREATE UNIQUE INDEX `pk_@{senses.table}` ON ${senses.table} (${senses.senseid});
CREATE UNIQUE INDEX `uk_@{senses.table}_@{senses.sensekey}` ON ${senses.table} (${senses.sensekey});
CREATE UNIQUE INDEX `uk_@{senses.table}_@{senses.luid}_@{senses.sensekey}` ON ${senses.table} (${senses.luid},${senses.sensekey});
CREATE UNIQUE INDEX `uk_@{senses.table}_@{senses.luid}_@{senses.synsetid}` ON ${senses.table} (${senses.luid},${senses.synsetid});
CREATE INDEX `k_@{senses.table}_@{senses.luid}` ON ${senses.table} (${senses.luid});
CREATE INDEX `k_@{senses.table}_@{senses.wordid}` ON ${senses.table} (${senses.wordid});
CREATE INDEX `k_@{senses.table}_@{senses.casedwordid}` ON ${senses.table} (${senses.casedwordid});
CREATE INDEX `k_@{senses.table}_@{senses.synsetid}` ON ${senses.table} (${senses.synsetid});
