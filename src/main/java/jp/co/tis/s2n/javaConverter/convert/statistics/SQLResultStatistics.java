package jp.co.tis.s2n.javaConverter.convert.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;

import jp.co.tis.s2n.converterCommon.statistics.StatisticsBase;

/**
 * SQLコンバートログクラス。<br>
 * <br>
 * 1ファイルの処理結果が１つのSQLResultStatisticsとなる。<br>
 * ファイル名をキーにSQLResultStatisticsが管理されており、それぞれのオブジェクトを取得し、集計する仕組みを備えている。<br>
 *
 * スレッドセーフではないので単一スレッドからの利用を想定する。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class SQLResultStatistics {

    //シングレトン化
    private static HashMap<String, SQLResultStatistics> instances = new HashMap<>();

    private SQLResultStatistics() {
    }

    /**
     * 指定したファイルに対するデータを取得する。
     * @param fileName ファイル名
     * @return SQLResultStatisticsのオブジェクト
     */
    public static SQLResultStatistics getInstance(String fileName) {
        SQLResultStatistics instance = instances.get(fileName);
        if (instance == null) {
            instance = new SQLResultStatistics();
            instance.fileName = fileName;
            instances.put(fileName, instance);
        }
        return instance;
    }

    //変換対象となる特殊記述(箇所数)
    public int allOfTarget;

    //変換対象となる特殊記述(行数)　※aggregateResultLine時に集計
    public int allOfTargetLineCnt;

    //変換に成功できた行数　※aggregateResultLine時に集計
    public int success_LineCnt;
    //変換に失敗した行数（１行中に成功したものと失敗したものがあれば失敗とみなす）　※aggregateResultLine時に集計
    public int failLineCnt;

    //変数として変換できた数
    public int success_asParameter;
    //コメントとして変換できた数
    public int success_asComment;

    //リテラル置換（未対応）
    public int leteralChg;
    //IF文（未対応）
    public int ifSent;

    //ソース全行数
    public int allOfLine;

    //ソース有効行数（空白やコメント行を除去）
    public int logicalLine;

    //変更があった行数
    public int chgLine;

    //ファイル名
    public String fileName;

    //行ごとの成否
    private LinkedHashMap<Integer, LineResult> lineResults = new LinkedHashMap<>();

    /**
     * 指定された行における処理結果(LineResult)を取得する。<br>
     * その行にLineResultがない場合は新規にLineResultを作成・登録したうえで返す。
     * @param editLineNo 行番号
     * @return 処理結果
     */
    private LineResult getLineResult(int editLineNo) {
        LineResult result = this.lineResults.get(editLineNo);
        if (result == null) {
            result = new LineResult();
            this.lineResults.put(editLineNo, result);
        }
        return result;
    }

    /**
     * 特殊記述要素数をカウントアップする。<br>
     * LineResultを作成し、特殊記述要素の存在する行数としてもカウントする。
     * @param editLineNo 行番号
     */
    public void incAllOfTarget(int editLineNo) {
        this.allOfTarget++;
        getLineResult(editLineNo); //レコード作るだけ作る
    }

    /**
     * IF文要素数をカウントアップする。<br>
     * LineResultを作成し、未対応記述の存在する行数としてもカウントする。
     * @param editLineNo 行番号
     */
    public void incIfSent(int editLineNo) {
        this.ifSent++;
        getLineResult(editLineNo).setFail();
    }

    /**
     * パラメータ置換要素数をカウントアップする。<br>
     * LineResultを作成し、処理成功行数としてもカウントする。
     * @param editLineNo 行番号
     */
    public void incSuccess_asParameter(int editLineNo) {
        this.success_asParameter++;
        getLineResult(editLineNo).setSuccess();
    }

    /**
     * リテラル置換要素数をカウントアップする。<br>
     * LineResultを作成し、未対応記述の存在する行数としてもカウントする。
     * @param editLineNo 行番号
     */
    public void incLeteralChg(int editLineNo) {
        this.leteralChg++;
        getLineResult(editLineNo).setFail();
    }

    /**
     * コメント要素数をカウントアップする。<br>
     * LineResultを作成し、処理成功行数としてもカウントする。
     * @param editLineNo 行番号
     */
    public void incSuccess_asComment(int editLineNo) {
        this.success_asComment++;
        getLineResult(editLineNo).setSuccess();

    }

    /**
     * CSVファイルに書き出す。
     * @throws IOException 例外
     * @throws IllegalAccessException 例外
     */
    public static void exportData() throws IOException, IllegalArgumentException, IllegalAccessException {
        File file = new File("logs/SQLResut.csv");
        StatisticsBase.exportCollection2Csv(file, instances.values());

    }

    /**
     * 行単位に成否を集計する。
     */
    public void aggregateResultLine() {

        this.allOfTargetLineCnt = 0;
        this.failLineCnt = 0;
        this.success_LineCnt = 0;

        for (Iterator<LineResult> iterator = this.lineResults.values().iterator(); iterator.hasNext();) {
            LineResult lineResult = iterator.next();
            this.allOfTargetLineCnt++;
            if (lineResult.isSuccess()) {
                this.success_LineCnt++;
            } else {
                this.failLineCnt++;
            }
        }
    }

    /**
     * 変換前後の文字列を解析して変更状況を記録する。
     * @param srcSqlStr 変更前SQL
     * @param sqlStr 変更後SQL
     */
    public void analysisResult(String srcSqlStr, String sqlStr) {
        try {

            //総行数、実行数のカウント
            BufferedReader br = new BufferedReader(new StringReader(srcSqlStr));
            String srcLine;
            allOfLine = 0;
            logicalLine = 0;
            while (true) {
                srcLine = br.readLine();
                allOfLine++;
                if (srcLine == null) {
                    break;
                }
                if (srcLine.trim().length() > 0) {
                    //空行でないなら有効行数としてカウント
                    logicalLine++;
                }
            }

            //差異行数のカウント
            Patch<String> diff = DiffUtils.diff(srcSqlStr, sqlStr, null);
            chgLine = diff.getDeltas().size();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 行の成否(成否両方ある場合は否を優先）
     */
    class LineResult {

        //0...NONE, 1...成功  -1...失敗
        int result = 0;

        /**
         * 失敗フラグをON。
         */
        public void setFail() {
            result = -1;

        }

        /**
         * 成功フラグをON。
         */
        public void setSuccess() {
            if (result == 0) {
                result = 1;
            }
        }

        /**
         * この行が成功のみであればtrueを返す。
         * １つでも失敗要素があればfalseを返す。
         * @return 行の成否結果
         */
        public boolean isSuccess() {
            if (result == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

}
