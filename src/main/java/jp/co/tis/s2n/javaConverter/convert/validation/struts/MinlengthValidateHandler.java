package jp.co.tis.s2n.javaConverter.convert.validation.struts;

import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm.Field;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;

/**
 * 文字列の長さが、一定の長さ以上かのチェックを行う。<br>
 * それよりも短ければ、エラーになる。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class MinlengthValidateHandler extends StrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param curField 変換対象フィールド
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(Field curField) {
        String minlength = getVarValue(curField, "minlength");

        if (contains("Length")) {
            //既存マージ
            ValidateAnnotation ret = getSameValidation("Length");
            ret.addIntegerParameter("min", minlength);
            return null;
        } else {
            //新規作成
            ValidateAnnotation ret = new FieldValidateAnnotation("Length");
            ret.addIntegerParameter("min", minlength);
            return ret;

        }

    }

}
