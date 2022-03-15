-- ALTER TABLE lexrelations${words.table}lexrelations ADD CONSTRAINT lexrelationspk_${words.table}lexrelations                 PRIMARY KEY      (lexrelations${words.wordid}lexrelations);
ALTER TABLE    lexrelations${words.table}lexrelations ADD CONSTRAINT lexrelationsuk_${words.table}_${words.word}lexrelations   UNIQUE KEY       (lexrelations${words.word}lexrelations);
