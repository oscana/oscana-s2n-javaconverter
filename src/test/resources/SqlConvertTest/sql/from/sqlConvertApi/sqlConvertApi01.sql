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
    /*BEGIN*/
    /*IF null != param1*/ PARAM1 = RPAD(/*param1*/, 10, ' ') /*END*/
    /*IF null != param2*/
    AND PARAM2 = /*param2*/
    /*END*/
    /*IF null != param3*/
    OR PARAM3 = /*param3*/
    /*END*/
    /*IF whereKu != null */
     AND   /*$whereKu*/
    /*END*/
    ORDER BY
        HANNEIYOTEIHI,
    /*IF null == param3*/
        PARAM3,
        PARAM4
    /*END*/
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
    ROWNUMBER >= /*firstRowNo*/ AND
    ROWNUMBER <= /*lastRowNo*/
ORDER BY ROWNUMBER