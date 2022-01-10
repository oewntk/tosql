SET SESSION group_concat_max_len = 1000000;

SELECT 
	CONCAT(
		GROUP_CONCAT(CONCAT('SELECT \'', table_name, '\' table_name,COUNT(*) rows FROM ', table_name) SEPARATOR ' UNION '),
		' ORDER BY table_name'
	)
INTO @sql 
FROM
(
	SELECT table_name
	FROM information_schema.tables
	WHERE table_schema = DATABASE() AND table_type = 'BASE TABLE'
) AS table_list;


PREPARE s FROM  @sql;
EXECUTE s;
DEALLOCATE PREPARE s;
