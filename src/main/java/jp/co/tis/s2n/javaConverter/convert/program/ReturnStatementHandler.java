package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 遷移先の記述を以下のように置換する。<br>
 *  return "XXXXX" → return OscanaHttpResourceConverUtil.createHttpResponse( "XXXXX", this, nabRequest, nabContext, "XXXXX");
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ReturnStatementHandler extends AbstractProgramConvertHandler {

    private String actionName;

    public ReturnStatementHandler(String actionName, ClassNameResolver tcnr) {
        super(tcnr);
        this.actionName = actionName;
    }

    /**
     * 変換主処理。
     * @param token 処理対象トークン
     * @param bracketLevel レベル
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return 処理結果 true:変換済、false:変換してない
     */
    @Override
    protected boolean handle(Token token, BracketLevel bracketLevel, Node lineNode) {

        switch (step) {
        case 0:
            if ((token.getText().equals("return"))) {
                step++;
                addCommand(new InsertAfterCommand(token, " OscanaHttpResourceConverUtil.createHttpResponse("));
            }
            break;
        case 1:
            if ((token.getText().equals(";"))) {
                step++;
                addCommand(new InsertBeforeCommand(token, ", this, nabRequest, nabContext, \"" + actionName + "\")"));
                return true;
            }
            break;
        }
        return false;
    }
}
