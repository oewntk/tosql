ALTER TABLE  ${usages.table} ADD CONSTRAINT `pk_@{usages.table}` PRIMARY KEY       (${usages.synsetid},${usages.usageid});
ALTER TABLE  ${usages.table} ADD KEY        `k_@{usages.table}_@{usages.synsetid}` (${usages.synsetid});
ALTER TABLE  ${usages.table} ADD KEY        `k_@{usages.table}_@{usages.luid}`     (${usages.luid});
ALTER TABLE  ${usages.table} ADD KEY        `k_@{usages.table}_@{usages.wordid}`   (${usages.wordid});
