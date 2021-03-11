package jp.co.tis.s2n.javaConverter.convert.sqlfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.convert.AbstractBatchBase;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.convert.statistics.SQLResultStatistics;

/**
 * Sqlファイルの変換処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class SqlFileConverter extends AbstractBatchBase {

    /**
     * S2JDBCのコメント＋サンプル値にマッチする正規表現
     * group1:式コメントの本体
     * group2:サンプル値
     */
    public static final String PATTERN_S2JDBC_COMMENT = "\\/\\*(.+?)\\*\\/([0-9]+|\\(.+?\\)|\'.+?\')?";

    public static final String PATTERN_BLOCK_COMMENT = "\\/\\*\\n(.+?)\\n\\*\\/";

    /**
     * IF文をパースする正規表現。Group1=IF文の条件。Group2=IFブロックで囲われた文字列。
     */
    public static final String PATTERN_IF_PARSE ="/\\*\\s*IF(.*?)\\*/(.*?)/\\*\\s*END\\s*\\*/";

    /**
     * 例：IF null != keiyakucode_EQ
     */
    public static final String PATTERN_IF_01 = "^null\\s+!=\\s+(\\w+)$";

    /**
     * 例：IF whereKu != null
     */
    public static final String PATTERN_IF_02 = "^(\\w+)\\s+!=\\s+(null)$";

    /**
     * 最初の通常行を見つける正規表現
     */
    public static final String PATTERN_FOR_INSERT_SQLNAME = "^(?!--)(.*)";

    /**
     * IF文があるかどうかの判定フラグ
     */
    private boolean ifFlag = false;

    /**
     * BEGINがあるかどうかの判定フラグ
     */
    private boolean beginFlag = false;

    protected S2nProfile activeProfile;

    /**
     * if文の行末に付く論理演算子(AND/OR)
     */
    private String followLogicalOp = null;


    /**
     * if文の中に２行以上あるかどうかの判定フラグ
     */
    private boolean twoStepsFlag = false;

    /**
     * プロファイルを設定する。
     * @param activeProfile プロファイル
     */
    public void setActiveProfile(S2nProfile activeProfile) {
        this.activeProfile = activeProfile;
    }



    /**
     * 処理の入り口。
     */
    public void execute() {
        execute(inPath, outPath, ".sql");
    }

    /**
     * 指定された場所から再帰的にディレクトリを走査して、全ファイルの処理を実施する。<br>
     * fromPathで指定されたディレクトリ内の全ファイルのうち、ファイル名がkeywdで終わるものだけを処理する。<br>
     * 処理結果はtoPathに出力される。
     * @param fromPath 処理対象(Input)
     * @param toPath 出力先(Output)
     * @param keywd 処理対象ファイルのkeywd
     */
    public void execute(String fromPath, String toPath, String keywd) {

        try {
            findDir(fromPath, toPath, keywd, new File(fromPath));
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
     * @param fromPath 処理対象(Input)
     * @param toPath 出力先(Output)
     * @param keywd 処理対象ファイルのkeywd
     * @param dir 処理対象ファイル
     */
    public void findDir(String fromPath, String toPath, String keywd, File dir) {

        println();
        println("<<Path:" + dir.getPath());
        String s1 = dir.getPath();
        String s2 = s1.replace(fromPath, "");
        String s3 = toPath + s2;
        println(">>Path:" + s3);

        // 変換用のsqlファイルが存在しない場合、何もしない
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        File newDir = new File(s3);

        newDir.getParentFile().mkdir();

        List<SqlData> sqlDataList = new ArrayList<SqlData>();

        for (File file : files) {
            if (!file.exists()) {
                continue;
            } else if (file.isDirectory()) {
                findDir(fromPath, toPath, keywd, file);
            } else if (file.isFile()) {
                SqlData ret = findFile(fromPath, toPath, keywd, file, newDir);
                if (ret != null) {
                    sqlDataList.add(ret);
                }
            }
        }

        //このディレクトリのSQLファイルを書き出す
        if (sqlDataList.size() > 0) {
            saveFile(newDir, sqlDataList);
        }

    }

    /**
     * 単一ファイルの処理。
     * @param fromPath 処理対象(Input)
     * @param toPath 出力先(Output)
     * @param keywd 処理対象ファイルのkeywd
     * @param file 処理対象ファイル
     * @param outPath 出力ファイル
     * @return sqlデータ
     */
    public SqlData findFile(String fromPath, String toPath, String keywd, File file, File outPath) {

        if (file.getName().endsWith(keywd) == false) {
            return null;
        }

        return executeFile(file.getAbsolutePath(), outPath.getAbsolutePath(), file.getName());

    }

    protected SqlData executeFile(String inFilePath, String outPath, String fileName) {

        println("<<File:" + inFilePath);
        try {
            String trgFileName = new File(inFilePath).getPath();
            SQLResultStatistics sqlStatistics = SQLResultStatistics.getInstance(trgFileName);
            String srcSqlStr = new String(Files.readAllBytes(Paths.get(trgFileName)),
                    Charset.forName("UTF-8"));
            String sqlStr = modify(srcSqlStr, inFilePath);

            SqlData sqldata = new SqlData(fileName, sqlStr);
            sqlStatistics.analysisResult(srcSqlStr, sqlStr);
            sqlStatistics.aggregateResultLine();
            return sqldata;

        } catch (Exception ex) {
            LogUtils.warn(fileName, "処理失敗", ex);
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 正規表現でコメントを抽出し必要に応じて変換する。
     * @param src sqlデータ
     * @param filename sqlファイル名
     * @return 変換結果
     * @throws IOException 例外
     */
    public String modify(String src, String filename) throws IOException {

        SqlLineAnalyzer sla = new SqlLineAnalyzer(src);

        Pattern p = Pattern.compile(PATTERN_S2JDBC_COMMENT, Pattern.DOTALL);

        Matcher m = p.matcher(src);
        m.reset();
        boolean result = m.find();
        if (result) {
            StringBuffer sb = new StringBuffer();
            int prevEditLineNo = -1; //直前に編集した行番号
            do {
                int editLineNo = sla.getLineOfPos(m.start());
                String replacement = handle(m.group(), m.group(1), m.group(2), filename, editLineNo, prevEditLineNo,
                        src, m.start());
                m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
                result = m.find();

                prevEditLineNo = editLineNo;
            } while (result);
            m.appendTail(sb);

            removeKeyWord(sb);
            return sb.toString();
        }
        return src;
    }

    /**
     * if文にあるAND・OR・WHEREを削除する。
     * @param sb 変換対象のsqlデータ
     * @throws IOException 例外
     */
    private void removeKeyWord(StringBuffer sb) throws IOException {
        int fromIndex = sb.indexOf("$if(");
        while (fromIndex != -1) {
            int endIndex = sb.indexOf("}", fromIndex);
            String temp = sb.substring(fromIndex, endIndex);
            String result = temp.replace("AND", "").replace("OR ", "").replace(" OR", "").replace("WHERE ", "");

            StringBuffer sbTemp = new StringBuffer();

            LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(result));
            String line = null;

            while ((line = lineNumberReader.readLine()) != null) {
                if (!"".equals(line.trim())) {
                    sbTemp.append(line + "\r\n");
                }
            }

            if (!"".equals(sbTemp.toString())) {
                result = sbTemp.toString();
            }

            String sbSrc = sb.substring(fromIndex, endIndex);
            if (sbSrc.contains("\n")) {
                String[] strs = sbSrc.split("\n");
                int t = 0;
                for (String str : strs) {
                    if ("AND".equals(str.trim()) ||
                            "OR".equals(str.trim()) ||
                            "WHERE".equals(str.trim())) {
                        t++;
                    }
                }
                int steps = strs.length - t;
                if (steps <= 3) {
                    sb.replace(fromIndex, endIndex, result);
                }
            }
            fromIndex = sb.indexOf("$if(", fromIndex + 1);
        }

    }

    /**
     * 抽出されたコメントの種類を判別し、変換できるものを変換する。
     * @param all マッチしたコメント全体
     * @param body 式の本体
     * @param sample サンプル値
     * @param filename ファイル名
     * @param editLineNo 現在の行番号
     * @param prevEditLineNo 直前に編集した行番号
     * @param src SQL全体
     * @param fromIndex マッチしたコメントのSQL全体(src)に対するインデックス
     * @return 変換結果
     */
    public String handle(String all, String body, String sample, String filename, int editLineNo, int prevEditLineNo,
            String src, int fromIndex) {
        String tBody = body.trim();
        String firstKey = body.substring(0, 1);
        SQLResultStatistics sqlStatistics = SQLResultStatistics.getInstance(filename);

        if (tBody.startsWith("IF ")) {
            //IF
            String content = "";
            sqlStatistics.incAllOfTarget(editLineNo);
            sqlStatistics.incIfSent(editLineNo);

            int whereIndex = src.lastIndexOf("WHERE", fromIndex);
            int orderByIndex = src.lastIndexOf("ORDER BY", fromIndex);
            followLogicalOp = null;

            // ORDER BYの場合、変換しない
            if (whereIndex < orderByIndex) {
                return "-- TODO ツールで変換できません  \r\n" + all;
            }

            // IF文をパースする
            Matcher macherIf = Pattern.compile(PATTERN_IF_PARSE, Pattern.DOTALL).matcher(src.substring(fromIndex));
            if (!macherIf.find()) {
                return "-- TODO ツールで変換できません  \r\n" + all;
            }
            String ifExpression = StringUtils.trim(macherIf.group(1));
            String ifBlock = StringUtils.trim(macherIf.group(2));

            // IFが囲っている範囲が1行ではない場合、変換しない
            if (ifBlock.contains("\n")) {
                String[] strs = ifBlock.split("\n");
                int t = 0;
                for (String str : strs) {
                    if ("AND".equals(str.trim()) ||
                            "OR".equals(str.trim())) {
                        t++;
                    }
                }
                int steps = ifBlock.split("\n").length-t;
                if (steps >= 2) {
                    twoStepsFlag = true;
                    return "-- TODO ツールで変換できません  \r\n" + all;
                }
            }

            // IF文の中に判断条件が二個以上の場合、変換しない
            Matcher macher1 = Pattern.compile(PATTERN_IF_01).matcher(ifExpression);
            Matcher macher2 = Pattern.compile(PATTERN_IF_02).matcher(ifExpression);
            if (macher1.matches() || macher2.matches()) {

                if(macher1.matches()) {
                    content = macher1.group(1);
                } else {
                    content = macher2.group(1);
                }
                ifFlag = true;
                // 前にAND,ORがつくパターン
                Pattern ptPreFixOp = Pattern.compile("^\\s*(AND|OR)(\\s+.*)*$",Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
                // 後ろAND,ORがつくパターン
                Pattern ptSuFixOp = Pattern.compile(".*\\s+(AND|OR)\\s*$", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);

                Matcher mptPreFixOp = ptPreFixOp.matcher(ifBlock);
                if (mptPreFixOp.matches()) {
                    return mptPreFixOp.group(1)+ "\r\n$if(" + content + ") {";

                }
                Matcher mptSuFixOp = ptSuFixOp.matcher(ifBlock);
                if (mptSuFixOp.matches()) {
                    followLogicalOp = mptSuFixOp.group(1);

                }

                return "$if(" + content + ") {";
            } else {
                //プロパティ値が存在しない場合、変換しない
                content = "-- TODO ツールで変換できません \r\n" + all;
                return content;
            }

        } else if (tBody.startsWith("BEGIN")) {
            //BEGIN
            beginFlag = true;
            sqlStatistics.incAllOfTarget(editLineNo);
            sqlStatistics.incIfSent(editLineNo);
            return "-- " + tBody;
        } else if (tBody.startsWith("END")) {
            //IF
            sqlStatistics.incAllOfTarget(editLineNo);
            sqlStatistics.incIfSent(editLineNo);
            if (ifFlag) {
                ifFlag = false;
                // 行末にAND・ORなどのキーワードが存在する場合、キーワードをIF文の下に付けます
                if(followLogicalOp != null) {
                    String content = "}" + "\r\n" +followLogicalOp;
                    followLogicalOp = null;
                    return content;
                }
                return "}";
            } else if (twoStepsFlag) {
                twoStepsFlag = false;
                return all;
            } else if (beginFlag) {
                beginFlag = false;
                return "-- " + tBody;
            } else {
                return all;
            }
        } else if (tBody.contains("---")) {
            // コメントアウト
            return "-- " + tBody;
        } else if (firstKey.matches("[ \\w]")) {
            //式コメント
            sqlStatistics.incAllOfTarget(editLineNo);
            if (!tBody.matches("[\\w]*")) {
                //未対応構文

                return all;
            }
            //式パラメータとして置換
            sqlStatistics.incSuccess_asParameter(editLineNo);
            return ":" + body.trim();
        } else if (firstKey.matches("[\\$\\%\\#\\@]")) {
            //未対応構文(リテラル置換)
            sqlStatistics.incAllOfTarget(editLineNo);
            sqlStatistics.incLeteralChg(editLineNo);
            return all;
        } else {
            //ブロックコメント
            sqlStatistics.incAllOfTarget(editLineNo);
            Pattern bc = Pattern.compile(PATTERN_BLOCK_COMMENT, Pattern.DOTALL);
            if (bc.matcher(all).matches()) {
                //完全ブロックコメント
                sqlStatistics.incSuccess_asComment(editLineNo);
                return "--" + tBody.replaceAll("\r\n", "\r\n--");
            } else {
                //不完全ブロックコメント
                return all;
            }

        }
    }

    /**
     * このディレクトリのSaveファイルを書き出す。
     * @param outPath 対象ディレクトリ（対象クラスに該当する）
     * @param sqlDataList 対象SQLファイルリスト
     */
    protected void saveFile(File outPath, List<SqlData> sqlDataList) {

        String sqlFilePath = outPath.getParent() + File.separator + outPath.getName() + ".sql";
        println(">>File:" + sqlFilePath);

        try (OutputStreamWriter osw = getOutputStreamWriter(sqlFilePath)) {

            int sqlCount = 0;
            for (SqlData sqlData : sqlDataList) {
                if (sqlCount > 0) {
                    osw.write("\r\n\r\n");
                }

                //設定ファイルに指定した改行コードが\r\nの場合、そのまま書き込む
                if (activeProfile.getLineSeparator().equals("\r\n")) {
                    osw.write(sqlData.getSqlString());
                } else {
                    //指定した改行コードが\nの場合、改行コードを\nに変換した上、書き込む
                    osw.write(sqlData.getSqlString().replaceAll("\r\n", activeProfile.getLineSeparator()));
                }
                sqlCount++;
            }
            osw.flush();
            osw.close();
        } catch (IOException ex) {
            LogUtils.warn(sqlFilePath, "書き込み失敗", ex);
            ex.printStackTrace();
        }
    }

    protected OutputStreamWriter getOutputStreamWriter(String filePath) throws IOException {
        return new OutputStreamWriter(new FileOutputStream(filePath), this.codeName);
    }

    /**
     * SqlDataにSQLIDを追加する。
     *
     * @author Fumihiko Yamamoto
     *
     */
    class SqlData {
        /** * SQLID	*/
        private String sqlId;
        /** * SQL本体 */
        private String sql;

        public SqlData(String fileName, String sqlStr) {
            int pos = fileName.indexOf(".");
            if (pos > 0) {
                this.sqlId = fileName.substring(0, pos);
            } else {
                this.sqlId = fileName;

            }
            this.sql = sqlStr;
        }

        /**
         * SQLID付きの文字列を生成する。
         * @return SQLID付きの文字列
         */
        String getSqlString() {
            Pattern p = Pattern.compile(PATTERN_FOR_INSERT_SQLNAME, Pattern.MULTILINE);
            Matcher m = p.matcher(sql);
            return m.replaceFirst(sqlId + " =\r\n$1");

        }

    }

}
