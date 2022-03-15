ALTER TABLE ${lexes_morphs.table} ADD CONSTRAINT `fk_@{lexes_morphs.table}_@{lexes_morphs.luid}`    FOREIGN KEY (${lexes_morphs.luid})    REFERENCES ${lexes.table}  (${lexes.luid});
ALTER TABLE ${lexes_morphs.table} ADD CONSTRAINT `fk_@{lexes_morphs.table}_@{lexes_morphs.wordid}`  FOREIGN KEY (${lexes_morphs.wordid})  REFERENCES ${words.table}  (${words.wordid});
ALTER TABLE ${lexes_morphs.table} ADD CONSTRAINT `fk_@{lexes_morphs.table}_@{lexes_morphs.morphid}` FOREIGN KEY (${lexes_morphs.morphid}) REFERENCES ${morphs.table} (${morphs.morphid});
ALTER TABLE ${lexes_morphs.table} ADD CONSTRAINT `fk_@{lexes_morphs.table}_@{lexes_morphs.posid}`   FOREIGN KEY (${lexes_morphs.posid})   REFERENCES ${poses.table}  (${poses.posid});
