package jp.co.tis.form.common;

import jp.co.tis.form.AbstractEditForm;
import jp.co.tis.oscana.web.annotation.LengthRange;
import oscana.s2n.validation.SystemChar;

import oscana.s2n.struts.upload.FormFile;
import oscana.s2n.validation.Required;
import jp.co.annotation.CodeTblExist;
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
import oscana.s2n.validation.FieldName;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@SessionScoped
public class TestConvertSAStrutsValidateAnnotationForm extends AbstractEditForm  implements Serializable{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @Required
    @SystemChar(charsetDef = "半角数字", message = "{errors.positiveNumericType}")
    @FieldName(value = "パラメータ1")
    public String arg1;

    @Required
    @SystemChar(charsetDef = "半角数字", message = "{errors.positiveNumericType}")
    @FieldName(value = "パラメータ2")
    public String arg2;

    public String aaa;

    public String bbb;

    public String selectValue;

    @CodeTblExist
    public String code;

    public String label;

    @Required
    @FieldName(value = "複数選択肢")
    public String[] resourcesSelect;

    @Required
    @LengthRange(minlength = 3, maxlength = 10)
    @FieldName(value = "テキストアリア")
    public String textarea;

    @Required
    @FieldName(value = "チェックボックス")
    public String check1;

    public String check2;

    public String radio1;
    public String radio2;

    @Required
    @FieldName(value = "ラジオボタン")
    public String radio;

    @Required
    @FieldName(value = "パスワード")
    public String pwd;

    @Required
    @FieldName(value = "ファイル")
    public FormFile uploadFile;


    /**
     * 変数を取得する
     *
     * @return arg1 変数
     */
    public String getArg1() {
        return arg1;
    }

    /**
     * 変数を設定する
     *
     * @param arg1 変数
     */
    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    /**
     * 変数を取得する
     *
     * @return arg2 変数
     */
    public String getArg2() {
        return arg2;
    }

    /**
     * 変数を設定する
     *
     * @param arg2 変数
     */
    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    /**
     * 変数を取得する
     *
     * @return aaa 変数
     */
    public String getAaa() {
        return aaa;
    }

    /**
     * 変数を設定する
     *
     * @param aaa 変数
     */
    public void setAaa(String aaa) {
        this.aaa = aaa;
    }

    /**
     * 変数を取得する
     *
     * @return bbb 変数
     */
    public String getBbb() {
        return bbb;
    }

    /**
     * 変数を設定する
     *
     * @param bbb 変数
     */
    public void setBbb(String bbb) {
        this.bbb = bbb;
    }

    /**
     * 変数を取得する
     *
     * @return selectValue 変数
     */
    public String getSelectValue() {
        return selectValue;
    }

    /**
     * 変数を設定する
     *
     * @param selectValue 変数
     */
    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }

    /**
     * 変数を取得する
     *
     * @return code 変数
     */
    public String getCode() {
        return code;
    }

    /**
     * 変数を設定する
     *
     * @param code 変数
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 変数を取得する
     *
     * @return label 変数
     */
    public String getLabel() {
        return label;
    }

    /**
     * 変数を設定する
     *
     * @param label 変数
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * 変数を取得する
     *
     * @return resourcesSelect 変数
     */
    public String[] getResourcesSelect() {
        return resourcesSelect;
    }

    /**
     * 変数を設定する
     *
     * @param resourcesSelect 変数
     */
    public void setResourcesSelect(String[] resourcesSelect) {
        this.resourcesSelect = resourcesSelect;
    }

    /**
     * 変数を取得する
     *
     * @return textarea 変数
     */
    public String getTextarea() {
        return textarea;
    }

    /**
     * 変数を設定する
     *
     * @param textarea 変数
     */
    public void setTextarea(String textarea) {
        this.textarea = textarea;
    }

    /**
     * 変数を取得する
     *
     * @return check1 変数
     */
    public String getCheck1() {
        return check1;
    }

    /**
     * 変数を設定する
     *
     * @param check1 変数
     */
    public void setCheck1(String check1) {
        this.check1 = check1;
    }

    /**
     * 変数を取得する
     *
     * @return check2 変数
     */
    public String getCheck2() {
        return check2;
    }

    /**
     * 変数を設定する
     *
     * @param check2 変数
     */
    public void setCheck2(String check2) {
        this.check2 = check2;
    }

    /**
     * 変数を取得する
     *
     * @return radio1 変数
     */
    public String getRadio1() {
        return radio1;
    }

    /**
     * 変数を設定する
     *
     * @param radio1 変数
     */
    public void setRadio1(String radio1) {
        this.radio1 = radio1;
    }

    /**
     * 変数を取得する
     *
     * @return radio2 変数
     */
    public String getRadio2() {
        return radio2;
    }

    /**
     * 変数を設定する
     *
     * @param radio2 変数
     */
    public void setRadio2(String radio2) {
        this.radio2 = radio2;
    }

    /**
     * 変数を取得する
     *
     * @return radio 変数
     */
    public String getRadio() {
        return radio;
    }

    /**
     * 変数を設定する
     *
     * @param radio 変数
     */
    public void setRadio(String radio) {
        this.radio = radio;
    }

    /**
     * 変数を取得する
     *
     * @return pwd 変数
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 変数を設定する
     *
     * @param pwd 変数
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 変数を取得する
     *
     * @return uploadFile 変数
     */
    public FormFile getUploadFile() {
        return uploadFile;
    }

    /**
     * 変数を設定する
     *
     * @param uploadFile 変数
     */
    public void setUploadFile(FormFile uploadFile) {
        this.uploadFile = uploadFile;
    }

}
