package jp.co.tis.s2n.javaConverter.convert.validation.annotation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.tis.s2n.javaConverter.node.Node;

/**
 * バリデーションアノテーションクラス。
 *
 * @author Fumihiko Yamamoto
 */
public abstract class ValidateAnnotation {

    // strutsのバリデーションに対して、improt文を追加
    protected Map<String, String> strutsAdditionalImportMap = new HashMap<>();

    {
        strutsAdditionalImportMap.put("ParseByte", "oscana.s2n.validation.ParseByte");
        strutsAdditionalImportMap.put("CreditCardNumber", "oscana.s2n.validation.CreditCardNumber");
        strutsAdditionalImportMap.put("ParseDate", "oscana.s2n.validation.ParseDate");
        strutsAdditionalImportMap.put("DecimalRange", "oscana.s2n.validation.DecimalRange");
        strutsAdditionalImportMap.put("ParseDouble", "oscana.s2n.validation.ParseDouble");
        strutsAdditionalImportMap.put("Email", "oscana.s2n.validation.Email");
        strutsAdditionalImportMap.put("DecimalRange", "oscana.s2n.validation.DecimalRange");
        strutsAdditionalImportMap.put("ParseFloat", "oscana.s2n.validation.ParseFloat");
        strutsAdditionalImportMap.put("ParseInt", "oscana.s2n.validation.ParseInt");
        strutsAdditionalImportMap.put("Range", "oscana.s2n.validation.Range");
        strutsAdditionalImportMap.put("ParseLong", "oscana.s2n.validation.ParseLong");
        strutsAdditionalImportMap.put("Pattern", "oscana.s2n.validation.Pattern");
        strutsAdditionalImportMap.put("Length", "oscana.s2n.validation.Length");
        strutsAdditionalImportMap.put("Required", "oscana.s2n.validation.Required");
        strutsAdditionalImportMap.put("ParseShort", "oscana.s2n.validation.ParseShort");
        strutsAdditionalImportMap.put("URL", "oscana.s2n.validation.URL");
    }

    /**
     * バリデーションアノテーション名
     */
    protected String name;
    private Map<String, ValidateAnnotationParameter> parameters = new LinkedHashMap<>();

    public ValidateAnnotation(String name) {
        this.name = name;
    }

    /**
     * バリデーションアノテーション名を取得する。
     * @return バリデーションアノテーション名
     */
    public String getName() {
        return name;
    }

    /**
     * String型のパラメータをセットする。<br>
     * String型パラメータはソース生成時にダブルクオートを伴って出力される。
     * @param key
     * @param value
     */
    public ValidateAnnotation addStringParameter(String key, String value) {
        if (value != null) {
            parameters.put(key, new StringValidateAnnotationParameter(key, value));
        }
        return this;
    }

    /**
     * Int型のパラメータをセットする。<br>
     * Int型パラメータはソース生成時にクオートせずに出力される。
     * @param key
     * @param value
     */
    public ValidateAnnotation addIntegerParameter(String key, String value) {
        if (value != null) {
            parameters.put(key, new IntegerValidateAnotationParameter(key, value));
        }
        return this;
    }

    /**
     * 正規表現のString型のパラメータをセットする。<br>
     * このパラメータはソース生成時にエスケープ処理を施したうえで、クオートを伴って出力される。
     * @param key
     * @param value
     */
    public ValidateAnnotation addRegexpStringParameter(String key, String value) {
        if (value != null) {
            parameters.put(key, new RegexpStringValidateAnnotationParameter(key, value));
        }
        return this;
    }

    /**
     * アノテーションを変換する。
     * @return 変換した結果
     */
    public String makeAnnotationStr() {

        StringBuffer sb = new StringBuffer();
        sb.append("@");
        sb.append(this.name);
        if (this.parameters.size() > 0) {
            sb.append("(");
            int count = 0;
            for (ValidateAnnotationParameter val : this.parameters.values()) {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append(val.export());
                count++;
            }
            sb.append(")");

        }
        return sb.toString();

    }

    /**
     * ノードにアノテーションを書き出す。
     * @param memberName メンバー名
     * @param methodName メソッド名
     * @param topNode トップノード
     * @param classNode クラスノード
     */
    public abstract void writeToNode(String memberName, String methodName, Node topNode, Node classNode);

}
