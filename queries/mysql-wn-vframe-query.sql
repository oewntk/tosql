SELECT 'WordNet (verb frames/templates)' AS section;

SELECT 'verb frames' AS subsection;

SELECT 'get verb frames for "snow"' AS comment;
SELECT word,frame,SUBSTRING(definition FROM 1 FOR 32)
FROM senses_vframes
INNER JOIN vframes USING (frameid)
INNER JOIN words USING (wordid)
INNER JOIN synsets USING (synsetid)
WHERE word = 'snow';

SELECT 'get verb template for "rain"' AS comment;
SELECT word,template,SUBSTRING(definition FROM 1 FOR 32)
FROM senses_vtemplates
LEFT JOIN vtemplates USING (templateid)
INNER JOIN words USING (wordid)
INNER JOIN synsets USING (synsetid)
WHERE word = 'rain';

SELECT 'get verb frames for "clean"' AS comment;
SELECT word,frame,SUBSTRING(definition FROM 1 FOR 32)
FROM senses_vframes
INNER JOIN vframes USING (frameid)
RIGHT JOIN words USING (wordid)
RIGHT JOIN synsets USING (synsetid)
WHERE word = 'clean'
ORDER BY synsetid;

SELECT 'get verb template for "clean"' AS comment;
SELECT word,template,SUBSTRING(definition FROM 1 FOR 32)
FROM senses_vtemplates
LEFT JOIN vtemplates USING (templateid)
RIGHT JOIN words USING (wordid)
RIGHT JOIN synsets USING (synsetid)
WHERE word = 'clean'
ORDER BY synsetid;

SELECT 'get verb frames for "write"' AS comment;
SELECT word,frame,SUBSTRING(definition FROM 1 FOR 32)
FROM senses_vframes
INNER JOIN vframes USING (frameid)
RIGHT JOIN words USING (wordid)
RIGHT JOIN synsets USING (synsetid)
WHERE word = 'write'
ORDER BY synsetid;

SELECT 'get example template for "write"' AS comment;
SELECT word,template,SUBSTRING(definition FROM 1 FOR 32)
FROM senses_vtemplates
LEFT JOIN vtemplates USING (templateid)
RIGHT JOIN words USING (wordid)
RIGHT JOIN synsets USING (synsetid)
WHERE word = 'write'
ORDER BY synsetid;

SELECT 'get verb frames and example template grouped by synset for "write"' AS comment;
SELECT word,SUBSTRING(definition FROM 1 FOR 32),SUBSTRING(GROUP_CONCAT(frame) FROM 1 FOR 70),SUBSTRING(GROUP_CONCAT(template) FROM 1 FOR 32)
FROM senses_vframes
INNER JOIN vframes USING (frameid)
LEFT JOIN senses_vtemplates USING (synsetid,wordid)
LEFT JOIN vtemplates USING (templateid)
RIGHT JOIN words USING (wordid)
RIGHT JOIN synsets USING (synsetid)
WHERE word = 'write'
GROUP BY synsetid
ORDER BY synsetid;

SELECT 'verb frames matching n senses' AS comment;
SELECT frameid,frame,COUNT(*) AS N
FROM senses 
INNER JOIN senses_vframes USING (wordid,synsetid) 
INNER JOIN vframes USING (frameid) 
LEFT JOIN words USING (wordid) 
LEFT JOIN synsets USING (synsetid)
GROUP BY frameid
ORDER BY N DESC;

SELECT 'sense matching n verb frames' AS comment;
SELECT sensekey,definition,COUNT(*) AS N
FROM senses 
INNER JOIN senses_vframes USING (wordid,synsetid) 
INNER JOIN vframes USING (frameid) 
LEFT JOIN words USING (wordid) 
LEFT JOIN synsets USING (synsetid)
GROUP BY sensekey,definition,senseid
ORDER BY N DESC
LIMIT 20;

SELECT 'verb frames for "prepare"' AS comment;
SELECT word,synsetid,frame,SUBSTRING(definition FROM 1 FOR 32)
FROM senses 
INNER JOIN senses_vframes USING (wordid,synsetid) 
INNER JOIN vframes USING (frameid) 
LEFT JOIN words USING (wordid) 
LEFT JOIN synsets USING (synsetid)
WHERE word = 'prepare';
