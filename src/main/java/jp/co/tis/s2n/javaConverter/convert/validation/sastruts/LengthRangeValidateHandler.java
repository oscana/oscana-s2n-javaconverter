package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/*
 * 文字列の長さが、一定の長さ以下かのチェックを行う。<br>
 * それよりも長ければ、エラーになる。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class LengthRangeValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {
        String minlength = memberAnnotationNode.getStringValue("minlength");
        String maxlength = memberAnnotationNode.getStringValue("maxlength");

        ValidateAnnotation ret = new FieldValidateAnnotation("LengthRange");
        if (!"".equals(minlength) && minlength != null) {
            ret.addIntegerParameter("minlength", minlength);
        }
        if (!"".equals(maxlength) && maxlength != null) {
            ret.addIntegerParameter("maxlength", maxlength);
        }
        return ret.addStringParameter("target", memberAnnotationNode.getStringValue("target"));

    }

}
