WherePattern =
SELECT
    COUNT(*) RECORDCOUNT
FROM
    TABLE1
WHERE
     PARAM1 IS NOT NULL
    AND
$if(whereKu) {
        /*$whereKu*/
}