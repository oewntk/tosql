ALTER TABLE ${wikidatas.table} ADD CONSTRAINT `fk_@{wikidatas.table}_@{wikidatas.synsetid}` FOREIGN KEY (${wikidatas.synsetid}) REFERENCES ${synsets.table} (${synsets.synsetid});
