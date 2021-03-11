IfPattern =
SELECT * FROM XENLON_SAMPLE.OPE
WHERE
$if(param1) {
     TEST_USERID = RPAD(:param1, 10, ' ')
}


IfPattern2 =
SELECT * FROM Table1
WHERE
$if(param1) {
     PARAM1 = RPAD(:param1, 10, ' ')
}
$if(param2) {
    　　PARAM2 <= :param2
}
-- TODO ツールで変換できません 
/*IF  !(null != param1 and null == param2 || null == param3) */
        ORDER BY PARAM3
/*END*/