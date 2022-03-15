CREATE UNIQUE INDEX `pk_@{relations.table}` ON ${relations.table} (${relations.relationid});
ALTER TABLE ${relations.table} ADD CONSTRAINT `uk_@{relations.table}_@{relations.relation}` UNIQUE INDEX (${relations.relation});
