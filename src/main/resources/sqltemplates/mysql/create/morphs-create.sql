CREATE TABLE `${morphs.table}` (
    `${morphs.morphid}`   INT                           NOT NULL,
    `${morphs.morph}`     VARCHAR(70)                   NOT NULL,

    PRIMARY KEY                                         (`${morphs.morphid}`)
    -- UNIQUE KEY `uk_${morphs.table}_${morphs.morph}`  (`${morphs.morph}`)
)
DEFAULT CHARSET=utf8mb3;
