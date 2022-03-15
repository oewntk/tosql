ALTER TABLE ${lexes.table} ADD CONSTRAINT `fk_@{lexes.table}_@{lexes.posid}`       FOREIGN KEY (${lexes.posid})       REFERENCES ${poses.table}      (${poses.posid});
ALTER TABLE ${lexes.table} ADD CONSTRAINT `fk_@{lexes.table}_@{lexes.wordid}`      FOREIGN KEY (${lexes.wordid})      REFERENCES ${words.table}      (${words.wordid});
ALTER TABLE ${lexes.table} ADD CONSTRAINT `fk_@{lexes.table}_@{lexes.casedwordid}` FOREIGN KEY (${lexes.casedwordid}) REFERENCES ${casedwords.table} (${casedwords.casedwordid});
