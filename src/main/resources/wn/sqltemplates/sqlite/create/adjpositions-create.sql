CREATE TABLE ${adjpositions.table} (
${adjpositions.positionid} CHARACTER (1) CHECK( ${adjpositions.positionid} IN ('a','p','ip') ) NOT NULL,
${adjpositions.position} VARCHAR(24) NOT NULL
)
;
