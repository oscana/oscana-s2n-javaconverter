package jp.co.tis.s2n.javaConverter.convert.validation.struts;

import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm.Field;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.FieldValidateAnnotation;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;

/**
 * クレジットカード形式であるかのチェックを行う。
 *
 * @author Fumihiko Yamamoto
 */
public class CreditCardValidateHandler extends StrutsAbstractValidateHandler {

    /**
     * 変換主処理。
     * @param curField 変換対象フィールド
     * @return 変換結果
     */
    @Override
    public ValidateAnnotation handle(Field curField) {
        return new FieldValidateAnnotation("CreditCardNumber");
    }

}
