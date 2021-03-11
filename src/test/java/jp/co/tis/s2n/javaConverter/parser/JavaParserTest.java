package jp.co.tis.s2n.javaConverter.parser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;

import org.junit.Test;

import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;
import jp.co.tis.s2n.javaConverter.token.TokenMgr;

/**
*
* {@link JavaParser}のテスト。
*
*/
public class JavaParserTest {

    /**
     * parseメソッドのテスト
     */
    @Test
    public void testParse() {
        assertEquals("TempForStr",JavaParser.parse("test", false).getString());
    }

    /**
     * procColonメソッドのテスト
     */
    @Test
    public void testProcColon() {
        JavaParseCtrl jpc = new JavaParseCtrl("test");
        jpc.eNode = Node.create(Node.T_NORMAL, "default");
        JavaParser.procColon(jpc, new Token(Token.NAME,"test"));
        assertFalse(jpc.initEnd);
    }

    /**
     * procComment1メソッドのテスト
     */
    @Test
    public void testProcComment1() {
        JavaParseCtrl jpc = new JavaParseCtrl("test");
        jpc.prevCRLF = true;
        jpc.kakkoCount = 1;
        jpc.eNode = Node.create(Node.T_NORMAL, "default");
        JavaParser.procComment1(jpc, new Token(Token.NAME,"test"));
        assertEquals("test",jpc.getLastTokenWithoutSpace().getText());

        jpc.prevCRLF = false;
        JavaParser.procComment1(jpc, new Token(Token.NAME,"test"));
        assertEquals("test",jpc.getLastTokenWithoutSpace().getText());
    }

    /**
     * procComment2メソッドのテスト０１
     * @throws IOException
     */
    @Test
    public void testProcComment201() throws IOException {
        JavaParseCtrl jpc = new JavaParseCtrl("test");
        jpc.kakkoCount = 1;
        jpc.eNode = Node.create(Node.T_NORMAL, "default");

        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        PushbackReader pbr = new PushbackReader(reader, 3);
        TokenMgr mgr = new TokenMgr(pbr, "");

        JavaParser.procComment2(jpc, new Token(Token.NAME,"test"),mgr);
        assertEquals("defaulttest",jpc.eNode.getString());

    }

    /**
     * procComment2メソッドのテスト０２
     * @throws IOException
     */
    @Test
    public void testProcComment202() throws IOException {
        JavaParseCtrl jpc = new JavaParseCtrl("test");
        jpc.kakkoCount = 0;
        jpc.eNode = Node.create(Node.T_NORMAL, "}");

        Reader reader = new InputStreamReader(new ByteArrayInputStream("\nelse".getBytes()));
        PushbackReader pbr = new PushbackReader(reader, 3);
        TokenMgr mgr = new TokenMgr(pbr, "");

        JavaParser.procComment2(jpc, new Token(Token.NAME,"test"),mgr);
        assertEquals("}test",jpc.eNode.getString());

    }

    /**
     * procComment2メソッドのテスト０３
     * @throws IOException
     */
    @Test
    public void testProcComment203() throws IOException {
        JavaParseCtrl jpc = new JavaParseCtrl("test");
        jpc.kakkoCount = 0;
        jpc.eNode = Node.create(Node.T_NORMAL, "}");

        Reader reader = new InputStreamReader(new ByteArrayInputStream("\naaa".getBytes()));
        PushbackReader pbr = new PushbackReader(reader, 3);
        TokenMgr mgr = new TokenMgr(pbr, "");

        JavaParser.procComment2(jpc, new Token(Token.NAME,"test"),mgr);
        assertEquals("test",jpc.eNode.getString());

    }

    /**
     * procComment2メソッドのテスト０４
     * @throws IOException
     */
    @Test
    public void testProcComment204() throws IOException {
        JavaParseCtrl jpc = new JavaParseCtrl("test");
        jpc.kakkoCount = 0;
        jpc.eNode = Node.create(Node.T_NORMAL, "}");

        Reader reader = new InputStreamReader(new ByteArrayInputStream("else".getBytes()));
        PushbackReader pbr = new PushbackReader(reader, 3);
        TokenMgr mgr = new TokenMgr(pbr, "");

        JavaParser.procComment2(jpc, new Token(Token.NAME,"test"),mgr);
        assertEquals("}test",jpc.eNode.getString());

    }

    /**
     * procComment2メソッドのテスト０５
     * @throws IOException
     */
    @Test
    public void testProcComment205() throws IOException {
        JavaParseCtrl jpc = new JavaParseCtrl("test");
        jpc.kakkoCount = 0;
        jpc.eNode = Node.create(Node.T_NORMAL, "}");

        Reader reader = new InputStreamReader(new ByteArrayInputStream(":aaa".getBytes()));
        PushbackReader pbr = new PushbackReader(reader, 3);
        TokenMgr mgr = new TokenMgr(pbr, "");

        JavaParser.procComment2(jpc, new Token(Token.NAME,"test"),mgr);
        assertEquals("test",jpc.eNode.getString());

    }

    /**
     * procOtherメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testProcOther() throws IOException {
        JavaParseCtrl jpc = new JavaParseCtrl("test");
        jpc.initEnd = false;
        jpc.prevCRLF = true;

        JavaParser.procOther(jpc, new Token(Token.NAME,"finally"));
        assertEquals("finally",jpc.eNode.getString());

        jpc.cNode = Node.create(Node.T_NORMAL, "case");
        jpc.cNode.setParent(Node.create(Node.T_NORMAL, "case"));
        JavaParser.procOther(jpc, new Token(Token.NAME,"default"));
        assertEquals("default",jpc.eNode.getString());

        jpc.initEnd = false;
        JavaParser.procOther(jpc, new Token(Token.NAME,"while"));
        assertEquals("while",jpc.eNode.getString());

        jpc.prevCRLF = false;
        JavaParser.procOther(jpc, new Token(Token.NAME,"if"));
        assertEquals("while if",jpc.eNode.getString());
    }
}
