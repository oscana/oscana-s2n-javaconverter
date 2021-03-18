package jp.co.tis.s2n.javaConverter.convert.program;

import org.junit.Before;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.ConvertTest;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * Handlerのテスト
 * 対象Handler：-AssertionHandler
 *            -BeansCopyHander
 *            -BeanUtilHander
 *            -CollectionsUtilHandler
 *            -SingletonS2ContainerHandler
 *
 */
public class AutoInstallConvertTest extends ConvertTest {
    private S2nProfile activeProfile;

    @Before
    public void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/program/AutoInstallConvertTest");
        activeProfile.setFileEncoding("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");
        activeProfile.setBasePackage("jp.co");
        activeProfile.setLineSeparator("\r\n");
        executeConvert(activeProfile);
    }

    /**
     * javaソースを変換すること
     */
    @Test
    public void testConvertJavaProgramWithAutoInstall() {

        String actionFileName = "TestConvertJavaProgramWithAutoInstall.java";
        executeTest(actionFileName, activeProfile);
    }

}
