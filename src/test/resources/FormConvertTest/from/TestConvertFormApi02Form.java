package from;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertFormApi02Form {

    @ByteType
    public Object field0;

    @CreditCardType
    @DateType(datePatternStrict = true)
    public Object field1;

    @DateType
    public Object field2;

    @DoubleRange
    public Object field3;

    @DoubleType
    public Object field4;

    @EmailType
    public Object field5;

    @FloatRange
    public Object field6;

    @FloatType
    public Object field7;

    @IntegerType
    public Object field8;

    @IntRange
    @LongType
    public Object field9;

    @LongRange
    @Maxbytelength
    public Object field10;

    @Maxbytelength
    @Maxbytelength
    public Object field11;

    @Maxlength
    @Maxlength
    public Object field12;

    @Minbytelength
    @Minbytelength
    public Object field13;

    @Minlength
    @Minlength
    public Object field14;

    @ShortType
    public Object field14;

    @UrlType
    public Object field15;

}

