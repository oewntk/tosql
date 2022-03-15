ALTER TABLE ${senses_vframes.table} ADD CONSTRAINT `fk_@{senses_vframes.table}_@{senses_vframes.synsetid}` FOREIGN KEY (${senses_vframes.synsetid}) REFERENCES ${synsets.table} (${synsets.synsetid});
ALTER TABLE ${senses_vframes.table} ADD CONSTRAINT `fk_@{senses_vframes.table}_@{senses_vframes.luid}`     FOREIGN KEY (${senses_vframes.luid})     REFERENCES ${lexes.table}   (${lexes.luid});
ALTER TABLE ${senses_vframes.table} ADD CONSTRAINT `fk_@{senses_vframes.table}_@{senses_vframes.wordid}`   FOREIGN KEY (${senses_vframes.wordid})   REFERENCES ${words.table}   (${words.wordid});
ALTER TABLE ${senses_vframes.table} ADD CONSTRAINT `fk_@{senses_vframes.table}_@{senses_vframes.frameid}`  FOREIGN KEY (${senses_vframes.frameid})  REFERENCES ${vframes.table} (${vframes.frameid});
