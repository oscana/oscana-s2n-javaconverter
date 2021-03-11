package jp.co.tis.s2n.javaConverter.convert.validation.annotation;

/**
 * コメントアウトされたフィールドに対するバリデーションアノテーション。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class CommentoutFieldValidateAnnotation extends FieldValidateAnnotation {

    public CommentoutFieldValidateAnnotation(String name) {
        super(name);
    }

    /* コメントアウトした結果を出力する。
     * @see jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation#makeAnnotationStr()
     */
    @Override
    public String makeAnnotationStr() {
        return "//" + super.makeAnnotationStr();
    }

}
