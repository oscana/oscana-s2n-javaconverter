package jp.co.tis.form.common;

import jp.co.tis.form.AbstractEditForm;
import jp.co.tis.oscana.web.annotation.LengthRange;
import jp.co.tis.oscana.web.annotation.PositiveNumericType;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Required;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@Singleton
public class TestAddAccessorForm extends AbstractEditForm implements Xxx {

   /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @Required(arg0 =  @Arg(key = "パラメータ1"))
    @PositiveNumericType(arg0 =  @Arg(key = "パラメータ1"))
    public String arg1;

    @Required(arg0 =  @Arg(key = "パラメータ2"))
    @PositiveNumericType(arg0 =  @Arg(key = "パラメータ2"))
    public String arg2;

    public String aaa;

    public String bbb;

    public String selectValue;

    public String code;

    public String label;

    @Required(arg0 =  @Arg(key = "複数選択肢"))
    public String[] resourcesSelect;

    @Required(arg0 =  @Arg(key = "テキストアリア"))
    @LengthRange(minlength=3,maxlength=10,arg0 =  @Arg(key = "テキストアリア"))
    public String textarea;

    @Required(arg0=@Arg(key="チェックボックス"))
    public String check1;

    public String check2;

    public String radio1;
    public String radio2;

    @Required(arg0 =  @Arg(key = "ラジオボタン"))
    public String radio;

    @Required(arg0 =  @Arg(key = "パスワード"))
    public String pwd;

    @Required(arg0 =  @Arg(key = "ファイル"))
    public FormFile uploadFile;

    @Binding()
    public final String param1;

}
