CREATE TABLE ${samples.table} (
${samples.sampleid} INT NOT NULL,
${samples.sample} MEDIUMTEXT NOT NULL,
${samples.source} MEDIUMTEXT NULL,
${samples.synsetid} INT NOT NULL,
${samples.luid} INT DEFAULT NULL,
${samples.wordid} INT DEFAULT NULL
)
;
