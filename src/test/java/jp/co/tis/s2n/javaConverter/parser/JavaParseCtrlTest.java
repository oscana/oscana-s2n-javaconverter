package jp.co.tis.s2n.javaConverter.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.co.tis.s2n.javaConverter.node.Node;

/**
*
* {@link JavaParseCtrl}のテスト。
*
*/
public class JavaParseCtrlTest {

    /**
     * existDoBlockメソッドのテスト
     */
   @Test
   public void testExistDoBlock() {
       JavaParseCtrl jpc = new JavaParseCtrl("test");
       jpc.cNode.add(Node.create(Node.T_MODULE, "do"));
       assertFalse(jpc.existDoBlock());

       jpc.cNode.add(Node.create(Node.T_MODULE, "do"));
       assertTrue(jpc.existDoBlock());

       JavaParseCtrl jpc2 = new JavaParseCtrl("test");
       jpc2.cNode.add(Node.create(Node.T_MODULE, "test"));
       jpc2.cNode.add(Node.create(Node.T_MODULE, "test"));
       assertFalse(jpc2.existDoBlock());
   }

}
