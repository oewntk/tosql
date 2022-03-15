CREATE UNIQUE INDEX `pk_@{vtemplates.table}` ON ${vtemplates.table} (${vtemplates.templateid});
CREATE UNIQUE INDEX `uk_@{vtemplates.table}_@{vtemplates.template}` ON ${vtemplates.table} (${vtemplates.template});
