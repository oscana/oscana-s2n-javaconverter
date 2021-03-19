sqlConvertApi01 =
SELECT
    T1.*,
    T2.PARAM1
FROM
    (SELECT
        TEMP.*,
        ROWNUM ROWNUMBER
    FROM
    (SELECT
        PARAM1,
        PARAM2,
        PARAM3,
        PARAM4,
        PARAM5,
        PARAM6,
        RTRIM(PARAM6) AS PARAM6,
        PARAM7,
        PARAM8
    FROM
        TABLE2
    WHERE
    -- BEGIN
    $if(param1) { PARAM1 = RPAD(:param1, 10, ' ') }
    AND
$if(param2) {
     PARAM2 = :param2
}
    OR
$if(param3) {
    PARAM3 = :param3
}
    AND
$if(whereKu) {
        /*$whereKu*/
}
    ORDER BY
        HANNEIYOTEIHI,
    -- TODO ツールで変換できません  
/*IF null == param3*/
        PARAM3,
        PARAM4
    -- END
    -- TODO ツールで変換できません  
/*IF null != param5*/
        PARAM5
    /*END*/
    /*END*/
          ) TEMP
    ) T1
    INNER JOIN TABLE3 T2 ON (
        T1.PARAM1 = T2.PARAM1
    AND T2.PARAM2 = 0)
WHERE
    ROWNUMBER >= :firstRowNo AND
    ROWNUMBER <= :lastRowNo
ORDER BY ROWNUMBER

sqlConvertApi02 =
SELECT
    test
FROM
    TEST


sqlConvertApi03 =
SELECT
    COUNT(*) RECORDCOUNT
FROM
    TABLE1
    -- BEGIN
    -- TODO ツールで変換できません  
/*IF null != param1*/
    WHERE
        PARAM2 LIKE :param2 || '%'  ESCAPE '/'
    /*END*/
    AND
$if(param3) {
        PARAM3 LIKE :param3 || '%'  ESCAPE '/'
}
    OR
$if(param4) {
        PARAM4 LIKE :param4 || '%'  ESCAPE '/'
}
    -- END

--ブロックコメント
  ブロックコメント
  ブロックコメント

/*ブロックコメント*/

sqlConvertApi04 =
SELECT
    COUNT(*) RECORDCOUNT
FROM
    TABLE1
    -- BEGIN
    -- TODO ツールで変換できません  
/*IF null != param1*/
    WHERE
        PARAM1 LIKE :param1 || '%'  ESCAPE '/'
    /*END*/
    -- TODO ツールで変換できません  
/*IF null != param2*/
    AND
        PARAM2 LIKE :param2 || '%'  ESCAPE '/'
    OR
        PARAM3 LIKE :param3 || '%'  ESCAPE '/'
    /*END*/
    -- TODO ツールで変換できません 
/*IF null != param4 && null != param5 */
    AND PARAM4 LIKE :param4 || '%'  ESCAPE '/'
    -- END
    /*END*/


sqlConvertApi05 =
SELECT * FROM XENLON_SAMPLE.OPE
WHERE
$if(param1) {
     TEST_USERID = RPAD(:param1, 10, ' ') 
}
AND
$if(param2) {
     TEST_USERID = RPAD(:param2, 10, ' ')
}
OR
$if(param3) {
     TEST_USERID = RPAD(:param3, 10, ' ')
}

sqlConvertApi06 =
    $if(param1) {  }
