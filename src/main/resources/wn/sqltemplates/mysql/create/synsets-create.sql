CREATE TABLE ${synsets.table} (
${synsets.synsetid}   INT                       NOT NULL,
${synsets.posid}      ENUM('n','v','a','r','s') NOT NULL,
${synsets.domainid}   INT                       NOT NULL,
${synsets.definition} MEDIUMTEXT                NOT NULL
 )
DEFAULT CHARSET=utf8mb4;
