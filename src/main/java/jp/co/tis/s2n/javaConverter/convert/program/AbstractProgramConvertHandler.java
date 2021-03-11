package jp.co.tis.s2n.javaConverter.convert.program;

import java.util.ArrayList;
import java.util.List;

import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.convert.statistics.S2JDBCStatistics;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * プログラム本体の書き換え用ハンドラ。<br>
 * <br>
 *
 * 本処理では、処理対象ノード（行）のトークンを前から順に確認し、完全な変換対象であることが確認できたところで、変換を行う。<br>
 * 不完全であれば変換しない。<br>
 * これを実現させるため、本処理では変換については遅延処理としている。<br>
 * <br>
 *
 * handleメソッドは1トークン単位で繰り返し呼ばれるので、handleメソッドのコーディングとして、
 * 1トークン単位ですべきことを記載する。<br>
 * 編集については遅延処理とするので、handle内で直接tokenを修正するのではなく、
 * 修正の指示を入れたUpdateCommandをcommandListに登録するだけにする。<br>
 * handleメソッド内で、変換対象として完全であると判断できた場合はtrueでreturnすること。<br>
 * <br>
 *
 * 本クラスをextendsしhandleメソッドを個別実装することで様々な変換に対応できる。<br>
 * <br>
 *
 * handleメソッドがtrueでreturnすると、それ以降のトークンは処理しない。<br>
 * commandListに登録されたコマンドを順次実行し、変換を行う。
 *
 *
 * @author Fumihiko Yamamoto
 *
 */
public abstract class AbstractProgramConvertHandler {

    /**
     * 変換元プログラムの中でjdbcManagerが宣言されている変数名
     */
    public static final String KEY_JDBC_MANAGER = "jdbcManager";

    public static final String[] KEY_LOGGERS = { "logger", "LOG" };

    public static final String KEY_CATCH_EXCEPTION = "catch";

    /**
     * 変換先となるUniversalDaoのクラス名
     */
    public static final String CN_UNIVERSALDAO = "UniversalDao";

    /**
     * トークン単位の変換。<br>
     * ※トークンやノードを直接編集するのではなく、コマンドを登録すること。
     * @param token 処理対象トークン
     * @param bracketLevel 処理対象トークンに対する括弧のレベル（判断に必要な場合に参照する）
     * @param lineNode 処理対象トークンを含む行全体を表すノード（判断に必要な場合に参照する）
     * @return 変換対象として完全であると判断できた場合true
     */
    protected abstract boolean handle(Token token, BracketLevel bracketLevel, Node lineNode);

    /**
     * クラスネームリゾルバ（ない場合もある）
     */
    protected ClassNameResolver cnr = null;

    /**
     * ハンドル内で使用するパラメータ
     */
    protected int step = 0;

    /**
     * このフラグがtrueだとhandleがtrueを返しても未サポートログだけが出力される
     */
    protected boolean notSupportNode = false;

    /**
     * ここに追記しておくと未対応である旨のログが出力される
     */
    protected StringBuffer nonSupportStatement = new StringBuffer();

    /**
     * 遅延処理をする編集コマンド
     */
    protected List<UpdateCommand> commandList = new ArrayList<>();

    public AbstractProgramConvertHandler(ClassNameResolver tcnr) {
        this.cnr = tcnr;
        init();
    }

    /**
     * 初期化処理。
     */
    public void init() {
        step = 0;
        nonSupportStatement = new StringBuffer();
        commandList = new ArrayList<>();
    }

    /**
     * 変換主処理。
     * @param lineNode 処理対象トークンを含む行全体を表すノード（判断に必要な場合に参照する）
     * @param line String型の行ソース
     * @param fileName ファイル名
     * @return マッチした結果
     */
    public boolean convert(Node lineNode, String line, String fileName) {

        // 元々のソースをstringの形で保存しておく
        String lineNodeString = lineNode.getString();

        boolean matched = false;
        this.notSupportNode = false;

        BracketLevel bracketLevel = new BracketLevel();
        for (Token token : lineNode.getAllTokens()) {
            if (token == null) {
                continue;
            }
            switch (token.getText()) {
            case "(":
                bracketLevel.parenthesisLevel++;
                break;
            case ")":
                bracketLevel.parenthesisLevel--;
                break;
            case "{":
                bracketLevel.curlyBrachetLevel++;
                break;
            case "}":
                bracketLevel.curlyBrachetLevel--;
                break;
            case "[":
                bracketLevel.squareBracketLevel++;
                break;
            case "]":
                bracketLevel.squareBracketLevel--;
                break;
            default:
                break;
            }
            boolean ret = handle(token, bracketLevel, lineNode);
            if (ret) {
                matched = true;
                break;
            }
        }

        if (matched) {
            //コマンド実行
            if (!this.notSupportNode) {
                for (UpdateCommand updateCommand : commandList) {
                    updateCommand.execute(lineNode);
                }
                if (nonSupportStatement.length() > 0) {

                    // 未変換箇所はログとしてソースファイルに出力する
                    int pos = lineNode.getParent().getChildren().indexOf(lineNode);

                    // 未変換箇所ログを出力する
                    NodeUtil.addChildNode(lineNode.getParent(), pos,
                            Node.create(Node.T_COMMENT1, Node.NO_SUPPORT_LOG + " : " + nonSupportStatement.toString()));

                    // 未サポートのソースをそのまま出力する
                    NodeUtil.addChildNode(lineNode.getParent(), pos + 1,
                            Node.create(Node.T_COMMENT1, lineNodeString));
                    NodeUtil.removeChildNode(lineNode);

                    S2JDBCStatistics.getInstance().convertedAnnotation(fileName, this.getClass().getName(),
                            S2JDBCStatistics.RESULT_NOTSUPPORTED, line, nonSupportStatement.toString());
                } else {
                    S2JDBCStatistics.getInstance().convertedAnnotation(fileName, this.getClass().getName(),
                            S2JDBCStatistics.RESULT_SUCCESS, line, null);
                }
            } else {
                //未サポートノード
                S2JDBCStatistics.getInstance().convertedAnnotation(fileName, this.getClass().getName(),
                        S2JDBCStatistics.RESULT_NOTSUPPORTED, line, nonSupportStatement.toString());

            }
        }

        return matched;

    }

