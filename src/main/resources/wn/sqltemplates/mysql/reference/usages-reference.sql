ALTER TABLE ${usages.table} ADD CONSTRAINT `fk_@{usages.table}_@{usages.synsetid}` FOREIGN KEY (${usages.synsetid}) REFERENCES ${synsets.table} (${synsets.synsetid});
ALTER TABLE ${usages.table} ADD CONSTRAINT `fk_@{usages.table}_@{usages.luid}` FOREIGN KEY (${usages.luid}) REFERENCES ${lexes.table} (${lexes.luid});
ALTER TABLE ${usages.table} ADD CONSTRAINT `fk_@{usages.table}_@{usages.wordid}` FOREIGN KEY (${usages.wordid}) REFERENCES ${words.table} (${words.wordid});
