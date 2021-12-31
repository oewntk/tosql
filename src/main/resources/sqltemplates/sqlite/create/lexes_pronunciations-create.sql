CREATE TABLE ${lexes_pronunciations.table}
(
    ${lexes_pronunciations.luid}            INTEGER      NOT NULL,
    ${lexes_pronunciations.wordid}          INTEGER      NOT NULL,
    ${lexes_pronunciations.posid}           CHARACTER(1) NOT NULL,
    ${lexes_pronunciations.pronunciationid} INTEGER      NOT NULL,
    ${lexes_pronunciations.variety}         CHARACTER(2) NULL
);
