CREATE TABLE `${pronunciations.table}` (
    `${pronunciations.pronunciationid}`   INT                                   NOT NULL,
    `${pronunciations.pronunciation}`     VARCHAR(50)                           NOT NULL,

    PRIMARY KEY                                                                 (`${pronunciations.pronunciationid}`)
    -- UNIQUE KEY `uk_${pronunciations.table}_${pronunciations.pronunciation}`  (`${pronunciations.pronunciation}`)
)
DEFAULT CHARSET=utf8mb3;
