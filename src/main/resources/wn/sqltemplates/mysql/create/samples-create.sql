CREATE TABLE ${samples.table} (
${samples.sampleid} INT        NOT NULL,
${samples.sample}   MEDIUMTEXT NOT NULL,
${samples.source}   MEDIUMTEXT DEFAULT NULL,
${samples.synsetid} INT        NOT NULL,
${samples.luid}     INT        DEFAULT NULL,
${samples.wordid}   INT        DEFAULT NULL
)
DEFAULT CHARSET=utf8mb4;
