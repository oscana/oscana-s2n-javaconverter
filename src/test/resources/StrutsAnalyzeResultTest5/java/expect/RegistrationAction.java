/*
 * $Id: RegistrationAction.java 471754 2006-11-06 14:55:09Z husted $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.struts.webapp.validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oscana.s2n.struts.action.Action;
import oscana.s2n.struts.action.ActionForm;
import oscana.s2n.struts.action.ActionForward;
import oscana.s2n.struts.action.ActionMapping;
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
import org.apache.struts.webapp.validator.RegistrationForm;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public final class RegistrationAction extends Action  implements Serializable{
        @Inject
    @OscanaActionForm
    public RegistrationForm form;

    @Execute(validator=true)
    @OnError(type = ApplicationException.class, path = "/validator/registration.jsp")
    public HttpResponse execute(HttpRequest nabHttpRequest, ExecutionContext context)  throws Exception {

        RegistrationForm form = context.getRequestScopedVar("form");

        HttpServletRequest httpServletRequest = createStrutsRequest(context);
        HttpServletResponse httpServletResponse = createStrutsResponse(context);
        ActionMapping mapping = createStrutsMapping(nabHttpRequest, context)
            .add("input", "/validator/registration.jsp")
            .add("success", "/validator/index.jsp")
            .add("home", "/validator/index.jsp")
            .createActionMapping();
        mapping.setName("registrationForm");
        mapping.setScope("protype");
        mapping.setAttribute("");
        mapping.setActionId("");
        mapping.setValidate(true);
        mapping.setPath("/registration-submit");
        mapping.setParameter("");
        mapping.setType("org.apache.struts.webapp.validator.RegistrationAction");


        return execute(mapping, form, httpServletRequest, httpServletResponse).toResponse();
    }


    /**
     * Commons Logging instance.
     */
    private Log log = LogFactory.getFactory().getInstance(this.getClass().getName());

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @return Action to forward to
     * @exception Exception if an input/output error or servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Was this transaction cancelled?
        if (isCancelled(request)) {
            if (log.isInfoEnabled()) {
                log.info(" " + mapping.getAttribute() + " - Registration transaction was cancelled");
            }

            removeFormBean(mapping, request);

            return (mapping.findForward("success"));
        }

        return mapping.findForward("success");
    }

    /**
     * Convenience method for removing the obsolete form bean.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     */
    protected void removeFormBean(ActionMapping mapping, HttpServletRequest request) {

        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                request.removeAttribute(mapping.getAttribute());
            } else {
                HttpSession session = request.getSession();
                session.removeAttribute(mapping.getAttribute());
            }
        }
    }
}
