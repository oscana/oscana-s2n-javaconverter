package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * SAStrutsの変換テスト。
 */
public class SAStrutsConvertTest extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        activeProfile = new S2nProfile();
        activeProfile.setBasePackage("tutorial");
        activeProfile.setProjectPath("src/test/resources/SAStrutsConvertTest");
        activeProfile.setConvertMode(2);
        activeProfile.setFileEncording("UTF-8");
        activeProfile.setOnDoubleSubmissionPath(null);

        executeConvert(activeProfile);
    }


    /**
     * SAStrutsの場合、@OnDoubleSubmissionを追加しないこと
     */
    @Test
    public void testSAStrutsAction() {

        String actionFileName = "TestSAStrutsAction.java";

        executeTest(actionFileName, activeProfile);
    }

}
