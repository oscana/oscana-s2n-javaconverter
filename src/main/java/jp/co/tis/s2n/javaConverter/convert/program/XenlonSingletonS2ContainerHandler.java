package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような変換を行う。<br>
 * 変換前：XenlonSingletonS2Container.getComponent(XXXXXX); <br>
 * 変換後： Containers.get().getComponent(XXXXXX);
 *
 * @author Fumihiko Yamamoto
 *
 */
public class XenlonSingletonS2ContainerHandler extends AbstractProgramConvertHandler implements AutoInstall {

    public XenlonSingletonS2ContainerHandler(ClassNameResolver tcnr) {
        super(tcnr);

    }

    /**
     * ハンドラを実施するかどうかの判定条件。
     * @param line String型の行ソース
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return チェック結果 true:実行する、false:実行しない
     */
    @Override
    public boolean test(String line, Node lineNode) {
        return line.contains("XenlonSingletonS2Container");
    }

    /**
     * 変換主処理。
     * @param token 処理対象トークン
     * @param bracketLevel レベル
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return 処理結果
     */
    @Override
    protected boolean handle(Token token, BracketLevel bracketLevel, Node lineNode) {

        switch (step) {
        case 0:
            if ((token.getText().equals("XenlonSingletonS2Container.getComponent"))) {
                step++;
                addCommand(new ChangeToTxtCommand(token, "Containers.get().getComponent"));
            }
            break;
        case 1:
            if (token.getText().equals("(")) {
                step++;
                return true;
            }
            break;
        }
        return false;

    }

}
