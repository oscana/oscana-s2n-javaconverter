package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * Dtoクラスの変換テスト。
 *
 */
public class DtoConvertTest extends ConvertTest {
    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/DtoConvertTest");
        activeProfile.setFileEncoding("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");

        executeConvert(activeProfile);
    }

    /**
     * メンバに対するバリデーションアノテーションを付与すること
     */
    @Test
    public void testConvertSAStrutsValidateAnnotation() {

        String dtoFileName = "TestConvertSAStrutsValidateAnnotationDto.java";

        executeTest(dtoFileName, activeProfile);
    }

    /**
     * implementsを追加すること
     */
    @Test
    public void testAddScopeAnotationToClass() {

        String dtoFileName = "TestAddScopeAnotationToClassDto.java";

        executeTest(dtoFileName, activeProfile);
    }

    /**
     * IMPORTの最後に追加分を挿入すること
     */
    @Test
    public void testConvertProcCommon() {

        String dtoFileName = "TestConvertProcCommonDto.java";

        executeTest(dtoFileName, activeProfile);
    }

    /**
     * アクセッサを追加すること
     */
    @Test
    public void testAddAccessor() {

        String dtoFileName = "TestAddAccessorDto.java";

        executeTest(dtoFileName, activeProfile);
    }

    /**
     * アノテーションを変換すること
     */
    @Test
    public void testConvertComponentAnnotation() {

        String dtoFileName = "TestConvertComponentAnnotationDto.java";

        executeTest(dtoFileName, activeProfile);
    }
}
