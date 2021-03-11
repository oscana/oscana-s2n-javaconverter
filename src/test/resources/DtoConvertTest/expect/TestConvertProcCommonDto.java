package jp.co.tis.dto;


import java.io.Serializable;
import java.util.LinkedHashMap;
import jp.co.tis.oscana.seasar.dto.OscanaAbstractDto;
import oscana.s2n.validation.Required;
import jp.co.tis.oscana.seasar.struts.annotation.Fixlength;
import oscana.s2n.validation.SystemChar;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;



/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public class TestConvertProcCommonDto extends OscanaAbstractDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public String test1;

    public String test2;

    public String test3;

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    public String getTest3() {
        return test3;
    }

    public void setTest3(String test3) {
        this.test3 = test3;
    }
    /**
     * 変数を取得する
     *
     * @return test1 変数
     */
    public String getTest1() {
        return test1;
    }

}
