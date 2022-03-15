CREATE TABLE ${lexes_pronunciations.table} (
${lexes_pronunciations.luid}            INT                       NOT NULL,
${lexes_pronunciations.wordid}          INT                       NOT NULL,
${lexes_pronunciations.posid}           ENUM('n','v','a','r','s') NOT NULL,
${lexes_pronunciations.pronunciationid} INT                       NOT NULL,
${lexes_pronunciations.variety}         VARCHAR(2)                DEFAULT NULL
);
