package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * 文字列長が固定桁数であるかを検証するためのアノテーション。
 * @author Fumihiko Yamamoto
 *
 */
public class FixlengthValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {
        String len = memberAnnotationNode.getStringValue("length");
        return new FieldValidateAnnotation("Fixlength").addIntegerParameter("length", len).addStringParameter("target", memberAnnotationNode.getStringValue("target"));

    }

}
