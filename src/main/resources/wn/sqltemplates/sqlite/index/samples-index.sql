CREATE UNIQUE INDEX `pk_@{samples.table}` ON ${samples.table} (${samples.synsetid},${samples.sampleid});
CREATE INDEX `k_@{samples.table}_@{samples.synsetid}` ON ${samples.table} (${samples.synsetid});
CREATE INDEX `k_@{samples.table}_@{samples.luid}` ON ${samples.table} (${samples.luid});
CREATE INDEX `k_@{samples.table}_@{samples.wordid}` ON ${samples.table} (${samples.wordid});
