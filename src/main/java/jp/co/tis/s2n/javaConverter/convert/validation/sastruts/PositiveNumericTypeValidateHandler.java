package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * 半角の正の数字のみかを検証するためのアノテーション。
 * @author Fumihiko Yamamoto
 *
 */
public class PositiveNumericTypeValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {

        return new FieldValidateAnnotation("SystemChar").addIntegerParameter("charsetDef", "\"半角数字\"")
                .addIntegerParameter("message", "\"{errors.positiveNumericType}\"").addStringParameter("target", memberAnnotationNode.getStringValue("target"));

    }

}
