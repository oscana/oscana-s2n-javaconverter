package jp.co.tis.action;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.*;
import javax.annotation.Resource;
import org.seasar.struts.annotation.Execute;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@Component
public class TestConvertComponentAnnotationAction {

    @Resource
    @ActionForm
    public TestForm testForm;

    /** エンティティのリスト */
    public List list;

    /**
     * 検索一覧画面を表示する。
     *
     * @return 遷移先画面
     */
    public String index() {
        return "searchList.jsp";
    }


}
