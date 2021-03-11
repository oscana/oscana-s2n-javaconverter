package jp.co.tis.dto.project;

import jp.co.tis.xenlon.seasar.util.csvutil.annotation.CsvElement;

import java.io.Serializable;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class CsvElementSample implements Serializable {

    /** シリアル・バージョンID */
    private static final long serialVersionUID = 1L;

    /** プロジェクトＩＤ */
    @CsvElement(name = "プロジェクトＩＤ", priority = 1)
    public String projectId;

    /** プロジェクト名 */
    @CsvElement(name = "プロジェクト名", priority = 2)
    public String projectNm;

    /** プロジェクト種別 */
    @CsvElement(name = "プロジェクト種別", priority = 3)
    public String projectType;

    /** プロジェクト開始日付 */
    @CsvElement(name = "プロジェクト開始日付", priority = 4)
    public String projectStartDate;

    /** プロジェクト終了日付 */
    @CsvElement(name = "プロジェクト終了日付", priority = 5)
    public String projectEndDate;

    /** プロジェクト終了日付 */
    @CsvElement(property = "テスト", priority = 5)
    public String test;

}
