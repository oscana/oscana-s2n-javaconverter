package jp.co.tis.s2n.javaConverter.node;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.co.tis.s2n.javaConverter.token.Token;

/**
*
* {@link NodeUtil}のテスト。
*
*/
public class ImportNodeUtilTest {

    /**
     * addImplementsメソッドのテスト
     */
    @Test
    public void testImportNodeUtil() {
        Node testNode = Node.create(new Token(Token.NAME,"test"));
        testNode.addParam(new Token(Token.NAME,"Package"));
        ImportNodeUtil util2 = new ImportNodeUtil(testNode);
        assertEquals(0, util2.getTextPos());
    }


}
