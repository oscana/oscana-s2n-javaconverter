package jp.co.tis.s2n.javaConverter.node;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.Test;

import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.file.S2nFileWriter;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
*
* {@link NodeUtil}のテスト。
*
*/
public class NodeUtilTest {

    /**
     * fprintAllメソッドのテスト
     * @throws IOException
     */
    @Test
    public void testFprintAll() throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("logs/error.log"));
        S2nFileWriter s2nfilewriter = new S2nFileWriter(osw, new S2nProfile());
        Node node1 = Node.create(Node.T_CLASS,"test");
        NodeUtil.fprintAll(s2nfilewriter, node1);

        Node node2 = Node.create(Node.T_CLASS,"XenCrLf");
        node2.addParam(new Token(Node.T_NORMAL,"test"));
        NodeUtil.fprintAll(s2nfilewriter, node2);

        Node node3 = Node.create(Node.T_CLASS,"test");
        node3.addParam(new Token(Node.T_NORMAL,"test"));
        NodeUtil.fprintAll(s2nfilewriter, node3);

        S2nProfile s2nProfile = new S2nProfile();
        s2nProfile.setLineSeparator("\r");
        S2nFileWriter s2nfilewriter2 = new S2nFileWriter(osw, s2nProfile);
        NodeUtil.fprintAll(s2nfilewriter2, node3);
    }

    /**
     * addImplementsメソッドのテスト
     */
    @Test
    public void testAddImplements() {
        Node node = Node.create(new Token(Token.NAME,"implements"));
        node.addParam(new Token(Token.SYMBOL,"implements"));
        node.addParam(new Token(Token.NAME,"dsa"));
        node.addParam(new Token(Token.NAME,"implements"));
        node.addParam(new Token(Token.NAME,"extends"));
        NodeUtil.addImplements(node, "test");
        assertEquals("implementsimplementsdsaimplements,test extends",node.getString());

        Node node2 = Node.create(new Token(Token.NAME,"implements"));
        node2.addParam(new Token(Token.NAME,"implements"));
        node2.addParam(new Token(Token.SYMBOL,"{"));
        NodeUtil.addImplements(node2, "test");
        assertEquals("implementsimplements,test{",node2.getString());

    }

    /**
     * clearCrLfAndComment1メソッドのテスト
     */
    @Test
    public void testClearCrLfAndComment1() {
        Node node = Node.create(new Token(Token.NAME,"implements"));
        node.addParam(new Token(Token.COMMENT1,"/**/"));
        node.addParam(new Token(Token.COMMENT1,"/**/"));
        node.addParam(new Token(Token.COMMENT1,"/**/"));
        node.addParam(new Token(Token.CRLF,"/r/n"));
        node.addParam(new Token(Token.CRLF,"/r/n"));
        node.addParam(new Token(Token.COMMENT1,"/**/"));

        NodeUtil.clearCrLfAndComment1(node);
        assertEquals("implements/**/*//**/*//**/*/  /**/",node.getString());

        Node node2 = Node.create(new Token(Token.NAME,"implements"));
        node2.addParam(new Token(Token.CRLF,"/r/n"));
        node2.addParam(new Token(Token.CRLF,")"));
        node2.addParam(new Token(Token.COMMENT1,"/**/"));

        NodeUtil.clearCrLfAndComment1(node2);
        assertEquals("implements /**/",node2.getString());

    }

    /**
     * getClassGenericsTypeメソッドのテスト
     */
    @Test
    public void testGetClassGenericsType() {
        Node node = Node.create(Node.T_CLASS,"class <T> AAA");
        node.addParam(new Token(Token.SYMBOL,"implements"));
        assertEquals("T",NodeUtil.getClassGenericsType(node));
    }

    /**
     * getFieldメソッドのテスト
     */
    @Test
    public void testGetField() {
        Node node = Node.create(Node.T_CLASS,"class <T> AAA");
        Node nodeParent = Node.create(Node.T_CLASS,"class <T> AAA");
        node.setParent(nodeParent);
        nodeParent.add(node);
        assertNull(NodeUtil.getField(node));
    }

    /**
     * getMethodメソッドのテスト
     */
    @Test
    public void testGetMethod() {
        Node node = Node.create(Node.T_CLASS,"class <T> AAA");
        Node node2 = Node.create(Node.T_BLOCK,"class <T> AAA2");
        Node nodeParent = Node.create(Node.T_CLASS,"class <T> AAA");
        node.setParent(nodeParent);
        node2.setParent(nodeParent);
        nodeParent.add(node2);
        nodeParent.add(node);
        assertEquals(Node.T_BLOCK,NodeUtil.getMethod(node).getType());
    }

    /**
     * getMethodGenericsTypeメソッドのテスト
     */
    @Test
    public void testGetMethodGenericsType() {
        Node node = Node.create(Node.T_BLOCK,new Token(Token.SPACE," "));
        node.addParam(new Token(Token.SPACE," "));
        node.addParam(new Token(Token.SYMBOL,"A"));
        node.addParam(new Token(Token.NAME,"static"));
        node.addParam(new Token(Token.NAME,"final"));
        node.addParam(new Token(Token.NAME,"protected"));
        node.addParam(new Token(Token.NAME,"private"));
        node.addParam(new Token(Token.NAME,"public"));
        node.addParam(new Token(Token.NAME,"void"));
        assertNull(NodeUtil.getMethodGenericsType(node));

        Node node2 = Node.create(Node.T_COMMENT1,new Token(Token.SPACE," "));
        assertNull(NodeUtil.getMethodGenericsType(node2));
    }
}
