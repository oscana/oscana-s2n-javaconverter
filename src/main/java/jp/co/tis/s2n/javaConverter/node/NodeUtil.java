package jp.co.tis.s2n.javaConverter.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.tis.s2n.javaConverter.file.S2nFileWriter;
import jp.co.tis.s2n.javaConverter.keyword.JavaKeyword;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * ノートユーティリティクラス。
 *
 * @author Fumihiko Yamamoto
 */
public class NodeUtil implements JavaKeyword {

    public static final String SP4 = "    ";
    public static final String SP8 = SP4 + SP4;

    public static int T_CLASS = 0;
    public static int T_METHOD = 1;
    public static int T_FIELD = 2;

    /**
     * Nodeタイプが一般ノード(T_NORMAL)となっているもののうち、以下にすべきもののNodeタイプを変更する。<br>
     * T_BLOCK, T_IMPORT, T_PACKAGE, T_RETURN, T_THROW, T_ANNOTATION。
     * @param node ノード
     */
    public static void coordinate(Node node) {

        if (node.getType() == Node.T_NORMAL) {
            if (node.getName().equals(IF) || node.getName().equals(CASE)
                    || node.getName().equals(DEFAULT) || node.getName().equals(BREAK)
                    || node.getName().startsWith("}")) {
                //名前がIF, CASE, DEFAULT, BREAKに一致するか、}で開始する
                node.setType(Node.T_BLOCK);
            } else if (node.getName().equals(IMPORT)) {
                //名前がIMPORTに一致する
                node.setType(Node.T_IMPORT);
            } else if (node.getName().equals(PACKAGE)) {
                //名前がPACKAGEに一致する
                node.setType(Node.T_PACKAGE);
            } else if (node.getName().equals(RETURN)) {
                //名前がRETURNに一致sる
                node.setType(Node.T_RETURN);
            } else if (node.getName().equals(THROW)) {
                //名前がTHROWに一致する
                node.setType(Node.T_THROW);
            } else if (node.getName().startsWith("@")) {
                //名前が@で開始する
                node.setType(Node.T_ANNOTATION);
            } else {
                String str = node.getString();
                if (str.endsWith("{")) {
                    //ノード全体が } で終了する
                    node.setType(Node.T_BLOCK);
                } else if ((node.getName().equals(PUBLIC) || node.getName().equals(PROTECTED)
                        || node.getName().equals(PRIVATE))
                        && str.endsWith(";")) {
                    //名前がPUBLIC, PROTECTED, PRIVATEのいずれかであり、";"で終了する
                    //XXX 無印のフィールドがT_NORMALとなるのは正しい？
                    node.setType(Node.T_MEMBER);
                }
            }
        }

        for (Node childNode : node.getChildren()) {
            coordinate(childNode);
        }

    }

    /**
     * ノードの行番号をカウントし、ノードに付与する。
     * @param node ノード
     */
    public static void calcLineNumber(Node node) {

        int count = 1;

        for (Node childNode : node.getChildren()) {
            count = calcLineNumberSub(childNode, count);
        }

    }

    private static int calcLineNumberSub(Node node, int count) {

        node.setLineNo(count);

        if (node.getType() == Node.T_COMMENT2) {
            String[] sa = node.getString().split("\r\n");
            count += sa.length;
        } else {
            count++;
        }

        for (Node childNode : node.getChildren()) {
            count = calcLineNumberSub(childNode, count);
        }

        return count;

    }

    /**
     * ノードの階層を調べて、各ノードに階層番号(depth)を付与する。
     * @param node ノード
     */
    public static void calcDepth(Node node) {

        int count = 0;
        node.setDepth(count);
        for (Node childNode : node.getChildren()) {
            calcDepthSub(childNode, count + 1);
        }

    }

    private static void calcDepthSub(Node node, int count) {

        node.setDepth(count);
        for (Node childNode : node.getChildren()) {
            calcDepthSub(childNode, count + 1);
        }

    }

