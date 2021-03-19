package jp.co.tis.s2n.javaConverter.convert.program;

import org.junit.Test;

import jp.co.tis.s2n.javaConverter.token.Token;

/**
*
* {@link ParamUtil}のテスト。
*
*/
public class ParamUtilTest {

    /**
     * addメソッドのテスト
     */
    @Test
    public void testAdd() {
        Token token1 = new Token(Token.SYMBOL,"(");
        Token token2 = new Token(Token.SYMBOL,")");
        Token token3 = new Token(Token.SYMBOL,"[");
        Token token4 = new Token(Token.SYMBOL,"]");
        Token token5 = new Token(Token.SYMBOL,"{");
        Token token6 = new Token(Token.SYMBOL,"}");
        Token token7 = new Token(Token.SYMBOL,";");
        Token token8 = new Token(Token.SYMBOL,",");
        Token token9 = new Token(Token.SYMBOL,"\"");
        Token token10 = new Token(Token.SYMBOL,"'");

        ParamUtil util1 = new ParamUtil();
        util1.add(token1);
        util1.add(token9);
        util1.add(new Token(Token.NAME,"a"));
        util1.add(token9);
        util1.add(token8);
        util1.add(token10);
        util1.add(new Token(Token.NAME,"b"));
        util1.add(token10);
        util1.add(token2);

        ParamUtil util2 = new ParamUtil();
        util2.add(token9);
        util2.add(token10);
        util2.add(token10);
        util2.add(token9);
        util2.add(token10);
        util2.add(token9);
        util2.add(token9);
        util2.add(token10);
        util2.add(token3);
        util2.add(token4);
        util2.add(token5);
        util2.add(token7);
        util2.add(token6);
        util2.add(token7);

        ParamUtil util3 = new ParamUtil();
        util3.add(token10);
        util3.add(token9);
        util3.add(token9);
        util3.add(token10);
        util3.add(token9);
        util3.add(token10);
        util3.add(token10);
        util3.add(token9);

        ParamUtil util4 = new ParamUtil();
        util4.add(token7);//;
        util4.add(token5);//{
        util4.add(token7);//;
        util4.add(token3);//[
        util4.add(token7);//;
        util4.add(token1);//(
        util4.add(token7);//;
        util4.add(token2);//)
        util4.add(token7);//;
        util4.add(token4);//]
        util4.add(token7);//;
        util4.add(token6);//}
        util4.add(token7);//;

        util4.add(token3);//[
        util4.add(token7);//;
        util4.add(token5);//{
        util4.add(token7);//;
        util4.add(token1);//(
        util4.add(token7);//;
        util4.add(token2);//)
        util4.add(token7);//;
        util4.add(token6);//}
        util4.add(token7);//;
        util4.add(token4);//]
        util4.add(token7);//;

        util4.add(token1);
        util4.add(token7);//;
        util4.add(token3);//[
        util4.add(token7);//;
        util4.add(token5);//{
        util4.add(token7);//;
        util4.add(token6);//}
        util4.add(token7);//;
        util4.add(token4);//]
        util4.add(token7);//;
        util4.add(token2);//)
        util4.add(token7);//;


    }
}
