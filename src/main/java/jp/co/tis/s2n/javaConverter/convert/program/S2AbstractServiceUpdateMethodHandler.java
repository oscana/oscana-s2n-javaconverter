package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような更新系メソッドを置換する。<br>
 * update(XXXX) → UniversalDao.update(XXXX)<br>
 * insert(XXXX) → UniversalDao.insert(XXXX)<br>
 * delete(XXXX) → UniversalDao.delete(XXXX)<br>
 *
 * @author Fumihiko Yamamoto
 *
 */
public class S2AbstractServiceUpdateMethodHandler extends AbstractProgramConvertHandler {

    String crudString;

    public S2AbstractServiceUpdateMethodHandler(String crudString, ClassNameResolver tcnr) {
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
            if ((token.getText().equals(crudString))) {
                step++;
                addCommand(new ChangeToTxtCommand(token, CN_UNIVERSALDAO + "." + crudString));
                ClassPathConvertUtil.getInstance().addImprt("nablarch.common.dao.UniversalDao");
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
                return true;
            }
            break;
        }

        return false;
    }

}
