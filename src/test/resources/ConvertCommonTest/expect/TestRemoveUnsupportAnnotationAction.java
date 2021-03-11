package jp.co.tis.action;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oscana.s2n.struts.action.ActionMapping;
import oscana.s2n.struts.OscanaHttpResourceConverUtil;
import oscana.s2n.common.web.interceptor.Execute;
import nablarch.common.dao.DeferredEntityList;
import nablarch.common.dao.UniversalDao;
import nablarch.common.databind.ObjectMapper;
import nablarch.common.databind.ObjectMapperFactory;
import nablarch.common.web.download.FileResponse;
import nablarch.common.web.token.OnDoubleSubmission;
import nablarch.core.message.ApplicationException;
import nablarch.core.message.MessageLevel;
import nablarch.core.message.MessageUtil;
import nablarch.fw.ExecutionContext;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.interceptor.OnError;
import oscana.s2n.common.OscanaActionForm;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public class TestRemoveUnsupportAnnotationAction  implements Serializable{

    @Inject
    @OscanaActionForm
    public TestForm testForm;

    /** エンティティのリスト */
    public List list;

    /**
     * 検索一覧画面を表示する。
     *
     * @return 遷移先画面
     */
    @OnDoubleSubmission(path="/error.jsp")
    @Execute(validator=true, validate="validX", reset="set", removeActionForm=false, stopOnValidationError=false)
    @OnError(type = ApplicationException.class, path = "/WEB-INF/view/testRemoveUnsupportAnnotation/A.jsp")
    public HttpResponse index(HttpRequest nabRequest, ExecutionContext nabContext) {
        return OscanaHttpResourceConverUtil.createHttpResponse( "searchList.jsp", this, nabRequest, nabContext, "TestRemoveUnsupportAnnotation");
    }

    /**
     * 入力された検索条件に基づきTBLを検索し、検索結果一覧を表示する。
     *
     * @return 出力系メソッド
     */
    @OnDoubleSubmission(path="/error.jsp")
    @Execute(validator=true)
    public HttpResponse doSearch(HttpRequest nabRequest, ExecutionContext nabContext) {
        return OscanaHttpResourceConverUtil.createHttpResponse( searchList(), this, nabRequest, nabContext, "TestRemoveUnsupportAnnotation");
    }

}
