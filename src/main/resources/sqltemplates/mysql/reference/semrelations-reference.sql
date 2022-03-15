ALTER TABLE ${semrelations.table} ADD CONSTRAINT `fk_@{semrelations.table}_@{semrelations.synset1id}`  FOREIGN KEY (${semrelations.synset1id})  REFERENCES ${synsets.table}  (${synsets.synsetid});
ALTER TABLE ${semrelations.table} ADD CONSTRAINT `fk_@{semrelations.table}_@{semrelations.synset2id}`  FOREIGN KEY (${semrelations.synset2id})  REFERENCES ${synsets.table}  (${synsets.synsetid});
ALTER TABLE ${semrelations.table} ADD CONSTRAINT `fk_@{semrelations.table}_@{semrelations.relationid}` FOREIGN KEY (${semrelations.relationid}) REFERENCES ${relations.table}(${relations.relationid});
