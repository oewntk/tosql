ALTER TABLE ${samples.table} ADD CONSTRAINT `fk_@{samples.table}_@{samples.synsetid}` FOREIGN KEY (${samples.synsetid}) REFERENCES ${synsets.table} (${synsets.synsetid});
