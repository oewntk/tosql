CREATE TABLE `${samples.table}` (
    `${samples.sampleid}`     INT                               NOT NULL,
    `${samples.sample}`       MEDIUMTEXT                        NOT NULL,
    `${samples.synsetid}`     INT                               NOT NULL,

    PRIMARY KEY                                                 (`${samples.synsetid}`,`${samples.sampleid}`)
    -- KEY `k_${samples.table}_${samples.synsetid}`             (`${samples.synsetid}`)
)
DEFAULT CHARSET=utf8mb3;
