package jp.co.tis.dto;


import java.io.Serializable;
import java.util.LinkedHashMap;
import jp.co.tis.xenlon.seasar.dto.XenlonAbstractDto;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import jp.co.tis.xenlon.seasar.struts.annotation.Fixlength;
import jp.co.tis.xenlon.seasar.struts.annotation.PositiveNumericType;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@Entity
public class TestStrutsEntity extends XenlonAbstractDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public String koshinnKubunn;

    public String tokenIssId;

    public String cardBanngou;

}
