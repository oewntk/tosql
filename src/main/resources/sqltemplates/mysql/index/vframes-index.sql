-- ALTER TABLE  lexrelations${vframes.table}lexrelations ADD CONSTRAINT lexrelationspk_${vframes.table}lexrelations                     PRIMARY KEY     (lexrelations${vframes.frameid}lexrelations);
ALTER TABLE     lexrelations${vframes.table}lexrelations ADD CONSTRAINT lexrelationsuk_${vframes.table}_${vframes.frame}lexrelations    UNIQUE KEY      (lexrelations${vframes.frame}lexrelations);
