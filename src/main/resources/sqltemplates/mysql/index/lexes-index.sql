-- ALTER TABLE  lexrelations${lexes.table}lexrelations ADD CONSTRAINT lexrelationspk_${lexes.table}lexrelations                     PRIMARY KEY     (lexrelations${lexes.luid}lexrelations);
ALTER TABLE     lexrelations${lexes.table}lexrelations ADD KEY        lexrelationsk_${lexes.table}_${lexes.wordid}lexrelations                      (lexrelations${lexes.wordid}lexrelations);
ALTER TABLE     lexrelations${lexes.table}lexrelations ADD KEY        lexrelationsk_${lexes.table}_${lexes.casedwordid}lexrelations                 (lexrelations${lexes.casedwordid}lexrelations);
