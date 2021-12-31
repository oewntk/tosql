CREATE TABLE `${senses_adjpositions.table}` (
    `${senses_adjpositions.synsetid}`   INT                                     NOT NULL,
    `${senses_adjpositions.luid}`       INT                                     NOT NULL,
    `${senses_adjpositions.wordid}`     INT                                     NOT NULL,
    `${senses_adjpositions.positionid}` ENUM('a','p','ip')                      NOT NULL,

    PRIMARY KEY                                                                 (`${senses_adjpositions.synsetid}`,`${senses_adjpositions.luid}`)
    -- KEY `k_${senses_adjpositions.table}_${senses_adjpositions.synsetid}`     (`${senses_adjpositions.synsetid}`),
    -- KEY `k_${senses_adjpositions.table}_${senses_adjpositions.luid}`         (`${senses_adjpositions.luid}`),
    -- KEY `k_${senses_adjpositions.table}_${senses_adjpositions.wordid}`       (`${senses_adjpositions.wordid}`)
);
