package jp.co.tis.s2n.javaConverter.convert.program;

import java.util.ArrayList;
import java.util.List;

import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような変換を行う。<br>
 * 変換前：AssertionUtil.assertNotNull(String,Object); <br>
 * 変換後：if (Object == null) { throw new NullPointerException(String); }
 *
 * @author Rai Shuu
 *
 */
public class AssertionUtilHandler extends AbstractProgramConvertHandler implements AutoInstall {

    private List<Token> msgTokens;//変数Stringを保存用Token
    private List<Token> objTokens;//変数Objectを保存用Token

    public AssertionUtilHandler(ClassNameResolver tcnr) {
        super(tcnr);
    }

    /**
     * 初期化処理。
     */
    @Override
    public void init() {
        super.init();
        // オブジェクト変数初期化
        msgTokens = new ArrayList<Token>();
        objTokens = new ArrayList<Token>();
    }

    /**
     * ハンドラを実施するかどうかの判定条件。
     * @param line String型の行ソース
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return チェック結果 true:実行する、false:実行しない
     */
    @Override
    public boolean test(String line, Node lineNode) {
        return line.contains("AssertionUtil.assertNotNull");
    }

    /**
     * 変換主処理。
     * @param token 処理対象トークン
     * @param bracketLevel レベル
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return 処理結果
     *
     */
    @Override
    protected boolean handle(Token token, BracketLevel bracketLevel, Node lineNode) {

        switch (step) {
        case 0:
            if ((token.getText().equals("AssertionUtil.assertNotNull"))) {
                // "AssertionUtil.assertNotNull"を空白に変換
                addCommand(new ChangeToTxtCommand(token, ""));
                step++;
                bracketLevel.reset();
            }
            break;
        case 1:
            if (token.getText().equals("(")) {
                step++;
            }
            // "("を削除
            addCommand(new DeleteCommand(token));
            break;
        case 2:
            // assertNotNullのStringパラメータを保存
            if (!token.getText().equals(",") && token.getType() != Token.SPACE) {
                msgTokens.add(token);
            }
            if(token.getText().equals(",")) {
                step++;
            }
            addCommand(new DeleteCommand(token));
            break;
        case 3:
            if ((bracketLevel.isInParenthesisLevel(0)) && (token.getText().equals(")"))) {
                step++;
            }else if (token.getType() != Token.SPACE) {
                // assertNotNullのObjectパラメータを保存
                objTokens.add(token);
            }
            addCommand(new DeleteCommand(token));
            break;
        case 4:
            step++;
            // 変換後の文字列を出力する
            StringBuffer sbObjToken = new StringBuffer();
            for (Token objToken : objTokens) {
                sbObjToken.append(objToken.getText());
            }

            StringBuffer sbMsgToken = new StringBuffer();
            for (Token msgToken : msgTokens) {
                sbMsgToken.append(msgToken.getText());
            }

            String insText = "if (" + sbObjToken.toString() + " == null) { throw new NullPointerException(" + sbMsgToken.toString() + "); }";
            addCommand(new InsertAfterCommand(token, insText));
            // ";"を削除
            addCommand(new DeleteCommand(token));
            return true;

        }
        return false;

    }

}
