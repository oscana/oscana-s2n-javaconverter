SELECT
    COUNT(*) RECORDCOUNT
FROM
    TABLE1
    /*BEGIN*/
    /*IF null != param1*/
    WHERE
        PARAM1 LIKE /*param1*/ || '%'  ESCAPE '/'
    /*END*/
    /*IF null != param2*/
    AND
        PARAM2 LIKE /*param2*/ || '%'  ESCAPE '/'
    OR
        PARAM3 LIKE /*param3*/ || '%'  ESCAPE '/'
    /*END*/
    /*IF null != param4 && null != param5 */
    AND PARAM4 LIKE /*param4*/ || '%'  ESCAPE '/'
    /*END*/
    /*END*/
