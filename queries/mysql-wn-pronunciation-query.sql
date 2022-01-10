SELECT * 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bow'
ORDER BY pronunciation;

SELECT word,pronunciation 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bow'
GROUP BY pronunciation
ORDER BY pronunciation;

-- LEGACY QUERY
SELECT * 
FROM words
INNER JOIN senses USING(wordid)
INNER JOIN synsets USING (synsetid)
WHERE word = 'bow'
ORDER BY sensekey;

-- NEW QUERY
SELECT * 
FROM words
INNER JOIN lexes USING (wordid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
WHERE word = 'bow'
ORDER BY sensekey;

-- WRONG
SELECT * 
FROM words
INNER JOIN senses USING(wordid)
INNER JOIN synsets USING (synsetid)
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bow'
ORDER BY sensekey;

-- RIGHT
SELECT * 
FROM words
INNER JOIN lexes USING (wordid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
INNER JOIN lexes_pronunciations USING (luid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bow'
ORDER BY sensekey;

-- from word 'bow'
SELECT word, pronunciation, variety, sensekey, definition 
FROM words
INNER JOIN lexes USING (wordid)
INNER JOIN senses USING (luid)
INNER JOIN synsets USING (synsetid)
INNER JOIN lexes_pronunciations USING (luid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE word = 'bow'
ORDER BY pronunciation,sensekey;

-- from word 'row'
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
SELECT word,GROUP_CONCAT(pronunciation SEPARATOR ' , '),GROUP_CONCAT(variety) 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
GROUP BY word;

-- words having several distinct standard pronunciations
SELECT word,GROUP_CONCAT(DISTINCT pronunciation SEPARATOR ' , '),GROUP_CONCAT(variety) 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE variety IS NULL
GROUP BY word HAVING COUNT(DISTINCT pronunciation) > 1;

-- words having several distinct local pronunciations
SELECT word,GROUP_CONCAT(DISTINCT CONCAT(pronunciation,' [',variety,']') SEPARATOR ' , ') 
FROM words
INNER JOIN lexes_pronunciations USING (wordid)
LEFT JOIN pronunciations USING (pronunciationid)
WHERE variety IS NOT NULL
GROUP BY word HAVING COUNT(DISTINCT CONCAT(pronunciation, '@',variety)) > 1;

