package jp.co.tis.s2n.javaConverter.convert.program;

import java.util.List;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.convert.program.ParamUtil.ParamTokenGroup;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような検索系メソッドを置換する。<br>
 *  selectBySQLFile(Class<T> baseClass, String path,Object parameter).getResultList →UniversalDao.findAllBySqlFile(final Class<T> entityClass, final String sqlId, final Object params)<br>
 *  selectBySQLFile(Class<T> baseClass, String path).getResultList→UniversalDao.findAllBySqlFile(final Class<T> entityClass, final String sqlId)<br>
 *  selectBySQLFile(Class<T> baseClass, String path,Object parameter).getSingleResult→UniversalDao.findBySqlFile(final Class<T> entityClass, final String sqlId, final Object params)<br>
 *  selectBySQLFile(Class<T> baseClass, String path).getSingleResult→UniversalDao.findBySqlFile(final Class<T> entityClass, final String sqlId, EMPTY_PARAM)
 *
 * @author Fumihiko Yamamoto
 *
 */
public class S2AbstractServiceSelectBySQLHandler extends AbstractProgramConvertHandler {

    private Node classNode;
    private Token mainToken;
    private boolean getId = false;
    private boolean isBeanMapResult = false;

    private ParamUtil inParams = new ParamUtil();

    /**
     * IDを取得する。
     * @return id
     */
    public boolean isGetId() {
        return getId;
    }

    public S2AbstractServiceSelectBySQLHandler(Node classNode, ClassNameResolver tcnr) {
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
            if ((token.getText().equals("selectBySqlFile"))) {
                step++;
                this.mainToken = token;
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
                ParamTokenGroup ti = groups.get(1);
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

                if (groups.size() > 2) {
                    addCommand(new InsertAfterCommand(ti.getLastToken(), ")"));
                } else {
                    addCommand(new InsertAfterCommand(ti.getLastToken(), "),new Object[0]"));
                    if (getId) {
                        addCommand(new InsertAfterCommand(token, ".getString(\"Id\")"));
                    }
                }

            } else {
                if (token.getText().equals("String.class")) {
                    token.setText("SqlRow.class");
                    getId = true;

                }else if(token.getText().equals("BeanMap.class")) {
                    token.setText("SqlRow.class");
                    isBeanMapResult = true;
                    ClassPathConvertUtil.getInstance().addImprt("nablarch.core.db.statement.SqlRow");
                }
                this.inParams.add(token);
            }
            break;
        case 3:
            if ((token.getText().equals("."))) {
                step++;
                addCommand(new DeleteCommand(token));
            }
            break;

        case 4:
            if (token.getText().equals("getSingleResult")) {
                step++;
                if(isBeanMapResult) {
                    addCommand(new ChangeToTxtCommand(mainToken,
                            "DataUtil.convertToBeanMap(" + CN_UNIVERSALDAO + ".findBySqlFile"));
                    addCommand(new InsertAfterCommand(token, ")"));
                    ClassPathConvertUtil.getInstance().addImprt("oscana.s2n.common.DataUtil");
                }else {
                    addCommand(new ChangeToTxtCommand(mainToken, CN_UNIVERSALDAO + ".findBySqlFile"));
                }

                addCommand(new DeleteCommand(token));
                ClassPathConvertUtil.getInstance().addImprt("nablarch.common.dao.UniversalDao");
            } else if (token.getText().equals("getResultList")) {
                step++;
                if (isBeanMapResult) {
                    addCommand(new ChangeToTxtCommand(mainToken,
                            "DataUtil.convertToBeanMapList(" + CN_UNIVERSALDAO + ".findAllBySqlFile"));
                    addCommand(new InsertAfterCommand(token, ")"));
                    ClassPathConvertUtil.getInstance().addImprt("oscana.s2n.common.DataUtil");
                } else {
                    addCommand(new ChangeToTxtCommand(mainToken, CN_UNIVERSALDAO + ".findAllBySqlFile"));
                }

                addCommand(new DeleteCommand(token));
                ClassPathConvertUtil.getInstance().addImprt("nablarch.common.dao.UniversalDao");
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

            if (token.getText().equals(")")) {
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
