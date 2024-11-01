ALTER TABLE ${ilis.table} ADD CONSTRAINT `fk_@{ilis.table}_@{ilis.synsetid}` FOREIGN KEY (${ilis.synsetid}) REFERENCES ${synsets.table} (${synsets.synsetid});
