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
 *  getCountBySqlFile(path, parameter) →DataUtil.getCountQueryResult(DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId(path, parameter)
 *
 * @author Fumihiko Yamamoto
 *
 */
public class S2AbstractServiceGetCountBySQLHandler extends AbstractProgramConvertHandler {

    private Node classNode;
    private ParamUtil inParams = new ParamUtil();
    private List<String> params = new ArrayList<String>();

    public S2AbstractServiceGetCountBySQLHandler(Node classNode, ClassNameResolver tcnr) {
        super(tcnr);
        this.classNode = classNode;

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
            if ((token.getText().equals("getCountBySqlFile"))) {
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
                // sqlパス未設定の場合
                if (!ti.getLastToken().getText().contains("#")) {
                    StringBuilder sqlPath = new StringBuilder();
                    // sqlパスを推測
                    String className = this.classNode.getAllTokens().get(4).getText();
                    String entityName = "";
                    int servicePos = className.indexOf("BaseService") == -1 ? className.indexOf("Service")
                            : className.indexOf("BaseService");
                    if (servicePos != -1) {
                        entityName += className.substring(0, servicePos);
                    }
                    if (ti.getLastToken().getText().contains(".sql")) {
                        sqlPath.append("\"").append(this.cnr.resolveFullName(entityName)).append("#")
                                .append("\"").append(" + ").append(ti.getLastToken().getText());
                    } else {
                        sqlPath.append("\"").append(this.cnr.resolveFullName(entityName)).append("#")
                                .append("\"").append(" + ").append(ti.getLastToken().getTextWithoutQuote());
                    }
                    ti.getLastToken().setText(sqlPath.toString());
                }

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
