package jp.co.tis.s2n.javaConverter.convert.validation.annotation;

import java.util.List;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.convert.statistics.OtherStatistics;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;

/**
 * フィールドに対するバリデーションアノテーション。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class FieldValidateAnnotation extends ValidateAnnotation {

    public FieldValidateAnnotation(String name) {
        super(name);
    }

    /**
     * アクセッサまたはメンバに対して書き出す。
     * @param memberName メンバー名
     * @param methodName メソッド名
     * @param topNode トップノード
     * @param classNode クラスノード
     */
    public void writeToNode(String memberName, String methodName, Node topNode, Node classNode) {

        //メソッドしていならアクセッサに対して設定を行う（なければメンバに対して書き出す）
        if (methodName != null) {
            //メソッドへの書き込み
            List<Node> blockNodeList = NodeUtil.findAllNode(topNode, Node.T_BLOCK);
            for (Node blockNode : blockNodeList) {
                if (StringUtils.mkGetterName(methodName).equals(NodeUtil.getBlockName(blockNode))) {
                    //メソッド検出したので書き込む
                    int pos = blockNode.getParent().getChildren().indexOf(blockNode);
                    NodeUtil.addChildNode(blockNode.getParent(), pos,
                            Node.create(Node.T_ANNOTATION, makeAnnotationStr()));

                    // 利用しているバリデーションのimprot文を追加
                    if(strutsAdditionalImportMap.containsKey(this.name)) {
                        ClassPathConvertUtil.getInstance().addImprt(strutsAdditionalImportMap.get(this.name));
                    }
                    OtherStatistics.getInstance().makeMethoddAnnotation++;
                    return;
                }
            }
        }
        String trgName = (memberName != null) ? memberName : methodName;

        //メンバーへの書き込み
        List<Node> memberNodeList = NodeUtil.findAllNode(topNode, Node.T_MEMBER);
        for (Node memberNode : memberNodeList) {
            if (trgName.equals(NodeUtil.getMemberName(memberNode))) {
                //メンバー検出したので書き込む
                int pos = memberNode.getParent().getChildren().indexOf(memberNode);
                NodeUtil.addChildNode(memberNode.getParent(), pos, Node.create(Node.T_ANNOTATION, makeAnnotationStr()));
                OtherStatistics.getInstance().makeFiledAnnotation++;
                return;
            }
        }

    }

}
