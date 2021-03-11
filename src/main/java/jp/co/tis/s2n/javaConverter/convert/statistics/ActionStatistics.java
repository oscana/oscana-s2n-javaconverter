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
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ActionStatistics {

    //シングレトン化
    private static ActionStatistics instance;

    private ActionStatistics() {
    }

    /**
     * インスタンスを取得する。
     * @return 唯一のインスタンスを返す
     */
    public static ActionStatistics getInstance() {
        if (instance == null) {
            instance = new ActionStatistics();
        }
        return instance;
    }

    /**
     * ログファイルのレコード定義（1行分）。
     */
    public class ActionData {
        public String packageName;
        public String actionName;
        public String beforeString;
        public String afterString;
    }

    //ログファイルデータ
    private List<ActionData> actionData = new ArrayList<ActionData>();

    /**
     * Action処理結果を登録する。
     * @param packageName パッケージ名
     * @param actionName アクション名
     * @param beforeString 変換前の文字列
     * @param afterString 変換後の文字列
     */
    public void convertedAction(String packageName, String actionName, String beforeString, String afterString) {
        ActionData td = new ActionData();
        td.packageName = packageName;
        td.actionName = actionName;
        td.beforeString = beforeString;
        td.afterString = afterString;
        actionData.add(td);

    }

    /**
     * CSVファイルに書き出す。
     * @throws IOException 例外
     * @throws IllegalAccessException 例外
     */
    public void exportData() throws IOException, IllegalAccessException {

        File file = new File("logs/actionResult.csv");

        StatisticsBase.exportCollection2Csv(file, actionData);

    }

}
