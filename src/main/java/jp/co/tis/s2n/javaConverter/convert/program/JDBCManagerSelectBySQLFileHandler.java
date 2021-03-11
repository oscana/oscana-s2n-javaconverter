package jp.co.tis.s2n.javaConverter.convert.program;

import java.util.List;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.convert.program.ParamUtil.ParamTokenGroup;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような変換を行う。<br>
 *
 *  jdbcManager.selectBySqlFile(Class<T> baseClass, String path,Object parameter).getResultList →UniversalDao.findAllBySqlFile(final Class<T> entityClass, final String sqlId, final Object params)<br>
 *  jdbcManager.selectBySqlFile(Class<T> baseClass, String path).getResultList→UniversalDao.findAllBySqlFile(final Class<T> entityClass, final String sqlId)<br>
 *  jdbcManager.selectBySqlFile(Class<T> baseClass, String path,Object parameter).getSingleResult→UniversalDao.findBySqlFile(final Class<T> entityClass, final String sqlId, final Object params)<br>
 *  jdbcManager.selectBySqlFile(Class<T> baseClass, String path).getSingleResult→UniversalDao.findBySqlFile(final Class<T> entityClass, final String sqlId, EMPTY_PARAM)<br>
 *  <br>
 *
 * ※insertの部分はcrudStringとして付与した文字列に置換される。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class JDBCManagerSelectBySQLFileHandler extends AbstractProgramConvertHandler {

    private Token mainToken;
    private boolean isBeanMapResult = false;

    private ParamUtil inParams = new ParamUtil();

    public JDBCManagerSelectBySQLFileHandler(Node classNode, ClassNameResolver tcnr) {
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
            if ((token.getText().equals(KEY_JDBC_MANAGER + ".selectBySqlFile"))) {
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
                if (groups.size() > 2) {
                    addCommand(new InsertAfterCommand(ti.getLastToken(), ")"));
                } else {
                    addCommand(new InsertAfterCommand(ti.getLastToken(), "),new Object[0]"));
                }

            } else {
                if(token.getText().equals("BeanMap.class")) {
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
                // ２個の引数の時は第三引数をEMPTY_PARAMで補完する
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
                if(isBeanMapResult) {
                    addCommand(new ChangeToTxtCommand(mainToken,
                            "DataUtil.convertToBeanMapList(" + CN_UNIVERSALDAO + ".findAllBySqlFile"));
                    addCommand(new InsertAfterCommand(token, ")"));
                    ClassPathConvertUtil.getInstance().addImprt("oscana.s2n.common.DataUtil");
                }else {
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
