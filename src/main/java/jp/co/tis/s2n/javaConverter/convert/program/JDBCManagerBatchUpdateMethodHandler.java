package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような更新系メソッドを置換する。<br>
 * <br>
 * jdbcManager.updateBatch(XXXX) → UniversalDao.batchUpdate(XXXX)<br>
 * jdbcManager.insertBatch(XXXX) → UniversalDao.batchInsert(XXXX)<br>
 * jdbcManager.deleteBatch(XXXX) → UniversalDao.batchDelete(XXXX)
 *
 * @author Fumihiko Yamamoto
 *
 */
public class JDBCManagerBatchUpdateMethodHandler extends AbstractProgramConvertHandler {

    String crudString;

    public JDBCManagerBatchUpdateMethodHandler(String crudString, ClassNameResolver tcnr) {
        super(tcnr);
        this.crudString = crudString;
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
            if ((token.getText().equals(KEY_JDBC_MANAGER + "." + crudString + "Batch"))) {
                step++;
                addCommand(new ChangeToTxtCommand(token,
                        CN_UNIVERSALDAO + ".batch" + StringUtils.capitalizeStr(crudString)));
                ClassPathConvertUtil.getInstance().addImprt("nablarch.common.dao.UniversalDao");
                bracketLevel.reset();
            }
            break;
        case 1:
            if (token.getText().equals("(")) {
                addCommand(new InsertAfterCommand(token, "ParamFilter.toList("));
                step++;
            }
            break;
        case 2:
            if ((bracketLevel.isInParenthesisLevel(0)) && (token.getText().equals(")"))) {
                addCommand(new InsertBeforeCommand(token, ")"));
                step++;
            }
            break;
        case 3:
            if ((token.getText().equals("."))) {
                step++;
                addCommand(new DeleteCommand(token));
            }
            break;
        case 4:
            if (token.getText().equals("execute")) {
                step++;
                addCommand(new DeleteCommand(token));
            } else {
                nonSupportStatement.append(token.getText());
                addCommand(new DeleteCommand(token));
            }
            break;
        case 5:
            if (token.getText().equals("(")) {
                step++;
                addCommand(new DeleteCommand(token));
            } else {
                addCommand(new DeleteCommand(token));
            }
            break;
        case 6:
            if ((bracketLevel.isInParenthesisLevel(0)) && (token.getText().equals(")"))) {
                step++;
                addCommand(new DeleteCommand(token));
                return true;
            } else {
                addCommand(new DeleteCommand(token));
            }
            break;
        }

        return false;
    }

}
