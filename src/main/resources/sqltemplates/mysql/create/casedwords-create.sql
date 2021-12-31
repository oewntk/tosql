CREATE TABLE `${casedwords.table}` (
    `${casedwords.casedwordid}` INT                                             NOT NULL,
    `${casedwords.wordid}`      INT                                             NOT NULL ,
    `${casedwords.casedword}`   VARCHAR(80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,

    PRIMARY KEY                                                                 (`${casedwords.casedwordid}`)
    -- UNIQUE KEY `uk_${casedwords.table}_${casedwords.casedword}`              (`${casedwords.casedword}`),
    -- KEY        `k_${casedwords.table}_${casedwords.wordid}`                  (`${casedwords.wordid}`)
)
DEFAULT CHARSET=utf8mb3;
