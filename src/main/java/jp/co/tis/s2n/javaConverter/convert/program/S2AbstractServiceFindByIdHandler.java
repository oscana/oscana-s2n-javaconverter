package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような検索系メソッドを置換する。<br>
 *  findById(XXXXX) → UniversalDao.findById(new GenericsUtil<ENTITY>().getType() ,XXXXX);
 *
 * @author Fumihiko Yamamoto
 *
 */
public class S2AbstractServiceFindByIdHandler extends AbstractProgramConvertHandler {

    private Node classNode;
    private String genericsType;

    public S2AbstractServiceFindByIdHandler(Node classNode, ClassNameResolver tcnr) {
        super(tcnr);
        this.classNode = classNode;
        this.genericsType = NodeUtil.getClassGenericsType(this.classNode);

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
            if (token.getText().equals("findById") || token.getText().startsWith("findById")) {
                step++;
                addCommand(new ChangeToTxtCommand(token, CN_UNIVERSALDAO + ".findById"));
                ClassPathConvertUtil.getInstance().addImprt("nablarch.common.dao.UniversalDao");
                bracketLevel.reset();
            }
            break;
        case 1:
            if (token.getText().equals("(")) {
                step++;
                if (this.genericsType == null || this.genericsType.equals("null")) {
                    Node methodNode = NodeUtil.getMethod(lineNode);
                    if (methodNode != null) {
                        this.genericsType = NodeUtil.getMethodGenericsType(methodNode);
                    }
                }
                addCommand(new InsertAfterCommand(token, "new GenericsUtil<" + this.genericsType + ">().getType() ,"));
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
