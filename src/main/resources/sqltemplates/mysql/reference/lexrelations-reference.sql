ALTER TABLE ${lexrelations.table} ADD CONSTRAINT `fk_@{lexrelations.table}_@{lexrelations.synset1id}`  FOREIGN KEY (${lexrelations.synset1id})  REFERENCES ${synsets.table}  (${synsets.synsetid});
ALTER TABLE ${lexrelations.table} ADD CONSTRAINT `fk_@{lexrelations.table}_@{lexrelations.synset2id}`  FOREIGN KEY (${lexrelations.synset2id})  REFERENCES ${synsets.table}  (${synsets.synsetid});
ALTER TABLE ${lexrelations.table} ADD CONSTRAINT `fk_@{lexrelations.table}_@{lexrelations.relationid}` FOREIGN KEY (${lexrelations.relationid}) REFERENCES ${relations.table}(${relations.relationid});
ALTER TABLE ${lexrelations.table} ADD CONSTRAINT `fk_@{lexrelations.table}_@{lexrelations.lu1id}`      FOREIGN KEY (${lexrelations.lu1id})      REFERENCES ${lexes.table}    (${lexes.luid});
ALTER TABLE ${lexrelations.table} ADD CONSTRAINT `fk_@{lexrelations.table}_@{lexrelations.lu2id}`      FOREIGN KEY (${lexrelations.lu2id})      REFERENCES ${lexes.table}    (${lexes.luid});
ALTER TABLE ${lexrelations.table} ADD CONSTRAINT `fk_@{lexrelations.table}_@{lexrelations.word1id}`    FOREIGN KEY (${lexrelations.word1id})    REFERENCES ${words.table}    (${words.wordid});
ALTER TABLE ${lexrelations.table} ADD CONSTRAINT `fk_@{lexrelations.table}_@{lexrelations.word2id}`    FOREIGN KEY (${lexrelations.word2id})    REFERENCES ${words.table}    (${words.wordid});
