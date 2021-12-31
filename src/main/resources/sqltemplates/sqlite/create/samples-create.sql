CREATE TABLE ${samples.table}
(
    ${samples.synsetid} INTEGER NOT NULL,
    ${samples.sampleid} INTEGER NOT NULL,
    ${samples.sample}   TEXT    NOT NULL,
    CONSTRAINT pk_${samples.table} PRIMARY KEY (${samples.synsetid}, ${samples.sampleid})
);
