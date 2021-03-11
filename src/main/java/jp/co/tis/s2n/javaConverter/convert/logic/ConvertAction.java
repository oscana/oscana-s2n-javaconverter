package jp.co.tis.s2n.javaConverter.convert.logic;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzeResult;
import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzer;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.Route;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsAction;
import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.convert.program.ReturnStatementHandler;
import jp.co.tis.s2n.javaConverter.convert.statistics.ActionStatistics;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;
import jp.co.tis.s2n.javaConverter.node.FieldNodeUtil;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * Actionクラスの変換処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ConvertAction extends ConvertBase {

    /** * メソッド前挿入用アダプタ パーツ*/
    private static final String XENLON_S2N_CONVERT_XENLON_STRUTS_ACTION_INJECT_PARTS_VM = "templete/StrutsActionInjectParts.vm";
    /** * メソッド前挿入用アダプタ パーツ*/
    private static final String XENLON_S2N_CONVERT_XENLON_SASTRUTS_ACTION_INJECT_PARTS_VM = "templete/SAStrutsActionInjectParts.vm";

    /**
     * Actionクラス変換の主処理。
     * @param fileName 変換ファイル名
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
        String className = NodeUtil.getClassName(topNode);
        String fullClassName = StringUtils.addClassPackage(packageName, className);
        String actionName = getActionName(className);

        //@ActioFormがついているフォームを見つける
        FieldNodeUtil saForm = convertAndFindForm(fileName, topNode);

        if(activeProfile.getConvertMode() == S2nProfile.CONVERT_MODE_XENLON) {
            convertTokenSkipMethod(topNode);
        }

        //アクションメソッドの変換処理

        //Struts.xmlに基づく変換
        if (this.strutsAnalyzeResult != null) {
            //Struts.xmlに基づく変換
            convertStrutsAction(fileName, classNode, fullClassName);
        }
        //@Executeがついてるメソッドを変換する
        convertExecuteMethod(topNode, classNode, saForm, actionName);

        //ファイルの種類によらない変換を実施
        convertCommon(topNode, fileName, classNode);

        //追加import挿入
        insertStrutsImportInjectParts(topNode, "Action");

    }

    /**
     * Struts.xmlに基づく変換を実施する。
     * @param fileName ファイル名
     * @param classNode クラスノード
     * @param fullClassName 完全修飾クラス名
     */
    private void convertStrutsAction(String fileName, Node classNode, String fullClassName) {
        List<StrutsAction> actionList = new ArrayList<StrutsAction>();
        for (StrutsAnalyzeResult analyzeResult : this.strutsAnalyzeResult) {
            List<StrutsAction> cActionList = analyzeResult.getActionList().stream()
                    .filter((action) -> fullClassName.equals(action.getType())).collect(Collectors.toList());
            if (cActionList.size() > 0) {
                actionList.addAll(cActionList);
            }
        }

        StrutsAction curAction = null;
        if (actionList.size() > 1) {
            curAction = actionList.get(0);
        } else if (actionList.size() == 1) {
            curAction = actionList.get(0);
        }

        println("StrutsAction置換:" + fullClassName);
        if (curAction != null) {
            //Action変換メソッド挿入
            insertStrutsActionInjectParts(classNode, curAction);

            //クラスにアノテーションを付与
            addScopeAnotationToClass(classNode, "@RequestScoped");
        }
    }

    /**
     * ＠ActionFormアノテーションが付与されているメンバを見つけ、メンバ名を返す。
     * @param fileName ファイル名
     * @param topNode トップノード
     * @return フィールドノードユーティリティ
     */
    private FieldNodeUtil convertAndFindForm(String fileName, Node topNode) {
        // アノテーション・ノードの取得
        FieldNodeUtil retObj = null;
        List<Node> annotations = NodeUtil.findAllNode(topNode, "@ActionForm");
        boolean findForm = false;
        for (Node annotation : annotations) {
            // アノテーションが付与されているメンバ・ノードを取得する。
            Node fieldNode = NodeUtil.searchNodeForAnnotation(annotation);
            if (findForm == false) {
                //メンバ名を見つける
                retObj = new FieldNodeUtil(fieldNode);

                findForm = true;

                // @ActionFormを@OscanaActionFormに変換
                annotation.setName("@OscanaActionForm");
            }
        }

        return retObj;

    }

    /**
     * Executeアノテーションがついているメソッドはアクションとして変換を実施する。<br>
     * Executeアノテーションノードが見つからなければこのクラスはアクションではないので何もしない。
     * @param topNode トップノード
     * @param classNode クラスノード
     * @param saForm フィールドノードのオブジェクト
     * @param actionName アクション名
     */
    private void convertExecuteMethod(Node topNode, Node classNode, FieldNodeUtil saForm, String actionName) {
        boolean isAction = false;
        // アノテーション・ノードの取得
        List<Node> annotations = NodeUtil.findAllNode(topNode, "@Execute");

        String packageName = NodeUtil.getPackageName(topNode);

        for (Node annotation : annotations) {

            AnnotationNodeUtil executeAnnotation = new AnnotationNodeUtil(annotation);

            // アノテーションが付与されているメソッド・ノードを取得する。
            Node methodNode = NodeUtil.searchNodeForAnnotation(annotation);

            if (methodNode != null) {

                // メソッド名を取得
                String methodName = methodNode.getParams().get(methodNode.getPosOfParams("(") - 1).getText();
                isAction = true;
                String beforeChange = methodNode.getString();//ログ用変更前文字列
                // 返り値の変更
                methodNode.replaceParams("String", "HttpResponse");

                // 引数の追加位置の取得
                int pos = methodNode.getPosOfParams(")");
                if (pos != -1) {
                    // 引数の追加
                    methodNode.addParamPos(pos,
                            new Token(Token.NAME, "HttpRequest nabRequest, ExecutionContext nabContext"));

                    // メソッドを使ってるところ
                    List<Node> callerList = NodeUtil.findAllNode(topNode, methodName);

                    for (Node caller : callerList) {
                        // メソッド自身を除外
                        if (methodNode.getLineNo() == caller.getLineNo()) {
                            continue;
                        }

                        int callerPos = -1;
                        int posIns = -1;
                        List<Token> tokens = caller.getParams();
                        for (int i = 0; i < tokens.size(); i++) {
                            if (tokens.get(i).getText().equals(methodName)) {
                                callerPos = i;
                            }
                            if (tokens.get(i).getText().equals(")")) {
                                posIns = i;
                            }
                            if (callerPos > 0 && posIns > callerPos) {
                                break;
                            }
                        }
                        // シグネチャに合わせる、パラメータを追加　※括弧ネストの場合未対応
                        if (posIns != -1) {
                            caller.addParamPos(posIns, new Token(Token.NAME, "nabRequest, nabContext"));
                        }

                    }
                }

                //Route.xmlに出力用データを作成
                insertRouteXml(executeAnnotation, topNode, actionName, methodName);

                //メソッド前の処理挿入
                insertSAStrutsActionInjectParts(methodNode, actionName, saForm, executeAnnotation, topNode);

                //Returnの変更
                List<Node> returnList = NodeUtil.findAllNode(methodNode, "return");
                for (Node retrunNode : returnList) {

                    new ReturnStatementHandler(actionName, cnr).convert(retrunNode, null, null);

                }

                ActionStatistics.getInstance().convertedAction(packageName, actionName, beforeChange,
                        methodNode.getString());

            }
            //Executeアノテーションは消去
            NodeUtil.removeChildNode(annotation);
        }
        if (isAction) {
            //Actionむけ追加処理

            //クラスにアノテーションを付与
            addScopeAnotationToClass(classNode, "@RequestScoped");
        }

    }

    /**
     *
     * アクションクラスに対して、publicメソッドに「@OnDoubleSubmission」を追加する。<br>
     * ただ、TokenSkipアノテーションがついているメソッドでは、追加不要。<br>
     *
     * OnDoubleSubmissionのパスは、設定ファイルに指定する。
     *
     * @param topNode トップノード
     */
    private void convertTokenSkipMethod(Node topNode) {
        // メソッド一覧を取得する
        List<Node> nodeList = NodeUtil.findAllNode(topNode, Node.T_BLOCK);

        List<Node> findList = new ArrayList<>();
        for (Node node : nodeList) {
            if (node.getName().equals("public")) {

                // 対象メソッドに付与されたアノテーションのリストを返す
                findList = NodeUtil.getAnnotationList(node);

                boolean isSkip = true;
                for (Node methodNode : findList) {
                    if (methodNode.getName().equals("@TokenSkip")) {

                        // TokenSkipアノテーションを削除
                        NodeUtil.removeChildNode(methodNode);
                        isSkip = true;
                        break;
                    } else if(methodNode.getName().equals("@Execute")) {
                        isSkip = false;
                    }
                }

                if (!isSkip) {
                    int index = node.getParent().getChildren().indexOf(node);
                    String onDoubleSubmissionAnnotation = "@OnDoubleSubmission" + "(path=\""
                            + this.activeProfile.getOnDoubleSubmissionPath() + "\")";

                    //publicメソッドに「@OnDoubleSubmission」を追加する
                    NodeUtil.addChildNode(node.getParent(), index,
                            Node.create(Node.T_ANNOTATION, onDoubleSubmissionAnnotation));
                }
            }

        }

    }

    /**
     * StrutsActionに固定のアダプタパーツを挿入する。
     * @param classNode クラスノード
     * @param curAction アクションクラス
     */
    private void insertStrutsActionInjectParts(Node classNode, StrutsAction curAction) {
        String module = curAction.getStrutsAnalyzeResult().getModule();

        VelocityContext context = new VelocityContext();
        context.put("errorjsp", StrutsAnalyzer.resolveForward(curAction.getInputAndResolveForward(),
                curAction.getStrutsAnalyzeResult()));

        context.put("module", module);
        context.put("action", curAction);
        context.put("forwardList", curAction.getForwardList());
        if (curAction.getForm() != null) {
            context.put("form", StringUtils.getClassShortName(curAction.getForm().getType()));
        }

        if(curAction.getValidate() == null) {
        	context.put("isValidate", true);
        } else {
        	context.put("isValidate", curAction.getValidate());
        }

        context.put("strutsConfig", curAction.getActionConfig());

        context.put("StringUtils", new StringUtils());
        if (curAction.getForm() != null) {
            ClassPathConvertUtil.getInstance().addImprt(curAction.getForm().getType());
        }
        StringWriter sw = new StringWriter();

        //テンプレートの作成
        Template template = this.velocityEngine.getTemplate(XENLON_S2N_CONVERT_XENLON_STRUTS_ACTION_INJECT_PARTS_VM,
                "UTF-8");
        //テンプレートとマージ
        template.merge(context, sw);
        //マージしたデータはWriterオブジェクトであるswが持っているのでそれを文字列として出力
        sw.flush();

        classNode.getChildren().add(0, Node.create(Node.T_BLOCK, sw.toString()));
    }

    /**
     * Route.xmlに出力用データを作成する。
     * @param executeAnnotation アノテーションノード
     * @param topNode トップノード
     * @param actionName アクション名
     * @param methodName メソッド名
     */
    private void insertRouteXml(AnnotationNodeUtil executeAnnotation, Node topNode, String actionName, String methodName) {
        String basePackage = this.activeProfile.getBasePackage();
        String packageName = NodeUtil.getPackageName(topNode);

        if(!packageName.contains(basePackage.trim()+".action")) {
            return;
        }


        //controllerを生成
        String controller = null;
        String path = "";
        if("".equals(packageName.substring((basePackage.trim()+".action").length()))){
            controller = actionName;
            //pathの先頭の文字を小文字にする
            path = Character.toLowerCase(actionName.charAt(0)) + actionName.substring(1);
        }else {
            controller = packageName.substring((basePackage.trim()+".action.").length()) + "." + actionName;
            //pathの先頭の文字を小文字にする
            path = packageName.substring((basePackage.trim()+".action.").length()) + "." + Character.toLowerCase(actionName.charAt(0)) + actionName.substring(1);
        }

        //actionを生成
        String action = methodName;
        //pathを生成
        String urlPtn = executeAnnotation.getStringValueWithoutQuote("urlPattern");
        if(!StringUtils.isEmpty(urlPtn)) {
            path = "/" + path.replaceAll("\\.", "\\/") + "/"
                    + urlPtn.replaceAll("\"", "").replaceAll("\\+", "").replaceAll("\\{", ":").replaceAll("\\}", "");
        }else {
            path = "/" + path.replaceAll("\\.", "\\/") + "/" + methodName;
        }

        Route route = new Route(path, "", controller, action, null);

        this.allRoutes.put(route.getPath(), route);
    }

    /**
     * SAStrutsActionに固定のアダプタパーツを挿入する。
     * @param methodNode メソッドノード
     * @param actionName アクション名
     * @param saForm フィールドノード
     * @param executeAnnotation アノテーションノード
     * @param topNode トップノード
     */
    private void insertSAStrutsActionInjectParts(Node methodNode, String actionName, FieldNodeUtil saForm,
            AnnotationNodeUtil executeAnnotation, Node topNode) {

        VelocityContext context = new VelocityContext();

        String validator = executeAnnotation.getStringValueWithoutQuote("validator");
        boolean isValidate = true;
        if (!StringUtils.isEmpty(validator)) {
            isValidate = Boolean.valueOf(validator);
        }

        context.put("input", executeAnnotation.getStringValueWithoutQuote("input"));

        String basePackage = this.activeProfile.getBasePackage();
        String packageName = NodeUtil.getPackageName(topNode);

        String errorPath = "";
        if(packageName.contains(basePackage.trim()+".action.")) {
            errorPath = packageName.substring((basePackage.trim()+".action.").length()) + "/" + Character.toLowerCase(actionName.charAt(0)) + actionName.substring(1);
        } else {
            errorPath = Character.toLowerCase(actionName.charAt(0)) + actionName.substring(1);
        }

        context.put("action", errorPath);

        if (saForm != null) {
            context.put("formClass", saForm.getClassName());
        }
        context.put("isValidate", isValidate);
        context.put("validate", executeAnnotation.getStringValueWithoutQuote("validate"));
        context.put("reset", executeAnnotation.getStringValueWithoutQuote("reset"));
        context.put("removeActionForm", executeAnnotation.getStringValueWithoutQuote("removeActionForm"));
        context.put("stopOnValidationError", executeAnnotation.getStringValueWithoutQuote("stopOnValidationError"));

        StringWriter sw = new StringWriter();

        //テンプレートの作成
        Template template = this.velocityEngine.getTemplate(XENLON_S2N_CONVERT_XENLON_SASTRUTS_ACTION_INJECT_PARTS_VM,
                "UTF-8");
        //テンプレートとマージ
        template.merge(context, sw);
        //マージしたデータはWriterオブジェクトであるswが持っているのでそれを文字列として出力
        sw.flush();
        int pos = methodNode.getParent().getChildren().indexOf(methodNode);

        NodeUtil.addChildNode(methodNode.getParent(), pos, Node.create(Node.T_BLOCK, sw.toString()));

    }

    /**
     * クラス名に対するアクション名を取得する。
     * @param className クラス名
     * @return Actionを除いたクラス名
     */
    private String getActionName(String className) {
        String subPath;
        if (className.endsWith("Action")) {
            subPath = className.substring(0, className.length() - "Action".length());
        } else {
            subPath = className;
        }
        return subPath;
    }

}
