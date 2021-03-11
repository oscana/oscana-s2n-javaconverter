package jp.co.tis.form;
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
@RequestScoped
public class ConvertTestForm  implements Serializable{

    @SystemChar(charsetDef = "半角英字・半角数字・半角記号", message = "{errors.alphaNumericHyphenUnderbarType}", target = "doNext1, doDecide")
    public Object field0;

    @SystemChar(charsetDef = "半角英数", message = "{errors.alphaNumericType}", target = "doNext1, doDecide")
    public Object field1;

    @SystemChar(charsetDef = "半角英字", message = "{errors.alphaType}", target = "doNext1, doDecide")
    public Object field2;

    @SystemChar(charsetDef = "システム許容文字", message = "{errors.asciiHalfKanaWin31jType}")
    public Object field3;

    @SystemChar(charsetDef = "半角英字（大文字）・半角スペース", message = "{errors.capitalAlphaSpaceType}")
    public Object field4;

    @SystemChar(charsetDef = "全角アルファベット", message = "{errors.fullAlphaType}")
    public Object field5;

    @SystemChar(charsetDef = "全角カタカナ", message = "{errors.fullKanaType}")
    public Object field6;

    @SystemChar(charsetDef = "全角文字@FullType", message = "{errors.fullType}", target = "doNext1, doDecide")
    public Object field7;

    @SystemChar(charsetDef = "全角文字", message = "{errors.fullWin31jType}")
    public Object field8;

    @SystemChar(charsetDef = "半角文字（半角カナ以外）", message = "{errors.halfExclusiveHalfKanaType}")
    public Object field9;

    @SystemChar(charsetDef = "半角カナ", message = "{errors.halfKanaType}")
    public Object field10;

    @SystemChar(charsetDef = "半角", message = "{errors.halfType}", target = "doNext1, doDecide")
    public Object field11;

    @SystemChar(charsetDef = "半角数字・半角ハイフン", message = "{errors.positiveNumericHyphenType}")
    public Object field12;

    @SystemChar(charsetDef = "半角数字", message = "{errors.positiveNumericType}", target = "doNext2, doDecide")
    public Object field13;

    @Fixlength(length = 10, target = "doNext2, doDecide")
    public Object field14;

    @CodeValue(codeId = "0011", pattern = "PATTERN01", message = "{errors.codeexist}")
    public Object field15;

    @AlphaAndNumericType(message = "{errors.alphaandnumerictype}")
    public Object field16;

    @DateFormat(format = "yyyyMMdd", message = "{errors.dateformat}", target = "doNext1, doDecide")
    public Object field17;

    @Required(target = "doNext1, doDecide")
    public Object field18;

    @Length(target = "doNext1, doDecide")
    public Object field19;

    @LengthRange(target = "doNext1, doDecide")
    public Object filed20;

    @ByteLength(target = "doNext1, doDecide")
    public Object field21;

    @SystemChar(charsetDef = "全角半角", message = "{errors.fullHalfType}", target = "doNext1, doDecide")
    public Object field22;

    @Pattern(regexp = "\\d\\d-\\d\\d\\d\\d-\\d\\d\\d\\d", target = "doNext1, doDecide")
    public Object field23;

    // TODO ツールで変換できません
    // @ByteLocale
    public Object field24;

    @SystemChar(charsetDef = "全角半角", message = "{errors.fullHalfType}", target = "doNext1, doDecide")
    public Object field25;


    /**
     * 変数を取得する
     *
     * @return field0 変数
     */
    public Object getField0() {
        return field0;
    }

    /**
     * 変数を設定する
     *
     * @param field0 変数
     */
    public void setField0(Object field0) {
        this.field0 = field0;
    }

    /**
     * 変数を取得する
     *
     * @return field1 変数
     */
    public Object getField1() {
        return field1;
    }

    /**
     * 変数を設定する
     *
     * @param field1 変数
     */
    public void setField1(Object field1) {
        this.field1 = field1;
    }

    /**
     * 変数を取得する
     *
     * @return field2 変数
     */
    public Object getField2() {
        return field2;
    }

    /**
     * 変数を設定する
     *
     * @param field2 変数
     */
    public void setField2(Object field2) {
        this.field2 = field2;
    }

    /**
     * 変数を取得する
     *
     * @return field3 変数
     */
    public Object getField3() {
        return field3;
    }

    /**
     * 変数を設定する
     *
     * @param field3 変数
     */
    public void setField3(Object field3) {
        this.field3 = field3;
    }

    /**
     * 変数を取得する
     *
     * @return field4 変数
     */
    public Object getField4() {
        return field4;
    }

    /**
     * 変数を設定する
     *
     * @param field4 変数
     */
    public void setField4(Object field4) {
        this.field4 = field4;
    }

    /**
     * 変数を取得する
     *
     * @return field5 変数
     */
    public Object getField5() {
        return field5;
    }

    /**
     * 変数を設定する
     *
     * @param field5 変数
     */
    public void setField5(Object field5) {
        this.field5 = field5;
    }

    /**
     * 変数を取得する
     *
     * @return field6 変数
     */
    public Object getField6() {
        return field6;
    }

    /**
     * 変数を設定する
     *
     * @param field6 変数
     */
    public void setField6(Object field6) {
        this.field6 = field6;
    }

