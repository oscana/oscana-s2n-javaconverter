package tutorial.action;

import javax.annotation.Resource;


import tutorial.form.AddForm;
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


@RequestScoped
public class TestSAStrutsAction  implements Serializable{

    public Integer result;

    @OscanaActionForm
    @Inject
    protected AddForm addForm;

    @Execute(validator=false)
    public HttpResponse index(HttpRequest nabRequest, ExecutionContext nabContext) {
        return OscanaHttpResourceConverUtil.createHttpResponse( "index.jsp", this, nabRequest, nabContext, "TestSAStruts");
    }

    @Execute(validator=true)
    @OnError(type = ApplicationException.class, path = "/WEB-INF/view/testSAStruts/index.jsp")
    public HttpResponse submit(HttpRequest nabRequest, ExecutionContext nabContext) {
        result = Integer.valueOf(addForm.arg1) + Integer.valueOf(addForm.arg2);
        return OscanaHttpResourceConverUtil.createHttpResponse( "index.jsp", this, nabRequest, nabContext, "TestSAStruts");
    }
}
