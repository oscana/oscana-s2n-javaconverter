package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 以下のような変換を行う。<br>
 * 変換前：Beans.copy(XXXX,YYYY); <br>
 * 変換後： new Copy(XXXX,YYYY);<br>
 * <br>
 * 変換前：Beans.createAndCopy(XXXX.class,YYYY); <br>
 * 変換後： new CreateAndCopy<XXXX>(XXXX.class,YYYY);
 *
 * @author Rai Shuu
 *
 */
public class BeansCopyHandler extends AbstractProgramConvertHandler implements AutoInstall {

    boolean isCreate;//createAndCopyであるのフラグ
    Token insertPosToken;//挿入情報を保存用Token
    boolean addCommentFlag;//警告のコメントを追加するフラグ

    public BeansCopyHandler(ClassNameResolver tcnr) {
        super(tcnr);

    }

    /**
     * 初期化処理。
     */
    @Override
    public void init() {
        super.init();
        // オブジェクト変数初期化
        isCreate = false;
        insertPosToken = null;
        addCommentFlag = false;
    }

    /**
     * ハンドラを実施するかどうか判定用メソッド。
     * @param line String型の行ソース
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return チェック結果 true:実行する、false:実行しない
     */
    @Override
    public boolean test(String line, Node lineNode) {
        return line.contains("Beans.copy") || line.contains("Beans.createAndCopy");
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

        if (!addCommentFlag) {

            // JavaBeansやMapのコピーについて、includes＋prefixとincludes＋excludesが組み合わせる場合、新旧動作が違うため、変換後警告を出力する
            if (lineNode.getString().contains("Beans.copy") || lineNode.getString().contains("Beans.createAndCopy")) {
                if (lineNode.getString().contains("includes")
                        && (lineNode.getString().contains("prefix") || lineNode.getString().contains("excludes("))) {
                    lineNode.setName(Node.NO_SUPPORT_COMBINATION + "\r\n" + lineNode.getName());
                    addCommentFlag = true;
                }
            }
        }

        switch (step) {
        case 0:
            if ((token.getText().equals("Beans.copy"))) {
                step++;
                // Beans.copyをnew Copyに変換
                addCommand(new ChangeToTxtCommand(token, "new Copy"));
                // import文を挿入
                ClassPathConvertUtil.getInstance().addImprt("oscana.s2n.seasar.framework.beans.util.Copy");
            }
            if ((token.getText().equals("Beans.createAndCopy"))) {
                step++;
                // Beans.createAndCopyをnew CreateAndCopyに変換
                addCommand(new ChangeToTxtCommand(token, "new CreateAndCopy"));
                // import文を挿入
                ClassPathConvertUtil.getInstance().addImprt("oscana.s2n.seasar.framework.beans.util.CreateAndCopy");
                isCreate = true;
            }
            break;
        case 1:
            if (token.getText().equals("(")) {
                step++;
                if(!isCreate) {

                    return true;
                }else {
                    // createAndCopyの場合、挿入用情報を保存
                    insertPosToken = token;
                }

            }
            break;
        case 2:
            if ((token.getText().contains(".class"))) {
                // createAndCopyである且つパラメータは値の場合、クラス含む挿入用情報を挿入
                addCommand(new InsertBeforeCommand(insertPosToken, "<" + token.getText().replace(".class", ">")));
            }else {
                // createAndCopyの場合、<>を挿入
                addCommand(new InsertBeforeCommand(insertPosToken, "<>"));
            }

            return true;
        }
        return false;

    }

}
