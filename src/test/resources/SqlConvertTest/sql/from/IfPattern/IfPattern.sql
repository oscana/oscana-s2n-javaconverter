SELECT * FROM XENLON_SAMPLE.OPE
WHERE
/*IF null != param1*/
     TEST_USERID = RPAD(/*param1*/, 10, ' ')
/*END*/
