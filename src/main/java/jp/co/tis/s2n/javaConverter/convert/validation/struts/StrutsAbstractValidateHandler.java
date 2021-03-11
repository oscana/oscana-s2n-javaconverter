package jp.co.tis.s2n.javaConverter.convert.validation.struts;

import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm.Field;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm.Field.Var;
import jp.co.tis.s2n.javaConverter.convert.validation.AbstractValidateHandler;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;

/**
 * バリデーションの書き換えロジックのAbstractクラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public abstract class StrutsAbstractValidateHandler extends AbstractValidateHandler {
    public abstract ValidateAnnotation handle(Field curField);

    /**
     * フィールドに関連付けられたvar値を返す。
     * @param curField 変換対象フィールド
     * @param string 関連文字列
     * @return 変換結果
     */
    protected String getVarValue(Field curField, String string) {
        Var var = curField.getVarList().get(string);
        if (var == null) {
            return null;
        } else {
            return var.getVarValue();
        }
    }

}
