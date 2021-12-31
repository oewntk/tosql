ALTER TABLE `${lexes_pronunciations.table}` ADD KEY `k_${lexes_pronunciations.table}_${lexes_pronunciations.pronunciationid}`   (`${lexes_pronunciations.pronunciationid}`);
ALTER TABLE `${lexes_pronunciations.table}` ADD KEY `k_${lexes_pronunciations.table}_${lexes_pronunciations.luid}`              (`${lexes_pronunciations.luid}`);
ALTER TABLE `${lexes_pronunciations.table}` ADD KEY `k_${lexes_pronunciations.table}_${lexes_pronunciations.wordid}`            (`${lexes_pronunciations.wordid}`);
