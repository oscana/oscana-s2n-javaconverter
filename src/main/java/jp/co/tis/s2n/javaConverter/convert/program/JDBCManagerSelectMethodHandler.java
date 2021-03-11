package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような検索系メソッドを置換する。<br>
 * jdbcManager.from(XXXX) → UniversalDao.select(XXXX)
 *
 * @author Fumihiko Yamamoto
 *
 */
public class JDBCManagerSelectMethodHandler extends AbstractProgramConvertHandler {

    String idKey = "";
    boolean handlingId = false;

    private String classObject = "";

    public JDBCManagerSelectMethodHandler(Node classNode, ClassNameResolver tcnr) {
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
            if ((token.getText().equals(KEY_JDBC_MANAGER + ".from"))) {
                step++;
                addCommand(new ChangeToTxtCommand(token, CN_UNIVERSALDAO + "."));
                ClassPathConvertUtil.getInstance().addImprt("nablarch.common.dao.UniversalDao");
                bracketLevel.reset();
            }
            break;
        case 1:
            if (token.getText().equals("(")) {
                step++;
                addCommand(new DeleteCommand(token));
            }
            break;
        case 2:
            if ((bracketLevel.isInParenthesisLevel(0)) && (token.getText().equals(")"))) {
                step++;
                addCommand(new DeleteCommand(token));
            } else {
                classObject = classObject + token.getText();
                addCommand(new DeleteCommand(token));
            }
            break;
        case 3:
            if ((token.getText().equals("."))) {
                step++;
                addCommand(new DeleteCommand(token));
            }
            break;
        case 4:
            if (token.getText().equals("id")) {
                step++;
                addCommand(new DeleteCommand(token));
                handlingId = true;
            } else if (token.getText().equals("getSingleResult")) {
                step++;
                if (StringUtils.isEmpty(idKey)) {
                    addCommand(new ChangeToTxtCommand(token, "findAll"));
                } else {
                    addCommand(new ChangeToTxtCommand(token, "findById"));
                }
            } else if (token.getText().equals("getResultList")) {
                step++;
                if (StringUtils.isEmpty(idKey)) {
                    addCommand(new ChangeToTxtCommand(token, "findAll"));

                } else {
                    addCommand(new ChangeToTxtCommand(token, "findById"));
                }
            } else {
                nonSupportStatement.append(token.getText());
                addCommand(new DeleteCommand(token));
            }
            break;
        case 5:
            if (handlingId) {
                if (token.getText().equals("(")) {
                    step++;
                    addCommand(new DeleteCommand(token));
                } else {
                    addCommand(new DeleteCommand(token));
                }

            } else {
                if (token.getText().equals("(")) {
                    step++;
                }

            }
            break;

        case 6:
            if (handlingId) {

                if ((bracketLevel.isInParenthesisLevel(0)) && (token.getText().equals(")"))) {
                    step = 3;
                    addCommand(new DeleteCommand(token));
                    handlingId = false;
                } else {
                    idKey = idKey + token.getText();
                    addCommand(new DeleteCommand(token));
                }
            } else {
                if ((bracketLevel.isInParenthesisLevel(0)) && (token.getText().equals(")"))) {
                    step++;
                    if (StringUtils.isEmpty(idKey)) {
                        addCommand(new InsertBeforeCommand(token, classObject));
                    } else {
                        addCommand(new InsertBeforeCommand(token, classObject + "," + idKey));
                    }
                    return true;
                }
            }
            break;

        }

        return false;

    }

}
