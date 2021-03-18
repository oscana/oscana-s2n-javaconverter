package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * Entityクラスの変換テスト。
 *
 */
public class EntityConvertTest extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/EntityConvertTest/Upper");
        activeProfile.setFileEncoding("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");

        executeConvert(activeProfile);
    }

    /**
     * Entityクラスを変換すること
     */
    @Test
    public void testEntityConvert() {
        String entityFileName = "TestEntityBase.java";

        executeTest(entityFileName, activeProfile);
    }

    /**
     * 大文字の場合、変換できること
     */
    @Test
    public void testEntityConvertUpper() {
        String entityFileName = "TestEntityUpperBase.java";

        executeTest(entityFileName, activeProfile);
    }

    /**
     * 小文字の場合、変換できること
     */
    @Test
    public void testEntityConvertLower() {

        S2nProfile activeProfileLower = new S2nProfile();
        activeProfileLower.setProjectPath("src/test/resources/EntityConvertTest/Lower");
        activeProfileLower.setFileEncoding("UTF-8");
        activeProfileLower.setOnDoubleSubmissionPath("/error.jsp");
        activeProfileLower.setTableNameCase(1);
        executeConvert(activeProfileLower);

        String entityFileName = "TestEntityLowerBase.java";

        executeTest(entityFileName, activeProfileLower);
    }

    /**
     * convertCommon共通機能をテストすること
     */
    @Test
    public void testConvertCommon() {
        String entityFileName = "TestConvertCommonEntity.java";

        executeTest(entityFileName, activeProfile);
    }

    /**
     * IMPORTの最後に追加分を挿入すること
     */
    @Test
    public void testInsertStrutsImportInjectParts() {
        String entityFileName = "TestInsertStrutsImportInjectPartsEntity.java";

        executeTest(entityFileName, activeProfile);
    }

    /**
     * entityクラスのアノテーションを変換すること
     */
    @Test
    public void testConvertClassHeader() {
        String entityFileName = "TestConvertClassHeaderEntity.java";

        executeTest(entityFileName, activeProfile);
    }

    /**
     * ボディ部分のコンバートすること
     */
    @Test
    public void testConvertClassBody() {
        String entityFileName = "TestConvertClassBodyEntity.java";

        executeTest(entityFileName, activeProfile);
    }

    /**
     * convertEntityApi補足テストケース
     */
    @Test
    public void testConvertEntityApi() {
        S2nProfile activeProfile2 = new S2nProfile();
        activeProfile2.setProjectPath("src/test/resources/EntityConvertTest");
        activeProfile2.setFileEncoding("UTF-8");
        activeProfile2.setOnDoubleSubmissionPath("/error.jsp");
        executeConvert(activeProfile2);
        String entityFileName01 = "TestConvertEntityApi01Entity.java";
        executeTest(entityFileName01, activeProfile2);

        String entityFileName02 = "TestConvertEntityApi02Entity.java";
        executeTest(entityFileName02, activeProfile2);
    }
}
