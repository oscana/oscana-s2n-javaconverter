package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * 数値の範囲チェックを行う。<br>
 * min以上max以下のdouble型であるかのチェックを行う。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class DoubleRangeValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {
        String maxValue = memberAnnotationNode.getStringValueWithoutQuote("max");
        String minValue = memberAnnotationNode.getStringValueWithoutQuote("min");
        return new FieldValidateAnnotation("DecimalRange").addRegexpStringParameter("max", maxValue)
                .addRegexpStringParameter("min", minValue);

    }

}
