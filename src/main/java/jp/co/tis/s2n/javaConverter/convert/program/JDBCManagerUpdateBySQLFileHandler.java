package jp.co.tis.s2n.javaConverter.convert.program;

import java.util.List;

import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.convert.program.ParamUtil.ParamTokenGroup;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような更新系メソッドを置換する。<br>
 * jdbcManager.updateBySqlFile(path, parameter).execute() → DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId(sqlId).executeUpdateByObject(params);
 *
 * @author Fumihiko Yamamoto
 *
 */
public class JDBCManagerUpdateBySQLFileHandler extends AbstractProgramConvertHandler {

    private ParamUtil inParams = new ParamUtil();

    public JDBCManagerUpdateBySQLFileHandler(ClassNameResolver tcnr) {
        super(tcnr);
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
            if ((token.getText().equals(KEY_JDBC_MANAGER + ".updateBySqlFile"))) {
                step++;
                addCommand(new ChangeToTxtCommand(token,
                        "DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId"));
                bracketLevel.reset();
            }
            break;
        case 1:
            if (token.getText().equals("(")) {
                step++;
            }
            break;
        case 2:
            if ((bracketLevel.isInParenthesisLevel(0)) && (token.getText().equals(")"))) {
                step++;
                List<ParamTokenGroup> paramList = this.inParams.getParamTokenGroups();
                addCommand(new InsertBeforeCommand(token, paramList.get(0).getString()));
            } else {
                this.inParams.add(token);
                addCommand(new DeleteCommand(token));
            }
            break;
        case 3:
            if ((token.getText().equals("."))) {
                step++;
            }
            break;
        case 4:
            if ((token.getText().equals("execute"))) {
                step++;
                addCommand(new ChangeToTxtCommand(token, "executeUpdateByObject"));
            } else {
                nonSupportStatement.append(token.getText());
                addCommand(new DeleteCommand(token));
            }
            break;
        case 5:
            if (token.getText().equals("(")) {
                step++;
            }
            break;
        case 6:
            if ((bracketLevel.isInParenthesisLevel(0)) && (token.getText().equals(")"))) {
                step++;
                List<ParamTokenGroup> paramList = this.inParams.getParamTokenGroups();
                addCommand(new InsertBeforeCommand(token, paramList.get(1).getString()));
                return true;
            } else {
                nonSupportStatement.append(token.getText());
                addCommand(new DeleteCommand(token));
            }
            break;
        }

        return false;
    }

}
