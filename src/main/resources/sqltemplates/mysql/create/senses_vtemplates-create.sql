CREATE TABLE ${senses_vtemplates.table} (
    ${senses_vtemplates.synsetid}     INT                                 NOT NULL,
    ${senses_vtemplates.luid}         INT                                 NOT NULL,
    ${senses_vtemplates.wordid}       INT                                 NOT NULL,
    ${senses_vtemplates.templateid}   INT                                 NOT NULL

    -- KEY k_${senses_vtemplates.table}_${senses_vtemplates.templateid}   (${senses_vtemplates.templateid}),
    -- KEY k_${senses_vtemplates.table}_${senses_vtemplates.synsetid}     (${senses_vtemplates.synsetid}),
    -- KEY k_${senses_vtemplates.table}_${senses_vtemplates.luid}         (${senses_vtemplates.luid}),
    -- KEY k_${senses_vtemplates.table}_${senses_vtemplates.wordid}       (${senses_vtemplates.wordid})
);
