ALTER TABLE ${relations.table} ADD CONSTRAINT `pk_@{relations.table}`                       PRIMARY KEY  (${relations.relationid});
ALTER TABLE ${relations.table} ADD CONSTRAINT `uk_@{relations.table}_@{relations.relation}` UNIQUE KEY   (${relations.relation});
