CREATE TABLE ${synsets.table} (
${synsets.synsetid} INT NOT NULL,
${synsets.posid} CHARACTER (1) CHECK( ${synsets.posid} IN ('n','v','a','r','s') ) NOT NULL,
${synsets.domainid} INT NOT NULL,
${synsets.definition} MEDIUMTEXT NOT NULL
)
;
