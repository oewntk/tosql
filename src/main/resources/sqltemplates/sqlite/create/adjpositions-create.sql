CREATE TABLE ${adjpositions.table}
(
    ${adjpositions.positionid} CHARACTER(2) NOT NULL,
    ${adjpositions.position}   VARCHAR(24)  NOT NULL,
    CONSTRAINT pk_${adjpositions.table} PRIMARY KEY (${adjpositions.positionid})
);
