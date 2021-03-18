package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * 共通機能のテスト。
 *
 */
public class ConvertCommonTest extends ConvertTest {
    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/ConvertCommonTest");
        activeProfile.setFileEncoding("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");
        activeProfile.setBasePackage("jp.co");
        activeProfile.setConvertMode(3);

        executeConvert(activeProfile);
    }

    /**
     * ベースクラスを置換すること
     */
    @Test
    public void testConvertBaseClassName() {

        String actionFileName = "TestConvertBaseClassNameAction.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * インポート文の変換を行うこと
     */
    @Test
    public void testConvertImportCommon() {

        String actionFileName = "TestConvertImportCommonAction.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     *  CsvElemetnアノテーションを変換すること
     */
    @Test
    public void testConvertCsvAnnotation() {

        String actionFileName = "TestCsvElementSample.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * 未対応のアノテーションを削除すること
     */
    @Test
    public void testRemoveUnsupportAnnotation() {

        String actionFileName = "TestRemoveUnsupportAnnotationAction.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * Resourceを@Injectに置換すること
     */
    @Test
    public void testConvertResourceAnnotation() {

        String actionFileName = "TestConvertResourceAnnotationAction.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * Componentを@XXXXScopedに置換すること
     */
    @Test
    public void testConvertComponentAnnotation() {

        String actionFileName = "TestConvertComponentAnnotationAction.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * 検索の場合、変換できること
     */
    @Test
    public void testConvertJavaProgram01() {

        String actionFileName = "TestConvertJavaProgramService01.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * 集計の場合、変換できること
     */
    @Test
    public void testConvertJavaProgram02() {

        String actionFileName = "TestConvertJavaProgramService02.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * 更新の場合、変換できること
     */
    @Test
    public void testConvertJavaProgram03() {

        String actionFileName = "TestConvertJavaProgramService03.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * 流れる(from)ｓｑｌの場合、変換できること
     */
    @Test
    public void testConvertJavaProgram04() {

        String actionFileName = "TestConvertJavaProgramService04.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * バッチで新規登録の場合、変換できること
     */
    @Test
    public void testConvertJavaProgram05() {

        String actionFileName = "TestConvertJavaProgramService05.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * バッチで更新の場合、変換できること
     */
    @Test
    public void testConvertJavaProgram06() {

        String actionFileName = "TestConvertJavaProgramService06.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * バッチで削除の場合、変換できること
     */
    @Test
    public void testConvertJavaProgram07() {

        String actionFileName = "TestConvertJavaProgramService07.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * 更新の場合、変換できること
     */
    @Test
    public void testConvertJavaProgram08() {

        String actionFileName = "TestConvertJavaProgramService08.java";
        executeTest(actionFileName, activeProfile);
    }

}
