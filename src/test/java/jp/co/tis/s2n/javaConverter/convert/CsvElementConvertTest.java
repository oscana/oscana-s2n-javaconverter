package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * CsvElemetnアノテーションのテストクラス。
 *
 */
public class CsvElementConvertTest extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/CsvElementConvertTest");
        activeProfile.setFileEncording("UTF-8");

        executeConvert(activeProfile);
    }

    /**
     * CsvElemetnアノテーションを変換すること
     */
    @Test
    public void testCsvElementConvert() {
        String targetFileName = "CsvElementSample.java";

        executeTest(targetFileName, activeProfile);
    }

}
