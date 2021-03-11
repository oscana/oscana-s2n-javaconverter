package jp.co.tis.s2n.javaConverter.convert.validation.annotation;

/**
 * Int型のパラメータ。
 *
 * @author Fumihiko Yamamoto
 *
 */
class IntegerValidateAnotationParameter implements ValidateAnnotationParameter {
    public IntegerValidateAnotationParameter(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    String name;
    String value;

    /**
     * データ型に応じて書き出す。
     * @return 出力文字列
     */
    @Override
    public String export() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.name);
        sb.append(" = ");
        sb.append(this.value);
        return sb.toString();
    }
}