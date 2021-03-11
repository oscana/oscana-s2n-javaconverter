package from;
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
public class TestConvertFormApi02Form  implements Serializable{

    @ParseByte
    public Object field0;

    @CreditCardNumber
    @ParseDate(datePattern = "true")
    public Object field1;

    @ParseDate
    public Object field2;

    @DecimalRange
    public Object field3;

    @ParseDouble
    public Object field4;

    @Email
    public Object field5;

    @DecimalRange
    public Object field6;

    @ParseFloat
    public Object field7;

    @ParseInt
    public Object field8;

    @Range
    @ParseLong
    public Object field9;

    @Range
    @ByteLength
    public Object field10;

    @ByteLength
    public Object field11;

    @Length
    public Object field12;

    @ByteLength
    public Object field13;

    @Length
    @ParseShort
    public Object field14;

    public Object field14;

    @URL
    public Object field15;


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

}

