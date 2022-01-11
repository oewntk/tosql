CREATE OR REPLACE VIEW samplesets AS SELECT synsetid,GROUP_CONCAT(DISTINCT samples.sample ORDER BY sampleid SEPARATOR '|') AS sampleset FROM samples GROUP BY synsetid;

CREATE OR REPLACE VIEW dict AS SELECT * FROM words LEFT JOIN senses s USING (wordid) LEFT JOIN casedwords USING (wordid,casedwordid) LEFT JOIN synsets USING (synsetid) LEFT JOIN samplesets USING (synsetid);
