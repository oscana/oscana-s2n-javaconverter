package jp.co.tis.constants;

import jp.co.tis.oscana.util.OscanaPropertyUtil;
import static java.lang.Integer.*;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;



/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@Prototype
public class TestConvertProcCommonOthers {

    /** 日付フォーマット（日付） */
    public static final String FORMAT_DATE = "yyyy/MM/dd HH:mm:ss";

    /** 日付フォーマット（年月日） */
    public static final String FORMAT_YYYYMMDD = "yyyy/MM/dd";

    /** 日付フォーマット（日付ミリ秒） */
    public static final String FORMAT_DATE_MSEC = "yyyy/MM/dd HH:mm:ss.SSS";

    /** 日付フォーマットスラッシュなし（日付ミリ秒） */
    public static final String FORMAT_DATE_MSEC_NO_SLASH = "yyyyMMddHHmmssSSS";

    /** 日付フォーマット（時） */
    public static final String FORMAT_HOUR = "HH";

    /** 日付フォーマット（分） */
    public static final String FORMAT_MINUTE = "mm";

    /** 日付フォーマット（秒） */
    public static final String FORMAT_SECOND = "ss";

    /** 日付フォーマット（年月） */
    public static final String FORMAT_YYYYMM = "yyyyMM";

    /** 日付フォーマット（年月） */
    public static final String FORMAT_YYMM = "yy/MM";

    /** 日付フォーマット（年月日） */
    public static final String FORMAT_YYYYMMDD_NO_SLASH = "yyyyMMdd";

    /** 日付フォーマット（年） */
    public static final String FORMAT_YYYY = "yyyy";

    /** ログインロック規定回数 */
    public static final int LOGIN_LOCK_KITEI_KAISU = Integer.parseInt(OscanaPropertyUtil.getOscanaProperty("LOGIN_LOCK_KITEI_KAISU"));

    /** パスワードハッシュ化のストレッチ回数 */
    public static final Integer PASSWORD_STRETCH_COUNT = Integer.parseInt(OscanaPropertyUtil.getOscanaProperty("PASSWORD_STRETCH_COUNT"));

    /** パスワード保護用に付加するソルトの長さ */
    public static final Integer PASSWORD_SALT_LENGTH = Integer.parseInt(OscanaPropertyUtil.getOscanaProperty("PASSWORD_SALT_LENGTH"));

    /** パスワード有効日数 */
    public static final Integer PASSWORD_VALID_DAYS = Integer.parseInt(OscanaPropertyUtil.getOscanaProperty("PASSWORD_VALID_DAYS"));

    /** パスワード履歴を保持する数 */
    public static final Integer PASSWORD_HISTORY_COUNT = Integer.parseInt(OscanaPropertyUtil. getOscanaProperty("PASSWORD_HISTORY_COUNT"));


    OscanaPropertyUtil.getOscanaProperty("PASSWORD_SALT_LENGTH");
}
