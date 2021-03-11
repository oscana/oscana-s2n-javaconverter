package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * Ascii、半角カナ、Win31jの範囲の文字のみかを検証するためのアノテーション。
 * @author Fumihiko Yamamoto
 *
 */
public class AsciiHalfKanaWin31jTypeValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {

        return new FieldValidateAnnotation("SystemChar").addStringParameter("charsetDef", "\"システム許容文字\"")
                .addStringParameter("message", "\"{errors.asciiHalfKanaWin31jType}\"");

    }

}
