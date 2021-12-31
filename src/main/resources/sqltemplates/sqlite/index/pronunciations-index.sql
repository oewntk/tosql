-- CREATE UNIQUE INDEX pk_${pronunciations.table} ON  ${pronunciations.table}(${pronunciations.pronunciationid});
CREATE UNIQUE INDEX uk_${pronunciations.table}_${pronunciations.pronunciation} ON  ${pronunciations.table}(${pronunciations.pronunciation});
