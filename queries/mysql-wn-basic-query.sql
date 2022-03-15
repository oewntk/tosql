SELECT 'WordNet (basic)' AS section;

SELECT 'word lookup' AS subsection;

SELECT 'synsets "option" is member of' AS comment;
SELECT word,posid,sensenum,synsetid,SUBSTRING(definition FROM 1 FOR 64)
FROM words
LEFT JOIN senses USING (wordid)
LEFT JOIN synsets USING (synsetid)
WHERE word = 'option'
ORDER BY posid,sensenum;

SELECT 'synsets "want" is member of' AS comment;
SELECT word,posid,sensenum,synsetid,SUBSTRING(definition FROM 1 FOR 64)
FROM words
LEFT JOIN senses USING (wordid)
LEFT JOIN synsets USING (synsetid)
WHERE word = 'want'
ORDER BY posid,sensenum;

SELECT 'synsets verb "like" is member of' AS comment;
SELECT word,posid,sensenum,synsetid,SUBSTRING(definition FROM 1 FOR 64)
FROM words
LEFT JOIN senses USING (wordid)
LEFT JOIN synsets USING (synsetid)
WHERE word = 'like' AND posid = 'v'
ORDER BY posid,sensenum;

SELECT 'starting with "fear"' AS comment;
SELECT word,posid,sensenum,SUBSTRING(definition FROM 1 FOR 64)
FROM words
LEFT JOIN senses USING (wordid)
LEFT JOIN synsets USING (synsetid)
WHERE word LIKE 'fear%'
ORDER BY word,posid,sensenum;

SELECT 'ending with "wards"' AS comment;
SELECT word,posid,sensenum,SUBSTRING(definition FROM 1 FOR 64)
FROM words
LEFT JOIN senses USING (wordid)
LEFT JOIN synsets USING (synsetid)
WHERE word LIKE '%wards'
ORDER BY word,posid,sensenum;

SELECT 'containing "ipod"' AS comment;
SELECT word,posid,sensenum,SUBSTRING(definition FROM 1 FOR 64)
FROM words
LEFT JOIN senses USING (wordid)
LEFT JOIN synsets USING (synsetid)
WHERE word LIKE '%ipod%'
ORDER BY word,posid,sensenum;

SELECT 'synset members' AS subsection;

SELECT 'synset members' AS comment;
SELECT synsetid,posid,GROUP_CONCAT(word),SUBSTRING(definition FROM 1 FOR 60)
FROM synsets
LEFT JOIN senses USING (synsetid)
LEFT JOIN words AS w USING (wordid)
WHERE synsetid IN (1,2,3,4,5)
GROUP BY synsetid;

SELECT 'members of synsets "want" belongs to' AS comment;
SELECT y.synsetid,y.posid,GROUP_CONCAT(sw2.word),SUBSTRING(definition FROM 1 FOR 60)
FROM words AS sw
LEFT JOIN senses AS s USING (wordid)
LEFT JOIN synsets AS y USING (synsetid)
LEFT JOIN senses AS s2 ON (y.synsetid = s2.synsetid)
LEFT JOIN words AS sw2 ON (sw2.wordid = s2.wordid)
WHERE sw.word = 'want'
GROUP BY synsetid;

SELECT 'synonyms' AS subsection;

SELECT 'synonyms for "want"' AS comment;
SELECT y.synsetid,y.posid,GROUP_CONCAT(sw2.word),SUBSTRING(definition FROM 1 FOR 60)
FROM words AS sw
LEFT JOIN senses AS s USING (wordid)
LEFT JOIN synsets AS y USING (synsetid)
LEFT JOIN senses AS s2 ON (y.synsetid = s2.synsetid)
LEFT JOIN words AS sw2 ON (sw2.wordid = s2.wordid)
WHERE sw.word = 'want' AND sw.wordid <> sw2.wordid
GROUP BY synsetid;

SELECT 'semantical relations' AS subsection;

SELECT 'get words semantically-related to "horse"' AS comment;
SELECT s.sensenum,sw.word,relation,dw.word AS relatedlemma,SUBSTRING(definition FROM 1 FOR 60)
FROM semrelations AS l
LEFT JOIN senses AS s ON (l.synset1id = s.synsetid)
LEFT JOIN words AS sw ON s.wordid = sw.wordid
LEFT JOIN synsets AS sy ON (l.synset1id = sy.synsetid)
LEFT JOIN senses AS d ON (l.synset2id = d.synsetid)
LEFT JOIN words AS dw ON d.wordid = dw.wordid
LEFT JOIN relations USING (relationid)
WHERE sw.word = 'horse'
ORDER BY relationid,s.sensenum;

