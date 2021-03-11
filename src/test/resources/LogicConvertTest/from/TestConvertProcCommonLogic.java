package jp.co.tis.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@SessionScoped
public class TestConvertProcCommonLogic extends OscanaAbstractLogic implements Serializable {
    /** コードマスタアクセス用サービス */
    @Resource
    private TestService testService;

    /**
     * 指定テーブルの指定カラムを検索します。
     *
     * @param codeType 取得テーブル名
     * @param columnId 取得項目ID
     * @param columnName 取得項目名
     * @param delFlg 削除フラグ
     * @return 取得カラムのリスト
     */
    public List<ColumnListDto> findBycodeType(String codeType, String columnId, String columnName, String delFlg) {

        List<ColumnListDto> columnList = new ArrayList<ColumnListDto>();

        // 対象リストを全件取得
        List<ColumnListDto> culumnListAll = testService.findColumnList(codeType, columnId, columnName, delFlg);

        columnList = culumnListAll;

        return columnList;
    }

    /**
     * 指定テーブルの指定カラムのキー値を検索します。
     *
     * @param codeType 取得テーブル名
     * @param columnId 取得項目ID
     * @param value キー値
     * @param delFlg 削除フラグ
     * @return 取得カラムのリスト
     */
    public List<ColumnListDto> findBycodeTypeValue(String codeType, String columnId, String value, String delFlg) {

        List<ColumnListDto> columnList = new ArrayList<ColumnListDto>();

        // 対象リストを全件取得
        List<ColumnListDto> culumnListAll = testService.findColumnVlaue(codeType, columnId, value, delFlg);

        columnList = culumnListAll;

        return columnList;
    }
}
