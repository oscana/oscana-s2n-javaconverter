package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような変換を行う<BR>
 * 変換前：SingletonS2Container.getComponent(Class<T>); <BR>
 * 変換後：(T) OscanaSingletonContainerFactory.getContainer().getComponent(Class<T>);
 * <BR>
 * 変換前：SingletonS2Container.getComponent(String); <BR>
 * 変換後：OscanaSingletonContainerFactory.getContainer().getComponent(String);
 * @author Rai Shuu
 *
 */
public class SingletonS2ContainerHandler extends AbstractProgramConvertHandler implements AutoInstall {

    Token insertPosToken;//挿入情報を保存用Token

    public SingletonS2ContainerHandler(ClassNameResolver tcnr) {
        super(tcnr);

    }

    /**
     * 初期化処理
     */
    @Override
    public void init() {
        super.init();
        // オブジェクト変数初期化
        insertPosToken = null;
    }

    /**
     * ハンドラを実施するかどうかの判定条件。
     * @param line String型の行ソース
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return チェック結果 true:実行する、false:実行しない
     */
    @Override
    public boolean test(String line, Node lineNode) {
        return line.contains("SingletonS2Container.getComponent");
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
            if ((token.getText().equals("SingletonS2Container.getComponent"))) {
                step++;
                //SingletonS2Container.getComponentをOscanaSingletonContainerFactory.getContainer().getComponent
                addCommand(
                        new ChangeToTxtCommand(token, "OscanaSingletonContainerFactory.getContainer().getComponent"));
                insertPosToken = token;
                // インポート文を追加
                ClassPathConvertUtil.getInstance()
                        .addImprt("oscana.s2n.seasar.framework.container.factory.OscanaSingletonContainerFactory");
            }

            break;
        case 1:
            if (token.getText().equals("(")) {
                step++;
            }
            break;
        case 2:
            if (token.getText().contains(".class")) {
                addCommand(new InsertBeforeCommand(insertPosToken, "(" + token.getText().replace(".class", ")")));
            }
            step++;
            return true;
        }
        return false;

    }

}
