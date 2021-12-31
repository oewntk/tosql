ALTER TABLE `${senses.table}` ADD CONSTRAINT `fk_${senses.table}_${senses.luid}`            FOREIGN KEY     (`${senses.luid}`)          REFERENCES  `${lexes.table}`        (`${lexes.luid}`);
ALTER TABLE `${senses.table}` ADD CONSTRAINT `fk_${senses.table}_${senses.wordid}`          FOREIGN KEY     (`${senses.wordid}`)        REFERENCES  `${words.table}`        (`${words.wordid}`);
ALTER TABLE `${senses.table}` ADD CONSTRAINT `fk_${senses.table}_${senses.casedwordid}`     FOREIGN KEY     (`${senses.casedwordid}`)   REFERENCES  `${casedwords.table}`   (`${casedwords.casedwordid}`);
ALTER TABLE `${senses.table}` ADD CONSTRAINT `fk_${senses.table}_${senses.synsetid}`        FOREIGN KEY     (`${senses.synsetid}`)      REFERENCES  `${synsets.table}`      (`${synsets.synsetid}`);
