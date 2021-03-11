package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * 数値の範囲チェックを行う。<br>
 * min以上max以下のint型であるかのチェックを行う。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class IntRangeValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {
        String maxValue = memberAnnotationNode.getStringValue("max");
        String minValue = memberAnnotationNode.getStringValue("min");
        return new FieldValidateAnnotation("Range").addIntegerParameter("max", maxValue).addIntegerParameter("min",
                minValue);

    }

}
