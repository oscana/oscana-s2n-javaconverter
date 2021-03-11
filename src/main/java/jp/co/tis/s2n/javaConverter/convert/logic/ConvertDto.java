package jp.co.tis.s2n.javaConverter.convert.logic;

import java.util.List;

import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;

/**
 * Dtoクラスの変換処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ConvertDto extends ConvertBase {

    /**
     * Dtoクラス変換の主処理。
     * @param fileName ファイル名
     * @param topNode トップノード
     * @throws Exception 例外
     */
    @Override
    public void convertProc(String fileName, Node topNode) throws Exception {
        super.convertProc(fileName, topNode);
        List<Node> classNodeList = NodeUtil.findAllNode(topNode, Node.T_CLASS);
        Node classNode = classNodeList.get(0);

        //完全修飾クラス名特定
        String packageName = NodeUtil.getPackageName(topNode);
        String className = NodeUtil.getClassName(topNode);
        String fullClassName = StringUtils.addClassPackage(packageName, className);
        //メンバに付与されたバリデーションアノテーションの置換(SAStruts向け)
        convertSAStrutsValidateAnnotation(topNode, classNode, fullClassName);

        addScopeAnotationToClass(classNode, "@RequestScoped");
        convertProcCommon(fileName, topNode, "Dto");

        if (activeProfile.getConvertMode() == S2nProfile.CONVERT_MODE_STRUTS) {
            //Strutsならこれ以下の処理はやらない
            return;
        }
        //アクセッサの付与
        addAccessor(topNode, classNode);
    }


}
