package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * バリデーションアノテーションの変換クラス。
 *
 */
public class ValidAnotationConvertTest extends ConvertTest {

    private final String formFileName = "ConvertTestForm.java";
    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/ValidAnotationConvertTest");
        activeProfile.setFileEncording("UTF-8");

        executeConvert(activeProfile);
    }

    /**
     * アノテーションを変換すること
     */
    @Test
    public void testValidAnotationConvert() {
        executeTest(formFileName, activeProfile);
    }

}
