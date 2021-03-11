package jp.co.tis.s2n.javaConverter.convert.validation.struts;

import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm.Field;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;

/**
 * 数値の範囲チェックを行う。<br>
 * min以上max以下のdouble型であるかのチェックを行う。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class DoubleRangeValidateHandler extends StrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param curField 変換対象フィールド
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(Field curField) {
        String maxValue = getVarValue(curField, "max");
        String minValue = getVarValue(curField, "min");
        return new FieldValidateAnnotation("DecimalRange").addRegexpStringParameter("max", maxValue)
                .addRegexpStringParameter("min", minValue);

    }

}
