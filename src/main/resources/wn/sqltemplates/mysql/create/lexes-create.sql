CREATE TABLE ${lexes.table} (
${lexes.luid}        INT                           NOT NULL,
${lexes.posid}       ENUM('n','v','a','r','s','e') NOT NULL,
${lexes.wordid}      INT                           NOT NULL,
${lexes.casedwordid} INT                           DEFAULT NULL
);
