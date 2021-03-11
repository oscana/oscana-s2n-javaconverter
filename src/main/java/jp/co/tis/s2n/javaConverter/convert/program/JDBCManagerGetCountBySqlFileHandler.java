package jp.co.tis.s2n.javaConverter.convert.program;

import java.util.ArrayList;
import java.util.List;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.convert.program.ParamUtil.ParamTokenGroup;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような検索系メソッドを置換する。<br>
 *  jdbcManager.getCountBySqlFile(path, parameter) →UnivarsalDao.countBySqlFile(java.lang.Class<T> entityClass,java.lang.String sqlId,java.lang.Object params)
 *
 * @author Fumihiko Yamamoto
 *
 */
public class JDBCManagerGetCountBySqlFileHandler extends AbstractProgramConvertHandler {

    private ParamUtil inParams = new ParamUtil();
    private List<String> params = new ArrayList<String>();

    public JDBCManagerGetCountBySqlFileHandler(Node classNode, ClassNameResolver tcnr) {
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
            if ((token.getText().equals(KEY_JDBC_MANAGER + ".getCountBySqlFile"))) {
                step++;
                addCommand(new ChangeToTxtCommand(token, "DataUtil.getCountQueryResult(DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId"));
                ClassPathConvertUtil.getInstance().addImprt("oscana.s2n.common.DataUtil");

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
                List<ParamTokenGroup> groups = this.inParams.getParamTokenGroups();
                ParamTokenGroup ti = groups.get(0);
                addCommand(new InsertBeforeCommand(ti.getFirstToken(), "ParamFilter.sqlFileNameToKey("));
                addCommand(new InsertAfterCommand(ti.getLastToken(), ")"));

                if(params.size() > 1) {
                    addCommand(new InsertAfterCommand(token, ".executeQueryByMap("+ params.get(params.size()-1) +"))"));
                }else {
                    addCommand(new InsertAfterCommand(token, ".executeQueryByMap(null))"));
                }

                return true;
            } else {
                this.inParams.add(token);
                params.add(token.getText());
            }
            break;

        }

        return false;

    }

}
