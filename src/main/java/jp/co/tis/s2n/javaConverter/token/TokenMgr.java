package jp.co.tis.s2n.javaConverter.token;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.Stack;

/**
 * ファイルからトークンを切り出す。<br>
 *
 * ファイルを読み込み、パースしながらトークンを切り出す。<br>
 * 切り出す単位はTokenのデータ型を参照する。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class TokenMgr {

    private PushbackReader pbr = null;

    /**
     * ファイル名
     */
    public String currentFileName = null;

    /**
     * 現在の行番号
     */
    public int lct = 0; // Line Counter

    private Stack<Token> pushedTokens = new Stack<>();

    public TokenMgr(PushbackReader pbr, String fileName) {
        this.pbr = pbr;
        this.currentFileName = fileName;
        lct = 0;
    }

    /**
     * トークンを再度キューの先頭に戻す。<br>
     * pushしたトークンはキューの先頭に挿入されるので、次回のgetTokenで返される。
     * @param token トークン
     */
    public void pushToken(Token token) {
        pushedTokens.push(token);

    }

    /**
     * １トークンを切り出す。
     * @return 切り出したトークン
     * @throw IOException 例外
     */
    public Token getToken() throws IOException {

        if (pushedTokens.empty() == false) {
            return pushedTokens.pop();
        }

        int c = 0;

        StringBuilder sb = new StringBuilder();

        for (;;) {

            c = pbr.read();

            if (c == '\r') {

                // Skip YEN-R;
                continue;

            } else if (c == -1 || c == 65535) {

                return new Token(Token.END, null);

            } else if (c == ';') {

                return new Token(Token.SEMICOLON, ";");

            } else if (c == ':') {

                return new Token(Token.COLON, ":");

            } else if (c == '\n') {

                lct++;
                return new Token(Token.CRLF, "\r\n");

            } else if (isSymbol(c)) {

                if (c == '.') {
                    int c1 = pbr.read();
                    pbr.unread(c1);
                    if (isNumeric(c1)) {
                        // ".003"のようなケース
                        return procNumeric(c, sb, pbr);
                    }
                }

                sb.append(Character.toChars(c));
                return new Token(Token.SYMBOL, sb.toString());

            } else if (isTab(c)) {

                sb.append(Character.toChars(c));
                return new Token(Token.TAB, sb.toString());

            } else if (isSpace(c)) {

                return procSpace(c, sb, pbr);

            } else if (isNumeric(c)) {

                return procNumeric(c, sb, pbr);

            } else if (isAlpha(c)) {

                return procAlphaNumeric(c, sb, pbr);

            } else if (c == '\'') {

                return procSingleQuote(c, sb, pbr);

            } else if (c == '"') {

                return procDoubleQuote(c, sb, pbr);

            } else if (c == '*') {

                Token token = procAster(c, sb, pbr);
                return token;

            } else if (c == '/') {

                return procSlash(c, sb, pbr);

            } else if (c == '=') {

                return procEQ(c, sb, pbr);

            } else if (c == '<') {

                return procLessThan(c, sb, pbr);

            } else if (c == '>') {

                return procLargeThan(c, sb, pbr);

            } else if (c == '%') {

                return procPercent(c, sb, pbr);

            } else if (c == '^') {

                return procHat(c, sb, pbr);

            } else if (c == '|') {

                return procOr(c, sb, pbr);

            } else {

                // 全角文字始まりの文字列の処理
                return procOther(c, sb, pbr);

            }

        }

    }

    /**
     * 数値であるかチェックする。
     * @param c チェック対象
     * @return チェック結果、0-9.であれば：true、その以外：false
     */
    private boolean isNumeric(int c) {

        if (('0' <= c && c <= '9') || c == '.') {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 数値であるかチェックする。
     * @param c チェック対象
     * @return チェック結果、0-9.であれば：true、その以外：false
     */
    private boolean isNumeric2(int c) {

        if (('0' <= c && c <= '9') || c == '.' || c == '_') {
            return true;
        } else {
            return false;
        }

    }

    /**
     * アルファベットかどうかチェックする。
     * @param c チェック対象
     * @return チェック結果、A-Za-z_$@であれば：true、その以外：false
     */
    private boolean isAlpha(int c) {

        if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || c == '_' || c == '$' || c == '@') {
            return true;
        } else {
            return false;
        }

    }


    /**
     * 英数文字（アルファベット、数字、アンダースコア）であるかチェックする。
     * @param c チェック対象
     * @return チェック結果、A-Za-z0-9_$@であれば：true、その以外：false
     */
    private boolean isAlphaNumeric(int c) {

        if (('A' <= c && c <= 'Z')
                || ('a' <= c && c <= 'z')
                || ('0' <= c && c <= '9')
                || ('_' == c)
                || ('$' == c)
                || ('@' == c)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * シンボル文字であるかチェックする。
     * @param c チェック対象
     * @return チェック結果、(){}[]+-.,&?!であれば：true、その以外：false
     */
    private boolean isSymbol(int c) {

        if (c == '('
                || c == ')'
                || c == '{'
                || c == '}'
                || c == '['
                || c == ']'
                || c == '+'
                || c == '-'
                || c == '.'
                || c == ','
                || c == '&'
                || c == '?'
                || c == '!') {
            return true;
        } else {
            return false;
        }

    }

    /**
     * タブ文字であるかチェックする。
     * @param c チェック対象
     * @return チェック結果、タブ文字であれば：true、その以外：false
     */
    private boolean isTab(int c) {

        if (c == '\t') {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 空白文字であるかチェックする。
     * @param c チェック対象
     * @return チェック結果、半角空白であれば：true、その以外：false
     */
    private boolean isSpace(int c) {

        if (c == ' ') {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 数値リテラルのトークンを切り出す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procNumeric(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {
        sb.append(Character.toChars(c1));
        boolean point = false;
        boolean exponent = false; // 指数部かどうか
        boolean exponentAndSign = false; // 指数部に符号が出現したかどうか
        boolean precision = false; // 最大単精度/最大倍精度/最大拡張精度
        for (;;) {
            int c = pbr.read();
            if (exponent) {
                if (c == '+' || c == '-') {
                    if (exponentAndSign) {
                        pbr.unread(c);
                        break;
                    }
                    // 指数部は +/- 1回だけ可
                    exponentAndSign = true;
                    sb.append(Character.toChars(c));
                    continue;
                } else if (c == 'B' || c == 'b') {
                    // 2 進浮動小数点定数
                    sb.append(Character.toChars(c));
                    break;
                }
                // 数字の場合は通常通り処理すればよい

            } else if (precision) {
                if (c == 'B' || c == 'b') {
                    // 2 進浮動小数点定数
                    sb.append(Character.toChars(c));
                    break;
                }
                // 数字の場合は通常通り処理すればよい

            } else if (c == 'E' || c == 'e') {
                // 指数表記
                exponent = true;
                sb.append(Character.toChars(c));
                continue;
            } else if (c == 'S' || c == 's') {
                // 最大単精度
                precision = true;
                sb.append(Character.toChars(c));
                continue;
            } else if (c == 'D' || c == 'd') {
                // 最大倍精度
                precision = true;
                sb.append(Character.toChars(c));
                continue;
            } else if (c == 'Q' || c == 'q') {
                // 最大拡張精度
                precision = true;
                sb.append(Character.toChars(c));
                continue;
            } else if (c == 'B' || c == 'b') {
                // 2進固定小数点数
                sb.append(Character.toChars(c));
                break;
            } else if (c == 'L') {
                // ロング(Long)
                sb.append(Character.toChars(c));
                break;
            }

            if (isNumeric2(c)) {
                //数字（0-9、ドット、アンダースコア）
                if (point == false && c == '.') {
                    point = true;
                } else if (point == true && c == '.') {
                    //ドットが2連続だったら最期のドットをアンリードして終了。
                    pbr.unread(c);
                    break;
                }
                sb.append(Character.toChars(c));
            } else {
                pbr.unread(c);
                break;
            }
        }

        String retStr = sb.toString();
        return new Token(Token.NUMERIC, retStr);
    }

    /**
     * アルファベット、数字、アンダースコア、ピリオドの連続を切り出す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procAlphaNumeric(int c1, StringBuilder sb, PushbackReader pbr)
            throws IOException {

        sb.append(Character.toChars(c1));

        for (;;) {
            int c = pbr.read();
            if (isAlphaNumeric(c) || c == '_' || c == '.') {
                sb.append(Character.toChars(c));
            } else {
                pbr.unread(c);
                break;
            }
        }
        return new Token(Token.NAME, sb.toString());
    }

    /**
     * 半角空白の連続を切り出す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procSpace(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));

        for (;;) {
            int c = pbr.read();
            if (isSpace(c)) {
                sb.append(Character.toChars(c));
            } else {
                pbr.unread(c);
                break;
            }
        }
        return new Token(Token.SPACE, sb.toString());
    }

    /**
     * シングルクォートで囲まれた文字列を返す。<br>
     * ・両端のシングルクオートも含めて収集する。<br>
     * ・エスケープ記号(\)の直後にあるシングルクオートは終端にはならず、その先のシングルクォートが終端となる。<br>
     * ・エスケープ記号(\)もそのまま含まれ他文字列を返す。<br>
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procSingleQuote(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));

        for (;;) {
            int c = pbr.read();
            sb.append(Character.toChars(c));
            if (c == '\\') {
                c = pbr.read();
                sb.append(Character.toChars(c));
            } else if (c == '\'') {
                break;
            }
        }
        return new Token(Token.STRING1, sb.toString());
    }

    /**
     * ダブルクォートで囲まれた文字列を返す。<br>
     * ・両端のダブルクオートも含めて収集する。<br>
     * ・エスケープ記号(\)の直後にあるダブルクオートは終端にはならず、その先のダブルクォートが終端となる。<br>
     * ・エスケープ記号(\)もそのまま含まれ他文字列を返す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procDoubleQuote(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));

        for (;;) {
            int c = pbr.read();
            sb.append(Character.toChars(c));
            if (c == '\\') {
                c = pbr.read();
                sb.append(Character.toChars(c));
            } else if (c == '"') {
                break;
            }
        }
        return new Token(Token.STRING2, sb.toString());
    }

    /**
     * コメントで囲まれた文字列を返す。<br>
     * ブロックコメント：<br>
     * ・ブロックコメント自体も含めて収集する。<br>
     * ・複数行あれば改行も含めて収集する。<br>
     * 1行コメント：<br>
     * ・コメント開始文字列自体も含めて収集する。<br>
     *
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procSlash(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));
        int c = pbr.read();
        if (c == '*') {
            sb.append(Character.toChars(c));

            // コメント指示子 "/* ----- */" の対応。
            // 改行は、読み飛ばしを行う。
            for (;;) {
                c = pbr.read();
                if (c == '\n') {
                    sb.append(Character.toChars(c));
                } else if (c == '\r') {
                    sb.append(Character.toChars(c));
                } else if (c == '*') {
                    sb.append(Character.toChars(c));
                    c = pbr.read();
                    if (c == '/') {
                        sb.append(Character.toChars(c));
                        return new Token(Token.COMMENT2, sb.toString());
                    } else {
                        pbr.unread(c);
                    }
                } else {
                    sb.append(Character.toChars(c));
                }
            }
        } else if (c == '/') {
            sb.append(Character.toChars(c));
            for (;;) {
                c = pbr.read();
                if (c == '\r' || c == '\n' || c == -1) {
                    return new Token(Token.COMMENT1, sb.toString());
                } else {
                    sb.append(Character.toChars(c));
                }
            }
        } else {
            pbr.unread(c);
            return new Token(Token.SYMBOL, sb.toString());
        }

    }

    /**
     * イコール(=)の処理を行う。<br>
     * =であればEQ1(代入演算子), ==であればEQ2(比較演算子)を割り当てる。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procEQ(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));
        int c = pbr.read();
        if (c == '=') {
            sb.append(Character.toChars(c));
            return new Token(Token.EQ2, sb.toString());
        } else {
            pbr.unread(c);
            return new Token(Token.EQ1, sb.toString());
        }

    }

    /**
     * アスタリスク(*)の処理を行う。<br>
     * *または**を1つのトークンに切り出す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procAster(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));
        int c = pbr.read();
        if (c == '*') {
            sb.append(Character.toChars(c));
            return new Token(Token.SYMBOL, sb.toString());

        } else {
            pbr.unread(c);
            return new Token(Token.SYMBOL, sb.toString());
        }
    }

    /**
     * パーセント(%)の処理を行う。<br>
     * %は1つのトークンとして切り出す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procPercent(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));
        return new Token(Token.SYMBOL, sb.toString());

    }

    /**
     * 小なり(<)の処理を行う。<br>
     * <=もしくは<を１つのトークンとして切り出す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procLessThan(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));
        int c = pbr.read();
        if (c == '=') {
            sb.append(Character.toChars(c));
            return new Token(Token.SYMBOL, sb.toString());
        } else {
            pbr.unread(c);
            return new Token(Token.SYMBOL, sb.toString());
        }

    }

    /**
     * 大なり(>)の処理を行う。<br>
     * >=もしくは>を1つのトークンとして切り出す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procLargeThan(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));
        int c = pbr.read();
        if (c == '=') {
            sb.append(Character.toChars(c));
            return new Token(Token.SYMBOL, sb.toString());
        } else {
            pbr.unread(c);
            return new Token(Token.SYMBOL, sb.toString());
        }

    }

    /**
     * ハット(^)の処理を行う。<br>
     * ^=もしくは^を1つのトークンとして切り出す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procHat(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));
        int c = pbr.read();
        if (c == '=') {
            sb.append(Character.toChars(c));
            return new Token(Token.SYMBOL, sb.toString());
        } else {
            pbr.unread(c);
            return new Token(Token.SYMBOL, sb.toString());
        }

    }

    /**
     * OR(|)1の処理を行う。<br>
     * ||もしくは|を1つのトークンとして切り出す。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procOr(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        sb.append(Character.toChars(c1));
        int c = pbr.read();
        if (c == '|') {
            sb.append(Character.toChars(c));
            return new Token(Token.SYMBOL, sb.toString());
        } else {
            pbr.unread(c);
            return new Token(Token.SYMBOL, sb.toString());
        }

    }

    /**
     * その他の文字の処理を行う。
     * @param c1 対象数値
     * @param sb 可変シーケンス
     * @param pbr 文字ストリーム
     * @return トークン
     * @throws IOException 例外
     */
    private Token procOther(int c1, StringBuilder sb, PushbackReader pbr) throws IOException {

        // 全角文字始まりの文字列の処理
        sb.append(Character.toChars(c1));
        for (;;) {
            int c = pbr.read();
            if (c != ' '
                    && c != '\r'
                    && c != '\n'
                    && c != -1
                    && c != '.'
                    && c != ','
                    && c != ':'
                    && c != ';'
                    && c != '('
                    && c != ')') {
                sb.append(Character.toChars(c));
            } else {
                pbr.unread(c);
                break;
            }
        }

        String retstr = sb.toString();

        return new Token(Token.NAMEJ, retstr);

    }

}
