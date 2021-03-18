package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * Action、Dto、Entity、Form、Logic、Service以外のクラスの変換テスト。
 *
 */
public class OthersConvertTest extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/OthersConvertTest");
        activeProfile.setFileEncoding("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");

        executeConvert(activeProfile);
    }

    /**
     * IMPORTの最後に追加分を挿入する
     */
    @Test
    public void testConvertSAStrutsValidateAnnotation() {

        String othersFileName = "TestConvertProcCommonOthers.java";

        executeTest(othersFileName, activeProfile);
    }

    /**
     * Nodeクラス補足テストケース(addParam)
     */
    @Test
    public void testNodeAddParam() {

        String othersFileName1 = "TestConvertNodeApi1Others.java";
        executeTest(othersFileName1, activeProfile);

        String othersFileName2 = "TestConvertNodeApi2Others.java";
        executeTest(othersFileName2, activeProfile);

        String othersFileName3 = "TestConvertNodeApi3Others.java";
        executeTest(othersFileName3, activeProfile);
    }

    /**
     * アノテーションを変換すること
     */
    @Test
    public void testConvertComponentAnnotation() {

        String othersFileName1 = "TestConvertComponentAnnotationConverter.java";
        executeTest(othersFileName1, activeProfile);

        String othersFileName2 = "TestConvertComponentAnnotationDao.java";
        executeTest(othersFileName2, activeProfile);

        String othersFileName3 = "TestConvertComponentAnnotationDxl.java";
        executeTest(othersFileName3, activeProfile);

        String othersFileName4 = "TestConvertComponentAnnotationHelper.java";
        executeTest(othersFileName4, activeProfile);

        String othersFileName5 = "TestConvertComponentAnnotationInterceptor.java";
        executeTest(othersFileName5, activeProfile);

        String othersFileName6 = "TestConvertComponentAnnotationPage.java";
        executeTest(othersFileName6, activeProfile);

        String othersFileName7 = "TestConvertComponentAnnotationValidator.java";
        executeTest(othersFileName7, activeProfile);

        String othersFileName8 = "TestNotClass.java";
        executeTest(othersFileName8, activeProfile);
    }

}
