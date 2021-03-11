package jp.co.tis.s2n.javaConverter.convert.logic;

import java.util.Iterator;
import java.util.List;

import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.convert.statistics.OtherStatistics;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;
import jp.co.tis.s2n.javaConverter.node.FieldNodeUtil;
import jp.co.tis.s2n.javaConverter.node.ImportNodeUtil;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * Entityクラスの変換処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ConvertEntity extends ConvertBase {

    public static final String XENLON_S2N_CONVERT_SASTRUTS_ENTITY_HEAD_INJECT_PARTS_VM = "templete/SAStrutsEntityInjectParts.vm";

    /**
     * Entityクラス変換の主処理。
     * @param fileName ファイル名
     * @param topNode トップノード
     * @throws Exception 例外
     */
    public void convertProc(String fileName, Node topNode) throws Exception {
        super.convertProc(fileName, topNode);

        List<Node> classNodeList = NodeUtil.findAllNode(topNode, Node.T_CLASS);
        Node classNode = classNodeList.get(0);
        //完全修飾クラス名特定
        String packageName = NodeUtil.getPackageName(topNode);
        //編集前の状態でクラスネームリゾルバを作成
        makeClassNameResolver(topNode, packageName);

        //ファイルの種類によらない変換を実施
        convertCommon(topNode, fileName, classNode);
        //追加import挿入
        insertStrutsImportInjectParts(topNode, "Entity");

        if (activeProfile.getConvertMode() == S2nProfile.CONVERT_MODE_STRUTS) {
            //Strutsなら以下の処理はやらない
            return;
        }

        //クラスヘッダ部分のコンバート
        // クラスに@Entityアノテーションが存在しない場合
        if(!isChildEntity(topNode)) {
           convertClassHeader(fileName, classNode);
        }

        //ボディ部分のコンバート
        convertClassBody(topNode, classNode);
    }

    private void convertClassBody(Node topNode, Node classNode) {
        List<Token> tokenList;
        Node parent;
        String str;
        List<Node> memberNodeList1 = NodeUtil.findAllNode(topNode, Node.T_MEMBER);
        List<Node> blockNodeList = NodeUtil.findAllNode(topNode, Node.T_BLOCK);

        for (Node memberNode : memberNodeList1) {

            List<Node> commentList = NodeUtil.getCommentList(memberNode);

            String comment = "変数";

            if (commentList.size() > 0) {
                String wstr = commentList.get(0).getString();
                comment = wstr.replace("/**", "").replace("*/", "").trim();
            }

            if (memberNode.getName().equals("public")) {

                FieldNodeUtil fnd = FieldNodeUtil.parse(memberNode);
                if (fnd == null) {
                    continue;
                }
                String name = fnd.getFieldName();
                String type = fnd.getClassName();

                // java.sql.Dateをjava.util.Dateに書き換え
                if ("java.sql.Date".equals(type)) {
                    // java.util.Dateに変更
                    type = "java.util.Date";
                    // 書き換え
                    memberNode.getParams().get(1).setText(type);
                } else if ("Date".equals(type)) {
                    String fullPath = this.cnr.resolveFullName(type);//フルバスに変換
                    if ("java.sql.Date".equals(fullPath)) {
                        // import文探す
                        List<Node> importNodeList = NodeUtil.findAllNode(topNode, Node.T_IMPORT);
                        //書き換え
                        for (Iterator<Node> itr = importNodeList.iterator(); itr.hasNext();) {
                            Node importNode = itr.next();
                            ImportNodeUtil inode = new ImportNodeUtil(importNode);
                            int pos = inode.getTextPos();
                            if (importNode.getAllTokens().get(pos).getText().equals("java.sql.Date")) {
                                importNode.getAllTokens().get(pos).setText("java.util.Date");
                            }
                        }
                    }
                }

                if (memberNode.getParams().get(3).getType() == Token.NAME) {
                    List<Node> annotationList = NodeUtil.getAnnotationList(memberNode);

                    for (Node node : annotationList) {
                        parent = node.getParent();
                        parent.getChildren().remove(node);
                    }

                    int getterPos = getMethodNodePos(blockNodeList, "get" + headUpString(name));

                    //Getterコメント作成
                    String commentStr = "/**" + CRLF
                            + "* " + comment + "を取得する" + CRLF
                            + "*" + CRLF
                            + "* @return " + name + " " + comment + CRLF
                            + "*/";
                    //コメントノードの追加
                    Node node1 = Node.create(Node.T_COMMENT2, commentStr);
                    if (getterPos < 0) {
                        classNode.add(node1);
                    }

                    //Columnアノテーションの編集(name属性の追加)
                    for (Node node : annotationList) {
                        if (node.getString().startsWith("@Column")) {
                            AnnotationNodeUtil cNode = new AnnotationNodeUtil(node);
                            tokenList = node.getParams();

                            if(this.activeProfile.getTableNameCase()==0) {
                                str = "name = \"" + fromCamelCaseToSnakeCaseUp(name).toUpperCase() + "\", ";
                            }else {
                                str = "name = \"" + fromCamelCaseToSnakeCaseUp(name).toLowerCase() + "\", ";
                            }

                            if (StringUtils.isEmpty(cNode.getStringValueWithoutQuote("name"))) {
                                tokenList.add(1, new Token(Token.NAME, str));
                            }
                        }
                    }
                    //メンバのアノテーションをすべてGetterにコピーする
                    for (int i = (annotationList.size() - 1); i >= 0; i--) {
                        Node node = annotationList.get(i);
                        if (node.getString().startsWith("@JoinColumn")
                                || node.getString().startsWith("@ManyToOne")
                                || node.getString().startsWith("@OneToMany")) {
                            // サポートしないアノテーションをコメントアウトする
                            node.setName(Node.NO_SUPPORT_LOG + CRLF +"    //" + node.getName());
                        }

                        if (getterPos < 0) {
                            classNode.add(node);
                        } else {
                            classNode.getChildren().add(getterPos, node);
                        }
                    }

                    if (getterPos < 0) {
                        //Getterの追加
                        addGetMethod(classNode, type, name, comment);
                        classNode.addCrLf();
                        OtherStatistics.getInstance().addEntityGetter++;
                    }

                    if (getMethodNodePos(blockNodeList, "set" + headUpString(name)) < 0 && !isFinalField(memberNode)) {
                        //Setterの追加
                        addSetMethod(classNode, type, name, comment);
                        classNode.addCrLf();
                        OtherStatistics.getInstance().addEntitySetter++;
                    }
                }
            }
        }
    }

    private int getMethodNodePos(List<Node> blockNodeList, String methodName) {
        for(Node node : blockNodeList) {
            if(node.getName().equals("public") && node.getParams().get(3).getText().equals(methodName)) {
                return node.getParent().getChildren().indexOf(node);
            }
        }

        return -1;
    }

    private boolean isFinalField(Node node) {
        for(Token token : node.getAllTokens()) {
            if(token.getType() == Token.NAME && token.getText().equals("final")) {
                return true;
            }
        }
        return false;
    }

    private void convertClassHeader(String fileName, Node classNode) {
        Node parent = classNode.getParent();
        int pos = parent.getChildren().indexOf(classNode);
        String tableName = fromCamelCaseToSnakeCaseUp(fileName.replace("Base.java", ""));
        if(this.activeProfile.getTableNameCase()==0) {
            tableName = tableName.toUpperCase();
        }else {
            tableName = tableName.toLowerCase();
        }
        String str = "@Table(name = \"" + tableName + "\")";
        parent.getChildren().add(pos, Node.create(Node.T_ANNOTATION, str));
        OtherStatistics.getInstance().addEntityTableName++;
        parent.getChildren().add(pos, Node.create(Node.T_ANNOTATION, "@Entity"));
        OtherStatistics.getInstance().addEntityEntity++;

        classNode.addCrLf();
    }

    protected void addGetMethod(Node classNode, String type, String name, String comment) {

        Node node1 = Node.create(Node.T_BLOCK, "public");
        Token token1 = new Token(Token.NAME, " " + type + " get" + headUpString(name) + "() {");
        node1.addParam(token1);
        classNode.add(node1);

        Node node2 = Node.create(Node.T_RETURN, "return");
        Token token2 = new Token(Token.NAME, " " + name + ";");
        node2.addParam(token2);
        node1.add(node2);

        node1 = Node.create(Node.T_BLOCK, "}");
        classNode.add(node1);

    }

}
