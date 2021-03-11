package jp.co.tis.s2n.javaConverter.node;

import java.util.ArrayList;
import java.util.List;

import jp.co.tis.s2n.javaConverter.keyword.JavaKeyword;
import jp.co.tis.s2n.javaConverter.token.Token;


/**
 * ノードクラス。<br>
 *
 * javaファイルをノード単位で読み込んで、変換を実施する。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class Node implements JavaKeyword {

    /** * TOPノード  */
    public final static int T_MODULE = 1;

    /** * class宣言 */
    public final static int T_CLASS = 2;
    /** * 一般ノード */
    public final static int T_NORMAL = 4;
    /** * 行コメント単体のノード  */
    public final static int T_COMMENT1 = 8;
    /** * ブロックコメント単体のノード */
    public final static int T_COMMENT2 = 16;
    /** * ブロックを表すノード */
    public final static int T_BLOCK = 32;
    /** * フィールド宣言 */
    public final static int T_MEMBER = 64;
    /** * パケージ文 */
    public final static int T_PACKAGE = 128;
    /** * import文 */
    public final static int T_IMPORT = 256;
    /** * return文 */
    public final static int T_RETURN = 512;
    /** * アノテーション(@ではじまる) */
    public final static int T_ANNOTATION = 1024;
    /** * throw文 */
    public final static int T_THROW = 2048;
    /** * interface宣言 */
    public final static int T_INTERFACE = 4096;
    /** * enum宣言 */
    public final static int T_ENUM = 8192;
    /** * アノテーション宣言*/
    public final static int T_ANNOTATIONDEF = 16384;

    private int type = T_NORMAL;
    //ノードのテキスト（headがあればhead.textと同じ）
    private String name = null;;
    //１つ目のトークン
    private Token head = null;

    private List<Token> tokenList = new ArrayList<>();

    private List<Node> childNodeList = new ArrayList<>();

    private Node parent = null;

    private int lineNo = 0;

    @SuppressWarnings("unused")
    private int depth = 0;

    public final static String NO_SUPPORT_LOG = "// TODO ツールで変換できません";

    public final static String NO_SUPPORT_COMBINATION = "// TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照";

    private Node(int type, String name) {
        this.head = null;
        this.type = type;
        this.name = name;
    }

    private Node(Token token) {
        this.head = token;
        this.type = Node.T_NORMAL;
        this.name = token.getText();
    }

    private Node(int type, Token token) {
        this.head = token;
        this.type = type;
        this.name = token.getText();
    }

    /**
     * テキストとノードタイプを指定してノードを作成する。<br>
     * このメソッドはトークンを作らない。
     * @param type ノードタイプ
     * @param name ノードのテキスト
     * @return ノード
     */
    public static Node create(int type, String name) {
        return new Node(type, name);
    }

    /**
     * 指定したトークンを１つ目のトークンに持つノードを作成する。<br>
     * タイプは一般ノード（T_NORMAL）が設定される。
     * @param token １つ目のトークン
     * @return ノード
     */
    public static Node create(Token token) {
        return new Node(token);
    }

    /**
     * 指定したトークンを１つ目のトークンに持つノードをノードタイプを指定して作成する。
     * @param type ノードタイプ
     * @param token １つ目のトークン
     * @return ノード
     */
    public static Node create(int type, Token token) {
        return new Node(type, token);
    }

    /**
     * ノードタイプを返す。
     * @return ノードタイプ
     */
    public int getType() {
        return type;
    }

    /**
     * ノードのテキストを取得する。<br>
     * １つ目のトークンがあれば１つ目のトークンのテキストを取得する。
     * @return ノードのテキスト
     */
    public String getName() {
        return name;
    }

    /**
     * ノードのテキストを変更する。<br>
     * １つ目のトークンがあれば１つ目のトークンのテキストも連動して変更する。
     * @param name ノードのテキスト
     */
    public void setName(String name) {
        this.name = name;
        this.head.setText(name);
    }

    /**
     * ノードタイプを変更する。
     * @param type ノードタイプ
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * １つ目のトークンを返す。
     * @return １つ目のトークン
     */
    public Token getHead() {
        return head;
    }

    /**
     * １つ目のトークン以降のトークンを返す。<br>
     * 1つ目のトークンは含まない。<br>
     * ノード全体のトークンが必要な場合はgetAllTokensを使用する。
     * @return トークンリスト
     * @see Node#getAllTokens()
     */
    public List<Token> getParams() {
        return tokenList;
    }

    /**
     * 指定したテキストを持つトークンの位置を返す。<br>
     * 検索対象に1つ目のトークンは含まない。
     * @param key 検索キー
     * @return 見つかればそのTokenのインデックス、見つからなければ-1を返す
     */
    public int getPosOfParams(String key) {

        int pos1 = -1;
        int count = 0;
        for (Token token : tokenList) {
            if (token.getText().equals(key)) {
                pos1 = count;
            }
            count++;
        }
        return pos1;

    }

    /**
     * すべてのトークンを返す。<br>
     * 1つ目のトークン(head)と2つ目以降のトークン(tokenList)は可能場所が異なっているが、<br>
     * 本メソッドは両方から収集し、すべてのトークンを返す。<br>
     * 1つ目のトークン(head)を含めなくてよい場合はgetParamを使用する。
     * @return トークンリスト
     * @see Node#getParams()
     */
    public List<Token> getAllTokens() {

        List<Token> tokens = new ArrayList<>();
        tokens.add(head);
        for (Token token : tokenList) {
            tokens.add(token);
        }
        return tokens;

    }

    /**
     * キーを利用してトークンのテキストを返す。
     * @param key1 キー
     * @param key2 キー
     * @return トークンのテキスト
     */
    public String getParamByKey(String key1, String key2) {
        int no = 1;
        boolean bFind = false;
        boolean arg0Flg = false;
        for (Token token : tokenList) {
            if (token.getText().equals(key2)) {
                arg0Flg = true;
            }
            if (token.getText().equals(key1)) {
                bFind = true;
                if (arg0Flg == true) {
                    break;
                }

            }
            no++;
        }
        if (bFind && arg0Flg) {
            if (!"=".equals(tokenList.get(no + 1).getText())) {
                // arg0=@Arg(key="labels.xxx")
                return tokenList.get(no + 1).getText();
            } else {
                // arg0 = @Arg(key = "labels.xxx")
                return tokenList.get(no + 3).getText();
            }

        } else {
            return null;
        }
    }

    /**
     * このノードの子要素リストを返す。
     * @return ノードリスト
     */
    public List<Node> getChildren() {
        return childNodeList;
    }

    /**
     * このノードの親要素を返す。
     * @return ノード
     */
    public Node getParent() {
        return parent;
    }

    /**
     * 深度を設定する。
     * @param depth 深度
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * 行番号を取得する。
     * @return 行番号
     */
    public int getLineNo() {
        return this.lineNo;
    }

    /**
     * 行番号を設定する。
     * @param lineNo 行番号
     */
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    /**
     * このノード(this)の親ノードを設定する。
     * @param parent このノード(this)の親となるノード
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * このノード(this)に子要素を追加する。<br>
     * 子要素はリストで管理され、順番が保持される。
     * @param node 子要素
     */
    public void add(Node node) {
        childNodeList.add(node);
        node.setParent(this);
    }

    /**
     * このノード(this)にトークンを追加する。
     * @param token トークン
     */
    public void addParam(Token token) {
        this.tokenList.add(token);
        if (token.getText().equals(CLASS)) {
            this.type = T_CLASS;
        } else if (token.getText().equals(INTERFACE)) {
            this.type = T_INTERFACE;
        } else if (token.getText().equals(ENUM)) {
            this.type = T_ENUM;
        } else if (token.getText().equals(ANNOTATIONDEF)) {
            this.type = T_ANNOTATIONDEF;
        }
    }

    /**
     * このノードの指定位置にトークンを挿入する。
     * @param pos 指定位置
     * @param token トークン
     */
    public void addParamPos(int pos, Token token) {
        this.tokenList.add(pos, token);
        if (token.getText().equals(CLASS)) {
            this.type = T_CLASS;
        } else if (token.getText().equals(INTERFACE)) {
            this.type = T_INTERFACE;
        } else if (token.getText().equals(ENUM)) {
            this.type = T_ENUM;
        } else if (token.getText().equals(ANNOTATIONDEF)) {
            this.type = T_ANNOTATIONDEF;
        }
    }

    /**
     * ノードの末尾に空白行を追加する。
     */
    public void addCrLf() {
        this.add(Node.create(Node.T_NORMAL, "XenCrLf"));
    }

    /**
     * Nodeの中身を文字列で応答する。
     * @return Nodeの中身（タイプは出力しない）
     */
    public String getString() {

        StringBuilder sb = new StringBuilder();

        sb.append(this.name);

        for (Token token : this.tokenList) {
            sb.append(token.getText());
        }

        return sb.toString();

    }

    /**
     * ノードの中身を改行せずに文字列形式で取得する。
     * @return ノードの文字列
     */
    public String getStringWithoutCRLF() {

        StringBuilder sb = new StringBuilder();
        int nodeType = (head != null) ? head.getType() : this.getType();
        String nodeText = (head != null) ? head.getText() : this.getName();
        if (nodeType != Token.CRLF) {
            if (nodeType == Token.SPACE) {
                sb.append(" ");
            } else {
                sb.append(nodeText);
            }
        }

        for (Token token : this.tokenList) {
            if (token.getType() != Token.CRLF) {
                if (token.getType() == Token.SPACE) {
                    sb.append(" ");
                } else if (token.getType() == Token.NAME) {
                    if (token.getText().equals("property")) {
                        sb.append("name");
                    } else {
                        sb.append(token.getText());
                    }
                } else {
                    sb.append(token.getText());
                }
            }
        }

        return sb.toString();

    }

    /**
     * １つ目のトークン(head)以外のトークンについて、テキスト置換を実施する。<br>
     * 1つ目のトークン(head)は置換しない。
     * @param strFrom 変換元
     * @param strTo 変換先
     */
    public void replaceParams(String strFrom, String strTo) {

        for (Token token : this.tokenList) {
            if (token.getText().equals(strFrom)) {
                token.setText(strTo);
            }
        }

    }

}
