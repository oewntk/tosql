SELECT 'WordNet (cardinality)' AS section;

SELECT 'general' AS subsection;

SELECT 'synset having N words (1..28)' AS comment;
SELECT MIN(N),MAX(N) FROM (
SELECT COUNT(wordid) AS N FROM synsets
LEFT JOIN senses USING(synsetid)
GROUP BY synsetid) AS C;

SELECT 'word having N synset (1..75)' AS comment;
SELECT MIN(N),MAX(N) FROM (
SELECT COUNT(synsetid) AS N FROM words
LEFT JOIN senses USING(wordid)
GROUP BY wordid) AS C;

SELECT 'morphology' AS subsection;

SELECT 'morph having N words (1..3)' AS comment;
SELECT MIN(N),MAX(N) FROM (
SELECT COUNT(wordid) AS N FROM morphs
LEFT JOIN lexes_morphs USING(morphid)
GROUP BY morphid) AS C;

SELECT 'word having N morphs (0..7)' AS comment;
SELECT MIN(N),MAX(N) FROM (
SELECT COUNT(morphid) AS N FROM words
LEFT JOIN lexes_morphs USING(wordid)
GROUP BY wordid) AS C;

SELECT 'verb frames' AS subsection;

SELECT 'sense having N vframes (0..8)' AS comment;
SELECT MIN(N),MAX(N) FROM (
SELECT COUNT(frameid) AS N FROM senses
LEFT JOIN senses_vframes USING(wordid,synsetid)
GROUP BY synsetid,wordid) AS C;

SELECT 'vframe having N senses (1..801)' AS comment;
SELECT MIN(N),MAX(N) FROM (
SELECT COUNT(CONCAT(wordid,synsetid)) AS N FROM vframes
LEFT JOIN senses_vframes USING(frameid)
GROUP BY frameid) AS C;

SELECT 'verbframe sentences' AS comment;

SELECT 'sense having N vtemplates (0..8)' AS comment;
SELECT MIN(N),MAX(N) FROM (
SELECT COUNT(templateid) AS N FROM senses
LEFT JOIN senses_vtemplates USING(wordid,synsetid)
GROUP BY synsetid,wordid) AS C;

SELECT 'vtemplates having N senses (1..801)' AS comment;
SELECT MIN(N),MAX(N) FROM (
SELECT COUNT(CONCAT(wordid,synsetid)) AS N FROM vtemplates
LEFT JOIN senses_vtemplates USING(templateid)
GROUP BY templateid) AS C;

