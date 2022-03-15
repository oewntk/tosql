CREATE TABLE ${poses.table} (
${poses.posid} CHARACTER (1) CHECK( ${poses.posid} IN ('n','v','a','r','s') ) NOT NULL,
${poses.pos} VARCHAR(20) NOT NULL
)
;
