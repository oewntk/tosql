CREATE TABLE ${poses.table}
(
    ${poses.posid} CHARACTER(1) NOT NULL,
    ${poses.pos}   VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_${poses.table} PRIMARY KEY (${poses.posid})
);
