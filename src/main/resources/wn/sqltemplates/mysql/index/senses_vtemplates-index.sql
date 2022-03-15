ALTER TABLE ${senses_vtemplates.table} ADD KEY `k_@{senses_vtemplates.table}_@{senses_vtemplates.templateid}` (${senses_vtemplates.templateid});
ALTER TABLE ${senses_vtemplates.table} ADD KEY `k_@{senses_vtemplates.table}_@{senses_vtemplates.synsetid}`   (${senses_vtemplates.synsetid});
ALTER TABLE ${senses_vtemplates.table} ADD KEY `k_@{senses_vtemplates.table}_@{senses_vtemplates.luid}`       (${senses_vtemplates.luid});
ALTER TABLE ${senses_vtemplates.table} ADD KEY `k_@{senses_vtemplates.table}_@{senses_vtemplates.wordid}`     (${senses_vtemplates.wordid});
