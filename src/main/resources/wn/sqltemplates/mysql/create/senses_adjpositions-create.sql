CREATE TABLE ${senses_adjpositions.table} (
${senses_adjpositions.synsetid}   INT                NOT NULL,
${senses_adjpositions.luid}       INT                NOT NULL,
${senses_adjpositions.wordid}     INT                NOT NULL,
${senses_adjpositions.positionid} ENUM('a','p','ip') NOT NULL
);
