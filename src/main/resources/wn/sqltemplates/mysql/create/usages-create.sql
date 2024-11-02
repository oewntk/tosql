CREATE TABLE ${usages.table} (
${usages.usageid}   INT        NOT NULL,
${usages.usagenote} MEDIUMTEXT NOT NULL,
${usages.synsetid}  INT        NOT NULL,
${usages.luid}      INT        DEFAULT NULL,
${usages.wordid}    INT        DEFAULT NULL
)
DEFAULT CHARSET=utf8mb4;
