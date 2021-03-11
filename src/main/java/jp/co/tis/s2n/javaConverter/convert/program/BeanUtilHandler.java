package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような変換を行う。<br>
 * 変換前：BeanUtil.copyProperties(XXXX,YYYY); <br>
 * 変換後： BeanUtil.copy(XXXX,YYYY);
 *
 * @author Rai Shuu
 *
 */
public class BeanUtilHandler extends AbstractProgramConvertHandler implements AutoInstall {

    public BeanUtilHandler(ClassNameResolver tcnr) {
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
        return line.contains("BeanUtil.");
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
            if ((token.getText().equals("BeanUtil.copyProperties"))) {
                step++;
                // BeanUtil.copyPropertiesをBeanUtil.copyに変換
                addCommand(new ChangeToTxtCommand(token, "BeanUtil.copy"));
            }
            // インポート文を追加
            ClassPathConvertUtil.getInstance().addImprt("nablarch.core.beans.BeanUtil");
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
