CREATE TABLE `${senses.table}` (
    `${senses.senseid}`         INT                                                 NOT NULL,
    `${senses.sensekey}`        VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin    DEFAULT NULL,
    `${senses.synsetid}`        INT                                                 NOT NULL,
    `${senses.luid}`            INT                                                 NOT NULL,
    `${senses.wordid}`          INT                                                 NOT NULL,
    `${senses.casedwordid}`     INT                                                 DEFAULT NULL,
    `${senses.lexid}`           INT                                                 NOT NULL,
    `${senses.sensenum}`        INT                                                 DEFAULT NULL,
    `${senses.tagcount}`        INT                                                 DEFAULT NULL,

    PRIMARY KEY                                                             (`${senses.senseid}`)
    -- UNIQUE KEY `uk_${senses.table}_${senses.sensekey}`                   (`${senses.sensekey}`),
    -- UNIQUE KEY `uk_${senses.table}_${senses.luid}_${senses.synsetid}`    (`${senses.luid}`,`${senses.synsetid}`),
)
DEFAULT CHARSET=utf8mb3;
