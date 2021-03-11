package jp.co.tis.s2n.javaConverter.convert;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.co.tis.s2n.javaConverter.convert.common.TestSecurityManager;
import jp.co.tis.s2n.javaConverter.convert.common.TestSecurityManager.ExitException;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;

/**
 * {@link S2nProfile}のテスト。
 */
public class S2nProfileTest {

    private PrintStream defaultPrintStream;
    private SecurityManager securityManager;
    ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        // SecurityManager退避
        securityManager = System.getSecurityManager();
        // エラー出力退避
        defaultPrintStream = System.err;

        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(new BufferedOutputStream(byteArrayOutputStream)));
        System.setSecurityManager(new TestSecurityManager());

    }

    /**
     * tableNameCase未設定の場合のテスト
     * @throws Exception
     */
    @Test
    public void testTableNameCaseDefault() throws Exception {

        S2nProfile s2nProfile = new S2nProfile("src/test/resources/S2nProfileTest/tableNameCaseDefault.properties");
        assertEquals(0, s2nProfile.getTableNameCase());

    }

    /**
     * tableNameCaseを0:Upperに設定の場合のテスト
     * @throws Exception
     */
    @Test
    public void testTableNameCaseUpper() throws Exception {
        S2nProfile s2nProfile = new S2nProfile("src/test/resources/S2nProfileTest/tableNameCaseUpper.properties");
        assertEquals(0, s2nProfile.getTableNameCase());
    }

    /**
     * tableNameCaseを0:Lowerに設定の場合のテスト
     * @throws Exception
     */
    @Test
    public void testTableNameCaseLower() throws Exception {
        S2nProfile s2nProfile = new S2nProfile("src/test/resources/S2nProfileTest/tableNameCaseLower.properties");
        assertEquals(1, s2nProfile.getTableNameCase());
    }

    /**
     * tableNameCaseを1:Lowerに設定の場合のテスト
     * @throws Exception
     */
    @Test
    public void testTableNameCaseError() throws Exception {
        try {
            new S2nProfile("src/test/resources/S2nProfileTest/tableNameCaseError.properties");
        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(-1, ite.getStatus());

            // 出力メッセージを確認
            System.err.flush();
            final String actual = byteArrayOutputStream.toString();
            final String expected = "エラー:tableNameCaseは「0,1」しか設定出来ません";
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
