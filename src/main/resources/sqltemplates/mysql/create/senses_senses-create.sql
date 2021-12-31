CREATE TABLE `${senses_senses.table}` (
    `${senses_senses.synset1id}`                                                            INT NOT NULL,
    `${senses_senses.lu1id}`                                                                INT NOT NULL,
    `${senses_senses.word1id}`                                                              INT NOT NULL,
    `${senses_senses.synset2id}`                                                            INT NOT NULL,
    `${senses_senses.lu2id}`                                                                INT NOT NULL,
    `${senses_senses.word2id}`                                                              INT NOT NULL,
    `${senses_senses.relationid}`                                                           INT NOT NULL,

    PRIMARY KEY                                                                             (`${senses_senses.synset1id}`,`${senses_senses.lu1id}`,`${senses_senses.lu2id}`,`${senses_senses.synset2id}`,`${senses_senses.relationid}`)
    -- KEY `k_${senses_senses.table}_${senses_senses.relationid}`                           (`${senses_senses.relationid}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.synset1id}`                            (`${senses_senses.synset1id}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.lu1id}`                                (`${senses_senses.lu1id}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.word1id}`                              (`${senses_senses.word1id}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.synset2id}`                            (`${senses_senses.synset2id}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.lu2id}`                                (`${senses_senses.lu2id}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.word2id}`                              (`${senses_senses.word2id}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.word1id}_${senses_senses.synset1id}`   (`${senses_senses.word1id}`, `${senses_senses.synset1id}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.lu1id}_${senses_senses.synset1id}`     (`${senses_senses.lu1id}`, `${senses_senses.synset1id}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.word2id}_${senses_senses.synset2id}`   (`${senses_senses.word2id}`, `${senses_senses.synset2id}`),
    -- KEY `k_${senses_senses.table}_${senses_senses.lu2id}_${senses_senses.synset2id}`     (`${senses_senses.lu2id}`, `${senses_senses.synset2id}`)
);
