ALTER TABLE ${vtemplates.table} ADD CONSTRAINT `pk_@{vtemplates.table}`                        PRIMARY KEY (${vtemplates.templateid});
ALTER TABLE ${vtemplates.table} ADD CONSTRAINT `uk_@{vtemplates.table}_@{vtemplates.template}` UNIQUE KEY  (${vtemplates.template}(64));
