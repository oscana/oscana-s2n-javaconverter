package jp.co.tis.s2n.javaConverter.token;

/**
 * Tokenクラス。<br>
 *
 * 目入れを単語や記号、リテラルなどの単位で分割したものである。<br>
 * 分割単位については以下のTokenデータ型に準ずる。<br>
 * NAME,NAMEJ,NUMERIC,STRING1,STRING2,SYMBOL,EQ1,EQ2,TAB,SPACE,CRLF,COLON,SEMICOLON,COMMENT1,COMMENT2,END,EOF
 *
 * @author Fumihiko Yamamoto
 *
 */
public class Token {

    /** * 通常文字の連続<br>連続する通常文字（半角のアルファベット、途中で現れたアンダースコア、ピリオド、コロン）からなるWord */
    public final static int NAME = 1;
    /** * 全角文字始まりの文字列 */
    public final static int NAMEJ = 2;
    /** * 数値リテラル */
    public final static int NUMERIC = 3;
    /** * シングルクオートで囲まれた文字リテラル  */
    public final static int STRING1 = 4;
    /** * ダブルクオートで囲まれた文字リテラル   */
    public final static int STRING2 = 5;
    /**
     * 以下のような記号
     *  ・"(){}[]+-.,&!%" <BR>
     *  ・* または **<BR>
     *  ・コメントになっていないスラッシュ
     *  ・<,<=,>,>=,^,^=,|,||
     *
     *  ・".3"のようなケースはNUMERICである。  */
    public final static int SYMBOL = 6;
    /** * イコール（代入演算子） = （1トークンで1文字) */
    public final static int EQ1 = 7;
    /** * イコール（比較演算子） == （1トークンで1つの比較演算子) */
    public final static int EQ2 = 8;

    /** * TAB文字（1トークンで1文字)  */
    public final static int TAB = 9;
    /** * 半角空白の連続 （空白の連続は１つのトークンになる）*/
    public final static int SPACE = 10;

    /** * 改行文字(1トーンで１改行）<br>\r\nもしくは\nどちらであっても\r\nが格納される */
    public final static int CRLF = 11;

    /** * コロン文字（1トークンで1文字)  */
    public final static int COLON = 12;
    /** * セミコロン文字（1トークンで1文字)  */
    public final static int SEMICOLON = 13;
    /** * 行コメント<br>開始記号とコメント文字列が含まれる  */
    public final static int COMMENT1 = 90;
    /** * ブロックコメント<br>開始終了記号とその間の文字列が含まれる   */
    public final static int COMMENT2 = 91;

    /** * ファイルの終端を表す EOFと同じ */
    public final static int END = 99;
    /** * ファイルの終端を表す ENDと同じ*/
    public final static int EOF = 99;

    private int type;
    private String text;

    public Token(int type, String text) {
        this.type = type;
        this.text = text;
    }

    /**
     * このトークンのTokenデータ型を返す。
     * @return Tokenデータ型
     */
    public int getType() {
        return type;
    }

    /**
     * トークンのテキストをクオート無しで返す。<br>
     * Tokenデータ型が、STRING1,STRING2の場合はシングルクオート、ダブルクオートを取り除いた中身を返す。<br>
     * それいがのデータ型の場合は格納されているテキストをそのまま返す。
     * @return トークンのテキスト
     */
    public String getTextWithoutQuote() {
        if (this.type == Token.STRING1 || this.type == Token.STRING2) {
            int size = text.length();
            return text.substring(1, size - 1);
        } else {
            return text;
        }
    }

    /**
     * トークンのテキストをそのまま返す。<br>
     * Tokenデータ型が、STRING1,STRING2の場合はシングルクオート、ダブルクオートがついた形で返す。
     * @return トークンのテキスト
     */
    public String getText() {
        return text;
    }

    /**
     * Tokenデータ型を変更する。<br>
     * Tokenデータ型を変更しても保持されるテキストは変更しない。<br>
     * 区切り文字、囲み文字などがある場合は別途テキストを変更しないとデータ型とテキストに不整合が生ずる。
     * @param type Tokenデータ型
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * トークンのテキストを変更する。<br>
     * 本機能は、Tokenデータ型に応じた特別なフォローは行わない。<br>
     * 区切り文字、囲み文字などがある場合はデータ型に応じた区切り文字、囲み文字を呼び出し側で用意しないとデータ型とテキストに不整合が生ずる。
     * @param text トークンのテキスト
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * トークンのテキストを変更する。<br>
     * 本機能は、Tokenデータ型がSTRING1, STRING2の場合はデータ型応じたクオートを自動で付与する。<br>
     * 呼び出し側でクオートを付与すると2重でクオートがつくことになる。<br>
     * Tokenデータ型がSTRING1, STRING2以外の場合はそのままテキストを変更する。
     * @param text トークンのテキスト
     */
    public void setTextWithoutQuote(String text) {
        if (this.type == Token.STRING1) {
            this.text = "'" + text + "'";
        } else if (this.type == Token.STRING2) {
            this.text = "\"" + text + "\"";
        } else {
            this.text = text;
        }
    }

}
