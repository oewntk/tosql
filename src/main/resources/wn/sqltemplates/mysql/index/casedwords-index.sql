ALTER TABLE ${casedwords.table} ADD CONSTRAINT `pk_@{casedwords.table}`                         PRIMARY KEY           (${casedwords.casedwordid});
ALTER TABLE ${casedwords.table} ADD CONSTRAINT `uk_@{casedwords.table}_@{casedwords.casedword}` UNIQUE KEY            (${casedwords.casedword});
ALTER TABLE ${casedwords.table} ADD KEY        `k_@{casedwords.table}_@{casedwords.wordid}`                           (${casedwords.wordid});
ALTER TABLE ${casedwords.table} ADD KEY        `k_@{casedwords.table}_@{casedwords.wordid}_@{casedwords.casedwordid}` (${casedwords.wordid},${casedwords.casedwordid});
