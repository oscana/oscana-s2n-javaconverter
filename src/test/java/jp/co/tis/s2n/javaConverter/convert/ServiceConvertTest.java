package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * Serviceクラスの変換テスト。
 *
 */
public class ServiceConvertTest extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/ServiceConvertTest");
        activeProfile.setFileEncoding("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");

        executeConvert(activeProfile);
    }

    /**
     * implementsを追加すること
     */
    @Test
    public void testAddScopeAnotationToClass() {

        String serviceFileName = "TestAddScopeAnotationToClassService.java";

        executeTest(serviceFileName, activeProfile);

        String serviceFileName2 = "TestAddScopeAnotationToAbstractService.java";

        executeTest(serviceFileName2, activeProfile);
    }

    /**
     * convertCommon共通機能をテストすること
     */
    @Test
    public void testConvertCommon() {

        String serviceFileName = "TestConvertCommonService.java";

        executeTest(serviceFileName, activeProfile);
    }

    /**
     * sql呼び出す方式を変換すること
     */
    @Test
    public void testConvertJavaProgramForService() {

        String serviceFileName = "TestConvertJavaProgramForServiceService.java";

        executeTest(serviceFileName, activeProfile);
    }

    /**
     * IMPORTの最後に追加分を挿入すること
     */
    @Test
    public void testInsertStrutsImportInjectParts() {

        String serviceFileName = "TestInsertStrutsImportInjectPartsService.java";

        executeTest(serviceFileName, activeProfile);
    }

    /**
     * classではない場合、そのまま返すこと
     */
    @Test
    public void testConvertProc() {

        String serviceFileName = "TestConvertServiceApiNgService.java";

        executeTest(serviceFileName, activeProfile);
    }

    /**
     * 変換ファイルにclassがない場合、変更しないこと
     */
    @Test
    public void testConvertServiceNg() {

        String serviceFileName = "TestConvertServiceApiNgService.java";

        executeTest(serviceFileName, activeProfile);
    }

    /**
     * s2jdbcメソッドを変換できること
     */
    @Test
    public void testConvertServiceApiOk() {

        String serviceFileName = "TestConvertServiceApiOkService.java";

        executeTest(serviceFileName, activeProfile);

        String serviceFileName2 = "TestConvertServiceApi02Service.java";

        executeTest(serviceFileName2, activeProfile);
    }

    /**
     * アノテーションを変換すること
     */
    @Test
    public void testConvertComponentAnnotation() {

        String othersFileName = "TestConvertComponentAnnotationService.java";
        executeTest(othersFileName, activeProfile);
    }
}
