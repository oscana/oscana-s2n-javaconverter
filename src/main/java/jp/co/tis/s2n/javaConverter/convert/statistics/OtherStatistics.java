package jp.co.tis.s2n.javaConverter.convert.statistics;

import java.io.File;
import java.io.IOException;

import jp.co.tis.s2n.converterCommon.statistics.StatisticsBase;

/**
 *  統計情報を蓄積するツール。<br>
 *
 * このクラスはシングレトンである、
 * スレッドセーフではないので単一スレッドからの利用を想定する。
 *
 * @author Fumihiko Yamamoto
 */
public class OtherStatistics {

    //シングレトン化
    private static OtherStatistics instance;

    private OtherStatistics() {
    }

    /**
     * インスタンスを取得する。
     * @return 唯一のインスタンスを返す
     */
    public static OtherStatistics getInstance() {
        if (instance == null) {
            instance = new OtherStatistics();
        }
        return instance;
    }

    //ログファイルのレコード定義（1行分）
    public int makeGetterCnt = 0;
    public int makeSetterCnt = 0;
    public int makeFiledAnnotation = 0;
    public int makeClassAnnotation = 0;
    public int makeMethoddAnnotation = 0;
    //import 行数
    public int editImport = 0;
    //import 挿入行数
    public int insertImport = 0;
    //extends付与行数
    public int editExtend = 0;
    //serializable付与数
    public int addSerializable = 0;
    //export, serializableなどのクラス編集を実施した行数
    public int editClassLine = 0;
    public int notSupportValidateAnnotation;
    //entityの@entity
    public int addEntityEntity;
    //entityのテーブル名
    public int addEntityTableName;
    //entityのgetter
    public int addEntityGetter;
    //entityのsetter
    public int addEntitySetter;
    //entity の冒頭の共通ヘッダ
    public int addEntityHeader;

    /**
     * CSVファイルに書き出す。
     * @throws IOException 例外
     * @throws IllegalAccessException 例外
     */
    public void exportData() throws IOException, IllegalArgumentException, IllegalAccessException {
        File file = new File("logs/OtherResut.csv");
        StatisticsBase.exportObject2Csv(file, this);

    }

}
