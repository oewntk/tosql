CREATE TABLE ${lexes_morphs.table} (
${lexes_morphs.luid} INT NOT NULL,
${lexes_morphs.wordid} INT NOT NULL,
${lexes_morphs.posid} CHARACTER (1) CHECK( ${lexes_morphs.posid} IN ('n','v','a','r','s') ) NOT NULL,
${lexes_morphs.morphid} INT NOT NULL
);
