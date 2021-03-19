package jp.co.tis.s2n.javaConverter.token;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;

import org.junit.Test;

/**
*
* {@link TokenMgr}のテスト。
*
*/
public class TokenMgrTest {

    /**
     * getTokenメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testGetToken() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(".003".getBytes()));
        PushbackReader pbr = new PushbackReader(reader, 3);
        TokenMgr mgr = new TokenMgr(pbr, "");
        assertEquals(".003",mgr.getToken().getText());

        Reader reader2 = new InputStreamReader(new ByteArrayInputStream("\t".getBytes()));
        PushbackReader pbr2 = new PushbackReader(reader2, 3);
        TokenMgr mgr2 = new TokenMgr(pbr2, "");
        assertEquals("",mgr2.getToken().getText().trim());

        Reader reader3 = new InputStreamReader(new ByteArrayInputStream("%".getBytes()));
        PushbackReader pbr3 = new PushbackReader(reader3, 3);
        TokenMgr mgr3 = new TokenMgr(pbr3, "");
        assertEquals("%",mgr3.getToken().getText().trim());

        Reader reader4 = new InputStreamReader(new ByteArrayInputStream("^=".getBytes()));
        PushbackReader pbr4 = new PushbackReader(reader4, 3);
        TokenMgr mgr4 = new TokenMgr(pbr4, "");
        assertEquals("^=",mgr4.getToken().getText());

        Reader reader5 = new InputStreamReader(new ByteArrayInputStream("^a".getBytes()));
        PushbackReader pbr5 = new PushbackReader(reader5, 3);
        TokenMgr mgr5 = new TokenMgr(pbr5, "");
        assertEquals("^",mgr5.getToken().getText());

        Reader reader6 = new InputStreamReader(new ByteArrayInputStream("||".getBytes()));
        PushbackReader pbr6 = new PushbackReader(reader6, 3);
        TokenMgr mgr6 = new TokenMgr(pbr6, "");
        assertEquals("||",mgr6.getToken().getText());

        Reader reader7 = new InputStreamReader(new ByteArrayInputStream("|a".getBytes()));
        PushbackReader pbr7 = new PushbackReader(reader7, 3);
        TokenMgr mgr7 = new TokenMgr(pbr7, "");
        assertEquals("|",mgr7.getToken().getText());

        Reader reader8 = new InputStreamReader(new ByteArrayInputStream("テスト".getBytes()));
        PushbackReader pbr8 = new PushbackReader(reader8, 3);
        TokenMgr mgr8 = new TokenMgr(pbr8, "");
        assertEquals("テスト",mgr8.getToken().getText());

        Reader reader9 = new InputStreamReader(new ByteArrayInputStream("**".getBytes()));
        PushbackReader pbr9 = new PushbackReader(reader9, 3);
        TokenMgr mgr9 = new TokenMgr(pbr9, "");
        assertEquals("**",mgr9.getToken().getText());

        Reader reader10 = new InputStreamReader(new ByteArrayInputStream("\"\\a\"".getBytes()));
        PushbackReader pbr10 = new PushbackReader(reader10, 3);
        TokenMgr mgr10 = new TokenMgr(pbr10, "");
        assertEquals("\"\\a\"",mgr10.getToken().getText());

        Reader reader11 = new InputStreamReader(new ByteArrayInputStream(">=".getBytes()));
        PushbackReader pbr11 = new PushbackReader(reader11, 3);
        TokenMgr mgr11 = new TokenMgr(pbr11, "");
        assertEquals(">=",mgr11.getToken().getText());

        Reader reader12 = new InputStreamReader(new ByteArrayInputStream("<=".getBytes()));
        PushbackReader pbr12 = new PushbackReader(reader12, 3);
        TokenMgr mgr12 = new TokenMgr(pbr12, "");
        assertEquals("<=",mgr12.getToken().getText());

        Reader reader13 = new InputStreamReader(new ByteArrayInputStream("テaト".getBytes()));
        PushbackReader pbr13 = new PushbackReader(reader13, 3);
        TokenMgr mgr13 = new TokenMgr(pbr13, "");
        assertEquals("テaト",mgr13.getToken().getText());

        Reader reader14 = new InputStreamReader(new ByteArrayInputStream("\'\\a\'".getBytes()));
        PushbackReader pbr14 = new PushbackReader(reader14, 3);
        TokenMgr mgr14 = new TokenMgr(pbr14, "");
        assertEquals("\'\\a\'",mgr14.getToken().getText());

        Reader reader15 = new InputStreamReader(new ByteArrayInputStream("3e-1".getBytes()));
        PushbackReader pbr15 = new PushbackReader(reader15, 3);
        TokenMgr mgr15 = new TokenMgr(pbr15, "");
        assertEquals("3e-1",mgr15.getToken().getText());

        Reader reader16 = new InputStreamReader(new ByteArrayInputStream("2E+01".getBytes()));
        PushbackReader pbr16 = new PushbackReader(reader16, 3);
        TokenMgr mgr16 = new TokenMgr(pbr16, "");
        assertEquals("2E+01",mgr16.getToken().getText());

        Reader reader17 = new InputStreamReader(new ByteArrayInputStream("1B".getBytes()));
        PushbackReader pbr17 = new PushbackReader(reader17, 3);
        TokenMgr mgr17 = new TokenMgr(pbr17, "");
        assertEquals("1B",mgr17.getToken().getText());

        Reader reader18 = new InputStreamReader(new ByteArrayInputStream("1b".getBytes()));
        PushbackReader pbr18 = new PushbackReader(reader18, 3);
        TokenMgr mgr18 = new TokenMgr(pbr18, "");
        assertEquals("1b",mgr18.getToken().getText());

        Reader reader19 = new InputStreamReader(new ByteArrayInputStream("1S".getBytes()));
        PushbackReader pbr19 = new PushbackReader(reader19, 3);
        TokenMgr mgr19 = new TokenMgr(pbr19, "");
        assertEquals("1S",mgr19.getToken().getText());

        Reader reader20 = new InputStreamReader(new ByteArrayInputStream("1s".getBytes()));
        PushbackReader pbr20 = new PushbackReader(reader20, 3);
        TokenMgr mgr20 = new TokenMgr(pbr20, "");
        assertEquals("1s",mgr20.getToken().getText());

        Reader reader21 = new InputStreamReader(new ByteArrayInputStream("1DB".getBytes()));
        PushbackReader pbr21 = new PushbackReader(reader21, 3);
        TokenMgr mgr21 = new TokenMgr(pbr21, "");
        assertEquals("1DB",mgr21.getToken().getText());

        Reader reader22 = new InputStreamReader(new ByteArrayInputStream("1db".getBytes()));
        PushbackReader pbr22 = new PushbackReader(reader22, 3);
        TokenMgr mgr22 = new TokenMgr(pbr22, "");
        assertEquals("1db",mgr22.getToken().getText());

        Reader reader23 = new InputStreamReader(new ByteArrayInputStream("1Q".getBytes()));
        PushbackReader pbr23 = new PushbackReader(reader23, 3);
        TokenMgr mgr23 = new TokenMgr(pbr23, "");
        assertEquals("1Q",mgr23.getToken().getText());

        Reader reader24 = new InputStreamReader(new ByteArrayInputStream("1q".getBytes()));
        PushbackReader pbr24 = new PushbackReader(reader24, 3);
        TokenMgr mgr24 = new TokenMgr(pbr24, "");
        assertEquals("1q",mgr24.getToken().getText());

        Reader reader25 = new InputStreamReader(new ByteArrayInputStream("1EB".getBytes()));
        PushbackReader pbr25 = new PushbackReader(reader25, 3);
        TokenMgr mgr25 = new TokenMgr(pbr25, "");
        assertEquals("1EB",mgr25.getToken().getText());

        Reader reader26 = new InputStreamReader(new ByteArrayInputStream("1eb".getBytes()));
        PushbackReader pbr26 = new PushbackReader(reader26, 3);
        TokenMgr mgr26 = new TokenMgr(pbr26, "");
        assertEquals("1eb",mgr26.getToken().getText());

        Reader reader27 = new InputStreamReader(new ByteArrayInputStream("1e++".getBytes()));
        PushbackReader pbr27 = new PushbackReader(reader27, 3);
        TokenMgr mgr27 = new TokenMgr(pbr27, "");
        assertEquals("1e+",mgr27.getToken().getText());

        Reader reader28 = new InputStreamReader(new ByteArrayInputStream("1..".getBytes()));
        PushbackReader pbr28 = new PushbackReader(reader28, 3);
        TokenMgr mgr28 = new TokenMgr(pbr28, "");
        assertEquals("1.",mgr28.getToken().getText());

        Reader reader29 = new InputStreamReader(new ByteArrayInputStream("1..".getBytes()));
        PushbackReader pbr29 = new PushbackReader(reader29, 3);
        TokenMgr mgr29 = new TokenMgr(pbr29, "");
        mgr29.pushToken(new Token(Token.NAME,"pop"));
        assertEquals("pop",mgr29.getToken().getText());
    }

    /**
     * isAlphaメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testIsAlpha() throws IOException {
        Reader reader1 = new InputStreamReader(new ByteArrayInputStream("A".getBytes()));
        PushbackReader pbr1 = new PushbackReader(reader1, 3);
        TokenMgr mgr1 = new TokenMgr(pbr1, "");
        assertEquals("A",mgr1.getToken().getText());

        Reader reader2 = new InputStreamReader(new ByteArrayInputStream("Z".getBytes()));
        PushbackReader pbr2 = new PushbackReader(reader2, 3);
        TokenMgr mgr2 = new TokenMgr(pbr2, "");
        assertEquals("Z",mgr2.getToken().getText());

        Reader reader3 = new InputStreamReader(new ByteArrayInputStream("a".getBytes()));
        PushbackReader pbr3 = new PushbackReader(reader3, 3);
        TokenMgr mgr3 = new TokenMgr(pbr3, "");
        assertEquals("a",mgr3.getToken().getText());

        Reader reader4 = new InputStreamReader(new ByteArrayInputStream("z".getBytes()));
        PushbackReader pbr4 = new PushbackReader(reader4, 3);
        TokenMgr mgr4 = new TokenMgr(pbr4, "");
        assertEquals("z",mgr4.getToken().getText());

        Reader reader5 = new InputStreamReader(new ByteArrayInputStream("_".getBytes()));
        PushbackReader pbr5 = new PushbackReader(reader5, 3);
        TokenMgr mgr5 = new TokenMgr(pbr5, "");
        assertEquals("_",mgr5.getToken().getText());

        Reader reader6 = new InputStreamReader(new ByteArrayInputStream("$".getBytes()));
        PushbackReader pbr6 = new PushbackReader(reader6, 3);
        TokenMgr mgr6 = new TokenMgr(pbr6, "");
        assertEquals("$",mgr6.getToken().getText());

        Reader reader7 = new InputStreamReader(new ByteArrayInputStream("@".getBytes()));
        PushbackReader pbr7 = new PushbackReader(reader7, 3);
        TokenMgr mgr7 = new TokenMgr(pbr7, "");
        assertEquals("@",mgr7.getToken().getText());
    }

    /**
     * isAlphaNumericメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testIsAlphaNumeric() throws IOException {
        Reader reader1 = new InputStreamReader(new ByteArrayInputStream("AA".getBytes()));
        PushbackReader pbr1 = new PushbackReader(reader1, 3);
        TokenMgr mgr1 = new TokenMgr(pbr1, "");
        assertEquals("AA",mgr1.getToken().getText());

        Reader reader2 = new InputStreamReader(new ByteArrayInputStream("ZZ".getBytes()));
        PushbackReader pbr2 = new PushbackReader(reader2, 3);
        TokenMgr mgr2 = new TokenMgr(pbr2, "");
        assertEquals("ZZ",mgr2.getToken().getText());

        Reader reader3 = new InputStreamReader(new ByteArrayInputStream("aa".getBytes()));
        PushbackReader pbr3 = new PushbackReader(reader3, 3);
        TokenMgr mgr3 = new TokenMgr(pbr3, "");
        assertEquals("aa",mgr3.getToken().getText());

        Reader reader4 = new InputStreamReader(new ByteArrayInputStream("za1".getBytes()));
        PushbackReader pbr4 = new PushbackReader(reader4, 3);
        TokenMgr mgr4 = new TokenMgr(pbr4, "");
        assertEquals("za1",mgr4.getToken().getText());

        Reader reader5 = new InputStreamReader(new ByteArrayInputStream("_0".getBytes()));
        PushbackReader pbr5 = new PushbackReader(reader5, 3);
        TokenMgr mgr5 = new TokenMgr(pbr5, "");
        assertEquals("_0",mgr5.getToken().getText());

        Reader reader6 = new InputStreamReader(new ByteArrayInputStream("$9".getBytes()));
        PushbackReader pbr6 = new PushbackReader(reader6, 3);
        TokenMgr mgr6 = new TokenMgr(pbr6, "");
        assertEquals("$9",mgr6.getToken().getText());

        Reader reader7 = new InputStreamReader(new ByteArrayInputStream("@@".getBytes()));
        PushbackReader pbr7 = new PushbackReader(reader7, 3);
        TokenMgr mgr7 = new TokenMgr(pbr7, "");
        assertEquals("@@",mgr7.getToken().getText());

        Reader reader8 = new InputStreamReader(new ByteArrayInputStream("__".getBytes()));
        PushbackReader pbr8 = new PushbackReader(reader8, 3);
        TokenMgr mgr8 = new TokenMgr(pbr8, "");
        assertEquals("__",mgr8.getToken().getText());

        Reader reader9 = new InputStreamReader(new ByteArrayInputStream("@$".getBytes()));
        PushbackReader pbr9 = new PushbackReader(reader9, 3);
        TokenMgr mgr9 = new TokenMgr(pbr9, "");
        assertEquals("@$",mgr9.getToken().getText());

        Reader reader10 = new InputStreamReader(new ByteArrayInputStream("az1".getBytes()));
        PushbackReader pbr10 = new PushbackReader(reader10, 3);
        TokenMgr mgr10 = new TokenMgr(pbr10, "");
        assertEquals("az1",mgr10.getToken().getText());

        Reader reader11 = new InputStreamReader(new ByteArrayInputStream("z{".getBytes()));
        PushbackReader pbr11 = new PushbackReader(reader11, 3);
        TokenMgr mgr11 = new TokenMgr(pbr11, "");
        assertEquals("z",mgr11.getToken().getText());
    }

    /**
     * isNumericメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testIsNumeric() throws IOException {
        Reader reader1 = new InputStreamReader(new ByteArrayInputStream(".01".getBytes()));
        PushbackReader pbr1 = new PushbackReader(reader1, 3);
        TokenMgr mgr1 = new TokenMgr(pbr1, "");
        assertEquals(".01",mgr1.getToken().getText());

        Reader reader2 = new InputStreamReader(new ByteArrayInputStream("..01".getBytes()));
        PushbackReader pbr2 = new PushbackReader(reader2, 3);
        TokenMgr mgr2 = new TokenMgr(pbr2, "");
        assertEquals("..01",mgr2.getToken().getText());

    }

    /**
     * isNumeric2メソッドのテスト
     * @throws IOException
     */
    @Test
    public void testIsNumeric2() throws IOException {
        Reader reader1 = new InputStreamReader(new ByteArrayInputStream("00".getBytes()));
        PushbackReader pbr1 = new PushbackReader(reader1, 3);
        TokenMgr mgr1 = new TokenMgr(pbr1, "");
        assertEquals("00",mgr1.getToken().getText());

        Reader reader2 = new InputStreamReader(new ByteArrayInputStream("0.1".getBytes()));
        PushbackReader pbr2 = new PushbackReader(reader2, 3);
        TokenMgr mgr2 = new TokenMgr(pbr2, "");
        assertEquals("0.1",mgr2.getToken().getText());

        Reader reader3 = new InputStreamReader(new ByteArrayInputStream("0_1".getBytes()));
        PushbackReader pbr3 = new PushbackReader(reader3, 3);
        TokenMgr mgr3 = new TokenMgr(pbr3, "");
        assertEquals("0_1",mgr3.getToken().getText());

    }

