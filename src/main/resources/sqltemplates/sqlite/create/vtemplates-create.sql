CREATE TABLE ${vtemplates.table}
(
    ${vtemplates.templateid} INTEGER NOT NULL,
    ${vtemplates.template}   TEXT DEFAULT NULL,
    CONSTRAINT pk_${vtemplates.table} PRIMARY KEY (${vtemplates.templateid})
);
