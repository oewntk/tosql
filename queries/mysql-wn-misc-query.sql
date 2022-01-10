SELECT 'WordNet (misc)' AS section;

SELECT 'adjective positions' AS subsection;

SELECT 'adj having multiple positions' AS comment;
SELECT word,COUNT(DISTINCT positionid) AS N,GROUP_CONCAT(positionid),GROUP_CONCAT(senseid)
FROM senses INNER JOIN senses_adjpositions USING (wordid,synsetid) LEFT JOIN words USING (wordid) LEFT JOIN synsets USING (synsetid)
GROUP BY wordid HAVING N >1
ORDER BY N DESC;

SELECT 'uses of "out"' AS comment;
SELECT word,positionid,position,definition
FROM senses INNER JOIN senses_adjpositions USING (wordid,synsetid) LEFT JOIN words USING (wordid) LEFT JOIN synsets USING (synsetid)
INNER JOIN adjpositions USING (positionid)
WHERE word = 'out';

SELECT 'uses of "big"' AS comment;
SELECT word,positionid,position,definition
FROM senses INNER JOIN senses_adjpositions USING (wordid,synsetid) LEFT JOIN words USING (wordid) LEFT JOIN synsets USING (synsetid)
INNER JOIN adjpositions USING (positionid)
WHERE word = 'big';

