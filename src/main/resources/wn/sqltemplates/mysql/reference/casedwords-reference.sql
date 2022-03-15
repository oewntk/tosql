ALTER TABLE ${casedwords.table} ADD CONSTRAINT `fk_@{casedwords.table}_@{casedwords.wordid}` FOREIGN KEY (${casedwords.wordid}) REFERENCES ${words.table} (${words.wordid});
