package jp.co.tis.s2n.javaConverter.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.parser.JavaParser;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * アノテーションノード用のアクセスフィルタ。<br>
 * アノテーションを表すノードに対して、アノテーションに依存した手順による属性読み書きを容易にするユーティリティクラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class AnnotationNodeUtil {

    private String name;

    private LinkedHashMap<String, Object> values;

    private static final String DEFAULT_NAME = "value";

    /**
     * アノテーション名を返す。
     * @return アノテーション名
     */
    public String getName() {
        return name;
    }

    /**
     * アノテーションのパラメータを返す。
     * @param key キー
     * @return キーに対する値
     */
    public Object getValue(String key) {
        return this.values.get(key);
    }

    /**
     * アノテーションののパラメータを文字列として返す。
     * @param key キー
     * @return キーに対する値(クオートがあればクオート付きで返す）
     */
    public String getStringValue(String key) {
        Object o = this.values.get(key);
        return (o != null) ? o.toString() : null;
    }

    /**
     * アノテーションののパラメータを文字列として返す。
     * @param key キー
     * @return キーに対する値（クオートがあればクオートを除去してから返す）
     */
    public String getStringValueWithoutQuote(String key) {
        String r = getStringValue(key);
        if (r != null) {
            r = StringUtils.delQuote(r.trim());
        }
        return r;
    }

    /**
     * ノードをアノテーションとしてパースする。
     * @param myNode ノード
     */
    public AnnotationNodeUtil(Node myNode) {


        LinkedHashMap<String, Object> list = new LinkedHashMap<>();

        int mode = 0;
        int pMode = 0; //パラメータと値のどちらを解析しているかのフラグ。 0...パラメータ解析時、1...値解析時
        int level = 0; //括弧レベル
        String paramName = "";
        String paramValue = "";
        for (Token ctk : myNode.getAllTokens()) {
            if (ctk.getType() == Token.SPACE) {
                //空白は読み飛ばす
                continue;
            }
            switch (mode) {
            case 0: //名前抽出
                if ((ctk.getType() == Token.NAME) && (ctk.getText().startsWith("@"))) {
                    this.name = ctk.getText().substring(1);
                    mode = 1;
                }
                break;
            case 1://パラメータ抽出
                if ((ctk.getType() == Token.SYMBOL)
                        && (ctk.getText().startsWith("(") || (ctk.getText().startsWith("{")))) {
                    level = level + 1;
                    if (level > 1) {
                        if (pMode == 2) {
                            paramValue = paramValue + ctk.getText();
                        } else {
                            paramName = paramName + ctk.getText();
                        }
                    }
                } else if ((ctk.getType() == Token.SYMBOL)
                        && (ctk.getText().startsWith(")") || (ctk.getText().startsWith("}")))) {
                    if (level > 1) {
                        if (pMode == 2) {
                            paramValue = paramValue + ctk.getText();
                        } else {
                            paramName = paramName + ctk.getText();
                        }
                    } else if (level == 1) {
                        if (pMode != 2) {
                            //値だけが記述されているパターン
                            paramValue = paramName;
                            paramName = DEFAULT_NAME;
                        }
                        createParamPair(list, paramName, paramValue);
                        paramValue = "";
                    }
                    level = level - 1;
                } else {
                    if (level == 1) {

                        switch (pMode) {
                        case 0://名前抽出
                            if (ctk.getType() == Token.EQ1) {
                                pMode = 2;
                            } else {
                                paramName = paramName + ctk.getText();
                            }
                            break;

                        case 2://データ
                            if ((ctk.getType() == Token.SYMBOL) && (ctk.getText().startsWith(","))) {
                                createParamPair(list, paramName, paramValue);
                                paramName = "";
                                paramValue = "";
                                pMode = 0;
                            } else {
                                paramValue = paramValue + ctk.getText();
                            }

                        default:
                            break;
                        }
                    } else {
                        //下位レベルはそのまま
                        if (pMode == 2) {
                            paramValue = paramValue + ctk.getText();
                        } else {
                            paramName = paramName + ctk.getText();

                        }
                    }

                }

            default:
                break;
            }

            this.values = list;

        }

    }

    /**
     * このノードのシグネチャを取得する。
     * @return シグネチャ
     */
    public String getSignature() {
        List<String> keys = new ArrayList<String>();
        for (String key : values.keySet()) {
            keys.add(key);
        }
        Collections.sort(keys);
        StringBuffer sb = new StringBuffer();
        for (String key : keys) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(key);
        }
        return sb.toString();
    }

    private void createParamPair(LinkedHashMap<String, Object> list, String paramName, String paramValue) {

        if (paramValue.startsWith("@")) {
            AnnotationNodeUtil v = new AnnotationNodeUtil(makeNode(paramValue));
            list.put(paramName, v);
        } else {
            list.put(paramName, paramValue);
        }
    }

    /**
     * 文字列からノードを取得する。
     * @param src 文字列
     * @return ノード
     */
    public static Node makeNode(String src) {
        return JavaParser.parse(src).getChildren().get(0);

    }

}
