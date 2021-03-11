package jp.co.tis.s2n.javaConverter.convert.logic;

import java.lang.reflect.InvocationTargetException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.velocity.VelocityContext;
import org.reflections.Reflections;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.convert.program.AbstractProgramConvertHandler;
import jp.co.tis.s2n.javaConverter.convert.program.AutoInstall;
import jp.co.tis.s2n.javaConverter.convert.program.JDBCManagerBatchUpdateMethodHandler;
import jp.co.tis.s2n.javaConverter.convert.program.JDBCManagerGetCountBySqlFileHandler;
import jp.co.tis.s2n.javaConverter.convert.program.JDBCManagerSelectBySQLFileHandler;
import jp.co.tis.s2n.javaConverter.convert.program.JDBCManagerSelectMethodHandler;
import jp.co.tis.s2n.javaConverter.convert.program.JDBCManagerUpdateBySQLFileHandler;
import jp.co.tis.s2n.javaConverter.convert.program.JDBCManagerUpdateMethodHandler;
import jp.co.tis.s2n.javaConverter.convert.statistics.AnnotationStatistics;
import jp.co.tis.s2n.javaConverter.convert.statistics.OtherStatistics;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.sastruts.SAStrutsAbstractValidateHandler;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;
import jp.co.tis.s2n.javaConverter.node.FieldNodeUtil;
import jp.co.tis.s2n.javaConverter.node.ImportNodeUtil;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;
import jp.co.tis.s2n.javaConverter.parser.JavaParser;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * 共通変換処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ConvertBase extends ConvertX {

    public static String CRLF = "\r\n";

    /** 削除対象アノテーション表記を定義する */
    public static List<String> DELETE_ANNOTATION = Arrays.asList("@Session", "@AlwaysExecute", "@TokenSave");

    private static final String IMPORT_INJECT_PARTS_VM = "templete/ImportInjectParts.vm";

    private static List<AutoInstall> autoInstallProgramHandler = null;

    protected ClassNameResolver cnr = null;

    static String[] xenValidateAnnotations = {
            "@AlphaAndNumericType",
            "@AlphaNumericHyphenUnderbarType",
            "@AlphaNumericType",
            "@AlphaType",
            "@AsciiHalfKanaWin31jType",
            "@CapitalAlphaSpaceType",
            "@DecimalType",
            "@FullAlphaType",
            "@FullKanaType",
            "@FullType",
            "@FullWin31jType",
            "@HalfExclusiveHalfKanaType",
            "@HalfKanaType",
            "@HalfType",
            "@PositiveNumericHyphenType",
            "@PositiveNumericType",
            "@MailAccountType",
            "@MailAddressType",
            "@MailDomainType",
            "@PasswordType",
            "@FileNameType",
            "@RequiredFormFile",
            "@FixBytelength",
            "@Fixlength",
            "@MaxDecimallength",
            "@MaxFormFileCount",
            "@DateFormat",
            "@ManageCodeExist",
            "@DecimalRange",
            "@LengthRange",
            "@AfterDate",
            "@AfterDatetime",
            "@BeforeDate",
            "@BeforeDatetime",
            "@WithinDate",
            "@WithinDatetime",
            "@AcceptFormFile",
    };

    /**
     * 基本的なことだけ実施するconvertProc。
     * @param fileName ファイル名
     * @param topNode トップノード
     * @param fileType ファイルタイプ
     * @throws Exception 例外
     */
    public void convertProcCommon(String fileName, Node topNode, String fileType) throws Exception {
        convertXenlonActionForm(topNode, null);
        super.convertProc(fileName, topNode);
        List<Node> classNodeList = NodeUtil.findAllNode(topNode, Node.T_CLASS);
        if (classNodeList.size() == 0) {
            return;
        }
        Node classNode = classNodeList.get(0);
        //完全修飾クラス名特定
        String packageName = NodeUtil.getPackageName(topNode);
        //編集前の状態でクラスネームリゾルバを作成
        makeClassNameResolver(topNode, packageName);

        //ファイルの種類によらない変換を実施
        convertCommon(topNode, fileName, classNode);
        //追加import挿入
        insertStrutsImportInjectParts(topNode, fileType);
        if (activeProfile.getConvertMode() == S2nProfile.CONVERT_MODE_STRUTS) {
            //Strutsなら以下の処理はやらない
            return;
        }

    }

    /**
     * importのリストを取得してClassNameResolverを生成する。
     * @param topNode トップノード
     * @param packageName パッケージ名
     */
    protected void makeClassNameResolver(Node topNode, String packageName) {
        List<Node> importNodeList = NodeUtil.findAllNode(topNode, Node.T_IMPORT);
        List<String> importList = new ArrayList<>();
        for (Node importNode : importNodeList) {
            for (Token token : importNode.getAllTokens()) {
                if (token.getType() == Token.NAME) {
                    if (token.getText().equals("import")) {
                        //次へ
                    } else if (token.getText().equals("static")) {
                        //次へ
                    } else {
                        importList.add(token.getText());
                        break;
                    }
                }
            }
        }

        cnr = new ClassNameResolver(packageName, importList);
    }

    /**
     * getメソッドを生成する。
     *
     * @param classNode クラスノード
     * @param type タイプ
     * @param name フィールド名
     * @param comment コメント
     */
    protected void addGetMethod(Node classNode, String type, String name, String comment) {

        String commentStr = "/**" + CRLF
                + "* " + comment + "を取得する" + CRLF
                + "*" + CRLF
                + "* @return " + name + " " + comment + CRLF
                + "*/";

        Node node1 = Node.create(Node.T_COMMENT2, commentStr);
        classNode.add(node1);

        node1 = Node.create(Node.T_BLOCK, "public");
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

    /**
     * setメソッドを生成する。
     *
     * @param classNode クラスノード
     * @param type タイプ
     * @param name フィールド名
     * @param comment コメント
     */
    protected void addSetMethod(Node classNode, String type, String name, String comment) {

        String commentStr = "/**" + CRLF
                + "* " + comment + "を設定する" + CRLF
                + "*" + CRLF
                + "* @param " + name + " " + comment + CRLF
                + "*/";

        Node node1 = Node.create(Node.T_COMMENT2, commentStr);
        classNode.add(node1);

        node1 = Node.create(Node.T_BLOCK, "public");
        Token token1 = new Token(Token.NAME, " void set" + headUpString(name) + "(" + type + " " + name + ") {");
        node1.addParam(token1);
        classNode.add(node1);

        Node node2 = Node.create(Node.T_NORMAL, "this." + name);
        Token token2 = new Token(Token.NAME, " = " + name + ";");
        node2.addParam(token2);
        node1.add(node2);

        node1 = Node.create(Node.T_BLOCK, "}");
        classNode.add(node1);

    }

    protected String headUpString(String instr) {

        String str1 = instr.substring(0, 1).toUpperCase();
        String str2 = instr.substring(1);
        return str1 + str2;

    }

    protected String fromCamelCaseToSnakeCaseUp(String text) {
        StringBuilder result = new StringBuilder();
        CharBuffer buf = CharBuffer.wrap(text);
        while (buf.hasRemaining()) {
            char c = buf.get();
            result.append(Character.toLowerCase(c));
            buf.mark();
            if (buf.hasRemaining()) {
                char c2 = buf.get();
                if (Character.isLowerCase(c) && Character.isUpperCase(c2)) {
                    result.append("_");
                }
                buf.reset();
            }
        }
        return result.toString().toUpperCase();
    }

    protected void println(String str) {
        System.out.println(str);
    }

    protected void printError(String s) {
        System.err.println(s);
    }

    /**
     * ベースクラスを置換する。<br>
     * @param classNode クラスノード
     * @param convertRules 変換ルール
     */
    protected void convertBaseClassName(Node classNode, Map<String, String> convertRules) {
        String prevName = null;
        for (Token token : classNode.getAllTokens()) {
            if (token.getType() == Token.NAME) {
                if ("extends".equals(prevName)) {
                    //extendsのクラスを検出

                    if (convertRules.containsKey(cnr.resolveFullName(token.getText()))) {
                        token.setText(convertRules.get(cnr.resolveFullName(token.getText())));
                        OtherStatistics.getInstance().editExtend++;
                        OtherStatistics.getInstance().editClassLine++;
                    }
                    break;
                }
                prevName = token.getText();
            }
        }
    }

    /**
     * IMPORTの最後に追加分を挿入する。<br>
     * 登録済の追加分のクラスも挿入する。
     * @param topNode トップノード
     * @param fileType ファイルタイプ
     */
    protected void insertStrutsImportInjectParts(Node topNode, String fileType) {
        List<Node> importList2 = NodeUtil.findAllNode(topNode, Node.T_IMPORT);
        Node lastNode = null;
        int lastPost = 0;
        if (importList2.size() != 0) {
            lastNode = importList2.get(importList2.size() - 1);
            lastPost = lastNode.getParent().getChildren().indexOf(lastNode);
        } else {
            List<Node> importListPkg = NodeUtil.findAllNode(topNode, Node.T_PACKAGE);
            lastNode = importListPkg.get(0);
            lastPost = lastNode.getParent().getChildren().indexOf(lastNode);
        }

        VelocityContext context = new VelocityContext();

        if("Entity".equals(fileType)) {
          context.put("is" + fileType, !isChildEntity(topNode));
        } else {
           context.put("is" + fileType, "true");
        }
        context.put("importList", ClassPathConvertUtil.getInstance().getAdditionalImport());
        context.put("convertMode", this.activeProfile.getConvertMode());
        String sw = mergeVelocityTemplate(IMPORT_INJECT_PARTS_VM, context);
        if (sw != null) {
            int insertLines = sw.split("\n").length;
            OtherStatistics.getInstance().insertImport = OtherStatistics.getInstance().insertImport + insertLines;
        }
        lastNode.getParent().getChildren().add(lastPost + 1, Node.create(Node.T_BLOCK, sw));

    }

    /**
     * ファイルの種類に依存しない変換処理。
     * @param topNode トップノード
     * @param fileName ファイル名
     * @throws Exception 例外
     */
    public void convertCommon(Node topNode, String fileName, Node classNode) throws Exception {

        //トークンの再編成
        for (Node curNode : NodeUtil.findAllNode(topNode)) {
            reorganizeToken(curNode);
        }

        convertBaseClassName(classNode, ClassPathConvertUtil.BASE_CLASSNAME_CONVERT_MAP); //extendsの変更
        convertImportCommon(topNode);
        convertCsvAnnotation(topNode, fileName);
        removeUnsupportAnnotation(topNode, fileName);
        convertResourceAnnotation(topNode, fileName);
        convertComponentAnnotation(topNode, fileName);
        convertJavaProgram(topNode, fileName, classNode);
    }

    private void convertCsvAnnotation(Node topNode, String fileName) {
        // アノテーション・ノードの取得
        List<Node> annotations = NodeUtil.findAllNode(topNode, "@CsvElement");

        if (annotations.size() == 0) {
            return;
        }

        //設定リスト
        Map<Integer, String> headers = new TreeMap<Integer, String>();
        Map<Integer, String> columns = new TreeMap<Integer, String>();
        for (Node annotation : annotations) {
            AnnotationNodeUtil na = new AnnotationNodeUtil(annotation);

            String name = na.getStringValue("name");
            int priolity = Integer.valueOf(na.getStringValue("priority"));
            FieldNodeUtil fieldNode = new FieldNodeUtil(NodeUtil.getField(annotation));
            String filedName = fieldNode.getFieldName();

            //Listに登録
            if (name != null) {
               headers.put(priolity, name);
            }

            columns.put(priolity, filedName);

            NodeUtil.removeChildNode(annotation); //@CsvLementは削除
        }

        //ヘッダの2行を生成
        StringBuilder csvHeaders = new StringBuilder();
        StringBuilder csvColumns = new StringBuilder();

        //name属性が定義される場合、ヘッダーを生成する
        if (headers.size() != 0) {
            for (String header : headers.values()) {
                if (csvHeaders.length() == 0) {
                    csvHeaders.append("headers = {");
                } else {
                    csvHeaders.append(",");
                }

                csvHeaders.append(header);
            }
            csvHeaders.append("},");
        }

        for (String column : columns.values()) {
            if (csvColumns.length() == 0) {
                csvColumns.append("properties = {");
            } else {
                csvColumns.append(",");
            }

            csvColumns.append("\"").append(column).append("\"");
        }
        csvColumns.append("},");

        Node csv = Node.create(Node.T_ANNOTATION, "@Csv(" + csvHeaders + csvColumns + "type = Csv.CsvType.CUSTOM)");
        Node csvFormat = Node.create(Node.T_ANNOTATION,
                "@CsvFormat(charset = \"Shift_JIS\", fieldSeparator = ',',ignoreEmptyLine = true,lineSeparator = \"\\r\\n\", quote = '\"',quoteMode = CsvDataBindConfig.QuoteMode.NORMAL, requiredHeader = true, emptyToNull = false)");
        //固定文字列をくっつけて冒頭部分に書き出す
        List<Node> classNodeList = NodeUtil.findAllNode(topNode, Node.T_CLASS);
        if (classNodeList.size() == 0) {
            return;
        }
        Node classNode = classNodeList.get(0);

        int pos = classNode.getParent().getChildren().indexOf(classNode);
        NodeUtil.addChildNode(classNode.getParent(), pos, csvFormat);
        NodeUtil.addChildNode(classNode.getParent(), pos, csv);
        ClassPathConvertUtil.getInstance().addImprt("nablarch.common.databind.csv.CsvFormat");
        ClassPathConvertUtil.getInstance().addImprt("nablarch.common.databind.csv.Csv");
        ClassPathConvertUtil.getInstance().addImprt("nablarch.common.databind.csv.CsvDataBindConfig");
    }

    /**
     * Javaプログラムコードを書き換える。(クラスに依存しない変換）
     * @param topNode トップノード
     * @param fileName ファイル名
     * @param classNode クラスノード
     * @throws IllegalAccessException 例外
     * @throws InstantiationException 例外
     * @throws SecurityException 例外
     * @throws NoSuchMethodException 例外
     * @throws InvocationTargetException 例外
     * @throws IllegalArgumentException 例外
     */
    protected void convertJavaProgram(Node topNode, String fileName, Node classNode)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {

        initAutoInstallProgramHandler();

        List<Node> nodeList = NodeUtil.findAllNode(topNode);
        for (Node lineNode : nodeList) {
            String line = lineNode.getString();

            // @TransactionAttributeをコメントアウトする
            if (line.contains("@TransactionAttribute") || line.contains(" selectBySql(")) {
                int pos = lineNode.getParent().getChildren().indexOf(lineNode);
                NodeUtil.addChildNode(lineNode.getParent(), pos, Node.create(Node.T_COMMENT1, Node.NO_SUPPORT_LOG));
                NodeUtil.addChildNode(lineNode.getParent(), pos + 1, Node.create(Node.T_COMMENT1, "// " + line));
                NodeUtil.removeChildNode(lineNode);
            }

            // @Bindingアノテーション、@MappedSuperclassアノテーションを単純削除する
            if(line.contains("@Binding(") || line.contains("@MappedSuperclass")) {
                NodeUtil.removeChildNode(lineNode);
            }

            if (line.contains(AbstractProgramConvertHandler.KEY_JDBC_MANAGER)) {
                //順番は重要
                new JDBCManagerSelectBySQLFileHandler(classNode, cnr).convert(lineNode, line, fileName);
                new JDBCManagerGetCountBySqlFileHandler(classNode, cnr).convert(lineNode, line, fileName);
                new JDBCManagerUpdateBySQLFileHandler(cnr).convert(lineNode, line, fileName);
                new JDBCManagerSelectMethodHandler(classNode, cnr).convert(lineNode, line, fileName);
                new JDBCManagerBatchUpdateMethodHandler("insert", cnr).convert(lineNode, line, fileName);
                new JDBCManagerBatchUpdateMethodHandler("update", cnr).convert(lineNode, line, fileName);
                new JDBCManagerBatchUpdateMethodHandler("delete", cnr).convert(lineNode, line, fileName);
                new JDBCManagerUpdateMethodHandler("insert", cnr).convert(lineNode, line, fileName);
                new JDBCManagerUpdateMethodHandler("update", cnr).convert(lineNode, line, fileName);
                new JDBCManagerUpdateMethodHandler("delete", cnr).convert(lineNode, line, fileName);
            }

            //自動インストール型のものをすべて実行する
            for (AutoInstall node : autoInstallProgramHandler) {
                if (node.test(line, lineNode)) {
                    AbstractProgramConvertHandler handle = (AbstractProgramConvertHandler) node;
                    handle.init();
                    handle.convert(lineNode, line, fileName);
                }
            }

        }

    }

    private synchronized void initAutoInstallProgramHandler() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        //インストール実施
        autoInstallProgramHandler = new ArrayList<>();
        String pname = AutoInstall.class.getPackage().getName();
        Reflections reflections = new Reflections(pname);
        Set<Class<? extends AutoInstall>> allClasses = reflections.getSubTypesOf(AutoInstall.class);
        for (Class<? extends AutoInstall> trgClass : allClasses) {
            autoInstallProgramHandler.add(trgClass.getConstructor(ClassNameResolver.class).newInstance(cnr));
        }

    }

    /**
     * トークンの再編成をする。
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     */
    private static void reorganizeToken(Node lineNode) {
        Token p2Token = null;
        Token p1Token = null;
        for (Token curToken : lineNode.getAllTokens()) {

            if ((p2Token != null) && (p1Token != null)) {
                if ((p2Token.getType() == Token.NAME) && (p1Token.getText().equals("."))
                        && (curToken.getType() == Token.NAME)) {
                    String newTokenText = p2Token.getText() + p1Token.getText() + curToken.getText();

                    if (p2Token.equals(lineNode.getHead())) {
                        //headトークンに寄せる
                        lineNode.setName(newTokenText);//連結文字列をセット
                    } else {
                        //通常トークンに寄せる
                        p2Token.setText(newTokenText);
                    }
                    lineNode.getParams().remove(p1Token); //先頭に取り込まれたトークンを削除
                    lineNode.getParams().remove(curToken);//先頭に取り込まれたトークンを削除
                    curToken = p2Token;
                    p1Token = null;
                    p2Token = null;
                }
            }
            p2Token = p1Token;
            p1Token = curToken;
        }

    }

    /**
     * インポート文の変換を行う。<br>
     * ・コンバートルールがないものは無変換。<br>
     * ・コンバートルールがあるものはインポート文を変換する。<br>
     * ・コンバートルールがあり変換後が空白であればインポート文を削除する。
     * @param topNode トップノード
     * @throws Exception 例外
     */
    public void convertImportCommon(Node topNode) throws Exception {
        Map<String, String> convertRule = ClassPathConvertUtil.getInstance().getConverterRules();

        OtherStatistics otherStatistics = OtherStatistics.getInstance();

        List<Node> importList2 = NodeUtil.findAllNode(topNode, Node.T_IMPORT);
        // 導入されたパッケージが重複しないように
        Set<String> sets = new HashSet<>();

        for (Iterator<Node> itr = importList2.iterator(); itr.hasNext();) {
            Node importNode = itr.next();

            ImportNodeUtil inode = new ImportNodeUtil(importNode);

            int pos = inode.getTextPos();

            if (importNode.getAllTokens().get(pos + 1).getText().equals("*")) {
                //import xxxxxx.*;の形式
                if (convertRule.containsKey(importNode.getAllTokens().get(pos).getText() + "*")) {
                    String convertToString = convertRule.get(importNode.getAllTokens().get(pos).getText() + "*");
                    if (StringUtils.isEmpty(convertToString)) {
                        importNode.getParent().getChildren()
                                .remove(importNode.getParent().getChildren().indexOf(importNode));
                    } else {
                        if (convertToString.endsWith("*")) {
                            convertToString = convertToString.substring(0, convertToString.length() - 1);
                        }
                        importNode.getAllTokens().get(pos).setText(convertToString);
                        otherStatistics.editImport++;
                    }
                }

            } else {
                //通常の形式
                if (convertRule.containsKey(importNode.getAllTokens().get(pos).getText())) {
                    String convertToString = convertRule.get(importNode.getAllTokens().get(pos).getText());
                    if (StringUtils.isEmpty(convertToString)) {
                        importNode.getParent().getChildren()
                                .remove(importNode.getParent().getChildren().indexOf(importNode));
                    } else if(sets.contains(convertToString)){
                    	// 重複導入する場合は削除する
                        importNode.getParent().getChildren()
                        .remove(importNode.getParent().getChildren().indexOf(importNode));
                    } else {
                        importNode.getAllTokens().get(pos).setText(convertToString);
                        otherStatistics.editImport++;
                    }
                    sets.add(convertToString);
                }
            }
        }
    }

    /**
     * 未対応のアノテーションを削除する。<br>
     * クラス内指定したannotationをすべて消してしまうので、使い方は要注意。
     * @param topNode トップノード
     * @param fileName ファイル名
     */
    protected void removeUnsupportAnnotation(Node topNode, String fileName) {
        // アノテーション・ノードの取得
        DELETE_ANNOTATION.forEach(a -> {
            List<Node> annotations = NodeUtil.findAllNode(topNode, a);
            for (Node annotation : annotations) {
                NodeUtil.removeChildNode(annotation);
            }
        });
    }

    /**
     * @Resourceを@Injectに置換する。
     * @param topNode トップノード
     * @param fileName ファイル名
     */
    protected void convertResourceAnnotation(Node topNode, String fileName) {
        // アノテーション・ノードの取得
        List<Node> annotations = NodeUtil.findAllNode(topNode, "@Resource");
        for (Node annotation : annotations) {
            // アノテーションが付与されているメンバ・ノードを取得する。
            Node fieldNode = NodeUtil.getField(annotation);
            String fieldClassName = new FieldNodeUtil(fieldNode).getClassName();
            String fullClassName = cnr.resolveFullName(fieldClassName);
            if (fullClassName.startsWith("javax.servlet")) {

                String changeTo = ClassPathConvertUtil.SERVLET_CLASSNAME_CONVERT_MAP.get(fieldClassName);
                if (changeTo != null) {
                    FieldNodeUtil.setFieldClass(fieldNode, changeTo);
                    int pos = annotation.getParent().getChildren().indexOf(annotation);
                    NodeUtil.addChildNode(annotation.getParent(), pos, Node.create(Node.T_ANNOTATION, "@Inject"));
                    NodeUtil.removeChildNode(annotation);
                }

            } else {
                int pos = annotation.getParent().getChildren().indexOf(annotation);
                NodeUtil.addChildNode(annotation.getParent(), pos, Node.create(Node.T_ANNOTATION, "@Inject"));
                NodeUtil.removeChildNode(annotation);
            }
        }
    }

    /**
     * @Componentを@XXXXScopedに置換する。
     * @param topNode トップノード
     * @param fileName ファイル名
     */
    protected void convertComponentAnnotation(Node topNode, String fileName) {
        // アノテーション・ノードの取得
        List<Node> annotations = NodeUtil.findAllNode(topNode, "@Component");
        for (Node annotation : annotations) {
            AnnotationNodeUtil aNode = new AnnotationNodeUtil(annotation);

            String instanceType = aNode.getStringValueWithoutQuote("instance");
            if (instanceType == null) {
                String fileNameTop = fileName.indexOf(".") == -1 ? fileName
                        : fileName.substring(0, fileName.indexOf("."));
                //http://s2container.seasar.org/2.4/ja/S2.4SmartDeployConfig.html
                if (fileNameTop.endsWith("Action")) {
                    instanceType = "REQUEST";
                } else if (fileNameTop.endsWith("Converter")) {
                    instanceType = "PROTOTYPE";
                } else if (fileNameTop.endsWith("Dao")) {
                    instanceType = "PROTOTYPE";
                } else if (fileNameTop.endsWith("Dto")) {
                    instanceType = "REQUEST";
                } else if (fileNameTop.endsWith("Dxl")) {
                    instanceType = "SINGLETON";
                } else if (fileNameTop.endsWith("Helper")) {
                    instanceType = "PROTOTYPE";
                } else if (fileNameTop.endsWith("Interceptor")) {
                    instanceType = "PROTOTYPE";
                } else if (fileNameTop.endsWith("Logic")) {
                    instanceType = "PROTOTYPE";
                } else if (fileNameTop.endsWith("Page")) {
                    instanceType = "REQUEST";
                } else if (fileNameTop.endsWith("Service")) {
                    instanceType = "PROTOTYPE";
                } else if (fileNameTop.endsWith("Validator")) {
                    instanceType = "PROTOTYPE";
                } else {
                    instanceType = "PROTOTYPE";
                }
            }

            String trgAnnotation = null;

            if (instanceType.contains("SINGLETON")) {
                trgAnnotation = "@Singleton";
            } else if (instanceType.contains("PROTOTYPE")) {
                trgAnnotation = "@Prototype";
            } else if (instanceType.contains("APPLICATION")) {
                trgAnnotation = "@Singleton";
            } else if (instanceType.contains("SESSION")) {
                trgAnnotation = "@SessionScoped";
            } else if (instanceType.contains("REQUEST")) {
                trgAnnotation = "@RequestScoped";
            }

            if (trgAnnotation != null) {

                //既にあれば上書きする
                List<Node> wannotations = NodeUtil.findAllNode(topNode, "@RequestScoped");
                for (Node wannotation : wannotations) {
                    NodeUtil.removeChildNode(wannotation);
                }

                addScopeAnotationToClass(annotation, trgAnnotation);
                NodeUtil.removeChildNode(annotation);

            }
        }

    }

    /**
     * クラスにスコープアノテーションを付与する。<br>
     * 付与したクラスがSerializeableでなければimplementsを追加する。<br>
     * Abstractクラスであれば付与を取りやめる。
     * @param classNode クラスノード
     * @param scopeAnnotation スコープアノテーション
     */
    protected void addScopeAnotationToClass(Node classNode, String scopeAnnotation) {

        if (NodeUtil.isAbstractClass(classNode)) {
            //abstractクラスには付与しない
            return;
        }

        int pos = classNode.getParent().getChildren().indexOf(classNode);

        if (!hasScopeAnnotation(classNode)) {
            NodeUtil.addChildNode(classNode.getParent(), pos, Node.create(Node.T_ANNOTATION, scopeAnnotation));
        }

        List<String> impList = NodeUtil.getImplements(classNode);

        boolean serializable = false;
        for (String string : impList) {
            if (string.endsWith("Serializable")) {
                serializable = true;
            }
        }
        if (!serializable) {
            //追加する
            NodeUtil.addImplements(classNode, "Serializable");
            OtherStatistics.getInstance().addSerializable++;
            OtherStatistics.getInstance().editClassLine++;
        }


    }

    /**
     * Scopeアノテーションがあれば、trueを返却する。
     * @param classNode クラスノード
     * @return チェック結果
     */
    private boolean hasScopeAnnotation(Node classNode) {
        List<Node> alist = NodeUtil.getAnnotationList(classNode);
        for (Node node : alist) {

            String nodeName = node.getName();
            if (("@RequestScoped".equals(nodeName))
                    || ("@SessionScoped".equals(nodeName))
                    || ("@Singleton".equals(nodeName))
                    || ("@Prototype".equals(nodeName))
                    ) {
                return true;
            }
        }
        return false;
    }

    /**
     * アノテーションのログを収集する。
     * @param topNode トップノード
     */
    @Override
    public void makeAnnotationLog(Node topNode) {

        String packageName = NodeUtil.getPackageName(topNode);
        String className = NodeUtil.getClassName(topNode);
        String fullClassName = StringUtils.addClassPackage(packageName, className);

        List<Node> annotationList = NodeUtil.findAllNode(topNode, Node.T_ANNOTATION);

        for (Node node : annotationList) {
            Node targetNode = NodeUtil.searchNodeForAnnotationEx(node); //アノテーションの付与対象
            String aType = null;
            switch (targetNode.getType()) {
            case Node.T_CLASS:
                aType = "class";
                break;
            case Node.T_BLOCK:
                aType = "method";
                break;
            case Node.T_MEMBER:
                aType = "field";
                break;
            case Node.T_NORMAL:
                aType = "normal";
                break;
            default:
                aType = "other";
                break;
            }
            AnnotationNodeUtil anode = new AnnotationNodeUtil(node);
            AnnotationStatistics.getInstance().convertedAnnotation(anode.getName(), "", anode.getSignature(),
                    fullClassName, node.getStringWithoutCRLF(), "", aType, targetNode.getStringWithoutCRLF());
        }

    }

    /**
     * メンバに対するバリデーションアノテーションの付与。（SAStruts共通処理）
     * @param topNode トップノード
     * @param classNode クラスノード
     * @param fullClassName 完全修飾クラス名
     * @throws InstantiationException 例外
     * @throws IllegalAccessException 例外
     */
    public void convertSAStrutsValidateAnnotation(Node topNode, Node classNode, String fullClassName)
            throws InstantiationException, IllegalAccessException {

        //アノテーションロジックが存在する場合は、既存アノテーションをいったん削除してから新規に書き込む実装である
        List<Node> memberNodeList = NodeUtil.findAllNode(topNode, Node.T_MEMBER);
        for (Node memberNode : memberNodeList) {
            Map<String, ValidateAnnotation> fieldAnnotationList = new LinkedHashMap<String, ValidateAnnotation>();
            String memberName = NodeUtil.getMemberName(memberNode);
            if (memberNode.getName().equals("public")) {
                List<Node> memberAnnotationNodeList = NodeUtil.getAnnotationList(memberNode);
                Collections.reverse(memberAnnotationNodeList);
                if (memberAnnotationNodeList.size() > 0
                        && memberAnnotationNodeList.get(0).getName().contains("@FieldName")) {
                    Node filedNameNode = memberAnnotationNodeList.remove(0);
                    memberAnnotationNodeList.add(filedNameNode);
                }

                for (Node memberAnnotationNode : memberAnnotationNodeList) {
                    AnnotationNodeUtil aNode = new AnnotationNodeUtil(memberAnnotationNode);
                    SAStrutsAbstractValidateHandler handler = ConvertFactory.getSAStrutsAbstractValidateHandler(aNode);

                    if (handler != null) {
                        //アノテーションは除去する
                        NodeUtil.removeChildNode(memberAnnotationNode);
                        handler.setFieldAnnotationList(fieldAnnotationList);
                        ValidateAnnotation resultValidateAnnotation = handler.handle(aNode);
                        if (resultValidateAnnotation != null) {
                            //戻り値がnullの場合は既存のものにマージ済とみなして登録しない
                            if (!fieldAnnotationList.containsKey(resultValidateAnnotation.getName())) {
                                fieldAnnotationList.put(resultValidateAnnotation.getName(),
                                        resultValidateAnnotation);
                            }
                        }
                    } else {

                        // 未対応validateアノテーションであるかどうかを判断する
                        if (isNoSupportValidateAnnotation(aNode.getName())) {
                            // 未変換箇所はログとしてソースファイルに出力する
                            int pos = memberAnnotationNode.getParent().getChildren().indexOf(memberAnnotationNode);

                            // 未変換箇所ログを出力する
                            NodeUtil.addChildNode(memberAnnotationNode.getParent(), pos,
                                    Node.create(Node.T_COMMENT1, Node.NO_SUPPORT_LOG));

                            // 元々のソースをコメントアウトする
                            NodeUtil.addChildNode(memberAnnotationNode.getParent(), pos + 1,
                                    Node.create(Node.T_COMMENT1, "// " + memberAnnotationNode.getString()));
                            NodeUtil.removeChildNode(memberAnnotationNode);
                        }

                    }
                }

                for (ValidateAnnotation cAnnotation : fieldAnnotationList.values()) {
                    cAnnotation.writeToNode(null, memberName, topNode, classNode);
                }
            }
        }
    }

    /**
     * XenlonActionFormクラスを変換する。
     *
     * @param topNode トップノード
     * @param classNode クラスノード
     */
    public void convertXenlonActionForm(Node topNode, Node classNode) {

        String packageName = NodeUtil.findAllNode(topNode, Node.T_PACKAGE).get(0).getParams().get(1).getText();
        List<Node> allNode = NodeUtil.findAllNode(topNode, Node.T_CLASS);
        if(allNode.size()==0) {//class見つからない場合、interfaceを探す
            allNode = NodeUtil.findAllNode(topNode, Node.T_INTERFACE);
        }
        if(allNode.size()==0) {//interface見つからない場合、annotationを探す
            allNode = NodeUtil.findAllNode(topNode, Node.T_ANNOTATIONDEF);
        }
        String className = allNode.get(0).getParams().get(3).getText()
                .replace("BaseForm", "Form");

        List<Node> annotationList = NodeUtil.findAllNode(topNode, Node.T_ANNOTATION);
        for (Node node : annotationList) {
            if (isXenValidateAnnotation(node.getName())) {
                for (Token token : node.getParams()) {
                    if (token.getType() != Token.SPACE) {
                    }
                }
            }
        }
        Set<String> fullNameList = new HashSet<>();
        // fieldNameのimport文を插入するフラグ
        boolean fieldNameImportFlg = false;
        for (Node node : annotationList) {

            Node valueNode = NodeUtil.searchNodeForAnnotation(node);

            if (valueNode.getParams().size() > 3) {
                String valueName = "";
                if (valueNode.getString().contains("[") || valueNode.getString().contains("]")) {
                    // 配列の場合：public String[] count;
                    valueName = valueNode.getParams().get(5).getText();
                } else {
                    // public Integer totalPage;
                    valueName = valueNode.getParams().get(3).getText();
                }
                String fullName = packageName
                        + "." + className
                        + "."
                        + valueName;

                String str = null;
                if(node.getString().contains("@Arg(key")) {
                	// パターン１． @Required (arg0=@Arg(key="xxx",resource = true))
                    str = node.getParamByKey("key", "arg0"); //getParamValue -> getParam
                } else {
                	// パターン２. @Required(msg = @Msg(key = "xxx"))
                	str = node.getParamByKey("key", "msg");
                }

            	// @FieldNameアノテーションを追加する
            	if(str != null && !fullNameList.contains(fullName)) {
                    int pos = node.getParent().getChildren().indexOf(node);
                    Node filedNameNode = JavaParser.parse("@FieldName(value=" + str + ")");
                    NodeUtil.addChildNode(node.getParent(), pos,
                    		filedNameNode.getChildren().get(0));
                    fullNameList.add(fullName);
                    fieldNameImportFlg = true;
            	}

            }

        }

        if(fieldNameImportFlg) {
        	ClassPathConvertUtil.getInstance().addImprt("oscana.s2n.validation.FieldName");
        }

        boolean bFindSystemChar = false;
        //
        List<Node> importList = NodeUtil.findAllNode(topNode, Node.T_IMPORT);
        //
        for (Node importNode : importList) {

            Token importPackageName = importNode.getParams().get(1);
            String importPackageNameText = importPackageName.getText();
            int idx = importPackageNameText.lastIndexOf(".");
            String lastName = importPackageNameText.substring(idx + 1);

            if (isSystemCharAnnotationImports(lastName)) {
                if (bFindSystemChar == false) {
                    importPackageName.setText("oscana.s2n.validation.SystemChar");
                    bFindSystemChar = true;
                } else {
                    Node parent = importNode.getParent();
                    parent.getChildren().remove(importNode);
                }
            } else if (importPackageNameText.contains("jp.co.tis.xenlon.web.annotation")) {
                String newText = importPackageNameText.replace("jp.co.tis.xenlon.web.annotation",
                        "oscana.s2n.validation");
                importPackageName.setText(newText);
            }

        }

    }

    protected boolean isXenValidateAnnotation(String str) {

        for (int i = 0; i < xenValidateAnnotations.length; i++) {
            if (xenValidateAnnotations[i].equals(str)) {
                return true;
            }
        }
        return false;

    }

    protected boolean isSystemCharAnnotationImports(String str) {

        for (int i = 0; i < toSystemCharAnnotationImports.length; i++) {
            if (toSystemCharAnnotationImports[i].equals(str)) {
                return true;
            }
        }
        return false;

    }

    static String[] toSystemCharAnnotationImports = {
            "AlphaNumericHyphenUnderbarType",
            "AlphaNumericType",
            "AlphaType",
            "AsciiHalfKanaWin31jType",
            "CapitalAlphaSpaceType",
            "FullAlphaType",
            "FullKanaType",
            "FullType",
            "FullWin31jType",
            "HalfExclusiveHalfKanaType",
            "HalfKanaType",
            "HalfType",
            "PositiveNumericHyphenType",
            "PositiveNumericType",
    };

    protected boolean isNoSupportValidateAnnotation(String str) {

        for (int i = 0; i < noSupportValidateAnnotation.length; i++) {
            if (noSupportValidateAnnotation[i].equals(str)) {
                return true;
            }
        }
        return false;

    }


    static String[] noSupportValidateAnnotation = {
            "Requiredif",
            "Validwhen",
            "ByteLocale",
            "ShortLocale",
            "IntegerLocale",
            "LongLocale",
            "FloatLocale",
            "FoubleLocale",
            "Minlength",
            "Maxlength",
            "IntRange",
            "LongRange",
            "FloatRange",
            "DoubleRange",
            "ByteType",
            "ShortType",
            "IntegerType",
            "LongType",
            "FloatType",
            "DoubleType",
            "DateType",
            "CreditCardType",
            "EmailType",
            "UrlType",
            "FullType",
            "FullWin31jType",
            "FullKanaType",
            "FullAlphaType",
            "HalfType",
            "AsciiHalfKanaWin31jType",
            "HalfExclusiveHalfKanaType",
            "HalfKanaType",
            "AlphaType",
            "CapitalAlphaSpaceType",
            "AlphaNumericHyphenUnderbarType",
            "PositiveNumericType",
            "PositiveNumericHyphenType",
            "EmailType",
            "MailAccount",
            "MailDomain",
            "AcceptFormFile"

    };

    /**
     * アクセッサを追加する。
     * @param topNode トップノード
     * @param classNode クラスノード
     */
    public void addAccessor(Node topNode, Node classNode) {
        List<Node> memberNodeList1 = NodeUtil.findAllNode(topNode, Node.T_MEMBER);
        List<Node> blockNodeList = NodeUtil.findAllNode(topNode, Node.T_BLOCK);

        for (Node memberNode : memberNodeList1) {

            List<Node> commentList = NodeUtil.getCommentList(memberNode);

            String comment = "変数";

            if (commentList.size() > 0) {
                int commentIndex = 0;
                if(commentList.size() > 1) {
                    commentIndex = commentList.size() -1;
                }
                String wstr = commentList.get(commentIndex).getString();
                comment = wstr.replace("/**", "").replace("*/", "").trim();
            }

            if (memberNode.getName().equals("public")) {

            	if(!memberNode.getString().contains("serialVersionUID")) {
            		memberNode.setName("private");
            	}

                FieldNodeUtil fnd = FieldNodeUtil.parse(memberNode);
                if (fnd != null) {
                    String type = fnd.getClassName();
                    String name = fnd.getFieldName();
                    if(getMethodNodePos(blockNodeList, "get" + headUpString(name)) < 0) {

                        addGetMethod(classNode, type, name, comment);
                        classNode.addCrLf();
                        OtherStatistics.getInstance().makeGetterCnt++;
                    }
                    if (getMethodNodePos(blockNodeList, "set" + headUpString(name)) < 0
                            && !isFinalField(memberNode)) {

                        addSetMethod(classNode, type, name, comment);
                        classNode.addCrLf();
                        OtherStatistics.getInstance().makeSetterCnt++;
                    }
                }
            }
            //Nablarch向けの可視化
            if (memberNode.getName().equals("private") && !memberNode.getString().contains("serialVersionUID")
            		) {
                memberNode.setName("public");
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

    /**
     * 子供のentityクラスかどうかを判断する。
     * @param topNode トップノード
     * @return 子供のentityであれば、trueを返す
     */
    protected boolean isChildEntity(Node topNode) {
        List<Node> annotationNodesEntity = NodeUtil.findAllNode(topNode, "@Entity");
        // クラスに@Entityアノテーションが存在しない場合
        if(annotationNodesEntity == null || annotationNodesEntity.size() == 0) {
            return false;
        } else {
            return true;
        }

    }
}
