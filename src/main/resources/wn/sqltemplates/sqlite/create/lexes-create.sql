CREATE TABLE ${lexes.table} (
${lexes.luid} INT NOT NULL,
${lexes.posid} CHARACTER (1) CHECK( ${lexes.posid} IN ('n','v','a','r','s') ) NOT NULL,
${lexes.wordid} INT NOT NULL,
${lexes.casedwordid} INT NULL
);