    /**
     * コマンドを登録する。
     * @param command 登録対象コマンド
     */
    protected void addCommand(UpdateCommand command) {
        this.commandList.add(command);
    }

    /**
     * 想定的なレベルチェックを行う。<br>
     * 任意の場所でresetすることで相対的にどうであるかを判断できる。
     * @author Fumihiko Yamamoto
     *
     */
    class BracketLevel {
        //()のレベル
        int parenthesisLevel;
        //{}のレベル
        int curlyBrachetLevel;
        //[]のレベル
        int squareBracketLevel;

        /**
         * 今いる階層をベースにするためにレベルをリセットする。
         */
        public void reset() {
            parenthesisLevel = 0;
            curlyBrachetLevel = 0;
            squareBracketLevel = 0;
        }

        /**
         * ()のレベルが指定されたレベルで、他の括弧のレベルは０であるかどうかを調べる。
         * @param level レベル
         * @return レベルは０であるかどうかの結果
         */
        public boolean isInParenthesisLevel(int level) {
            if ((parenthesisLevel == level) && (curlyBrachetLevel == 0) && (squareBracketLevel == 0)) {
                return true;
            } else {
                return false;
            }
        }

    }

    //  -----------------------------------------
    //  以下、編集コマンド
    //  -----------------------------------------

    /**
     * 抽出が成立した場合に実施する処理の定義（遅延処理）。
     * @author Fumihiko Yamamoto
     *
     */
    abstract class UpdateCommand {
        Token target;

        public UpdateCommand(Token target) {
            super();
            this.target = target;
        }

        /**
         * 文字列を取得。
         * @return 文字列
         */
        public abstract String getString();

        /**
         * 処理の入り口。
         * @param lineNode 処理対象トークンを含む行全体を表すノード
         */
        public abstract void execute(Node lineNode);
    }

    /**
     * このトークンのテキストをchangeToに書き換えるコマンド。
     * @author Fumihiko Yamamoto
     *
     */
    class ChangeToTxtCommand extends UpdateCommand {
        String changeTo;

        public ChangeToTxtCommand(Token target, String changeTo) {
            super(target);
            this.changeTo = changeTo;
        }

        /**
         * 処理の入り口。
         * @param lineNode 処理対象トークンを含む行全体を表すノード
         */
        @Override
        public void execute(Node lineNode) {
            if (target.equals(lineNode.getHead())) {
                lineNode.setName(changeTo);
            } else {
                target.setText(changeTo);
            }
        }

        /**
         * 文字列を取得。
         * @return 文字列
         */
        @Override
        public String getString() {
            return "ChangeToTxt:" + changeTo;

        }

    }

    /**
     * このトークンを削除するコマンド。
     * @author Fumihiko Yamamoto
     *
     */
    class DeleteCommand extends UpdateCommand {
        public DeleteCommand(Token target) {
            super(target);
        }

        /**
         * 処理の入り口。
         * @param lineNode 処理対象トークンを含む行全体を表すノード
         */
        @Override
        public void execute(Node lineNode) {
            lineNode.getParams().remove(target);
        }

        /**
         * 文字列を取得。
         * @return 文字列
         */
        @Override
        public String getString() {
            return "Delete:";
        }

    }

    /**
     * トークンの直前に新規トークンを挿入するコマンド
     * @author Fumihiko Yamamoto
     *
     */
    class InsertBeforeCommand extends UpdateCommand {

        private String idKey;

        public InsertBeforeCommand(Token target, String idKey) {
            super(target);
            this.idKey = idKey;
        }

        /**
         * 処理の入り口。
         * @param lineNode 処理対象トークンを含む行全体を表すノード
         */
        @Override
        public void execute(Node lineNode) {
            int pos = lineNode.getParams().indexOf(this.target);
            lineNode.addParamPos(pos, new Token(Token.NAME, idKey));
        }

        /**
         * 新規トークンの文字列を取得。
         * @return 文字列
         */
        @Override
        public String getString() {
            return "InsertBefore:" + idKey;
        }

    }

    /**
     * トークンの直後に新規トークンを挿入するコマンド。
     * @author Fumihiko Yamamoto
     *
     */
    class InsertAfterCommand extends UpdateCommand {

        private String idKey;

        public InsertAfterCommand(Token target, String idKey) {
            super(target);
            this.idKey = idKey;
        }

        /**
         * 処理の入り口。
         * @param lineNode 処理対象トークンを含む行全体を表すノード
         */
        @Override
        public void execute(Node lineNode) {
            int pos = lineNode.getParams().indexOf(this.target);
            lineNode.addParamPos(pos + 1, new Token(Token.NAME, idKey));
        }

        /**
         * 新規トークンの文字列を取得。
         * @return 文字列
         */
        @Override
        public String getString() {
            return "InsertAfter:" + idKey;
        }

    }

}
