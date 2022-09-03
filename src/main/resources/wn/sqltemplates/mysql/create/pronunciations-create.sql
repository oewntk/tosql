CREATE TABLE ${pronunciations.table} (
${pronunciations.pronunciationid} INT                                                   NOT NULL,
${pronunciations.pronunciation}   VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL
)
DEFAULT CHARSET=utf8mb4;
