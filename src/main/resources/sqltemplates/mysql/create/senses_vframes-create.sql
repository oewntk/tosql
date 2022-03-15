CREATE TABLE ${senses_vframes.table} (
    ${senses_vframes.synsetid}    INT                                 NOT NULL,
    ${senses_vframes.luid}        INT                                 NOT NULL,
    ${senses_vframes.wordid}      INT                                 NOT NULL,
    ${senses_vframes.frameid}     INT                                 NOT NULL

    -- KEY k_${senses_vframes.table}_${senses_vframes.frameid}       (${senses_vframes.frameid}),
    -- KEY k_${senses_vframes.table}_${senses_vframes.synsetid}      (${senses_vframes.synsetid}),
    -- KEY k_${senses_vframes.table}_${senses_vframes.luid}          (${senses_vframes.luid}),
    -- KEY k_${senses_vframes.table}_${senses_vframes.wordid}        (${senses_vframes.wordid})
);
