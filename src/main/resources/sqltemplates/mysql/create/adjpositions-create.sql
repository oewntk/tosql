CREATE TABLE `${adjpositions.table}` (
    `${adjpositions.positionid}`  ENUM('a','p','ip')    NOT NULL,
    `${adjpositions.position}`    VARCHAR(24)           NOT NULL,

    PRIMARY KEY                                         (`${adjpositions.positionid}`)
)
DEFAULT CHARSET=utf8mb3;
