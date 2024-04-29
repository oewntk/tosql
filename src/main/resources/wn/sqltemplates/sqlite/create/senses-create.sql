CREATE TABLE ${senses.table} (
${senses.senseid} INT NOT NULL,
${senses.sensekey} VARCHAR(100) mb4 COLLATE utf8mb4_bin DEFAULT NULL,
${senses.synsetid} INT NOT NULL,
${senses.luid} INT NOT NULL,
${senses.wordid} INT NOT NULL,
${senses.casedwordid} INT DEFAULT NULL,
${senses.lexid} INT NOT NULL,
${senses.sensenum} INT DEFAULT NULL,
${senses.tagcount} INT DEFAULT NULL
)
;
