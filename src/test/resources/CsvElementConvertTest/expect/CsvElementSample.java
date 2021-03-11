package jp.co.tis.dto.project;


import java.io.Serializable;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;

import nablarch.common.databind.csv.CsvFormat;
import nablarch.common.databind.csv.Csv;
import nablarch.common.databind.csv.CsvDataBindConfig;



/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@Csv(headers = {"プロジェクトＩＤ","プロジェクト名","プロジェクト種別","プロジェクト開始日付","プロジェクト終了日付"},properties = {"projectId","projectNm","projectType","projectStartDate","test"},type = Csv.CsvType.CUSTOM)
@CsvFormat(charset = "Shift_JIS", fieldSeparator = ',',ignoreEmptyLine = true,lineSeparator = "\r\n", quote = '"',quoteMode = CsvDataBindConfig.QuoteMode.NORMAL, requiredHeader = true, emptyToNull = false)
public class CsvElementSample implements Serializable {

    /** シリアル・バージョンID */
    private static final long serialVersionUID = 1L;

    /** プロジェクトＩＤ */
    public String projectId;

    /** プロジェクト名 */
    public String projectNm;

    /** プロジェクト種別 */
    public String projectType;

    /** プロジェクト開始日付 */
    public String projectStartDate;

    /** プロジェクト終了日付 */
    public String projectEndDate;

    /** プロジェクト終了日付 */
    public String test;

}
