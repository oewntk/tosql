-- ALTER TABLE  `${lexes_morphs.table}` ADD CONSTRAINT  `pk_${lexes_morphs.table}`                          PRIMARY KEY     (`${lexes_morphs.morphid}`,`${lexes_morphs.luid}`,`${lexes_morphs.posid}`);
ALTER TABLE     `${lexes_morphs.table}` ADD KEY         `k_${lexes_morphs.table}_${lexes_morphs.morphid}`                   (`${lexes_morphs.morphid}`);
ALTER TABLE     `${lexes_morphs.table}` ADD KEY         `k_${lexes_morphs.table}_${lexes_morphs.luid}`                      (`${lexes_morphs.luid}`);
ALTER TABLE     `${lexes_morphs.table}` ADD KEY         `k_${lexes_morphs.table}_${lexes_morphs.wordid}`                    (`${lexes_morphs.wordid}`);
