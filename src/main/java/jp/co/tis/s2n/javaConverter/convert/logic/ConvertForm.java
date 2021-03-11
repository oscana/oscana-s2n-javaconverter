package jp.co.tis.s2n.javaConverter.convert.logic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzeResult;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm.Field;
import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.struts.StrutsAbstractValidateHandler;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;

/**
 * Fromクラスの変換処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ConvertForm extends ConvertBase {

    /**
     * Formクラス変換の主処理。
     * @param fileName ファイル名
     * @param topNode トップノード
     * @throws Exception 例外
     */
    @Override
    public void convertProc(String fileName, Node topNode) throws Exception {

        convertXenlonActionForm(topNode, null);

        super.convertProc(fileName, topNode);

        List<Node> classNodeList = NodeUtil.findAllNode(topNode, Node.T_CLASS);
        Node classNode = classNodeList.get(0);

        classNode.addCrLf();

        //完全修飾クラス名特定
        String packageName = NodeUtil.getPackageName(topNode);
        makeClassNameResolver(topNode, packageName);

        String className = NodeUtil.getClassName(topNode);
        String fullClassName = StringUtils.addClassPackage(packageName, className);

        //Struts.xmlに基づく変換
        if (this.strutsAnalyzeResult != null) {
            convertStrutsActionForm(topNode, classNode, fullClassName);
        }
        //メンバに付与されたバリデーションアノテーションの置換(SAStruts向け)
        convertSAStrutsValidateAnnotation(topNode, classNode, fullClassName);

        //クラスDIアノテーション登録
        addScopeAnotationToClass(classNode, "@RequestScoped");

        //ファイルの種類によらない変換を実施
        convertCommon(topNode, fileName, classNode);

        //追加import挿入
        insertStrutsImportInjectParts(topNode, "Form");

        if (activeProfile.getConvertMode() == S2nProfile.CONVERT_MODE_STRUTS) {
            //Strutsならこれ以下の処理はやらない
            return;
        }
        //アクセッサの付与
        addAccessor(topNode, classNode);
    }

    /**
     * Struts.xmlに基づく変換を実施する。
     * @param topNode トップノード
     * @param classNode クラスノード
     * @param fullClassName 完全修飾クラス名
     * @throws InstantiationException 例外
     * @throws IllegalAccessException 例外
     */
    private void convertStrutsActionForm(Node topNode, Node classNode, String fullClassName)
            throws InstantiationException, IllegalAccessException {
        StrutsForm form = null;
        for (StrutsAnalyzeResult analyzeResult : this.strutsAnalyzeResult) {
            StrutsForm cform = analyzeResult.getFormList().get(fullClassName);
            if (cform != null) {
                form = cform;
                break;
            }
        }
        println("StrutsForm置換:" + fullClassName);
        if (form != null) {

            //classアノテーションの挿入
            insertScopeAnnotation(classNode, form);

            //メンバアノテーションの挿入
            insertMemberAnnotation(topNode, form, classNode);
        }
    }

    /**
     * メンバに対するバリデーション用アノテーションの付与。（Struts共通処理）
     * @param topNode トップノード
     * @param form Strutsフォーム
     * @param classNode クラスノード
     * @throws InstantiationException 例外
     * @throws IllegalAccessException 例外
     */
    private void insertMemberAnnotation(Node topNode, StrutsForm form, Node classNode)
            throws InstantiationException, IllegalAccessException {

        if (form.getFieldList() != null) {

            for (Field curField : form.getFieldList().values()) {
                String memberName = curField.getProperty();

                List<String> dependList = curField.getDependList();
                Map<String, ValidateAnnotation> fieldAnnotationList = new LinkedHashMap<String, ValidateAnnotation>();

                for (Iterator<String> itr = dependList.iterator(); itr.hasNext();) {
                    String depend = itr.next();
                    StrutsAbstractValidateHandler handler = ConvertFactory.getStrutsAbstractValidateHandler(depend);

                    if (handler != null) {
                        handler.setFieldAnnotationList(fieldAnnotationList);
                        ValidateAnnotation resultValidateAnnotation = handler.handle(curField);
                        if (resultValidateAnnotation != null) {
                            //戻り値がnullの場合は既存のものにマージ済とみなして登録しない
                            if (!fieldAnnotationList.containsKey(resultValidateAnnotation.getName())) {
                                fieldAnnotationList.put(resultValidateAnnotation.getName(), resultValidateAnnotation);
                            }
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
     * クラスに対するスコープアノテーションの付与。
     * @param classNode クラスノード
     * @param form Strutsフォーム
     */
    private void insertScopeAnnotation(Node classNode, StrutsForm form) {
        String cAnnotation = null;
        switch (StringUtils.toLowerCase(form.getAction().getScope())) {
        case "request":
            cAnnotation = "@RequestScoped";
            break;
        case "session":
            cAnnotation = "@SessionScoped";
            break;
        default:
            break;
        }
        if (cAnnotation != null) {
            addScopeAnotationToClass(classNode, cAnnotation);
        }
    }

}
