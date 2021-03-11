package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * 半角英大文字、半角スペースのみかを検証するためのアノテーション。
 * @author Fumihiko Yamamoto
 */
public class CapitalAlphaSpaceTypeValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {
        return new FieldValidateAnnotation("SystemChar").addStringParameter("charsetDef", "\"半角英字（大文字）・半角スペース\"")
                .addStringParameter("message", "\"{errors.capitalAlphaSpaceType}\"");
    }

}
