CREATE TABLE ${lexes_morphs.table} (
    ${lexes_morphs.luid}          INT                         NOT NULL,
    ${lexes_morphs.wordid}        INT                         NOT NULL,
    ${lexes_morphs.posid}         ENUM('n','v','a','r','s')   NOT NULL,
    ${lexes_morphs.morphid}       INT                         NOT NULL

    -- KEY k_${lexes_morphs.table}_${lexes_morphs.morphid}    (${lexes_morphs.morphid}),
    -- KEY k_${lexes_morphs.table}_${lexes_morphs.luid}       (${lexes_morphs.luid}),
    -- KEY k_${lexes_morphs.table}_${lexes_morphs.wordid}     (${lexes_morphs.wordid})
);
