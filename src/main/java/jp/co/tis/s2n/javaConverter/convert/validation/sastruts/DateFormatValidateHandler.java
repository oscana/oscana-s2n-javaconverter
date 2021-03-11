package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * Date型に変換可能であるかのチェックを行う。
 * @author Fumihiko Yamamoto
 *
 */
public class DateFormatValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {

        String datePattern = memberAnnotationNode.getStringValue("format");

        return new FieldValidateAnnotation("DateFormat").addStringParameter("format", datePattern)
                .addStringParameter("message", "\"{errors.dateformat}\"").addStringParameter("target", memberAnnotationNode.getStringValue("target"));
    }

}