    /**
     * ノードに含まれる半角空白を除去する。
     * @param node ノード
     */
    public static void cleanAll(Node node) {

        for (;;) {
            int size = node.getParams().size();
            if (size > 0) {
                Token token = node.getParams().get(size - 1);
                if (token.getType() == Token.SPACE) {
                    node.getParams().remove(size - 1);
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        for (Node childNode : node.getChildren()) {
            cleanAll(childNode);
        }

    }

    /**
     * 指定したノードをファイル出力用に変換する。
     * @param fw ファイル出力
     * @param node ノード
     * @throws IOException 例外
     */
    public static void fprintAll(S2nFileWriter fw, Node node) throws IOException {
        //        debugAll(node); //詳細ログ
        if (node.getType() != Node.T_MODULE) {
            if (node.getName().equals("XenCrLf")) {
                fw.fprintln();
                return;
            }
            fw.fprint(node.getName());
            for (Token token : node.getParams()) {
                fw.fprint(token.getText());
            }
            fw.fprintln();
        }
        for (Node node1 : node.getChildren()) {
            fprintAllSub(fw, node1, 0);
        }

    }

    private static void fprintAllSub(S2nFileWriter fw, Node node, int depth) throws IOException {

        if (node.getName().equals("XenCrLf")) {
            fw.fprintln();
            return;
        }

        if (node.getName().startsWith("/*") == true) {
            String[] sa = node.getName().split("\r\n");
            for (int i1 = 0; i1 < sa.length; i1++) {
                for (int i2 = 0; i2 < depth; i2++) {
                    fw.fprint("    ");
                }
                if (i1 > 0) {
                    fw.fprint(" ");
                }
                fw.fprintln(sa[i1]);
            }
        } else {
            for (int i = 0; i < depth; i++) {
                fw.fprint("    ");
            }
            fw.fprint(node.getName());
            for (Token token : node.getParams()) {
                fw.fprint(token.getText());
            }
            fw.fprintln();
        }

        depth++;
        for (Node node1 : node.getChildren()) {
            fprintAllSub(fw, node1, depth);
        }

    }

    /**
     * すべてのノードをリストに投入して返す。<br>
     * 空行ノードはスキップする。<br>
     * topNode自身は返さない。topNodeの子要素以下を再帰的に走査してリストにつめて返す。
     * @param topNode トップノード
     * @return ノードリスト
     */
    public static List<Node> findAllNode(Node topNode) {

        List<Node> findList = new ArrayList<>();
        findAllNode(topNode, findList);
        return findList;

    }

    private static void findAllNode(Node node, List<Node> findList) {

        if (node.getName().equals("XenCrLf") == false) {
            findList.add(node);
        }

        for (Node e : node.getChildren()) {
            findAllNode(e, findList);
        }

    }

    /**
     * 指定したノードタイプに該当するノードをリストに投入して返す。<br>
     * 空行ノードはスキップする。<br>
     * topNode自身は返さない。topNodeの子要素以下を再帰的に走査してリストにつめて返す。<br>
     * typeは、複数のノードタイプを論理和でつなげたものを指定するとOR検索が可能。
     * @param topNode トップノード
     * @param type 指定したノードタイプ（複数のノードタイプの論理和を設定するといずれかに該当するものを返す）
     * @return ノードリスト
     */
    public static List<Node> findAllNode(Node topNode, int type) {

        List<Node> findList = new ArrayList<>();
        findAllNode(topNode, type, findList);
        return findList;

    }

    private static void findAllNode(Node node, int type, List<Node> findList) {

        if ((node.getType() & type) > 0) {
            findList.add(node);
        }
        for (Node e : node.getChildren()) {
            findAllNode(e, type, findList);
        }

    }

    /**
     * 指定した文字列のトークンを持つノードをリストに投入して返す。<br>
     * 空行ノードはスキップする。<br>
     * keyで指定した文字列が、ノードのテキストに一致するか、そのノードに含まれるいずれかのトークンのテキストに一致する場合にリストに投入して返す。<br>
     * topNode自身は返さない。topNodeの子要素以下を再帰的に走査してリストにつめて返す。<br>
     * @param topNode トップノード
     * @param key 検索キー
     * @return ノードリスト
     */
    public static List<Node> findAllNode(Node topNode, String key) {

        List<Node> findList = new ArrayList<>();
        findAllNode(topNode, key, findList);
        return findList;

    }

    private static void findAllNode(Node node, String key, List<Node> findList) {

        if (node.getName().equals(key)) {
            findList.add(node);
        } else {
            for (Token token : node.getParams()) {
                if (token.getText().equals(key)) {
                    findList.add(node);
                    break;
                }
            }
        }

        for (Node e : node.getChildren()) {
            findAllNode(e, key, findList);
        }

    }

    /**
     * このクラス（ファイル）のパッケージを返す。<br>
     * パッケージ文のノードをさがし、そのテキストを応答する。
     * @param topNode ノードリスト
     * @return パッケージ名
     */
    public static String getPackageName(Node topNode) {
        List<Node> list1 = new ArrayList<>();
        findAllNode(topNode, Node.T_PACKAGE, list1);
        String packageName = list1.get(0).getParams().get(1).getText();
        return packageName;
    }

    /**
     * このクラス（ファイル）のクラス名を取得する。<br>
     * クラス宣言のノードをさがし、そのテキストを応答する。<br>
     * １つのファイルに複数のクラスが記述されている（インナークラスなど）場合は最初に見つかったクラス名を返す。
     * @param topNode トップノード
     * @return クラス名
     */
    public static String getClassName(Node topNode) {

        List<Node> nodeList = new ArrayList<>();

        findAllNode(topNode, Node.T_CLASS, nodeList);
        for (Node node : nodeList) {
            String className = null;
            for (int i = 0; i < node.getParams().size(); i++) {
                Token token = node.getParams().get(i);
                if (token.getText().equals("class")) {
                    i++;
                    i++;
                    className = node.getParams().get(i).getText();
                    return className;
                }
            }
        }

        return null;

    }

    /**
     * 抽象クラスであるかを判定する。
     * @param topNode トップノード
     * @return 判定結果、抽象クラスの場合：ture、それ以外：false
     */
    public static boolean isAbstractClass(Node topNode) {

        List<Node> nodeList = new ArrayList<>();
        boolean isAbstract = false;

        findAllNode(topNode, Node.T_CLASS, nodeList);
        for (Node node : nodeList) {
            for (int i = 0; i < node.getParams().size(); i++) {
                Token token = node.getParams().get(i);
                if (token.getText().equals("class")) {
                    i++;
                    i++;
                    return isAbstract;
                } else {
                    if (token.getText().equals("abstract")) {
                        isAbstract = true;
                    }
                }
            }
        }
        //クラスではない
        return false;

    }

    /**
     * アノテーションが付与されているメンバ・メソッドを調べる。
     * @param node ノード
     * @return アノテーションが付与されているメンバ・メソッドのノード
     */
    public static Node searchNodeForAnnotation(Node node) {

        // 親ノードの取得
        Node parent = node.getParent();
        // クラスノードの位置を取得
        int pos = parent.getChildren().indexOf(node);
        Node methodNode = null;
        for (int i = pos; i < parent.getChildren().size(); i++) {
            Node node1 = parent.getChildren().get(i);
            if ((node1.getType() == Node.T_BLOCK || node1.getType() == Node.T_MEMBER
                    || node1.getType() == Node.T_NORMAL) && (!"XenCrLf".equals(node1.getName()))) {
                methodNode = node1;
                break;
            }
        }
        return methodNode;

    }

    /**
     * 指定されたアノテーションの付与対象のノード（クラス、メソッド、メンバ）を抽出する。
     * @param annotationNode アノテーションノード
     * @return アノテーションノードで指定されたアノテーションに付与対象となっているノード
     */
    public static Node searchNodeForAnnotationEx(Node annotationNode) {

        // 親ノードの取得
        Node parent = annotationNode.getParent();
        // クラスノードの位置を取得
        int pos = parent.getChildren().indexOf(annotationNode);
        Node methodNode = null;
        for (int i = pos; i < parent.getChildren().size(); i++) {
            Node node1 = parent.getChildren().get(i);
            if (node1.getType() == Node.T_CLASS || node1.getType() == Node.T_BLOCK || node1.getType() == Node.T_MEMBER
                    || node1.getType() == Node.T_NORMAL) {
                methodNode = node1;
                break;
            }
        }
        return methodNode;

    }

    /**
     * 指定したノード（メソッド、メンバなど）に付与されたアノテーションのリストを返す。
     * @param node ノード
     * @return アノテーションノードリスト
     */
    public static List<Node> getAnnotationList(Node node) {

        Node parent = node.getParent();
        int no = parent.getChildren().indexOf(node);
        List<Node> nodeList = new ArrayList<>();
        int count = 1;
        for (;;) {
            int index = no - count;
            if(index < 0) {break;}
            Node prev = parent.getChildren().get(index);

            // アノテーションとフィールドの間に空行がある場合でも、変換できるように
            if (prev.getName().startsWith("@") == false && !prev.getName().equals("XenCrLf")) {
                break;
            }

            // 空行を除く
            if(!prev.getName().equals("XenCrLf")) {
            	nodeList.add(prev);
            }

            count++;
        }
        return nodeList;

    }

    /**
     * コメントが書いたノードリストを返す。
     * @param node ノード
     * @return ノードリスト
     */
    public static List<Node> getCommentList(Node node) {

        Node parent = node.getParent();
        int no = parent.getChildren().indexOf(node);
        List<Node> nodeList = new ArrayList<>();
        int count = 1;
        for (;;) {
            if ((no - count) < 0) {
                break;
            }
            Node prev = parent.getChildren().get(no - count);
            if (prev.getString().endsWith(";") == true) {
                break;
            }
            if (prev.getType() == Node.T_COMMENT1 || prev.getType() == Node.T_COMMENT2) {
                nodeList.add(prev);
            }
            count++;
        }
        return nodeList;

    }

    /**
     * 指定したアノテーションやコメントが付与されているフィールドを取得する。
     * @param node アノテーション、コメントなどフィールドの直前についているノード
     * @return nodeからみて最初に出現したフィールドを返す
     */
    public static Node getField(Node node) {

        Node parent = node.getParent();
        int no = parent.getChildren().indexOf(node);
        int count = 1;
        for (;;) {
            if ((no + count) > parent.getChildren().size()) {
                break;
            }
            Node prev = parent.getChildren().get(no + count - 1);
            if (prev.getString().endsWith(";") == true) {
                return prev;
            }
            count++;
        }
        return null;

    }

    /**
     * コメント、改行を除去する。
     * @param node ノード
     */
    public static void clearCrLfAndComment1(Node node) {

        List<Token> tokenList = node.getParams();
        for (int i = 0; i < tokenList.size(); i++) {
            Token token = tokenList.get(i);
            String text = token.getText();
            int type = token.getType();
            if (text.equals("XenCrLf")) {
                tokenList.remove(i);
                i--;
            } else if (type == Token.COMMENT1) {
                if (i < tokenList.size() - 1) { //行途中のコメントはブロックコメントに変換
                    token.setType(Token.COMMENT2);
                    token.setText("/*" + token.getText().substring(2) + "*/");
                }
            } else if (type == Token.CRLF) {
                if ((i - 1) > -1 && tokenList.get(i - 1).getText().equals("(")) {
                    tokenList.remove(i);
                    i--;
                } else if ((i + 1) < tokenList.size() && tokenList.get(i + 1).getText().equals(")")) {
                    tokenList.remove(i);
                    i--;
                } else if ((i + 1) < tokenList.size() && tokenList.get(i + 1).getText().equals(".")) {
                    tokenList.remove(i);
                    i--;
                } else {
                    token.setType(Token.SPACE);
                    token.setText(" ");
                }
            }
        }

        for (Node childNode : node.getChildren()) {
            clearCrLfAndComment1(childNode);
        }

    }

    /**
     * ノード（メンバ行）からメンバ名を取得する。<br>
     * "="や";"の直前にあるNAMEトークンをメンバ名とみなす。
     * @param node フィールド宣言のノード
     * @return トークンのテキスト
     */
    public static String getMemberName(Node node) {
        List<Token> tokens = node.getAllTokens();
        Token prevNameToken = null;
        for (Iterator<Token> iterator = tokens.iterator(); iterator.hasNext();) {
            // "="や";"が見つかった場合はその直前のNAMEトークン
            Token token = iterator.next();
            if ((token.getType() == Token.EQ1) || (token.getType() == Token.EQ2)
                    || (token.getType() == Token.SEMICOLON)) {
                return prevNameToken.getText();
            }
            if (token.getType() == Token.NAME) {
                prevNameToken = token;
            }
        }
        //文末まで来た場合は異常
        return null;
    }

    /**
     * ブロックからメソッド名を取得する。<br>
     * "("の直前にあるNAMEトークンをメソッド名とみなす。
     * @param node ブロックを表すノード
     * @return トークンのテキスト
     */
    public static String getBlockName(Node node) {
        List<Token> tokens = node.getAllTokens();
        Token prevNameToken = null;
        for (Iterator<Token> iterator = tokens.iterator(); iterator.hasNext();) {
            // "("が見つかった場合はその直前のNAMEトークン
            Token token = iterator.next();
            if ((token.getType() == Token.SYMBOL) && (token.getText().equals("("))) {
                return prevNameToken.getText();
            }
            if (token.getType() == Token.NAME) {
                prevNameToken = token;
            }
        }
        //文末まで来た場合は異常
        return null;
    }

    /**
     * 指定した親ノードの指定した位置に子ノードを追加する。<br>
     * 親→子、子→親の相互リンクを形成する。<br>
     * @param parent 追加する親ノード
     * @param pos 追加位置インデックス
     * @param trgNode 追加する子ノード
     */
    public static void addChildNode(Node parent, int pos, Node trgNode) {
        parent.getChildren().add(pos, trgNode);
        trgNode.setParent(parent);
    }

    /**
     * 指定したノードを親ノードから切り離す。<br>
     * 親ノードはtrgNode.getParentで確認する。<br>
     * 親→子、子→親の相互リンクを削除する。
     * @param trgNode 削除するノード
     */
    public static void removeChildNode(Node trgNode) {
        trgNode.getParent().getChildren().remove(trgNode);
        trgNode.setParent(null);

    }

    /**
     * クラスノードからimplementsされたクラスの一覧を取得する。<br>
     * @param classNode クラスノード
     * @return ソースにされていたものをそのまま返す（完全修飾クラス名のこともあるし、そうでない場合もある）
     */
    public static List<String> getImplements(Node classNode) {
        List<String> implementsList = new ArrayList<>();
        boolean foundImplements = false;
        for (Token cTk : classNode.getAllTokens()) {
            if ((cTk.getType() == Token.NAME) && (cTk.getText().equals("implements"))) {
                foundImplements = true;
            } else if (cTk.getType() == Token.NAME) {
                if (foundImplements) {
                    implementsList.add(cTk.getText());
                }
            } else if ((cTk.getType() == Token.SYMBOL) && (cTk.getText() == "{")) {
                break;
            }
        }
        return implementsList;
    }

    /**
     * implementsリストに指定のインターフェースを追加する。
     * @param classNode クラスノード
     * @param interfaceName インターフェース名
     */
    public static void addImplements(Node classNode, String interfaceName) {
        boolean foundImplements = false;
        List<Token> tokens = classNode.getAllTokens();
        for (int i = 0; i < tokens.size(); i++) {
            Token cTk = tokens.get(i);
            if ((cTk.getType() == Token.NAME) && (cTk.getText().equals("implements"))) {
                foundImplements = true;
            } else if ((cTk.getType() == Token.NAME) && (cTk.getText().equals("extends"))) {
                if (foundImplements) {
                    //追加※この操作をやったあとはtokensと実際の整合が取れなくなるのでこれ以上回さないこと
                    classNode.addParamPos(i - 1, new Token(Token.SYMBOL, ","));
                    classNode.addParamPos(i, new Token(Token.NAME, interfaceName));
                    classNode.addParamPos(i + 1, new Token(Token.SPACE, " "));
                    break;
                }
            } else if (cTk.getType() == Token.NAME) {
            } else if ((cTk.getType() == Token.SYMBOL) && (cTk.getText().equals("{"))) {
                if (foundImplements) {
                    //追加※この操作をやったあとはtokensと実際の整合が取れなくなるのでこれ以上回さないこと
                    classNode.addParamPos(i - 1, new Token(Token.SYMBOL, ","));
                    classNode.addParamPos(i, new Token(Token.NAME, interfaceName));
                    break;
                } else {
                    //追加※この操作をやったあとはtokensと実際の整合が取れなくなるのでこれ以上回さないこと
                    classNode.addParamPos(i - 1, new Token(Token.SPACE, " "));
                    classNode.addParamPos(i, new Token(Token.NAME, "implements"));
                    classNode.addParamPos(i + 1, new Token(Token.SPACE, " "));
                    classNode.addParamPos(i + 2, new Token(Token.NAME, interfaceName));

                }
                break;
            }
        }

    }

    /**
     * クラス名の型引数として設定した型を取得する。
     * @param topNode トップノード
     * @return クラス名の型引数として設定した型
     */
    public static String getClassGenericsType(Node topNode) {

        List<Node> nodeList = new ArrayList<>();

        //classのあとに出現する<>内の文字列を取得する
        Pattern p = Pattern.compile("class.*\\<(.+?)\\>");

        findAllNode(topNode, Node.T_CLASS, nodeList);
        for (Node node : nodeList) {
            String strLine = node.getString();
            Matcher m = p.matcher(strLine);
            if (m.find()) {
                return m.group(1);
            }
        }

        return null;

    }

    /**
     * 指定したアノテーションやコメントが付与されているフィールドを取得する。
     * @param node アノテーション、コメントなどフィールドの直前についているノード
     * @return nodeからみて最初に出現したフィールドを返す
     */
    public static Node getMethod(Node node) {

        Node parent = node.getParent();
        int no = parent.getChildren().indexOf(node);
        if (no == 0) {
            return parent;
        }
        int count = 1;
        for (;;) {
            if ((no - count) < 0) {
                break;
            }
            Node prev = parent.getChildren().get(no - count);
            if (prev.getType() == Node.T_BLOCK) {
                return prev;
            }
            count++;
        }
        return null;

    }

    /**
     * メソッドの戻り値設定した型を取得する。
     * @param topNode トップノード
     * @return メソッドの戻り値設定した型
     */
    public static String getMethodGenericsType(Node topNode) {

        List<Node> nodeList = new ArrayList<>();

        //classのあとに出現する<>内の文字列を取得する
        Pattern p = Pattern.compile(".*\\<(.+?)\\>");

        findAllNode(topNode, Node.T_BLOCK, nodeList);
        for (Node node : nodeList) {
            String strLine = node.getString();
            Matcher m = p.matcher(strLine);
            if (m.find()) {
                return m.group(1);
            } else {
                for (Token token : node.getAllTokens()) {
                    if (token.getType() == Token.SYMBOL || token.getType() == Token.SPACE) {
                        continue;
                    }
                    if (token.getText().equals(STATIC) || token.getText().equals(FINAL)
                            || token.getText().equals(PUBLIC) || token.getText().equals(PROTECTED)
                            || token.getText().equals(PRIVATE)) {
                        continue;
                    }
                    if (token.getText().equals(VOID)) {
                        return null;
                    }
                    return token.getText();
                }
            }

        }

        return null;

    }

}
