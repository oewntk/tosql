-- ALTER TABLE  `${lexes.table}` ADD CONSTRAINT `pk_${lexes.table}`                     PRIMARY KEY     (`${lexes.luid}`);
ALTER TABLE     `${lexes.table}` ADD KEY        `k_${lexes.table}_${lexes.wordid}`                      (`${lexes.wordid}`);
ALTER TABLE     `${lexes.table}` ADD KEY        `k_${lexes.table}_${lexes.casedwordid}`                 (`${lexes.casedwordid}`);
