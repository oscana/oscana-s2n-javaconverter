package jp.co.tis.form.sample;

import jp.co.tis.util.ValidateUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@Component(instance = InstanceType.SESSION)
public class SampleSearchListForm extends SampleSearchListBaseForm {

    /** シリアル・バージョンID */
    private static final long serialVersionUID = 1L;

    /** コード */
    public String kcode;

    /**
     * resetメソッド
     *
     */
    public void resetCondition() {

        search_kcode_EQ = null;
    }

    /**
     * validateFormメソッド
     *
     * @return errors
     */
    public ActionMessages validateForm() {

        ActionMessages errors = new ActionMessages();

        if (ValidateUtil.isEmpty(search_kcode_EQ) ) {
            errors.add("search_kcode_EQ", new ActionMessage("test"));
        }
        return errors;
    }

    /**
     * 画面IDを返します。
     *
     * @return 画面ID
     */
    public String getDispId() {

        if (isEnvTypeHonban() && isScreenTypeSearch()) {
            return "test";
        }

        return "画面ID取得エラー";
    }

    /**
     * タイトル名を返します。
     *
     * @return タイトル名
     */
    public String getDispName() {

        if (isEnvTypeHonban() && isScreenTypeSearch()) {
            return "テーブル検索";
        }
        if (isEnvTypeHonban() && isScreenTypeSearchList()) {
            return "テーブル検索結果";
        }
        if (isEnvTypeHaneiMae() && isScreenTypeSearch()) {
            return "本番テーブル検索";
        }
        if (isEnvTypeHaneiMae() && isScreenTypeSearchList()) {
            return "本番テーブル検索結果";
        }

        return "画面タイトル取得エラー";
    }
}
