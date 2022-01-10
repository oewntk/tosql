SELECT 'WordNet (relations)' AS section;

SELECT 'relations' AS subsection;

SELECT 'all relations' AS comment;
SELECT *
FROM relations
ORDER BY relationid;

SELECT 'semantical relations' AS subsection;

SELECT 'all semantic relations' AS comment;
SELECT relation, COUNT(*)
FROM synsets_synsets
LEFT JOIN relations USING(relationid)
GROUP BY relationid
ORDER BY relationid;

SELECT 'max synset relations per synset' AS comment;
SELECT synset1id, COUNT(*) AS c, definition
FROM synsets_synsets
LEFT JOIN synsets AS s ON (s.synsetid = synset1id)
GROUP BY synset1id
ORDER BY c DESC
LIMIT 5;

SELECT 'available semantic relations for synset' AS comment;
SELECT sy1.synsetid,sy1.definition,relation,COUNT(relation)
FROM synsets_synsets
LEFT JOIN synsets AS sy1 ON (sy1.synsetid = synset1id)
LEFT JOIN relations USING (relationid)
WHERE sy1.synsetid = 6
GROUP BY relationid;

SELECT 'semantic relations for "pull"''s synsets)' AS comment;
SELECT w1.word,sy1.definition,relation,GROUP_CONCAT(w2.word),sy2.definition
FROM synsets_synsets
LEFT JOIN synsets AS sy1 ON (sy1.synsetid = synset1id)
LEFT JOIN senses AS s1 ON (sy1.synsetid = s1.synsetid)
LEFT JOIN words AS w1 ON (s1.wordid = w1.wordid)
LEFT JOIN synsets AS sy2 ON (sy2.synsetid = synset2id)
LEFT JOIN senses AS s2 ON (sy2.synsetid = s2.synsetid)
LEFT JOIN words AS w2 ON (s2.wordid = w2.wordid)
LEFT JOIN relations USING(relationid)
WHERE w1.word = 'pull'
GROUP BY sy1.synsetid,w1.word,sy1.definition,relationid,relation,sy2.synsetid,sy2.synsetid,sy2.definition
ORDER BY sy1.synsetid,relationid;

SELECT 'available semantic relations for "pull"''s synsets)' AS comment;
SELECT w1.word,sy1.synsetid,sy1.definition,GROUP_CONCAT(DISTINCT relation)
FROM synsets_synsets
LEFT JOIN synsets AS sy1 ON (sy1.synsetid = synset1id)
LEFT JOIN senses AS s1 ON (sy1.synsetid = s1.synsetid)
LEFT JOIN words AS w1 ON (s1.wordid = w1.wordid)
LEFT JOIN relations USING (relationid)
WHERE w1.word = 'pull'
GROUP BY sy1.synsetid
ORDER BY sy1.synsetid;

SELECT 'lexical relations' AS subsection;

SELECT 'all lexical relations' AS comment;
SELECT relation, COUNT(*)
FROM senses_senses
LEFT JOIN relations USING(relationid)
GROUP BY relationid
ORDER BY relationid;

SELECT 'max sense relations per synset' AS comment;
SELECT synset1id, word1id, COUNT(*) AS c,word,GROUP_CONCAT(DISTINCT relation),definition
FROM senses_senses
LEFT JOIN synsets AS s ON (s.synsetid = synset1id)
LEFT JOIN words AS w ON (w.wordid = word1id)
LEFT JOIN relations USING(relationid)
GROUP BY word1id,synset1id
ORDER BY c DESC
LIMIT 5;

SELECT 'word lexical relations' AS comment;
SELECT w1.word,relation,w2.word,s1.definition,s2.definition
FROM senses_senses
LEFT JOIN synsets AS s1 ON (s1.synsetid = synset1id)
LEFT JOIN synsets AS s2 ON (s2.synsetid = synset2id)
LEFT JOIN words AS w1 ON (w1.wordid = word1id)
LEFT JOIN words AS w2 ON (w2.wordid = word2id)
LEFT JOIN relations USING(relationid)
WHERE w1.word = 'pull'
ORDER BY relationid;
