CREATE TABLE ${lexes_pronunciations.table} (
    ${lexes_pronunciations.luid}                INT                                   NOT NULL,
    ${lexes_pronunciations.wordid}              INT                                   NOT NULL,
    ${lexes_pronunciations.posid}               ENUM('n','v','a','r','s')             NOT NULL,
    ${lexes_pronunciations.pronunciationid}     INT                                   NOT NULL,
    ${lexes_pronunciations.variety}             VARCHAR(2)                            DEFAULT NULL

    -- KEY k_${lexes_pronunciations.table}_${lexes_pronunciations.pronunciationid}    (${lexes_pronunciations.pronunciationid}),
    -- KEY k_${lexes_pronunciations.table}_${lexes_pronunciations.luid}               (${lexes_pronunciations.luid}),
    -- KEY k_${lexes_pronunciations.table}_${lexes_pronunciations.wordid}             (${lexes_pronunciations.wordid})
);
