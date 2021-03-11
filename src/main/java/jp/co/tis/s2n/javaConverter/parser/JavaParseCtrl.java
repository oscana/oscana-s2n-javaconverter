package jp.co.tis.s2n.javaConverter.parser;

import java.util.List;

import jp.co.tis.s2n.javaConverter.keyword.JavaKeyword;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * パース経過保持。<br>
 * <br>
 * 以下の機能を提供する。<br>
 * ・パースの経過を保持する<br>
 * ・コンストラクタで作成されたTopノードを頂点としたツリーを構成する。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class JavaParseCtrl implements JavaKeyword {

    /**
     * TOPノード
     */
    public Node tNode = null;
    /**
     * カレント親ノード<br>
     * 現在の処理階層を意味する。新規ノードの登録はcNodeの子要素として登録される。<br>
     * indentDown, indentUpで階層を変更することができる。
     */
    public Node cNode = null;
    /**
     * 最新追加ノード<br>
     * 最後に追加されたノードを示す。<br>
     * トークンの追加はeNodeで指定されたノードに対して行われる。<br>
     * indentDown, indentUpで階層を変更しても、eNodeへの参照は変化しない。
     */
    public Node eNode = null;

    /**
     * ノードの終端とすべきでない状態であることを表すフラグ<br>
     * このフラグがfalseである場合に、ノードの開始に相当するトークンが出現したら新規ノードが作られる。
     */
    public boolean initEnd = false;

    /**
     * 改行の直後であることを表すフラグ<br>
     * このフラグがtrueである場合は、改行の直後であることを示す。
     */
    public boolean prevCRLF = false;

    /**
     * 括弧()の入れ子レベルを表すカウンタ<br>
     * kakkoCountが0の場合は括弧外であることを表す。
     */
    public int kakkoCount = 0;

    public JavaParseCtrl(String clazzName) {
        tNode = Node.create(Node.T_MODULE, clazzName);
        cNode = tNode;
        eNode = tNode;
    }

    /**
     * nodeで指定したノードをツリーに登録する。<br>
     * initEndのフラグをtrueに設定する。
     * @param node ノード
     */
    public void addNewNode(Node node) {
        this.cNode.add(node);
        this.eNode = node;
        this.initEnd = true;
    }

    /**
     * nodeで指定したノードをツリーに登録する。<br>
     * initEndのフラグを指定した値に設定する。
     * @param node ノード
     * @param initEnd ノードの終端とすべきでない状態であることを表すフラグ
     */
    public void addNewNode(Node node, boolean initEnd) {
        this.cNode.add(node);
        this.eNode = node;
        this.initEnd = initEnd;
    }

    /**
     * nodeで指定したノードをツリーに登録する。<br>
     * initEndのフラグは変更しない。
     * @param node ノード
     */
    public void addNewNodeWithoutInitEnd(Node node) {
        this.cNode.add(node);
        this.eNode = node;
    }

    /**
     * 最新追加ノード(eNode)にトークンを追加する。<br>
     * この処理ではinitEndは変更しない。
     * @param token トークン
     */
    public void addParam(Token token) {
        this.eNode.addParam(token);
    }

    /**
     * 最新追加ノード(eNode)にトークンを追加する。
     * @param token トークン
     * @param initEnd ノードの終端とすべきでない状態であることを表すフラグ
     */
    public void addParam(Token token, boolean initEnd) {
        this.eNode.addParam(token);
        this.initEnd = initEnd;
    }

    /**
     * カレント親ノード(cNode)を変更し、ツリーの処理階層を１階層下げる。<br>
     * これより後の処理は現在アクティブだったノードを親とした階層での処理となる。<br>
     * 本メソッドを新規ノードの追加をすることなく、2回以上繰り返し呼んでも何も変化は起こらない。(それ以上階層が下がることはない)
     */
    public void indentDown() {
        this.cNode = this.eNode;
    }

    /**
     * カレント親ノード(cNode)を変更し、ツリーの処理階層を1階層上げる。<br>
     * これより後の処理は親ノードと同じ階層での処理となる。<br>
     * 本メソッドを新規ノードの追加をすることなく、2回以上繰り返し呼ぶと、呼んだ回数だけ階層があがる。
     */
    public void indentUp() {
        this.cNode = this.cNode.getParent();
    }

    /**
     * doブロック内にいるかどうか判定する(Whileを見つけたときに使用する)。
     * @return 判定結果、doブロック内である場合：true、その以外：false
     */
    public boolean existDoBlock() {
        boolean bDoBlock = false;
        List<Node> nodeList = this.cNode.getChildren();
        int size = nodeList.size();
        if (size > 1) {
            Node node1 = nodeList.get(size - 2);
            if (node1.getName().equals(DO)) {
                bDoBlock = true;
            }
        }
        return bDoBlock;
    }

    /**
     * ノード内の最後のトークンを取得する。<br>
     * 最後のノードが半角スペースであればその前のトークンを返す。<br>
     * @return トークン
     */
    public Token getLastTokenWithoutSpace() {

        List<Token> tokenList = this.eNode.getParams();

        int size = tokenList.size();

        Token ret = null;

        for (int i = (size - 1); i >= 0; i--) {
            if (tokenList.get(i).getType() == Token.SPACE) {
                continue;
            } else {
                ret = tokenList.get(i);
                break;
            }
        }

        return ret;

    }

}
