-- CREATE UNIQUE INDEX pk_${lexes.table} ON ${lexes.table}(${lexes.luid});
CREATE INDEX k_${lexes.table}_${lexes.wordid} ON ${lexes.table}(${lexes.wordid});
CREATE INDEX k_${lexes.table}_${lexes.casedwordid} ON ${lexes.table}(${lexes.casedwordid});
