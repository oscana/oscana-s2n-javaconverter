package jp.co.tis.s2n.javaConverter.convert.validation.struts;

import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm.Field;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;

/**
 * Date型に変換可能であるかのチェックを行う。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class DateValidateHandler extends StrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param curField 変換対象フィールド
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(Field curField) {
        String datePattern = getVarValue(curField, "datePatternStrict");
        if(datePattern == null) {
            datePattern = getVarValue(curField, "datePattern");
        }

        return new FieldValidateAnnotation("ParseDate").addStringParameter("datePattern", datePattern);
    }

}
