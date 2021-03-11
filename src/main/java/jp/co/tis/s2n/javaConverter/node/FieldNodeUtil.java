package jp.co.tis.s2n.javaConverter.node;

import java.util.ArrayList;

import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * フィールドノード用のアクセスフィルタ。<br>
 * フィールドを表すノードに対して、フィールドに依存した手順による属性読み書きを容易にするユーティリティクラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class FieldNodeUtil {
    private ArrayList<Token> classNameTokens;
    private ArrayList<Token> fieldNameTokens;

    /**
     * クラス名を返す。
     * @return クラス名
     */
    public String getClassName() {
        StringBuffer sb = new StringBuffer();
        for (Token token : classNameTokens) {
            sb.append(token.getTextWithoutQuote());
        }
        return sb.toString();
    }

    /**
     * フィールド名を返す。
     * @return フィールド名
     */
    public String getFieldName() {
        StringBuffer sb = new StringBuffer();
        for (Token token : fieldNameTokens) {
            sb.append(token.getTextWithoutQuote());
        }
        return sb.toString();
    }

    public FieldNodeUtil(Node fieldNode) {
        super();

        ArrayList<Token> classNameCandidateToken = new ArrayList<Token>();
        ArrayList<Token> formNameCandidateToken = new ArrayList<Token>();

        int genericBracketLevel = 0;
        int arrayBracketLevel = 0;

        for (Token ct : fieldNode.getAllTokens()) {
            if (ct.getType() == Token.NAME) {
                if (genericBracketLevel > 0) {
                    formNameCandidateToken.add(ct);

                } else {
                    classNameCandidateToken = formNameCandidateToken;
                    formNameCandidateToken = new ArrayList<Token>();
                    ;
                    formNameCandidateToken.add(ct);
                }
            } else if ((ct.getType() == Token.SEMICOLON) || (ct.getType() == Token.EQ1)) {
                this.fieldNameTokens = formNameCandidateToken;
                this.classNameTokens = classNameCandidateToken;
                if (genericBracketLevel != 0) {
                    throw new RuntimeException(fieldNode.getStringWithoutCRLF() + " - <>が閉じていません。");
                }
                break;
            } else if (ct.getText().equals("<")) {
                formNameCandidateToken.add(ct);
                genericBracketLevel++;

            } else if (ct.getText().equals(">")) {
                formNameCandidateToken.add(ct);
                genericBracketLevel--;
            } else if (ct.getText().equals("[")) {
                formNameCandidateToken.add(ct);
                arrayBracketLevel++;

            } else if (ct.getText().equals("]")) {
                formNameCandidateToken.add(ct);
                arrayBracketLevel--;

            } else {
                if (genericBracketLevel > 0) {
                    formNameCandidateToken.add(ct);
                }
            }
        }
        if (this.fieldNameTokens == null) {
            this.fieldNameTokens = formNameCandidateToken;
            this.classNameTokens = classNameCandidateToken;
            if (genericBracketLevel != 0) {
                throw new RuntimeException(fieldNode.getStringWithoutCRLF() + " - <>が閉じていません。");
            }
            if (arrayBracketLevel != 0) {
                throw new RuntimeException(fieldNode.getStringWithoutCRLF() + " - []が閉じていません。");
            }

        }
    }

    //内部用
    private ArrayList<Token> getClassNameTokens() {
        return classNameTokens;
    }

    /*
     * フィールドノードのクラス名を変更する。
     * @param fieldNode ノード
     * @param className クラス名
     */
    public static void setFieldClass(Node fieldNode, String className) {
        FieldNodeUtil f = new FieldNodeUtil(fieldNode);
        ArrayList<Token> tks = f.getClassNameTokens();
        int index = 0;
        for (Token token : tks) {
            if (index == 0) {
                token.setTextWithoutQuote(className);
            } else {
                fieldNode.getParams().remove(token);
            }
            index++;
        }
    }

    /**
     * ノードをFieldとしてパースした結果を返す。<br>
     * 例外が出た場合はnullを返す。
     * @param trgNode ノード
     * @return パースできた場合はFieldNodeを、できなかった場合はnullを返す
     */
    public static FieldNodeUtil parse(Node trgNode) {
        try {
            return new FieldNodeUtil(trgNode);
        } catch (Exception e) {
            return null;
        }

    }

}
