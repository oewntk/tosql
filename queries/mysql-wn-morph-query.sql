SELECT 'WordNet (morphology)' AS section;

SELECT 'morphology' AS subsection;

SELECT 'word from morph' AS comment;
SELECT *
FROM morphs
WHERE morph = 'better';

SELECT 'morph from word' AS comment;
SELECT *
FROM morphs
LEFT JOIN lexes_morphs USING (morphid)
LEFT JOIN words USING (wordid)
WHERE word = 'good';

SELECT 'chivy-chevied,chevies,chevying,chivied,chivvied,chivvies,chivvying' AS comment;
SELECT word,posid,morph
FROM lexes_morphs
INNER JOIN words USING (wordid)
INNER JOIN morphs USING (morphid)
WHERE word = 'chivy';

SELECT 'quiz(n,v)-quizzes' AS comment;
SELECT word,posid,morph
FROM lexes_morphs
INNER JOIN words USING (wordid)
INNER JOIN morphs USING (morphid)
WHERE word = 'quiz';

SELECT 'gas(n,v)-gasses' AS comment;
SELECT word,posid,morph
FROM lexes_morphs
INNER JOIN words USING (wordid)
INNER JOIN morphs USING (morphid)
WHERE word = 'gas';

SELECT 'good,well-better' AS comment;
SELECT word,posid,morph
FROM lexes_morphs
INNER JOIN words USING (wordid)
INNER JOIN morphs USING (morphid)
WHERE morph = 'better';

SELECT 'well(a,r)-better' AS comment;
SELECT word,posid,morph
FROM lexes_morphs
INNER JOIN words USING (wordid)
INNER JOIN morphs USING (morphid)
WHERE word = 'well';

SELECT 'be-am,are,been,is,was,were' AS comment;
SELECT word,posid,morph
FROM lexes_morphs
INNER JOIN words USING (wordid)
INNER JOIN morphs USING (morphid)
WHERE word = 'be';

SELECT 'morphs per word' AS comment;
SELECT word,COUNT(morph) AS N,GROUP_CONCAT(morph) AS morphs,GROUP_CONCAT(posid)
FROM morphs
LEFT JOIN lexes_morphs USING (morphid)
LEFT JOIN words USING (wordid)
GROUP BY word HAVING N > 1
LIMIT 20;

SELECT 'word per morph' AS comment;
SELECT morph,COUNT(word) AS N,GROUP_CONCAT(word) AS lemmas,GROUP_CONCAT(posid)
FROM morphs
LEFT JOIN lexes_morphs USING (morphid)
LEFT JOIN words USING (wordid)
GROUP BY morph HAVING N > 1
LIMIT 20;

SELECT 'word - 3 (or more) morphs' AS comment;
SELECT word,COUNT(wordid) AS N,GROUP_CONCAT(morph),GROUP_CONCAT(posid)
FROM lexes_morphs
INNER JOIN words USING (wordid)
INNER JOIN morphs USING (morphid)
GROUP BY wordid
HAVING N > 2
ORDER BY N DESC;

SELECT 'morph - 2 (or more) lemmas' AS comment;
SELECT morph,COUNT(morphid) AS N ,GROUP_CONCAT(word),GROUP_CONCAT(posid)
FROM lexes_morphs
INNER JOIN words USING (wordid)
INNER JOIN morphs USING (morphid)
GROUP BY morphid
HAVING N > 1
ORDER BY N DESC;

SELECT 'morph matching several posid' AS comment;
SELECT word,morph,GROUP_CONCAT(posid),COUNT(*) AS N
FROM lexes_morphs
INNER JOIN words USING (wordid)
INNER JOIN morphs USING (morphid)
GROUP BY wordid,morphid
HAVING N > 1
ORDER BY N DESC;
