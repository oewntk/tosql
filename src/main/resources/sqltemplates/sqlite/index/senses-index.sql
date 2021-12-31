-- CREATE UNIQUE INDEX pk_${senses.table}_${senses.senseid} ON ${senses.table}(${senses.senseid});
CREATE UNIQUE INDEX uk_${senses.table}_${senses.sensekey} ON ${senses.table}(${senses.sensekey});
CREATE UNIQUE INDEX uk_${senses.table}_${senses.luid}_${senses.synsetid} ON ${senses.table}(${senses.luid},${senses.synsetid});
CREATE INDEX k_${senses.table}_${senses.luid} ON ${senses.table}(${senses.luid});
CREATE INDEX k_${senses.table}_${senses.synsetid} ON ${senses.table}(${senses.synsetid});
CREATE INDEX k_${senses.table}_${senses.wordid} ON ${senses.table}(${senses.wordid});
