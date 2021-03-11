package jp.co.tis.action;

import java.io.Serializable;

import java.io.Serializable;
import org.apache.struts.action.Action;
/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertBaseClassNameAction extends Action implements Serializable {

    public TestForm testForm;

    /** エンティティのリスト */
    public List list;

    /**
     * 検索一覧画面を表示する。
     *
     * @return 遷移先画面
     */
    public String index() {
        return "test.jsp";
    }

}
