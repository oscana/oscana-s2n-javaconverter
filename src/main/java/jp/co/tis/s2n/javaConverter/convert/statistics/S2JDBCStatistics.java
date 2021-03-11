package jp.co.tis.s2n.javaConverter.convert.statistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.tis.s2n.converterCommon.statistics.StatisticsBase;

/**
 * 統計情報を蓄積するツール。<br>
 *
 * このクラスはシングレトンである、
 * スレッドセーフではないので単一スレッドからの利用を想定する。
 * <br>
 * @author Fumihiko Yamamoto
 *
 */
public class S2JDBCStatistics {

    //シングレトン化
    private static S2JDBCStatistics instance;

    private S2JDBCStatistics() {
    }

    /**
     * インスタンスを取得する。
     * @return 唯一のインスタンスを返す
     */
    public static S2JDBCStatistics getInstance() {
        if (instance == null) {
            instance = new S2JDBCStatistics();
        }
        return instance;
    }

    /**
     * ログファイルのレコード定義（1行分）。
     */
    public class S2JDBCResultData {
        public String fileName;
        public String moduleName;
        public String result;
        public String line;
        public String nonSupportStatement;
    }

    private List<S2JDBCResultData> convdata = new ArrayList<S2JDBCResultData>();

    /** S2JDBCResultData.resultのパラメータ*/
    public static String RESULT_SUCCESS = "SUCCESS";
    /** S2JDBCResultData.resultのパラメータ*/
    public static String RESULT_NOTSUPPORTED = "NOTSUPPORTED";
    /** S2JDBCResultData.resultのパラメータ*/
    public static String RESULT_IGNORE = "IGNORE";
    /** S2JDBCResultData.resultのパラメータ*/
    public static String RESULT_ERROR = "ERROR";

    /**
     * Annotationの処理結果。
     * @param fileName 変換対象ファイル名
     * @param moduleName 変換処理を行うモジュール
     * @param result 変換結果のパラメータ
     * @param line 該当行の文字列
     * @param nonSupportStatement
     */
    public void convertedAnnotation(String fileName, String moduleName, String result, String line,
            String nonSupportStatement) {
        S2JDBCResultData td = new S2JDBCResultData();
        td.fileName = fileName;
        td.moduleName = moduleName;
        td.result = result;
        td.line = line;
        td.nonSupportStatement = (nonSupportStatement == null) ? "" : nonSupportStatement;
        convdata.add(td);

    }

    /**
     * CSVファイルに書き出す。
     * @throws IOException 例外
     * @throws IllegalAccessException 例外
     */
    public void exportData() throws IOException, IllegalAccessException {

        File file = new File("logs/s2jdbcConvertResult.csv");

        StatisticsBase.exportCollection2Csv(file, convdata);

    }

}
