CREATE UNIQUE INDEX `pk_@{samples.table}` ON ${samples.table} (${samples.synsetid},${samples.sampleid});
CREATE INDEX `k_@{samples.table}_@{samples.synsetid}` ON ${samples.table} (${samples.synsetid});
