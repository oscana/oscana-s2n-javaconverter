package jp.co.tis.s2n.javaConverter.convert;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.common.TestSecurityManager;
import jp.co.tis.s2n.javaConverter.convert.common.TestSecurityManager.ExitException;

/**
 * 設定ファイルの読み込みテスト。
 *
 */
public class ReadProfileTest {

    private PrintStream defaultPrintStream;
    private SecurityManager securityManager;
    ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws Exception {
        LogUtils.init();
        // SecurityManager退避
        securityManager = System.getSecurityManager();
        // エラー出力退避
        defaultPrintStream = System.err;

        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(new BufferedOutputStream(byteArrayOutputStream)));
        System.setSecurityManager(new TestSecurityManager());

    }

    /**
     * 設定ファイルが存在しない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase1() {
        try {
            String[] args = new String[] {};
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "usage:java -jar javaconverter.jar [設定ファイル]";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 設定ファイルが一つだけの場合、設定ファイルを読めること
     */
    @Test
    public void testReadProfileCase2() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase13.properties" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(0, ite.getStatus());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     *設定ファイルが見つからない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase3() {
        try {
            String[] args = new String[] { "a.properties", "b.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "a.properties - エラー:設定ファイルが見つかりません。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * projectPathを設定しない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase4() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase4.properties", "b.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "エラー:projectPathは必須です。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * basePackageを設定しない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase5() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase5.properties", "b.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "エラー:basePackageは必須です。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * fileEncordingを設定しない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase6() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase6.properties", "b.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "エラー:fileEncordingは必須です。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * convertModeを設定しない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase7() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase7.properties", "b.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "エラー:convertModeは必須です。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * convertModeが１、２、３でない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase8() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase8.properties", "b.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "エラー:convertModeは「1,2,3」しか設定出来ません";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * onDoubleSubmissionPathを設定しない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase9() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase9.properties", "b.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "エラー:onDoubleSubmissionPathは必須です。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * onDoubleSubmissionPathを設定しない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase10() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase10.properties", "b.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "エラー:onDoubleSubmissionPathは必須です。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * onDoubleSubmissionPathを設定しない場合、設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase11() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase11.properties", "b.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "エラー:onDoubleSubmissionPathは必須です。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 異常系：設定ファイルを読めないこと
     */
    @Test
    public void testReadProfileCase12() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase12.properties",
                    "src/test/resources/ReadProfileTest/testCase12.txt" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "src/test/resources/ReadProfileTest/testCase12.txt - エラー:パッケージマッピングファイルをCSVフォーマットにしてください。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 正常系のテスト
     */
    @Test
    public void testReadProfileCase13() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase13.properties",
                    "src/test/resources/ReadProfileTest/testCase13.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(0, ite.getStatus());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * tableNameCase未設定の場合のテスト
     * @throws Exception
     */
    @Test
    public void testReadProfileCase14() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase14.properties",
                    "src/test/resources/ReadProfileTest/testCase13.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(0, ite.getStatus());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * tableNameCaseを0:Upperに設定の場合のテスト
     * @throws Exception
     */
    @Test
    public void testReadProfileCase15() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase15.properties",
                    "src/test/resources/ReadProfileTest/testCase13.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(0, ite.getStatus());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * tableNameCaseを1:Lowerに設定の場合のテスト
     * @throws Exception
     */
    @Test
    public void testReadProfileCase16() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase16.properties",
                    "src/test/resources/ReadProfileTest/testCase13.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(0, ite.getStatus());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 変換モードが3以外、onDoubleSubmissionPathを設定しない場合、正常終了すること。
     */
    @Test
    public void testReadProfileCase17() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase17.properties",
                    "src/test/resources/ReadProfileTest/testCase13.csv" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(0, ite.getStatus());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 異常系：パッケージマッピングファイルが存在してない場合
     */
    @Test
    public void testReadProfileCase18() {
        try {
            String[] args = new String[] { "src/test/resources/ReadProfileTest/testCase12.properties",
                    "src/test/resources/ReadProfileTest/testCase122.txt" };
            ConvertStruts2Nablarch.main(args);
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "src/test/resources/ReadProfileTest/testCase122.txt - エラー:パッケージマッピングファイルが見つかりません。";
            assertEquals(expected, actual);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        System.setOut(defaultPrintStream);
        System.setSecurityManager(securityManager);
    }

}
