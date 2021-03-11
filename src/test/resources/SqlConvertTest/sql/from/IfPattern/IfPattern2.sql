SELECT * FROM Table1
WHERE
/*IF null != param1*/
     PARAM1 = RPAD(/*param1*/, 10, ' ')
/*END*/
/*IF null != param2 */
    　　OR PARAM2 <= /*param2*/
/*END*/
/*IF  !(null != param1 and null == param2 || null == param3) */
        ORDER BY PARAM3
/*END*/