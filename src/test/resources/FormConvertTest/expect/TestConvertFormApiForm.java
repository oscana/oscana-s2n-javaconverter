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
public class FormForConvertTest  implements Serializable{

    @SystemChar(charsetDef = "半角英字・半角数字・半角記号", message = "{errors.alphaNumericHyphenUnderbarType}", target = "doNext1, doDecide")
    public Object field0;

    @SystemChar(charsetDef = "半角英数", message = "{errors.alphaNumericType}", target = "doNext1")
    public Object field1;

    //@Reset
    public String field2;

    @Url()
    public String field2;


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
    public String getField2() {
        return field2;
    }

    /**
     * 変数を設定する
     *
     * @param field2 変数
     */
    public void setField2(String field2) {
        this.field2 = field2;
    }

    /**
     * 変数を取得する
     *
     * @return field2 変数
     */
    public String getField2() {
        return field2;
    }

    /**
     * 変数を設定する
     *
     * @param field2 変数
     */
    public void setField2(String field2) {
        this.field2 = field2;
    }

}

