SELECT * FROM XENLON_SAMPLE.OPE
WHERE
/*IF null != param1*/
     TEST_USERID = RPAD(/*param1*/, 10, ' ') AND
/*END*/
/*IF null != param2*/
     TEST_USERID = RPAD(/*param2*/, 10, ' ') OR
/*END*/
/*IF null != param3*/
     TEST_USERID = RPAD(/*param3*/, 10, ' ')
/*END*/