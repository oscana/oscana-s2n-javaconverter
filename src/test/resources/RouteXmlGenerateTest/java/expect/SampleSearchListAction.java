package jp.co.tis.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oscana.s2n.xenlon.exception.SystemErrorException;
import jp.co.tis.xenlon.seasar.struts.annotation.Forward;
import jp.co.tis.xenlon.util.OscanaPropertyUtil;

import org.apache.commons.lang.StringUtils;
import  oscana.s2n.struts.ActionMessages;
import  oscana.s2n.sastruts.util.BeanMap;
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
import oscana.s2n.seasar.struts.annotation.ActionForm;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public class SampleSearchListAction extends AbstractSearchAction  implements Serializable{

    /** アクションフォーム */
    @ActionForm
    @Inject
    protected SampleSearchListForm sampleSearchListForm;

    /**
     *
     * @return 遷移先画面
     */
    @OnDoubleSubmission(path="error.jsp")
    @Execute(validator=false)
    public HttpResponse index(HttpRequest nabRequest, ExecutionContext nabContext) {

        // フォームの検索条件を初期化
        sampleSearchListForm.resetCondition();

        sampleSearchListForm.screenType = SystemConstants.SCREEN_TYPE_SEARCH;

        return OscanaHttpResourceConverUtil.createHttpResponse( "SampleSearchList.jsp", this, nabRequest, nabContext, "SampleSearchList");
    }

    /**
     * 入力された検索条件に基づきテーブルを検索し、
     * 検索結果一覧を表示する。<br />
     * 検索結果はsampleListにセットする。
     *
     * @return 出力系メソッド
     */
    @OnDoubleSubmission(path="error.jsp")
    @Execute(validator=true, validate="validateForm,validateDoSearch")
    @OnError(type = ApplicationException.class, path = "/WEB-INF/view/sampleSearchList/SampleSearchList.jsp")
    public HttpResponse doSearch(HttpRequest nabRequest, ExecutionContext nabContext) {

        sampleSearchListForm.screenType = SystemConstants.SCREEN_TYPE_SEARCHLIST;

        sampleSearchListForm.setStartRecNo();

        // 1ページに表示するレコード数の上限
        sampleSearchListForm.limit = Integer.parseInt(OscanaPropertyUtil.getOscanaConstProperty("LIMIT_NUM5"));

        BeanMap where = getWhere(sampleSearchListForm);

        if (sampleSearchListForm.isEnvTypeHonban()) {

            List<Tbsample> tbsampleList = tbsampleService.findBySearchCondition(where);

            sampleList = new ArrayList<Tbsample>();

            for (Tbsample tbsample : tbsampleList) {

                Tbsample sample = Beans.createAndCopy(Tbsample.class, tbsample).execute();

                if (StringUtils.isNotBlank(sample.anngoukakyoutsuukagi)) {
                    sample.anngoukakyoutsuukagi = unScrambleHex(StringUtil.trimRight(tbsample.anngoukakyoutsuukagi));
                }

                if (StringUtils.isNotBlank(sample.initialvector)) {
                    sample.initialvector = unScrambleHex(StringUtil.trimRight(tbsample.initialvector));
                }

                sampleList.add(sample);
            }

            sampleSearchListForm.count = String.valueOf(tbsampleService.getCountBySearchCondition(where));

        } else if (sampleSearchListForm.isEnvTypeHaneiMae()) {

            List<Tbsample> thesampleList = sampleService.findBySearchCondition(where);

            sampleList = new ArrayList<Tbsample>();

            for (Tbsample bufsample : thesampleList) {

                Tbsample sample = Beans.createAndCopy(Tbsample.class, bufsample).execute();

                if (StringUtils.isNotBlank(bufsample.anngoukakyoutsuukagi)) {
                    sample.anngoukakyoutsuukagi = unScrambleHex(StringUtil.trimRight(bufsample.anngoukakyoutsuukagi));
                }

                if (StringUtils.isNotBlank(bufsample.initialvector)) {
                    sample.initialvector = unScrambleHex(StringUtil.trimRight(bufsample.initialvector));
                }

                sampleList.add(sample);
            }

            sampleSearchListForm.count = String.valueOf(sampleService.getCountBySearchCondition(where));

        } else {

            // 単項目精査を行っているため当パターンは発生しない。
            throw new SystemErrorException("errors.samplekbn");
        }

        return OscanaHttpResourceConverUtil.createHttpResponse( sampleSearchList(), this, nabRequest, nabContext, "SampleSearchList");
    }

    /**
     * 登録画面を表示する。
     *  (検索画面からの変更リンク遷移用)
     *
     * @return 遷移先画面
     */
    @Forward
    @OnDoubleSubmission(path="error.jsp")
    @Execute(validator=false)
    public HttpResponse fwCreate(HttpRequest nabRequest, ExecutionContext nabContext) {

        BeanMap where = new BeanMap();

        where.put("kcode", StringUtil.fillCharRight(sampleSearchListForm.kcode, 11, ' '));

        // レコード存在チェック
        if (sampleSearchListForm.isEnvTypeHonban()) {

            long recordCnt = tbsampleService.getCountById(where);

            if (recordCnt == 0) {

                // レコードが存在しない場合はエラーメッセージを設定して検索結果画面を再表示
                saveError("test", new Object[] {});

                return OscanaHttpResourceConverUtil.createHttpResponse( doSearch(nabRequest, nabContext), this, nabRequest, nabContext, "SampleSearchList");
            }

        } else if (sampleSearchListForm.isEnvTypeHaneiMae()) {

            long recordCnt = sampleService.getCountById(where);

            if (recordCnt == 0) {

                // レコードが存在しない場合はエラーメッセージを設定して検索結果画面を再表示
                saveError("test", new Object[] {});

                return OscanaHttpResourceConverUtil.createHttpResponse( doSearch(nabRequest, nabContext), this, nabRequest, nabContext, "SampleSearchList");
            }

        } else {

            // 単項目精査を行っているため当パターンは発生しない。
            throw new SystemErrorException("errors.samplekbn");
        }

        // レコードが存在した場合はForward
        return OscanaHttpResourceConverUtil.createHttpResponse( "/sample/sampleInsert/", this, nabRequest, nabContext, "SampleSearchList");
    }

    /**
     * 遷移先画面から戻った時の再表示。
     *
     * @return 遷移先画面
     */
    @OnDoubleSubmission(path="error.jsp")
    @Execute(validator=false)
    public HttpResponse indexBack(HttpRequest nabRequest, ExecutionContext nabContext) {

        return OscanaHttpResourceConverUtil.createHttpResponse( doSearch(nabRequest, nabContext), this, nabRequest, nabContext, "SampleSearchList");
    }

    /**
     * doSearchのvalidateメソッド。
     *
     * @return ActionMessagesオブジェクト
     */
    public ActionMessages validateDoSearch() {

        ActionMessages errors = new ActionMessages();

        return errors;
    }

    /**
     * 暗号情報（契約コード管理）検索画面を表示する。
     *
     * @return 遷移先画面
     */
    private String sampleSearchList() {

        return "SampleSearchList.jsp";
    }

    /**
     * 検索条件用(Where構築用)BeanMapを返却する。
     *
     * @param sampleSearchListForm ActionForm
     * @return Where条件を作成するためのBeanMap
     */
    private BeanMap getWhere(SampleSearchListForm sampleSearchListForm) {

        BeanMap map = Beans.createAndCopy(BeanMap.class, sampleSearchListForm).prefix("search_").execute();

        // ActionFormからコピーした値を SQL Bind変数用に変換して再設定
        Object keyValue = null;
        for (String keyName : map.keySet()){
            keyValue = map.get(keyName);
            map.put(keyName, StringUtil.getSQLBindParamValue(keyValue));
        }

        // 検索条件が空となった場合は例外を生成する。
        // （単項目精査を行っているため発生することはないが、万が一の全表走査を回避するため）
        if (map.isEmpty()) {
            throw new SystemErrorException("errors.systemError");
        }

        // ページング処理用の取得対象行番号を設定
        map.put("firstRowNo", sampleSearchListForm.getFirstRowNo());
        map.put("lastRowNo", sampleSearchListForm.getLastRowNo());

        return map;
    }

    private String unScrambleHex(String befUnScr) {

        ScrambleHex scr = new ScrambleHex();

        String aftUnScr = "";
        try {

            if (scr.unScrambleHex(befUnScr)) {

                aftUnScr = scr.getAftUnScrambleHex();
            } else {

                saveError("test");
            }
        } catch (Exception e) {

            throw new SystemErrorException("errors.systemError", e);
        }

        return aftUnScr;
    }
}
