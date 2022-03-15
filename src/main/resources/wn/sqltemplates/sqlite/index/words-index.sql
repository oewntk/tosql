CREATE UNIQUE INDEX `pk_@{words.table}` ON ${words.table} (${words.wordid});
CREATE UNIQUE INDEX `uk_@{words.table}_@{words.word}` ON ${words.table} (${words.word});
