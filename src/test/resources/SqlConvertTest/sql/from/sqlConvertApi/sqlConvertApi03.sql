SELECT
    COUNT(*) RECORDCOUNT
FROM
    TABLE1
    /*BEGIN*/
    /*IF null != param1*/
    WHERE
        PARAM2 LIKE /*param2*/ || '%'  ESCAPE '/'
    /*END*/
    /*IF null != param3*/
    AND
        PARAM3 LIKE /*param3*/ || '%'  ESCAPE '/'
    /*END*/
    /*IF null != param4*/
    OR
        PARAM4 LIKE /*param4*/ || '%'  ESCAPE '/'
    /*END*/
    /*END*/

/*
  ブロックコメント
  ブロックコメント
  ブロックコメント
*/

/*ブロックコメント*/