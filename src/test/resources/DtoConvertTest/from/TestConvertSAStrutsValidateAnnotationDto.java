package jp.co.tis.dto;


import java.io.Serializable;
import java.util.LinkedHashMap;
import jp.co.tis.oscana.seasar.dto.OscanaAbstractDto;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import jp.co.tis.oscana.seasar.struts.annotation.Fixlength;
import jp.co.tis.oscana.seasar.struts.annotation.PositiveNumericType;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public class TestConvertSAStrutsValidateAnnotationDto extends OscanaAbstractDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Required(msg = @Msg(key = "test1"))
    public String test1;

    @Fixlength(length = 1, msg = @Msg(key = "test2"))
    public String test2;

    @PositiveNumericType(msg = @Msg(key = "test3"))
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
}
