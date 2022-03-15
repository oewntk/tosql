CREATE TABLE ${words.table} (
    ${words.wordid}   INT                             NOT NULL,
    ${words.word}     VARCHAR(80)                     NOT NULL,

    PRIMARY KEY                                         (${words.wordid})
    -- UNIQUE KEY  uk_${words.table}_${words.word}    (${words.word})
)
DEFAULT CHARSET=utf8mb3;
