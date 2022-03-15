CREATE TABLE ${casedwords.table} (
    ${casedwords.casedwordid} INT                                             NOT NULL,
    ${casedwords.wordid}      INT                                             NOT NULL ,
    ${casedwords.casedword}   VARCHAR(80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL
)
DEFAULT CHARSET=utf8mb3;
