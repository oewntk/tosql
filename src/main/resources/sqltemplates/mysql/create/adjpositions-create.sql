CREATE TABLE ${adjpositions.table} (
${adjpositions.positionid} ENUM('a','p','ip') NOT NULL,
${adjpositions.position}   VARCHAR(24)        NOT NULL
)
DEFAULT CHARSET=utf8mb3;
