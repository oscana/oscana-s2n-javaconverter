package from;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class FormForConvertTest {

    @AlphaNumericHyphenUnderbarType(target = "doNext1, doDecide")
    public Object field0;

    @AlphaNumericType(target = doNext1, doDecide)
    public Object field1;

    @Reset()
    public String field2;

    @Url()
    public String field2;

}

