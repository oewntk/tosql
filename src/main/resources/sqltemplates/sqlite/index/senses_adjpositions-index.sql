-- CREATE UNIQUE INDEX pk_${senses_adjpositions.table} ON ${senses_adjpositions.table}(${senses_adjpositions.synsetid}, ${senses_adjpositions.luid});
CREATE INDEX k_${senses_adjpositions.synsetid}_${senses_adjpositions.synsetid} ON ${senses_adjpositions.table}(${senses_adjpositions.synsetid});
CREATE INDEX k_${senses_adjpositions.luid}_${senses_adjpositions.luid} ON ${senses_adjpositions.table}(${senses_adjpositions.luid});
CREATE INDEX k_${senses_adjpositions.wordid}_${senses_adjpositions.wordid} ON ${senses_adjpositions.table}(${senses_adjpositions.wordid});
