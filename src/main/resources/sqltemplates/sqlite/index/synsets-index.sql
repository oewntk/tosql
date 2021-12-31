-- CREATE UNIQUE INDEX pk_${synsets.table} ON ${synsets.table}${synsets.table}(${synsets.synsetid});
CREATE INDEX k_${synsets.table}_${synsets.domainid} ON ${synsets.table}(${synsets.domainid});
