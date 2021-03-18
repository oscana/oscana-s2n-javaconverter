package jp.co.tis.s2n.javaConverter.convert;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzeResult;
import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzer;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * StrutsAnalyzeResultのテスト。
 *
 */
public class StrutsAnalyzeResultTest2 extends ConvertTest {

    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/StrutsAnalyzeResultTest2/java");
        activeProfile.setFileEncoding("UTF-8");
        activeProfile.setConvertMode(1);
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");
        activeProfile.setBasePackage("org.apache.struts.webapp");

        //StrutsResource
        String[] strutsConfigFiles = new String[] {
                "src/test/resources/StrutsAnalyzeResultTest2/conf/struts-config.xml" };
        String[] validationFile = new String[] {
                "src/test/resources/StrutsAnalyzeResultTest2/conf/validation.xml" };
        String[] module = new String[] { "validator" };
        StrutsAnalyzeResult[] strutsAnalyzeResultList = new StrutsAnalyzeResult[strutsConfigFiles.length];
        for (int i = 0; i < strutsConfigFiles.length; i++) {
            if (StringUtils.isEmpty(strutsConfigFiles[i])) {
                continue;
            }
            strutsAnalyzeResultList[i] = StrutsAnalyzer.analyzeStrutsResource(
                    new File(strutsConfigFiles[i]),
                    (StringUtils.isEmpty(validationFile[i])) ? null
                            : new File(validationFile[i]),
                    StringUtils.isEmpty(module[i]) ? "" : module[i],
                    activeProfile.getBasePackage());
        }
        activeProfile.setStrutsAnalyzeResultList(strutsAnalyzeResultList);

        executeConvert(activeProfile);
    }

    /**
     * actionクラスの場合、変換できること
     */
    @Test
    public void testStrutsAction() {
        String actionFileName = "RegistrationAction.java";
        executeTest(actionFileName, activeProfile);
    }

    /**
     * formクラスの場合、変換できること
     */
    @Test
    public void testStrutsForm() {
        String actionFileName = "RegistrationForm.java";
        executeTest(actionFileName, activeProfile);
    }

}
