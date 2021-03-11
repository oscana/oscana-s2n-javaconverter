package jp.co.tis.action;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.*;
import javax.annotation.Resource;
import org.seasar.struts.annotation.Execute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertResourceAnnotationAction {

    @Resource
    @ActionForm
    public TestForm testForm;

    @Resource
    public HttpServletRequest request;

    @Resource
    public HttpServlet servlet;

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
