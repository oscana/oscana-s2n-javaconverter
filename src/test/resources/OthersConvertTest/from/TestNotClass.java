package jp.co.tis.others;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestNotClass {

    boolean value() default "";


}
