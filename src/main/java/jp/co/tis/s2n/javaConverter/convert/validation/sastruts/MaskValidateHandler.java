package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * 値が指定した正規表現にマッチするかどうかのチェックを行う。<br>
 * マッチしなければエラーになる。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class MaskValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {
        String mask = memberAnnotationNode.getStringValue("mask");
        return new FieldValidateAnnotation("Pattern").addStringParameter("regexp", mask)
                .addStringParameter("target", memberAnnotationNode.getStringValue("target"));
    }

}
