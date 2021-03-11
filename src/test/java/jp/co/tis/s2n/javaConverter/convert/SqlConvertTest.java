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
public class SqlConvertTest extends ConvertTest {
    private static S2nProfile activeProfile;

    @BeforeClass
    public static void setUp() throws Exception {
        LogUtils.init();
        activeProfile = new S2nProfile();
        activeProfile.setProjectPath("src/test/resources/SqlConvertTest");
        activeProfile.setFileEncording("UTF-8");
        activeProfile.setOnDoubleSubmissionPath("/error.jsp");

        executeConvert(activeProfile);
    }

    /**
     * ifパータンが含まれる場合、sqlファイルを変換できること
     */
    @Test
    public void testIfPattern() {
        String projectPath = activeProfile.getProjectPath();
        projectPath = new File(projectPath).getAbsolutePath();

        // sqlファイル
        SqlFileConverter sqlFileConv = new SqlFileConverter();

        sqlFileConv.setInPath(projectPath + sp + "sql" + sp + "from");
        sqlFileConv.setOutPath(projectPath + sp + "sql" + sp + "to");
        sqlFileConv.setTmpPath(projectPath + sp + "sql" + sp + "tmp");
        sqlFileConv.setCodeName(activeProfile.getFileEncording());
        sqlFileConv.setActiveProfile(activeProfile);

        sqlFileConv.execute();

        String expectSqlFile = "src/test/resources/SqlConvertTest/sql/expect/IfPattern.sql";
        String toFilesSql = "src/test/resources/SqlConvertTest/sql/to/IfPattern.sql";

        //　ファイルの中身を比較する
        assertTrue(fileCompare(expectSqlFile, toFilesSql));
    }

    /**
     * whereパータンが含まれる場合、sqlファイルを変換できること
     */
    @Test
    public void testWherePattern() {
        String projectPath = activeProfile.getProjectPath();
        projectPath = new File(projectPath).getAbsolutePath();

        // sqlファイル
        SqlFileConverter sqlFileConv = new SqlFileConverter();

        sqlFileConv.setInPath(projectPath + sp + "sql" + sp + "from");
        sqlFileConv.setOutPath(projectPath + sp + "sql" + sp + "to");
        sqlFileConv.setTmpPath(projectPath + sp + "sql" + sp + "tmp");
        sqlFileConv.setCodeName(activeProfile.getFileEncording());
        sqlFileConv.setActiveProfile(activeProfile);

        sqlFileConv.execute();

        String expectSqlFile = "src/test/resources/SqlConvertTest/sql/expect/WherePattern.sql";
        String toFilesSql = "src/test/resources/SqlConvertTest/sql/to/WherePattern.sql";

        //　ファイルの中身を比較する
        assertTrue(fileCompare(expectSqlFile, toFilesSql));
    }

    /**
     * BindValueパータンが含まれる場合、sqlファイルを変換できること
     */
    @Test
    public void testBindValuePattern() {
        String projectPath = activeProfile.getProjectPath();
        projectPath = new File(projectPath).getAbsolutePath();

        // sqlファイル
        SqlFileConverter sqlFileConv = new SqlFileConverter();

        sqlFileConv.setInPath(projectPath + sp + "sql" + sp + "from");
        sqlFileConv.setOutPath(projectPath + sp + "sql" + sp + "to");
        sqlFileConv.setTmpPath(projectPath + sp + "sql" + sp + "tmp");
        sqlFileConv.setCodeName(activeProfile.getFileEncording());
        sqlFileConv.setActiveProfile(activeProfile);

        sqlFileConv.execute();

        String expectSqlFile = "src/test/resources/SqlConvertTest/sql/expect/BindValuePattern.sql";
        String toFilesSql = "src/test/resources/SqlConvertTest/sql/to/BindValuePattern.sql";

        //　ファイルの中身を比較する
        assertTrue(fileCompare(expectSqlFile, toFilesSql));
    }

}
