CREATE TABLE ${relations.table}
(
    ${relations.relationid} INTEGER     NOT NULL,
    ${relations.relation}   VARCHAR(50) NOT NULL,
    ${relations.recurses}   BOOLEAN     NOT NULL,
    CONSTRAINT pk_${relations.table} PRIMARY KEY (${relations.relationid})
);
