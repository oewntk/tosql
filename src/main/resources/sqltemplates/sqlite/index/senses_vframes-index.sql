CREATE INDEX k_${senses_vframes.table}_${senses_vframes.synsetid}_${senses_vframes.synsetid} ON ${senses_vframes.table}(${senses_vframes.synsetid});
CREATE INDEX k_${senses_vframes.table}_${senses_vframes.luid}_${senses_vframes.luid} ON ${senses_vframes.table}(${senses_vframes.luid});
CREATE INDEX k_${senses_vframes.table}_${senses_vframes.wordid}_${senses_vframes.wordid} ON ${senses_vframes.table}(${senses_vframes.wordid});
CREATE INDEX k_${senses_vframes.table}_${senses_vframes.wordid}_${senses_vframes.frameid} ON ${senses_vframes.table}(${senses_vframes.frameid});
