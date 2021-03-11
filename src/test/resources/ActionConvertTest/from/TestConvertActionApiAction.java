package jp.co.tis.action.test;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertCommondaction {

    @Resource
    @ActionForm
    @ActionForm
    public TestForm testForm;

    /** エンティティのリスト */
    public List list;

    /**
     * 検索一覧画面を表示する。
     *
     * @return 遷移先画面
     */
    @TokenSkip
    public String index() {
        return "searchList.jsp";
    }

}
