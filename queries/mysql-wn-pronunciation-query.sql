SELECT 'WordNet (pronunciations)' AS section;

SELECT 'pronunciations' AS subsection;

-- pronunciations of
SELECT 'pronunciations of "bow"' AS comment;
SELECT * 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bow'
ORDER BY pronunciation;

-- grouped pronunciations of
SELECT 'grouped pronunciations of "bow"' AS comment;
SELECT word,pronunciation 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bow'
GROUP BY pronunciation
ORDER BY pronunciation;

-- pronunciation query with lexes
SELECT 'pronunciation of "bow" by sense' AS comment;
SELECT word,pronunciation,y.posid,sensekey,synsetid,definition 
FROM words AS w
INNER JOIN lexes AS l USING (wordid)
INNER JOIN senses AS s USING (luid)
INNER JOIN synsets AS y USING (synsetid)
INNER JOIN lexes_pronunciations AS lp USING (luid)
LEFT JOIN pronunciations AS p USING (pronunciationid)
WHERE word = 'bow'
ORDER BY sensekey;

-- from word 'bow'
SELECT 'from word "bow"' AS comment;
SELECT word,pronunciation, variety, sensekey, definition 
FROM words
INNER JOIN lexes USING (wordid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
INNER JOIN lexes_pronunciations USING (luid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bow'
ORDER BY pronunciation,sensekey;

-- from word 'row'
SELECT 'from word "row"' AS comment;
SELECT word, pronunciation, variety, sensekey, definition 
FROM words
INNER JOIN lexes USING (wordid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
INNER JOIN lexes_pronunciations USING (luid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'row'
ORDER BY pronunciation,sensekey;

-- from word 'bass'
SELECT 'from word "bass"' AS comment;
SELECT word, pronunciation, variety, sensekey, definition 
FROM words
INNER JOIN lexes USING (wordid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
INNER JOIN lexes_pronunciations USING (luid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bass'
ORDER BY pronunciation,sensekey;

-- from word 'wind'
SELECT 'from word "wind"' AS comment;
SELECT word, pronunciation, variety, sensekey, definition 
FROM words
INNER JOIN lexes USING (wordid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
INNER JOIN lexes_pronunciations USING (luid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'wind'
ORDER BY pronunciation,sensekey;

-- from word 'perfect'
SELECT 'from word "perfect"' AS comment;
SELECT word, pronunciation, variety, sensekey, definition 
FROM words
INNER JOIN lexes USING (wordid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
INNER JOIN lexes_pronunciations USING (luid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'perfect'
ORDER BY pronunciation,sensekey;

-- from pronunciation 'baʊ'
SELECT 'from pronunciation "baʊ"' AS comment;
SELECT word, pronunciation, variety, sensekey, definition 
FROM pronunciations
INNER JOIN lexes_pronunciations AS m USING (pronunciationid)
INNER JOIN words USING (wordid)
INNER JOIN lexes USING (luid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
WHERE pronunciation = 'baʊ'
ORDER BY sensekey;

-- from pronunciation 'bəʊ'
SELECT 'from pronunciation "bəʊ"' AS comment;
SELECT word, pronunciation, variety, sensekey, definition 
FROM pronunciations
INNER JOIN lexes_pronunciations AS m USING (pronunciationid)
INNER JOIN words USING (wordid)
INNER JOIN lexes USING (luid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
WHERE pronunciation = 'bəʊ'
ORDER BY sensekey;

-- from pronunciation 'ˈziːɹəʊ' or 'ˈzɪəɹəʊ'
SELECT 'from pronunciation "ˈziːɹəʊ" or "ˈzɪəɹəʊ"' AS comment;
SELECT word, pronunciation, variety, sensekey, definition 
FROM pronunciations
INNER JOIN lexes_pronunciations AS m USING (pronunciationid)
INNER JOIN words USING (wordid)
INNER JOIN lexes USING (luid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
WHERE pronunciation IN ('ˈziːɹəʊ', 'ˈzɪəɹəʊ')
ORDER BY sensekey;

-- group pronunciations by word
SELECT 'group pronunciations by word' AS comment;
SELECT word,GROUP_CONCAT(pronunciation SEPARATOR ' , '),GROUP_CONCAT(variety) 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
GROUP BY word
LIMIT 20;

-- words having several distinct standard pronunciations
SELECT 'words having several distinct standard pronunciations' AS comment;
SELECT word,GROUP_CONCAT(DISTINCT pronunciation SEPARATOR ' , '),GROUP_CONCAT(variety) 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE variety IS NULL
GROUP BY word HAVING COUNT(DISTINCT pronunciation) > 1
LIMIT 20;

-- words having several distinct local pronunciations
SELECT 'words having several distinct local pronunciations' AS comment;
SELECT word,GROUP_CONCAT(DISTINCT CONCAT(pronunciation,' [',variety,']') SEPARATOR ' , ') 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE variety IS NOT NULL
GROUP BY word HAVING COUNT(DISTINCT CONCAT(pronunciation, '@',variety)) > 1
LIMIT 20;