SELECT 'get hypernyms for "horse"' AS comment;
SELECT s.sensenum,sw.word,relation,dw.word AS relatedlemma,SUBSTRING(definition FROM 1 FOR 60)
FROM semrelations AS l
LEFT JOIN senses AS s ON (l.synset1id = s.synsetid)
LEFT JOIN words AS sw ON s.wordid = sw.wordid
LEFT JOIN synsets AS sy ON (l.synset1id = sy.synsetid)
LEFT JOIN senses AS d ON (l.synset2id = d.synsetid)
LEFT JOIN words AS dw ON d.wordid = dw.wordid
LEFT JOIN relations USING (relationid)
WHERE sw.word = 'horse' AND relationid=1 AND sy.posid='n'
ORDER BY relationid,s.sensenum;

SELECT 'get hyponyms for "horse"' AS comment;
SELECT s.sensenum,sw.word,relation,dw.word AS relatedlemma,SUBSTRING(definition FROM 1 FOR 60)
FROM semrelations AS l
LEFT JOIN senses AS s ON (l.synset1id = s.synsetid)
LEFT JOIN words AS sw ON s.wordid = sw.wordid
LEFT JOIN synsets AS sy ON (l.synset1id = sy.synsetid)
LEFT JOIN senses AS d ON (l.synset2id = d.synsetid)
LEFT JOIN words AS dw ON d.wordid = dw.wordid
LEFT JOIN relations USING (relationid)
WHERE sw.word = 'horse' AND relationid=2 AND sy.posid='n'
ORDER BY relationid,s.sensenum;

SELECT 'get words semantically-related to "horse" grouped by type' AS comment;
SELECT s.sensenum,sw.word,relation,GROUP_CONCAT(dw.word) AS relatedlemma,SUBSTRING(definition FROM 1 FOR 60)
FROM semrelations AS l
LEFT JOIN senses AS s ON (l.synset1id = s.synsetid)
LEFT JOIN words AS sw ON s.wordid = sw.wordid
LEFT JOIN synsets AS sy ON (l.synset1id = sy.synsetid)
LEFT JOIN senses AS d ON (l.synset2id = d.synsetid)
LEFT JOIN words AS dw ON d.wordid = dw.wordid
LEFT JOIN relations USING (relationid)
WHERE sw.word = 'horse'
GROUP BY relationid, relation, s.sensenum, sw.word, definition
ORDER BY relationid,s.sensenum;

SELECT 'lexical relations' AS subsection;

SELECT 'get words lexically-related to "black"' AS comment;
SELECT s.sensenum,sw.word,sy.posid,relation,dw.word AS relatedlemma,SUBSTRING(definition FROM 1 FOR 60)
FROM lexrelations AS l
LEFT JOIN senses AS s ON (l.synset1id = s.synsetid AND l.word1id = s.wordid)
LEFT JOIN words AS sw ON s.wordid = sw.wordid
LEFT JOIN synsets AS sy ON (l.synset1id = sy.synsetid)
LEFT JOIN senses AS d ON (l.synset2id = d.synsetid AND l.word2id = d.wordid)
LEFT JOIN words AS dw ON d.wordid = dw.wordid
LEFT JOIN relations USING (relationid)
WHERE sw.word = 'black'
ORDER BY relationid,sy.posid,s.sensenum;

SELECT 'get antonym to adjective "black"' AS comment;
SELECT s.sensenum,sw.word,sy.posid,relation,dw.word AS relatedlemma,SUBSTRING(definition FROM 1 FOR 60)
FROM lexrelations AS l
LEFT JOIN senses AS s ON (l.synset1id = s.synsetid AND l.word1id = s.wordid)
LEFT JOIN words AS sw ON s.wordid = sw.wordid
LEFT JOIN synsets AS sy ON (l.synset1id = sy.synsetid)
LEFT JOIN senses AS d ON (l.synset2id = d.synsetid AND l.word2id = d.wordid)
LEFT JOIN words AS dw ON d.wordid = dw.wordid
LEFT JOIN relations USING (relationid)
WHERE sw.word = 'black' AND relationid=30 AND sy.posid='a'
ORDER BY relationid,s.sensenum;
