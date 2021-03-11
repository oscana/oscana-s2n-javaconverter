package jp.co.tis.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.tis.xenlon.exception.SystemErrorException;
import jp.co.tis.xenlon.seasar.struts.annotation.Forward;
import jp.co.tis.xenlon.util.OscanaPropertyUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class SampleSearchListAction extends AbstractSearchAction {

    /** アクションフォーム */
    @ActionForm
    @Resource
    protected SampleSearchListForm sampleSearchListForm;

    /**
     *
     * @return 遷移先画面
     */
    @Execute(validator = false, urlPattern = "index/{sampleKbn}")
    public String index() {

        // フォームの検索条件を初期化
        sampleSearchListForm.resetCondition();

        sampleSearchListForm.screenType = SystemConstants.SCREEN_TYPE_SEARCH;

        return "SampleSearchList.jsp";
    }

    /**
     * 入力された検索条件に基づきテーブルを検索し、
     * 検索結果一覧を表示する。<br />
     * 検索結果はsampleListにセットする。
     *
     * @return 出力系メソッド
     */
    @Execute(validate = "validateForm,validateDoSearch",
        input = "SampleSearchList.jsp",
        urlPattern = "doSearch/{pageno}")
    public String doSearch() {

        sampleSearchListForm.screenType = SystemConstants.SCREEN_TYPE_SEARCHLIST;

        sampleSearchListForm.setStartRecNo();

        // 1ページに表示するレコード数の上限
        sampleSearchListForm.limit =
            Integer.parseInt(OscanaPropertyUtil.getOscanaConstProperty("LIMIT_NUM5"));

        BeanMap where = getWhere(sampleSearchListForm);

        if (sampleSearchListForm.isEnvTypeHonban()) {

            List<Tbsample> tbsampleList =
                tbsampleService.findBySearchCondition(where);

            sampleList = new ArrayList<Tbsample>();

            for (Tbsample tbsample : tbsampleList) {

                Tbsample sample =
                    Beans.createAndCopy(Tbsample.class, tbsample).execute();

                if (StringUtils.isNotBlank(sample.anngoukakyoutsuukagi)) {
                    sample.anngoukakyoutsuukagi =
                        unScrambleHex(StringUtil.trimRight(tbsample.anngoukakyoutsuukagi));
                }

                if (StringUtils.isNotBlank(sample.initialvector)) {
                    sample.initialvector =
                        unScrambleHex(StringUtil.trimRight(tbsample.initialvector));
                }

                sampleList.add(sample);
            }

            sampleSearchListForm.count =
                String.valueOf(tbsampleService.getCountBySearchCondition(where));

        } else if (sampleSearchListForm.isEnvTypeHaneiMae()) {

            List<Tbsample> thesampleList =
                sampleService.findBySearchCondition(where);

            sampleList = new ArrayList<Tbsample>();

            for (Tbsample bufsample : thesampleList) {

                Tbsample sample =
                    Beans.createAndCopy(Tbsample.class, bufsample).execute();

                if (StringUtils.isNotBlank(bufsample.anngoukakyoutsuukagi)) {
                    sample.anngoukakyoutsuukagi =
                        unScrambleHex(StringUtil.trimRight(bufsample.anngoukakyoutsuukagi));
                }

                if (StringUtils.isNotBlank(bufsample.initialvector)) {
                    sample.initialvector =
                        unScrambleHex(StringUtil.trimRight(bufsample.initialvector));
                }

                sampleList.add(sample);
            }

            sampleSearchListForm.count =
                String.valueOf(sampleService.getCountBySearchCondition(where));

        } else {

            // 単項目精査を行っているため当パターンは発生しない。
            throw new SystemErrorException("errors.samplekbn");
        }

        return sampleSearchList();
    }

    /**
     * 登録画面を表示する。
     *  (検索画面からの変更リンク遷移用)
     *
     * @return 遷移先画面
     */
    @Forward
    @Execute(validator = false,
        urlPattern = "fwCreate/{sampleKbn}"+"/{kcode}")
    public String fwCreate() {

        BeanMap where = new BeanMap();

        where.put("kcode",
                StringUtil.fillCharRight(sampleSearchListForm.kcode, 11, ' '));

        // レコード存在チェック
        if (sampleSearchListForm.isEnvTypeHonban()) {

            long recordCnt = tbsampleService.getCountById(where);

            if (recordCnt == 0) {

                // レコードが存在しない場合はエラーメッセージを設定して検索結果画面を再表示
                saveError("test", new Object[] {});

                return doSearch();
            }

        } else if (sampleSearchListForm.isEnvTypeHaneiMae()) {

            long recordCnt = sampleService.getCountById(where);

            if (recordCnt == 0) {

                // レコードが存在しない場合はエラーメッセージを設定して検索結果画面を再表示
                saveError("test", new Object[] {});

                return doSearch();
            }

        } else {

            // 単項目精査を行っているため当パターンは発生しない。
            throw new SystemErrorException("errors.samplekbn");
        }

        // レコードが存在した場合はForward
        return "/sample/sampleInsert/";
    }

    /**
     * 遷移先画面から戻った時の再表示。
     *
     * @return 遷移先画面
     */
    @Execute(validator = false)
    public String indexBack() {

        return doSearch();
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

        BeanMap map = Beans
            .createAndCopy(BeanMap.class, sampleSearchListForm)
            .prefix("search_")
            .execute();

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