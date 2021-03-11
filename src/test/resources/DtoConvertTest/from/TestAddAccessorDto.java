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
public class TestAddAccessorDto extends NablarchAbstractDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public String test1;

    public String test2;

    public String test3;
}
