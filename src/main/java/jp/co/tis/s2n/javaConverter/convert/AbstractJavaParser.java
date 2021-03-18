package jp.co.tis.s2n.javaConverter.convert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.file.S2nFileWriter;
import jp.co.tis.s2n.javaConverter.keyword.JavaKeyword;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;

/**
 * Javaパーサーの抽象クラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public abstract class AbstractJavaParser extends AbstractBatchBase implements JavaKeyword {

    public static final String CRLF = "\r\n";
    public static final String SP4 = "    ";

    /**
     * 処理の入り口<br>
     * トップ階層から再帰的にディレクトリを走査して、全ファイルの処理を実施する。
     */
    public void execute() {
        execute(inPath, outPath, ".java");
    }

    /**
     * 指定された場所から再帰的にディレクトリを走査して、全ファイルの処理を実施する。<br>
     * fromPathで指定されたディレクトリ内の全ファイルのうち、ファイル名がsuffixで終わるものだけを処理する。<br>
     * 処理結果はtoPathに出力される。
     * @param fromPath 処理対象(Input)
     * @param toPath 出力先(Output)
     * @param suffix 処理対象ファイルのサフィックス
     */
    public void execute(String fromPath, String toPath, String suffix) {


        try {
            findDir(fromPath, toPath, suffix, new File(fromPath));
        } catch (Exception ex) {
            LogUtils.warn(fromPath, "処理失敗", ex);
            ex.printStackTrace();
        }

    }

    /**
     * ファイルの再帰処理。<br>
     * 再帰的にディレクトリを走査しながら以下の処理を実施する。<br>
     * ・出力先ディレクトリの作成をしながら、ファイル単位の処理を呼び出す。<br>
     *  ディレクトリが空なら出力先ディレクトリは作成しない。
     *
     * @param fromPath 対象ファイルのあるディレクトリ
     * @param toPath 出力先
     * @param suffix サフィックス
     * @param dir 処理対象ファイル
     */
    public void findDir(String fromPath, String toPath, String suffix, File dir) {

        fromPath = fromPath.replace('\\', '/');
        toPath = toPath.replace('\\', '/');
        println();
        println("<<Path:" + dir.getPath().replace('\\', '/'));
        String s1 = dir.getPath().replace('\\', '/');
        String s2 = s1.replace(fromPath, "");
        String s3 = toPath + s2;
        println(">>Path:" + s3);

        File newDir = new File(s3);

        newDir.mkdir();

        //ディレクトリが空なら出力先ディレクトリを作成せず終了
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (!file.exists()) {
                continue;
            } else if (file.isDirectory()) {
                findDir(fromPath, toPath, suffix, file);
            } else if (file.isFile()) {
                findFile(fromPath, toPath, suffix, file, newDir);
            }
        }

    }

    /**
     * 単一ファイルの処理。
     * @param fromPath 処理対象(Input)
     * @param toPath 出力先(Output)
     * @param suffix サフィックス
     * @param file 処理対象ファイル
     * @param outPath 出力ファイル
     */
    public void findFile(String fromPath, String toPath, String suffix, File file, File outPath) {

        if (file.getName().endsWith(suffix) == false) {
            return;
        }

        try {
            executeFile(file.getAbsolutePath(), outPath.getAbsolutePath(), file.getName());
        } catch (Exception ex) {
            LogUtils.warn(file.getName(), "処理失敗", ex);
            ex.printStackTrace();
        }

    }

    protected abstract void executeFile(String inFilePath, String outPath, String fileName);

    /**
     * nodeをファイルに出力する。<br>
     * ・改行キーワード(XenCrLf;)を改行に戻す。
     * @param outPath 出力ファイル
     * @param fileName ファイル名
     * @param topNode トップノード
     * @param activeProfile プロファイル
     */
    protected void postProc(String outPath, String fileName, Node topNode, S2nProfile activeProfile) {

        String javaFilePath = outPath + File.separator + fileName;

        println(">>File:" + javaFilePath);

        File outDir = new File(outPath);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try (OutputStreamWriter osw = getOutputStreamWriter(javaFilePath)) {
            NodeUtil.fprintAll(new S2nFileWriter(osw, activeProfile), topNode);
        } catch (Exception ex) {
            LogUtils.warn(fileName, "処理失敗", ex);
            ex.printStackTrace();
        }

    }

    protected OutputStreamWriter getOutputStreamWriter(String filePath) throws IOException {
        return new OutputStreamWriter(new FileOutputStream(filePath), this.codeName);
    }

}
