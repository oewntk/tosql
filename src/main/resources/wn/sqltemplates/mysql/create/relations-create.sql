CREATE TABLE ${relations.table} (
${relations.relationid} INT         NOT NULL,
${relations.relation}   VARCHAR(50) NOT NULL,
${relations.recurses}   TINYINT(1)  NOT NULL
)
DEFAULT CHARSET=utf8mb3;
