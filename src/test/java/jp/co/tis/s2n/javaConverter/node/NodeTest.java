package jp.co.tis.s2n.javaConverter.node;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.co.tis.s2n.javaConverter.token.Token;

/**
 *
 * {@link Node}のテスト。
 *
 */
public class NodeTest {

    /**
     * AddParamPosメソッドのテスト
     */
    @Test
    public void testAddParamPos() {
        Node node = Node.create(new Token(Token.NAME,"AddParamPos"));

        Token token0 = new Token(Node.T_NORMAL,"test");
        node.addParamPos(0, token0);
        assertEquals(Node.T_NORMAL,node.getType());

        Token token1 = new Token(Node.T_CLASS,"class");
        node.addParamPos(0, token1);
        assertEquals(Node.T_CLASS,node.getType());

        Token token2 = new Token(Node.T_INTERFACE,"interface");
        node.addParamPos(0, token2);
        assertEquals(Node.T_INTERFACE,node.getType());

        Token token3 = new Token(Node.T_ENUM,"enum");
        node.addParamPos(0, token3);
        assertEquals(Node.T_ENUM,node.getType());

        Token token4 = new Token(Node.T_ANNOTATIONDEF,"@interface");
        node.addParamPos(0, token4);
        assertEquals(Node.T_ANNOTATIONDEF,node.getType());

    }

    /**
     * getStringWithoutCRLFメソッドのテスト
     */
    @Test
    public void testGetStringWithoutCRLF() {

        Node node = Node.create(new Token(Token.SPACE," "));
        Token token0 = new Token(Token.CRLF,"/r/n");
        node.addParam(token0);
        assertEquals(" ",node.getStringWithoutCRLF());

        Node node2 = Node.create(Token.CRLF,"/r/n");
        Token token2 = new Token(Token.CRLF,"/r/n");
        node.addParam(token2);
        assertEquals("",node2.getStringWithoutCRLF());
    }

}
