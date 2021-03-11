package jp.co.tis.s2n.javaConverter.convert.validation;

import java.util.Map;

import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;

/**
 * バリデーションアノテーションの基礎クラス。
 *
 * @author Fumihiko Yamamoto
 */
public class AbstractValidateHandler {

    public static String HANDLER_SUFFIX = "ValidateHandler";
    /**
     * 最後に出力するアノテーションを追加で登録するためのリストを用意する
     * 例：ValidWhenは各メンバにアノテーションを出力した後、最後にクラスに追加でアノテーションを出力する
     *     このようなケースで使用する
     */
    protected Map<String, ValidateAnnotation> addionalAnnotationList;
    /**
     * 現在処理しているフィールドにあてられているアノテーション
     * 複数のバリデーションを１つに統合するときなどに使用する
     */
    protected Map<String, ValidateAnnotation> fieldAnnotationList;

    public AbstractValidateHandler() {
        super();
    }

    /**
     * 現在処理しているフィールドにあてられているアノテーションを設定する。
     * @param annotationList 現在処理しているフィールドにあてられているアノテーションのリスト
     */
    public void setFieldAnnotationList(Map<String, ValidateAnnotation> annotationList) {
        this.fieldAnnotationList = annotationList;

    }

    /**
     * 指定したものと同じタイプのバリデーションアノテーションが作成済かどうかをチェックする。
     * @param validationName バリデーション名
     * @return チェック結果 同じタイプのバリデーション：true、その以外：false
     */
    protected boolean contains(String validationName) {

        return this.fieldAnnotationList.containsKey(validationName);
    }

    /**
     * 指定したものと同じタイプのバリデーションアノテーションを取得する。<br>
     * 作成済であればそれを返し、未作成であればnullにて応答する。
     * @param validationName バリデーション名
     * @return バリデーションアノテーション
     */
    protected ValidateAnnotation getSameValidation(String validationName) {
        return this.fieldAnnotationList.get(validationName);
    }

}