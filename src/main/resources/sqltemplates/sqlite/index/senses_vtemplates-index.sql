CREATE INDEX k_${senses_vtemplates.table}_${senses_vtemplates.synsetid}_${senses_vtemplates.synsetid} ON ${senses_vtemplates.table}(${senses_vtemplates.synsetid});
CREATE INDEX k_${senses_vtemplates.table}_${senses_vtemplates.luid}_${senses_vtemplates.luid} ON ${senses_vtemplates.table}(${senses_vtemplates.luid});
CREATE INDEX k_${senses_vtemplates.table}_${senses_vtemplates.wordid}_${senses_vtemplates.wordid} ON ${senses_vtemplates.table}(${senses_vtemplates.wordid});
CREATE INDEX k_${senses_vtemplates.table}_${senses_vtemplates.wordid}_${senses_vtemplates.templateid} ON ${senses_vtemplates.table}(${senses_vtemplates.templateid});
