CREATE TABLE ${senses_adjpositions.table} (
${senses_adjpositions.synsetid} INT NOT NULL,
${senses_adjpositions.luid} INT NOT NULL,
${senses_adjpositions.wordid} INT NOT NULL,
${senses_adjpositions.positionid} CHARACTER (1) CHECK( ${senses_adjpositions.positionid} IN ('a','p','ip') ) NOT NULL
);