    /**
     * isSymbolメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testIsSymbol() throws IOException {
        Reader reader1 = new InputStreamReader(new ByteArrayInputStream("(A".getBytes()));
        PushbackReader pbr1 = new PushbackReader(reader1, 3);
        TokenMgr mgr1 = new TokenMgr(pbr1, "");
        assertEquals("(",mgr1.getToken().getText());

        Reader reader2 = new InputStreamReader(new ByteArrayInputStream(")Z".getBytes()));
        PushbackReader pbr2 = new PushbackReader(reader2, 3);
        TokenMgr mgr2 = new TokenMgr(pbr2, "");
        assertEquals(")",mgr2.getToken().getText());

        Reader reader3 = new InputStreamReader(new ByteArrayInputStream("{a".getBytes()));
        PushbackReader pbr3 = new PushbackReader(reader3, 3);
        TokenMgr mgr3 = new TokenMgr(pbr3, "");
        assertEquals("{",mgr3.getToken().getText());

        Reader reader4 = new InputStreamReader(new ByteArrayInputStream("}a1".getBytes()));
        PushbackReader pbr4 = new PushbackReader(reader4, 3);
        TokenMgr mgr4 = new TokenMgr(pbr4, "");
        assertEquals("}",mgr4.getToken().getText());

        Reader reader5 = new InputStreamReader(new ByteArrayInputStream("[0".getBytes()));
        PushbackReader pbr5 = new PushbackReader(reader5, 3);
        TokenMgr mgr5 = new TokenMgr(pbr5, "");
        assertEquals("[",mgr5.getToken().getText());

        Reader reader6 = new InputStreamReader(new ByteArrayInputStream("]9".getBytes()));
        PushbackReader pbr6 = new PushbackReader(reader6, 3);
        TokenMgr mgr6 = new TokenMgr(pbr6, "");
        assertEquals("]",mgr6.getToken().getText());

        Reader reader7 = new InputStreamReader(new ByteArrayInputStream("+A".getBytes()));
        PushbackReader pbr7 = new PushbackReader(reader7, 3);
        TokenMgr mgr7 = new TokenMgr(pbr7, "");
        assertEquals("+",mgr7.getToken().getText());

        Reader reader8 = new InputStreamReader(new ByteArrayInputStream("-A".getBytes()));
        PushbackReader pbr8 = new PushbackReader(reader8, 3);
        TokenMgr mgr8 = new TokenMgr(pbr8, "");
        assertEquals("-",mgr8.getToken().getText());

        Reader reader9 = new InputStreamReader(new ByteArrayInputStream(".003".getBytes()));
        PushbackReader pbr9 = new PushbackReader(reader9, 3);
        TokenMgr mgr9 = new TokenMgr(pbr9, "");
        assertEquals(".003",mgr9.getToken().getText());

        Reader reader10 = new InputStreamReader(new ByteArrayInputStream(",A".getBytes()));
        PushbackReader pbr10 = new PushbackReader(reader10, 3);
        TokenMgr mgr10 = new TokenMgr(pbr10, "");
        assertEquals(",",mgr10.getToken().getText());

        Reader reader11 = new InputStreamReader(new ByteArrayInputStream("&A".getBytes()));
        PushbackReader pbr11 = new PushbackReader(reader11, 3);
        TokenMgr mgr11 = new TokenMgr(pbr11, "");
        assertEquals("&",mgr11.getToken().getText());

        Reader reader12 = new InputStreamReader(new ByteArrayInputStream("?A".getBytes()));
        PushbackReader pbr12 = new PushbackReader(reader12, 3);
        TokenMgr mgr12 = new TokenMgr(pbr12, "");
        assertEquals("?",mgr12.getToken().getText());

        Reader reader13 = new InputStreamReader(new ByteArrayInputStream("!A".getBytes()));
        PushbackReader pbr13 = new PushbackReader(reader13, 3);
        TokenMgr mgr13 = new TokenMgr(pbr13, "");
        assertEquals("!",mgr13.getToken().getText());
    }

    /**
     * procAlphaNumericメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testProcAlphaNumeric() throws IOException {
        Reader reader1 = new InputStreamReader(new ByteArrayInputStream("A.A".getBytes()));
        PushbackReader pbr1 = new PushbackReader(reader1, 3);
        TokenMgr mgr1 = new TokenMgr(pbr1, "");
        assertEquals("A.A",mgr1.getToken().getText());
    }

    /**
     * procSlashメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testProcSlash() throws IOException {
        Reader reader1 = new InputStreamReader(new ByteArrayInputStream("//\n".getBytes()));
        PushbackReader pbr1 = new PushbackReader(reader1, 3);
        TokenMgr mgr1 = new TokenMgr(pbr1, "");
        assertEquals("//",mgr1.getToken().getText());

        Reader reader2 = new InputStreamReader(new ByteArrayInputStream("//".getBytes()));
        PushbackReader pbr2 = new PushbackReader(reader2, 3);
        TokenMgr mgr2 = new TokenMgr(pbr2, "");
        assertEquals("//",mgr2.getToken().getText());

        Reader reader3 = new InputStreamReader(new ByteArrayInputStream("/f".getBytes()));
        PushbackReader pbr3 = new PushbackReader(reader3, 3);
        TokenMgr mgr3 = new TokenMgr(pbr3, "");
        assertEquals("/",mgr3.getToken().getText());
    }

    /**
     * procOtherメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testProcOther() throws IOException {
        Reader reader1 = new InputStreamReader(new ByteArrayInputStream("あ ".getBytes()));
        PushbackReader pbr1 = new PushbackReader(reader1, 3);
        TokenMgr mgr1 = new TokenMgr(pbr1, "");
        assertEquals("あ",mgr1.getToken().getText());

        Reader reader2 = new InputStreamReader(new ByteArrayInputStream("あ\r".getBytes()));
        PushbackReader pbr2 = new PushbackReader(reader2, 3);
        TokenMgr mgr2 = new TokenMgr(pbr2, "");
        assertEquals("あ",mgr2.getToken().getText());

        Reader reader3 = new InputStreamReader(new ByteArrayInputStream("あ\n".getBytes()));
        PushbackReader pbr3 = new PushbackReader(reader3, 3);
        TokenMgr mgr3 = new TokenMgr(pbr3, "");
        assertEquals("あ",mgr3.getToken().getText());

        Reader reader4 = new InputStreamReader(new ByteArrayInputStream("あ.".getBytes()));
        PushbackReader pbr4 = new PushbackReader(reader4, 3);
        TokenMgr mgr4 = new TokenMgr(pbr4, "");
        assertEquals("あ",mgr4.getToken().getText());

        Reader reader5 = new InputStreamReader(new ByteArrayInputStream("あ,".getBytes()));
        PushbackReader pbr5 = new PushbackReader(reader5, 3);
        TokenMgr mgr5 = new TokenMgr(pbr5, "");
        assertEquals("あ",mgr5.getToken().getText());

        Reader reader6 = new InputStreamReader(new ByteArrayInputStream("あ:".getBytes()));
        PushbackReader pbr6 = new PushbackReader(reader6, 3);
        TokenMgr mgr6 = new TokenMgr(pbr6, "");
        assertEquals("あ",mgr6.getToken().getText());

        Reader reader7 = new InputStreamReader(new ByteArrayInputStream("あ;".getBytes()));
        PushbackReader pbr7 = new PushbackReader(reader7, 3);
        TokenMgr mgr7 = new TokenMgr(pbr7, "");
        assertEquals("あ",mgr7.getToken().getText());

        Reader reader8 = new InputStreamReader(new ByteArrayInputStream("あ(".getBytes()));
        PushbackReader pbr8 = new PushbackReader(reader8, 3);
        TokenMgr mgr8 = new TokenMgr(pbr8, "");
        assertEquals("あ",mgr8.getToken().getText());

        Reader reader9 = new InputStreamReader(new ByteArrayInputStream("あ)".getBytes()));
        PushbackReader pbr9 = new PushbackReader(reader9, 3);
        TokenMgr mgr9 = new TokenMgr(pbr9, "");
        assertEquals("あ",mgr9.getToken().getText());
    }

}
