package jp.co.tis.s2n.javaConverter.convert.validation.annotation;

/**
 * アノテーションパラメータクラス。<br>
 *
 * ValidateAnnotaionが保持するパラメータを保持する。
 *
 * @author Fumihiko Yamamoto
 *
 */
interface ValidateAnnotationParameter {
    /**
     * データ型に応じて書き出す。
     * @return 出力文字列
     */
    String export();
}