package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * Logicクラスの変換テスト。
 *
 */
public class LogicConvertTest extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/LogicConvertTest");
        activeProfile.setFileEncording("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");

        executeConvert(activeProfile);
    }

    /**
     * implementsを追加すること
     */
    @Test
    public void testAddScopeAnotationToClass() {

        String logicFileName = "TestAddScopeAnotationToClassLogic.java";

        executeTest(logicFileName, activeProfile);
    }

    /**
     * IMPORTの最後に追加分を挿入する
     */
    @Test
    public void testConvertProcCommon() {

        String logicFileName = "TestConvertProcCommonLogic.java";

        executeTest(logicFileName, activeProfile);
    }

    /**
     * @Componentを@XXXXに置換すること
     */
    @Test
    public void TestConvertComponentAnnotation() {

        String logicFileName = "TestConvertComponentAnnotationLogic.java";

        executeTest(logicFileName, activeProfile);
    }
}
