CREATE UNIQUE INDEX `pk_@{vframes.table}` ON ${vframes.table} (${vframes.frameid});
CREATE UNIQUE INDEX `uk_@{vframes.table}_@{vframes.frame}` ON ${vframes.table} (${vframes.frame});
