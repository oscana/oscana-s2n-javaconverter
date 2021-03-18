package jp.co.tis.s2n.javaConverter.convert;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.convert.sqlfile.SqlFileConverter;

/**
 * Sqlファイルの変換テスト。
 *
 */
public class SqlConvertTest2 extends ConvertTest {
    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/SqlConvertTest");
        activeProfile.setFileEncoding("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");

        executeConvert(activeProfile);
    }

    /**
     * SqlFileConverterのメソッドをテストすること
     */
    @Test
    public void testSqlFileConverterApi01() {

        // sqlファイル
        SqlFileConverter sqlFileConv = new SqlFileConverter();

        sqlFileConv.setInPath(sp + "sql" + sp + "from");
        sqlFileConv.setOutPath(sp + "sql" + sp + "to");
        sqlFileConv.setTmpPath(sp + "sql" + sp + "tmp");
        sqlFileConv.setCodeName(activeProfile.getFileEncoding());
        sqlFileConv.setActiveProfile(activeProfile);

        sqlFileConv.execute();

    }

    /**
     * SqlFileConverterのメソッドをテストすること
     */
    @Test
    public void  testSqlFileConverterApi02() {
        String projectPath = activeProfile.getProjectPath();
        projectPath = new File(projectPath).getAbsolutePath();

        // sqlファイル
        SqlFileConverter sqlFileConv = new SqlFileConverter();

        sqlFileConv.setInPath(projectPath + sp + "sql" + sp + "from");
        sqlFileConv.setOutPath(projectPath + sp + "sql" + sp + "to");
        sqlFileConv.setTmpPath(projectPath + sp + "sql" + sp + "tmp");
        sqlFileConv.setCodeName(activeProfile.getFileEncoding());
        sqlFileConv.setActiveProfile(activeProfile);

        sqlFileConv.execute();
        String expectSqlFile = "src/test/resources/SqlConvertTest/sql/expect/sqlConvertApi.sql";
        String toFilesSql = "src/test/resources/SqlConvertTest/sql/to/sqlConvertApi.sql";

        //　ファイルの中身を比較する
        assertTrue(fileCompare(expectSqlFile, toFilesSql));
    }
}
