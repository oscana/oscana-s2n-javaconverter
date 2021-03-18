package jp.co.tis.s2n.javaConverter.convert;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * Actionクラスの変換テスト。
 *
 */
public class ActionConvertTest extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/ActionConvertTest");
        activeProfile.setFileEncoding("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");
        activeProfile.setBasePackage("jp.co.tis");
        activeProfile.setConvertMode(3);

        executeConvert(activeProfile);
    }

    /**
     * アクションクラスを変換すること
     */
    @Test
    public void testSignatureConvert() {

        String actionFileName = "SignatureTestAction.java";

        executeTest(actionFileName, activeProfile);
    }

    /**
     * アクションクラスに対して、publicメソッドに「@OnDoubleSubmission」を追加すること
     */
    @Test
    public void testConvertTokenSkipMethod() {

        String actionFileName = "TestConvertTokenSkipMethodAction.java";

        executeTest(actionFileName, activeProfile);
    }

    /**
     * struts-config.xml、validation.xmlからactionなどの情報をactionクラスに書き出すこと
     */
    @Test
    public void testConvertStrutsAction() {

        activeProfile.setConvertMode(1);
        String actionFileName = "TestConvertStrutsActionAction.java";

        executeTest(actionFileName, activeProfile);
        activeProfile.setConvertMode(0);
    }

    /**
     *  Executeアノテーションがついているメソッドはアクションとして変換すること
     */
    @Test
    public void testConvertExecuteMethod() {

        String actionFileName = "TestConvertExecuteMethodAction.java";

        executeTest(actionFileName, activeProfile);
    }

    /**
     * convertCommon共通機能ができること
     */
    @Test
    public void testConvertCommon() {

        String actionFileName = "TestConvertCommondAction.java";

        executeTest(actionFileName, activeProfile);
    }

    /**
     * IMPORTの最後に追加分を挿入すること
     */
    @Test
    public void testInsertStrutsImportInjectParts() {

        String actionFileName = "TestInsertStrutsImportInjectPartsAction.java";

        executeTest(actionFileName, activeProfile);
    }

    /**
     * convertActionメソッド補足テストケース
     */
    @Test
    public void testActionConvertApi() {
        String actionFileName = "TestConvertActionApiAction.java";
        executeTest(actionFileName, activeProfile);

        String actionFileName02 = "TestConvertActionApi02Action.java";
        executeTest(actionFileName02, activeProfile);
    }

    /**
     * convertActionメソッド補足テストケース(insertSAStrutsActionInjectParts)
     */
    @Test
    public void testActionConvertApi02() {
        String actionFileName = "TestConvertActionApi02Action.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * nodeメソッド補足テストケース(insertSAStrutsActionInjectParts)
     */
    @Test
    public void testNodeApi() {
        String actionFileName1 = "TestConvertNodeApi1Action.java";
        executeTest(actionFileName1, activeProfile);

        String actionFileName2 = "TestConvertNodeApi2Action.java";
        executeTest(actionFileName2, activeProfile);

        String actionFileName3 = "TestConvertNodeApi3Action.java";
        executeTest(actionFileName3, activeProfile);
    }
}
