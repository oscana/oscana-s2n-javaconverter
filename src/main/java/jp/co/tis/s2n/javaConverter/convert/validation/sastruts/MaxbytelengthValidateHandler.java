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
public class MaxbytelengthValidateHandler extends SAStrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode) {
        String maxlength = memberAnnotationNode.getStringValue("maxbytelength");

        if (contains("ByteLength")) {
            //既存マージ
            ValidateAnnotation ret = getSameValidation("ByteLength");
            ret.addIntegerParameter("max", maxlength);
            return null;
        } else {
            //新規作成
            ValidateAnnotation ret = new FieldValidateAnnotation("ByteLength");
            ret.addIntegerParameter("max", maxlength);
            return ret.addStringParameter("target", memberAnnotationNode.getStringValue("target"));

        }
    }

}
