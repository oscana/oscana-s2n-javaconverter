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
public class AnnotationStatistics {

    //シングレトン化
    private static AnnotationStatistics instance;

    private AnnotationStatistics() {
    }

    /**
     * インスタンスを取得する。
     * @return 唯一のインスタンスを返す
     */
    public static AnnotationStatistics getInstance() {
        if (instance == null) {
            instance = new AnnotationStatistics();
        }
        return instance;
    }

    /**
     * ログファイルのレコード定義（1行分）。
     */
    public class AnnotationData {
        public String annotationName;
        public String annotationSignature;
        public String aType;
        public String annotationConvertClassName;
        public String className;
        public String orgString;
        public String targetNodeString;
        public String result;
    }

    //ログファイルデータ
    private List<AnnotationData> tagdata = new ArrayList<AnnotationData>();

    /**
     * Annotation処理結果を登録する。
     * @param annotationName アノテーション名
     * @param annotationConvertClassName アノテーションクラス名
     * @param annotationSignature アノテーションシグネチャ
     * @param className 変換対象クラス名
     * @param orgString 変換対象文字列
     * @param result 変換結果
     * @param aType タイプ
     * @param targetNodeString 対象ノードの文字列
     */
    public void convertedAnnotation(String annotationName, String annotationConvertClassName,
            String annotationSignature, String className, String orgString, String result, String aType,
            String targetNodeString) {
        AnnotationData td = new AnnotationData();
        td.annotationName = annotationName;
        td.annotationConvertClassName = annotationConvertClassName;
        td.annotationSignature = annotationSignature;
        td.className = className;
        td.orgString = orgString;
        td.result = result;
        td.aType = aType;
        td.targetNodeString = targetNodeString;
        tagdata.add(td);

    }

    /**
     * CSVファイルに書き出す。
     * @throws IOException 例外
     * @throws IllegalAccessException 例外
     */
    public void exportData() throws IOException, IllegalAccessException {

        File file = new File("logs/annotationResult.csv");

        StatisticsBase.exportCollection2Csv(file, tagdata);

    }

}
