SELECT 'WordNet (legacy)' AS section;

SELECT 'legacy' AS subsection;

-- legacy
SELECT 'LEGACY QUERY' AS comment;
SELECT word,posid,sensekey,synsetid,definition
FROM words
INNER JOIN senses USING(wordid)
INNER JOIN synsets USING (synsetid)
WHERE word = 'bow'
ORDER BY sensekey;

-- new with lexes
SELECT 'NEW QUERY' AS comment;
SELECT word,l.posid,sensekey,synsetid,definition
FROM words AS w
INNER JOIN lexes AS l USING (wordid)
INNER JOIN senses AS s USING (luid)
INNER JOIN synsets AS y USING (synsetid)
WHERE word = 'bow'
ORDER BY sensekey;

-- pronunciation query without lexes
SELECT 'ERRONEOUS QUERY' AS comment;
SELECT word,pronunciation,y.posid,sensekey,synsetid,definition 
FROM words AS w
INNER JOIN senses AS s USING(wordid)
INNER JOIN synsets AS y USING (synsetid)
INNER JOIN lexes_pronunciations AS sp USING (wordid)
LEFT JOIN pronunciations AS p USING (pronunciationid)
WHERE word = 'bow'
ORDER BY sensekey;

-- pronunciation query with lexes
SELECT 'CORRECT QUERY' AS comment;
SELECT word,pronunciation,y.posid,sensekey,synsetid,definition
FROM words
INNER JOIN lexes AS l USING (wordid)
INNER JOIN senses AS s USING (luid)
INNER JOIN synsets AS y USING (synsetid)
INNER JOIN lexes_pronunciations USING (luid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bow'
ORDER BY sensekey;

