-- ALTER TABLE  `${vframes.table}` ADD CONSTRAINT `pk_${vframes.table}`                     PRIMARY KEY     (`${vframes.frameid}`);
ALTER TABLE     `${vframes.table}` ADD CONSTRAINT `uk_${vframes.table}_${vframes.frame}`    UNIQUE KEY      (`${vframes.frame}`);
