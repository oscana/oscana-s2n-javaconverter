package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * Formクラスの変換テスト。
 *
 */
public class FormConvertTest extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/FormConvertTest");
        activeProfile.setFileEncording("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");

        executeConvert(activeProfile);
    }

    /**
     * formクラスにあるfieldアノテーションを変換すること
     */
    @Test
    public void testConvertXenlonActionForm() {

        String formFileName = "TestConvertXenlonActionFormForm.java";

        executeTest(formFileName, activeProfile);
    }

    /**
     * struts-config.xm、validation.xmllからvalidationなどの情報をformクラスに書き出すこと
     */
    @Test
    public void testConvertStrutsActionForm() {
        String formFileName = "TestConvertStrutsActionFormForm.java";

        executeTest(formFileName, activeProfile);
    }

    /**
     * メンバに対するバリデーションアノテーションを付与すること
     */
    @Test
    public void testConvertSAStrutsValidateAnnotation() {
        String formFileName = "TestConvertSAStrutsValidateAnnotationForm.java";

        executeTest(formFileName, activeProfile);
    }

    /**
     * implementsを追加すること
     */
    @Test
    public void testAddScopeAnotationToClass() {
        String formFileName = "TestAddScopeAnotationToClassForm.java";

        executeTest(formFileName, activeProfile);
    }

    /**
     * IMPORTの最後に追加分を挿入すること
     */
    @Test
    public void testInsertStrutsImportInjectParts() {
        String formFileName = "TestInsertStrutsImportInjectPartsForm.java";

        executeTest(formFileName, activeProfile);
    }

    /**
     * convertCommon共通機能をテストすること
     */
    @Test
    public void testConvertCommon() {
        String formFileName = "TestConvertCommonForm.java";

        executeTest(formFileName, activeProfile);
    }

    /**
     * アクセッサを追加すること
     */
    @Test
    public void testAddAccessor() {
        String formFileName = "TestAddAccessorForm.java";

        executeTest(formFileName, activeProfile);
    }

    /**
     * convertForm補足テストケース
     */
    @Test
    public void testConvertFormApi() {
        String formFileName = "TestConvertFormApiForm.java";

        executeTest(formFileName, activeProfile);

        String formFileName02 = "TestConvertFormApi02Form.java";

        executeTest(formFileName02, activeProfile);
    }
}
