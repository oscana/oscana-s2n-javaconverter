package jp.co.tis.s2n.javaConverter.convert.validation.annotation;

/**
 * 正規表現文字列を格納するString型のパラメータ。
 *
 * @author Fumihiko Yamamoto
 *
 */
class RegexpStringValidateAnnotationParameter implements ValidateAnnotationParameter {
    public RegexpStringValidateAnnotationParameter(String name, String value) {
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
        if(!this.value.startsWith("\"")) {
        	sb.append("\"");
        }
        sb.append(this.value.replaceAll("\\\\", "\\\\\\\\"));
        if(!this.value.endsWith("\"")) {
        	sb.append("\"");
        }
        return sb.toString();
    }
}