-- CREATE UNIQUE INDEX pk_${casedwords.table} ON ${casedwords.table}(${casedwords.casedwordid});
CREATE UNIQUE INDEX uk_${casedwords.table}_${casedwords.casedword} ON ${casedwords.table}(${casedwords.casedword});
CREATE INDEX k_${casedwords.table}_${casedwords.wordid} ON ${casedwords.table}(${casedwords.wordid});
CREATE INDEX k_${casedwords.table}_${casedwords.wordid}_${casedwords.casedwordid} ON ${casedwords.table}(${casedwords.wordid},${casedwords.casedwordid});
