ALTER TABLE ${samples.table} ADD CONSTRAINT `fk_@{samples.table}_@{samples.synsetid}` FOREIGN KEY (${samples.synsetid}) REFERENCES ${synsets.table} (${synsets.synsetid});
ALTER TABLE ${samples.table} ADD CONSTRAINT `fk_@{samples.table}_@{samples.luid}` FOREIGN KEY (${samples.luid}) REFERENCES ${lexes.table} (${lexes.luid});
ALTER TABLE ${samples.table} ADD CONSTRAINT `fk_@{samples.table}_@{samples.wordid}` FOREIGN KEY (${samples.wordid}) REFERENCES ${words.table} (${words.wordid});
