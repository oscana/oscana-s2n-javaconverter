package jp.co.tis.form.sample;

import jp.co.tis.util.ValidateUtil;

import  oscana.s2n.struts.ActionMessage;
import  oscana.s2n.struts.ActionMessages;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;

import java.io.Serializable;
import oscana.s2n.struts.action.ActionForm;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@SessionScoped
public class SampleSearchListForm extends SampleSearchListBaseForm  implements Serializable{

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

    /**
     * コードを取得する
     *
     * @return kcode コード
     */
    public String getKcode() {
        return kcode;
    }

    /**
     * コードを設定する
     *
     * @param kcode コード
     */
    public void setKcode(String kcode) {
        this.kcode = kcode;
    }

}
