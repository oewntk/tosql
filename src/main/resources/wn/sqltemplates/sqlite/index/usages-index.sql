CREATE UNIQUE INDEX `pk_@{usages.table}` ON ${usages.table} (${usages.synsetid},${usages.usageid});
CREATE INDEX `k_@{usages.table}_@{usages.synsetid}` ON ${usages.table} (${usages.synsetid});
CREATE INDEX `k_@{usages.table}_@{usages.luid}` ON ${usages.table} (${usages.luid});
CREATE INDEX `k_@{usages.table}_@{usages.wordid}` ON ${usages.table} (${usages.wordid});
