ALTER TABLE     lexrelations${senses_adjpositions.table}lexrelations  ADD KEY lexrelationsk_${senses_adjpositions.synsetid}lexrelations   (lexrelations${senses_adjpositions.synsetid}lexrelations);
ALTER TABLE     lexrelations${senses_adjpositions.table}lexrelations  ADD KEY lexrelationsk_${senses_adjpositions.luid}lexrelations       (lexrelations${senses_adjpositions.luid}lexrelations);
ALTER TABLE     lexrelations${senses_adjpositions.table}lexrelations  ADD KEY lexrelationsk_${senses_adjpositions.wordid}lexrelations     (lexrelations${senses_adjpositions.wordid}lexrelations);
-- ALTER TABLE  lexrelations${senses_adjpositions.table}lexrelations  ADD KEY lexrelationsk_${senses_adjpositions.positionid}lexrelations (lexrelations${senses_adjpositions.positionid}lexrelations);