-- ALTER TABLE  `${pronunciations.table}` ADD CONSTRAINT `pk_${pronunciations.table}`                                   PRIMARY KEY     (`${pronunciations.pronunciationid}`);
ALTER TABLE     `${pronunciations.table}` ADD CONSTRAINT `uk_${pronunciations.table}_${pronunciations.pronunciation}`   UNIQUE KEY      (`${pronunciations.pronunciation}`);
