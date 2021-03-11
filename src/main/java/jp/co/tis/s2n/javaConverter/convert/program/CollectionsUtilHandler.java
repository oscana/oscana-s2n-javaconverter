package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような変換を行う。<br>
 * 変換前：CollectionsUtil.newArrayList(Collection);<br>
 * 変換後：new ArrayList<>(Collection);
 * <BR>
 * 変換前：CollectionsUtil.newHashMap(); <br>
 * 変換後：new HashMap<>();
 * @author Rai Shuu
 *
 */
public class CollectionsUtilHandler extends AbstractProgramConvertHandler implements AutoInstall {

    public CollectionsUtilHandler(ClassNameResolver tcnr) {
        super(tcnr);

    }

    /**
     * ハンドラを実施するかどうか判定用メソッド。
     * @param line String型の行ソース
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return チェック結果 true:実行する、false:実行しない
     */
    @Override
    public boolean test(String line, Node lineNode) {
        return line.contains("CollectionsUtil.newArrayList") || line.contains("CollectionsUtil.newHashMap");
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
            if ((token.getText().equals("CollectionsUtil.newArrayList"))) {
                step++;
                // CollectionsUtil.newArrayListをnew ArrayList<>に変換
                addCommand(new ChangeToTxtCommand(token, "new ArrayList<>"));
                // import文を挿入
                ClassPathConvertUtil.getInstance().addImprt("java.util.ArrayList");
            }
            if ((token.getText().equals("CollectionsUtil.newHashMap"))) {
                step++;
                // CollectionsUtil.newHashMapをnew HashMap<>に変換
                addCommand(new ChangeToTxtCommand(token, "new HashMap<>"));
                // import文を挿入
                ClassPathConvertUtil.getInstance().addImprt("java.util.HashMap");
            }

            break;
        case 1:
            if ((bracketLevel.isInParenthesisLevel(0)) && (token.getText().equals(")"))) {
                step++;
                return true;
            }
            break;
        }
        return false;

    }

}
