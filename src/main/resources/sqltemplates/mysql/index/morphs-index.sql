-- ALTER TABLE  lexrelations${morphs.table}lexrelations ADD CONSTRAINT lexrelationspk_${morphs.table}lexrelations                   PRIMARY KEY     (lexrelations${morphs.morphid}lexrelations);
ALTER TABLE     lexrelations${morphs.table}lexrelations ADD CONSTRAINT lexrelationsuk_${morphs.table}_${morphs.morph}lexrelations   UNIQUE KEY      (lexrelations${morphs.morph}lexrelations);
