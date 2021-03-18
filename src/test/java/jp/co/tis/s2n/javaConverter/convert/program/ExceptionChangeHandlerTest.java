package jp.co.tis.s2n.javaConverter.convert.program;

import org.junit.Before;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.ConvertTest;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * {@link ExceptionChangeHandler}のテスト。
 */
public class ExceptionChangeHandlerTest extends ConvertTest {

    private S2nProfile activeProfile;

    @Before
    public void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/program/ExceptionChangeHandlerTest");
        activeProfile.setFileEncoding("UTF-8");
        executeConvert(activeProfile);
    }

    /**
     * 例外ソースを変換すること
     */
    @Test
    public void testEntityConvert() {
        String actionFileName = "TestException.java";
        executeTest(actionFileName, activeProfile);
    }

}
