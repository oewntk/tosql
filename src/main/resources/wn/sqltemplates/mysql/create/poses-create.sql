CREATE TABLE ${poses.table} (
${poses.posid} ENUM('n','v','a','r','s') NOT NULL,
${poses.pos}   VARCHAR(20)               NOT NULL
)
DEFAULT CHARSET=utf8mb4;
