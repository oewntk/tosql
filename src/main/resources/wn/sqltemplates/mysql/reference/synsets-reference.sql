ALTER TABLE ${synsets.table} ADD CONSTRAINT `fk_@{synsets.table}_@{synsets.posid}` FOREIGN KEY (${synsets.posid}) REFERENCES ${poses.table} (${poses.posid});
