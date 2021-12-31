-- CREATE UNIQUE INDEX pk_${relations.table} ON  ${relations.table}(${relations.relationid});
CREATE UNIQUE INDEX uk_${relations.table}_${relations.relation} ON  ${relations.table}(${relations.relation});
