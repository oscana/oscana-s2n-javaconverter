package jp.co.tis.dto;


import java.io.Serializable;
import java.util.LinkedHashMap;
import oscana.s2n.xenlon.seasar.dto.NablarchAbstractDto;
import oscana.s2n.validation.Required;
import oscana.s2n.validation.Fixlength;
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
public class TestAddAccessorStrutsDto extends NablarchAbstractDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public String koshinnKubunn;

    public String tokenIssId;

    public String cardBanngou;

}
