package jp.co.tis.s2n.javaConverter.token;

import static org.junit.Assert.*;

import org.junit.Test;

/**
*
* {@link Token}のテスト。
*
*/
public class TokenTest {

    /**
     * getTextWithoutQuoteメソッドのテスト
     */
    @Test
    public void testGetTextWithoutQuote() {
       Token token = new Token(Token.STRING1,"test");
       assertEquals("es",token.getTextWithoutQuote());
    }

    /**
     * setTextWithoutQuoteメソッドのテスト
     */
    @Test
    public void testSetTextWithoutQuote() {
       Token token = new Token(Token.STRING1,"test");
       token.setTextWithoutQuote("test");
       assertEquals("'test'",token.getText());

       Token token2 = new Token(Token.STRING2,"test");
       token2.setTextWithoutQuote("test");
       assertEquals("\"test\"",token2.getText());
    }
}
