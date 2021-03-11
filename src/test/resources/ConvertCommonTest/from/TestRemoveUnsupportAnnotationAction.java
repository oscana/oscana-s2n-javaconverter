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
@Session
public class TestRemoveUnsupportAnnotationAction {

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
    @TokenSave
    @Execute(validate = "validX",input = "A.jsp",removeActionForm = false,reset="set",validator=true, stopOnValidationError=false, urlPattern = "doSearch/{pageno}")
    public String index() {
        return "searchList.jsp";
    }

    /**
     * 入力された検索条件に基づきTBLを検索し、検索結果一覧を表示する。
     *
     * @return 出力系メソッド
     */
    @AlwaysExecute
    @Execute(stopOnValidationError=false )
    public String doSearch() {
        return searchList();
    }

}
