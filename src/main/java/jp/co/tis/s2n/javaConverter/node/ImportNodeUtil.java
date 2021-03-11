package jp.co.tis.s2n.javaConverter.node;

import java.util.List;

import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * Importノード用のアクセスフィルタ。<br>
 * Importを表すノードに対して、Importに依存した手順による属性読みこみを容易にするユーティリティクラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ImportNodeUtil {
    /**
     * static型のimportならtrue
     */
    @SuppressWarnings("unused")
    private boolean isStatic;
    /**
     * import文のクラス（パッケージ）
     */
    @SuppressWarnings("unused")
    private String importStr;
    /**
     * Nodeにあるimportの位置（ノードを編集する時に使用する）
     */
    private int textPos;

    public ImportNodeUtil(Node importNode) {
        boolean foundImport = false;

        List<Token> allTokens = importNode.getAllTokens();
        for (int i = 0; i < allTokens.size(); i++) {
            Token t = allTokens.get(i);
            if (t.getType() == Token.NAME) {
                if (foundImport == false) {
                    if ("import".equals(t.getText())) {
                        foundImport = true;
                    }
                } else {
                    if ("static".equals(t.getText())) {
                        isStatic = true;
                    } else {
                        this.importStr = t.getText();
                        textPos = i;
                        break;
                    }
                }
            }
        }

    }

    /**
     * Nodeにあるimportのテキストの位置（ノードを編集する時に使用する）を取得する。
     * @return Nodeにあるimportのテキストの位置
     */
    public int getTextPos() {
        return textPos;
    }

}