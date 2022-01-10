SELECT 'WordNet (cased forms)' AS section;

SELECT 'cased forms' AS subsection;

SELECT * FROM words
WHERE word='neolithic';

SELECT * FROM words
INNER JOIN casedwords USING (wordid)
WHERE word='neolithic';

SELECT * FROM words
WHERE word='baroque';

SELECT * FROM words
INNER JOIN casedwords USING (wordid)
WHERE word='baroque';

SELECT * FROM words
INNER JOIN casedwords USING (wordid)
WHERE word='shakespeare';

SELECT * FROM words
INNER JOIN casedwords USING (wordid)
WHERE casedword='shaKEspeare';

SELECT * FROM words
INNER JOIN casedwords USING (wordid)
WHERE casedword='Shakespeare';

SELECT * FROM words
INNER JOIN casedwords USING (wordid)
WHERE word='am';

SELECT * FROM words
LEFT JOIN casedwords USING (wordid)
WHERE word='abolition';

SELECT casedword,definition
FROM senses
INNER JOIN casedwords USING (wordid,casedwordid)
INNER JOIN synsets USING (synsetid)
WHERE casedword='Jackson';

SELECT casedword,definition
FROM senses
INNER JOIN casedwords USING (wordid,casedwordid)
INNER JOIN synsets USING (synsetid)
WHERE casedword='C';

SELECT word,casedword,SUBSTRING(definition FROM 1 FOR 64)
FROM senses
INNER JOIN words USING (wordid)
LEFT JOIN casedwords USING (wordid,casedwordid)
INNER JOIN synsets USING (synsetid)
WHERE word='C';

SELECT 'word having several casedword forms' AS comment;
SELECT wordid,COUNT(casedwordid) AS N,GROUP_CONCAT(casedword)
FROM casedwords
GROUP BY wordid
HAVING N > 1
ORDER BY N DESC;

SELECT 'senses having a casedword form' AS comment;
SELECT COUNT(*)
FROM senses
WHERE NOT ISNULL(casedwordid);

SELECT 'words having a casedword form' AS comment;
SELECT COUNT(*)
FROM words
INNER JOIN casedwords USING (wordid);

