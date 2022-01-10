SELECT 'WordNet (dictionary-like, with views)' AS section;

SELECT 'dictionary' AS section;

SELECT '"want" entry' AS comment;
SELECT word,posid,sensenum,synsetid,SUBSTRING(definition FROM 1 FOR 64),SUBSTRING(sampleset FROM 1 FOR 50)
FROM dict
WHERE word = 'want'
ORDER BY posid,sensenum;

SELECT '"like" verb entry' AS comment;
SELECT word,posid,sensenum,synsetid,SUBSTRING(definition FROM 1 FOR 64),SUBSTRING(sampleset FROM 1 FOR 50)
FROM dict
WHERE word = 'like' AND posid = 'v'
ORDER BY posid,sensenum;

SELECT '"want" as verb entry' AS comment;
SELECT word,posid,sensenum,SUBSTRING(definition FROM 1 FOR 64),SUBSTRING(sampleset FROM 1 FOR 50)
FROM dict
WHERE word = 'want' AND posid = 'v'
ORDER BY posid,sensenum;

SELECT '"want" as noun with tag information' AS comment;
SELECT word,posid,sensenum,tagcount,SUBSTRING(definition FROM 1 FOR 64)
FROM dict
WHERE word = 'want'
ORDER BY posid,sensenum;

SELECT 'starting with "fear"' AS comment;
SELECT word,posid,sensenum,SUBSTRING(definition FROM 1 FOR 64)
FROM dict
WHERE word LIKE 'fear%'
ORDER BY word,posid,sensenum;

SELECT 'ending with "wards"' AS comment;
SELECT word,posid,sensenum,SUBSTRING(definition FROM 1 FOR 64)
FROM dict
WHERE word LIKE '%wards'
ORDER BY word,posid,sensenum;

SELECT 'containing "ipod"' AS comment;
SELECT word,posid,sensenum,SUBSTRING(definition FROM 1 FOR 64)
FROM dict
WHERE word LIKE '%ipod%'
ORDER BY word,posid,sensenum;

SELECT 'starting with "rhino", ending with "s"' AS comment;
SELECT word,posid,sensenum,SUBSTRING(definition FROM 1 FOR 64)
FROM dict
WHERE word LIKE 'rhino%s'
ORDER BY word,posid,sensenum;

SELECT 'with lexical domain' AS comment;
SELECT domainname,word,posid,sensenum,SUBSTRING(definition FROM 1 FOR 64)
FROM dict
INNER JOIN domains USING(domainid,posid)
WHERE word LIKE '%ipod%'
ORDER BY domainid,word,posid,sensenum;

SELECT 'matching definition' AS comment;
SELECT SUBSTRING(definition FROM 1 FOR 64)
FROM synsets
WHERE posid= 'n' AND definition LIKE '(trademark)%'
LIMIT 20;

SELECT 'synset members' AS comment;
SELECT synsetid,posid,GROUP_CONCAT(word),SUBSTRING(definition FROM 1 FOR 60)
FROM dict
WHERE synsetid IN (1,2,3,4,5)
GROUP BY synsetid;

SELECT 'synset members' AS comment;
SELECT synsetid,posid,GROUP_CONCAT(word),SUBSTRING(definition FROM 1 FOR 60)
FROM dict
WHERE synsetid IN (SELECT synsetid FROM synsets INNER JOIN senses USING (synsetid) INNER JOIN words USING(wordid) WHERE word='spread')
GROUP BY synsetid;
