CREATE TABLE ${vframes.table}
(
    ${vframes.frameid} INTEGER NOT NULL,
    ${vframes.frame}   VARCHAR(50) DEFAULT NULL,
    CONSTRAINT pk_${vframes.table} PRIMARY KEY (${vframes.frameid})
);
