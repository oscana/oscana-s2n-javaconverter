package jp.co.tis.s2n.javaConverter.convert;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.BeforeClass;

import com.opencsv.exceptions.CsvException;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * java変換ツールのテスト。
 *
 */
public class ConvertTest {

    public static String sp = System.getProperty("file.separator");


    @BeforeClass
    public static void init() throws IOException, CsvException {
        ClassPathConvertUtil.loadMappingFile("src/test/resources/ClassPathConvert.csv");
        /**StrutsにおけるFormのBaseクラス、置換前、置換後map*/
        ClassPathConvertUtil.loadBaseClassnameConvertMap("BaseClassnameConvertMap.csv");
        /**StrutsにおけるServlet関連クラス、置換前、置換後map*/
        ClassPathConvertUtil.loadServletClassnameConvertMap("ServletClassnameConvertMap.csv");
    }

    /**
     * ファイルが同じかどうかを判断する
     * @param expectFile　期待値ファイル
     * @param actualFile　実変換結果ファイル
     * @return
     */
    protected boolean fileCompare(String expectFile, String actualFile) {
        boolean bRet = false;
        try {
            bRet = Arrays.equals(Files.readAllBytes(Paths.get(expectFile)), Files.readAllBytes(Paths.get(actualFile)));
        } catch (IOException e) {
            return false;
        }
        return bRet;
    }

    /**
     * テストを実行する
     * @param actionFileName 変換対象ファイル名
     * @param activeProfile ツール設定ファイル
     */
    protected void executeTest(String actionFileName, S2nProfile activeProfile) {

        String expectFileName = activeProfile.getProjectPath() + "/expect/" + actionFileName;
        String toFileName = activeProfile.getProjectPath() + "/to/" + actionFileName;

        assertTrue(fileCompare(toFileName, expectFileName));
    }

    /**
     * テストを実行する
     * @param activeProfile ツール設定ファイル
     */
    protected static void executeConvert(S2nProfile activeProfile) {

        String projectPath = activeProfile.getProjectPath();
        projectPath = new File(projectPath).getAbsolutePath();

        ConvertStruts2Nablarch parserJava = new ConvertStruts2Nablarch();
        parserJava.setActiveProfile(activeProfile);
        parserJava.setInPath(projectPath + sp + "from");
        parserJava.setOutPath(projectPath + sp + "to");
        parserJava.setTmpPath(projectPath + sp + "tmp");
        parserJava.setCodeName(activeProfile.getFileEncoding());
        parserJava.setStrutsAnalyzeResultList(activeProfile.getStrutsAnalyzeResultList());
        try {
            parserJava.createRoutes(projectPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //ファイルを削除
        File[] tmpFiles = new File(projectPath + sp + "tmp" + sp).listFiles();
        if(tmpFiles != null) {
            for (File tmpFile : tmpFiles){
                tmpFile.delete();
            }
        }

        File[] toFiles = new File(projectPath + sp + "to" + sp).listFiles();
        if (toFiles != null) {
            for (File toFile : toFiles) {
                toFile.delete();
            }
        }

        parserJava.execute();

    }
}
