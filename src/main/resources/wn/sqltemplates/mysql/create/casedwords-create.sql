CREATE TABLE ${casedwords.table} (
${casedwords.casedwordid} INT                                                          NOT NULL,
${casedwords.wordid}      INT                                                          NOT NULL ,
${casedwords.casedword}   VARCHAR(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL
)
DEFAULT CHARSET=utf8mb4;
