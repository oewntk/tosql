CREATE TABLE ${lexes_pronunciations.table} (
${lexes_pronunciations.luid} INT NOT NULL,
${lexes_pronunciations.wordid} INT NOT NULL,
${lexes_pronunciations.posid} CHARACTER (1) CHECK( ${lexes_pronunciations.posid} IN ('n','v','a','r','s') ) NOT NULL,
${lexes_pronunciations.pronunciationid} INT NOT NULL,
${lexes_pronunciations.variety} VARCHAR(2) DEFAULT NULL
);
