package jp.co.tis.s2n.javaConverter.convert;

import jp.co.tis.s2n.javaConverter.file.S2nFileWriter;

/**
 * 処理の土台クラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class AbstractBatchBase {

    protected String inPath = null;
    protected String outPath = null;
    protected String tmpPath = null;
    protected String logPath = null;
    protected String logFileName = null;

    protected String codeName = null;

    protected S2nFileWriter logw = null;

    /**
     * 対象ファイルのあるディレクトリを設定する。
     * @param path 対象ファイルのあるディレクトリ
     */
    public void setInPath(String path) {
        inPath = path;
    }

    /**
     * テンポラリファイル作成ディレクトリを設定する。
     * @param path テンポラリファイル作成ディレクトリ
     */
    public void setTmpPath(String path) {
        tmpPath = path;
    }

    /**
     * 出力先を設定する。
     * @param path 出力先
     */
    public void setOutPath(String path) {
        outPath = path;
    }

    /**
     * 文字コードを設定する。
     * @param name 文字コード
     */
    public void setCodeName(String name) {
        codeName = name;
    }

    protected void println(String str) {
        System.out.println(str);
    }

    protected void println() {
        System.out.println("");
    }

}
