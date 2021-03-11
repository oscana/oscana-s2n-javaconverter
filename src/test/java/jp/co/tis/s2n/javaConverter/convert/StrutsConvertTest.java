package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * Strutsの変換テスト。
 */
public class StrutsConvertTest extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/StrutsConvertTest");
        activeProfile.setConvertMode(1);
        activeProfile.setFileEncording("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");

        executeConvert(activeProfile);
    }


    /**
     * Strutsの場合、アクセッサを追加しないこと
     */
    @Test
    public void testAddAccessor() {

        String dtoFileName = "TestAddAccessorStrutsDto.java";

        executeTest(dtoFileName, activeProfile);
    }

    /**
     * Strutsの場合、アクセッサを追加しないこと
     */
    @Test
    public void testStrutsEntity() {

        String dtoFileName = "TestStrutsEntity.java";

        executeTest(dtoFileName, activeProfile);
    }
}
