ALTER TABLE  ${samples.table} ADD CONSTRAINT `pk_@{samples.table}` PRIMARY KEY        (${samples.synsetid},${samples.sampleid});
ALTER TABLE  ${samples.table} ADD KEY        `k_@{samples.table}_@{samples.synsetid}` (${samples.synsetid});
ALTER TABLE  ${samples.table} ADD KEY        `k_@{samples.table}_@{samples.luid}`     (${samples.luid});
ALTER TABLE  ${samples.table} ADD KEY        `k_@{samples.table}_@{samples.wordid}`   (${samples.wordid});
