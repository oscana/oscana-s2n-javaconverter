package jp.co.tis.dao;
import java.io.Serializable;
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
@Prototype
public class TestConvertComponentAnnotationDao {

    private static final long serialVersionUID = 1L;

    public String test1;
}