    /**
     * 変数を取得する
     *
     * @return field7 変数
     */
    public Object getField7() {
        return field7;
    }

    /**
     * 変数を設定する
     *
     * @param field7 変数
     */
    public void setField7(Object field7) {
        this.field7 = field7;
    }

    /**
     * 変数を取得する
     *
     * @return field8 変数
     */
    public Object getField8() {
        return field8;
    }

    /**
     * 変数を設定する
     *
     * @param field8 変数
     */
    public void setField8(Object field8) {
        this.field8 = field8;
    }

    /**
     * 変数を取得する
     *
     * @return field9 変数
     */
    public Object getField9() {
        return field9;
    }

    /**
     * 変数を設定する
     *
     * @param field9 変数
     */
    public void setField9(Object field9) {
        this.field9 = field9;
    }

    /**
     * 変数を取得する
     *
     * @return field10 変数
     */
    public Object getField10() {
        return field10;
    }

    /**
     * 変数を設定する
     *
     * @param field10 変数
     */
    public void setField10(Object field10) {
        this.field10 = field10;
    }

    /**
     * 変数を取得する
     *
     * @return field11 変数
     */
    public Object getField11() {
        return field11;
    }

    /**
     * 変数を設定する
     *
     * @param field11 変数
     */
    public void setField11(Object field11) {
        this.field11 = field11;
    }

    /**
     * 変数を取得する
     *
     * @return field12 変数
     */
    public Object getField12() {
        return field12;
    }

    /**
     * 変数を設定する
     *
     * @param field12 変数
     */
    public void setField12(Object field12) {
        this.field12 = field12;
    }

    /**
     * 変数を取得する
     *
     * @return field13 変数
     */
    public Object getField13() {
        return field13;
    }

    /**
     * 変数を設定する
     *
     * @param field13 変数
     */
    public void setField13(Object field13) {
        this.field13 = field13;
    }

    /**
     * 変数を取得する
     *
     * @return field14 変数
     */
    public Object getField14() {
        return field14;
    }

    /**
     * 変数を設定する
     *
     * @param field14 変数
     */
    public void setField14(Object field14) {
        this.field14 = field14;
    }

    /**
     * 変数を取得する
     *
     * @return field15 変数
     */
    public Object getField15() {
        return field15;
    }

    /**
     * 変数を設定する
     *
     * @param field15 変数
     */
    public void setField15(Object field15) {
        this.field15 = field15;
    }

    /**
     * 変数を取得する
     *
     * @return field16 変数
     */
    public Object getField16() {
        return field16;
    }

    /**
     * 変数を設定する
     *
     * @param field16 変数
     */
    public void setField16(Object field16) {
        this.field16 = field16;
    }

    /**
     * 変数を取得する
     *
     * @return field17 変数
     */
    public Object getField17() {
        return field17;
    }

    /**
     * 変数を設定する
     *
     * @param field17 変数
     */
    public void setField17(Object field17) {
        this.field17 = field17;
    }

    /**
     * 変数を取得する
     *
     * @return field18 変数
     */
    public Object getField18() {
        return field18;
    }

    /**
     * 変数を設定する
     *
     * @param field18 変数
     */
    public void setField18(Object field18) {
        this.field18 = field18;
    }

    /**
     * 変数を取得する
     *
     * @return field19 変数
     */
    public Object getField19() {
        return field19;
    }

    /**
     * 変数を設定する
     *
     * @param field19 変数
     */
    public void setField19(Object field19) {
        this.field19 = field19;
    }

    /**
     * 変数を取得する
     *
     * @return filed20 変数
     */
    public Object getFiled20() {
        return filed20;
    }

    /**
     * 変数を設定する
     *
     * @param filed20 変数
     */
    public void setFiled20(Object filed20) {
        this.filed20 = filed20;
    }

    /**
     * 変数を取得する
     *
     * @return field21 変数
     */
    public Object getField21() {
        return field21;
    }

    /**
     * 変数を設定する
     *
     * @param field21 変数
     */
    public void setField21(Object field21) {
        this.field21 = field21;
    }

    /**
     * 変数を取得する
     *
     * @return field22 変数
     */
    public Object getField22() {
        return field22;
    }

    /**
     * 変数を設定する
     *
     * @param field22 変数
     */
    public void setField22(Object field22) {
        this.field22 = field22;
    }

    /**
     * 変数を取得する
     *
     * @return field23 変数
     */
    public Object getField23() {
        return field23;
    }

    /**
     * 変数を設定する
     *
     * @param field23 変数
     */
    public void setField23(Object field23) {
        this.field23 = field23;
    }

    /**
     * // TODO ツールで変換できませんを取得する
     *
     * @return field24 // TODO ツールで変換できません
     */
    public Object getField24() {
        return field24;
    }

    /**
     * // TODO ツールで変換できませんを設定する
     *
     * @param field24 // TODO ツールで変換できません
     */
    public void setField24(Object field24) {
        this.field24 = field24;
    }

    /**
     * 変数を取得する
     *
     * @return field25 変数
     */
    public Object getField25() {
        return field25;
    }

    /**
     * 変数を設定する
     *
     * @param field25 変数
     */
    public void setField25(Object field25) {
        this.field25 = field25;
    }

}

