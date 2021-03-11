package jp.co.tis.form;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class ConvertTestForm {

    @AlphaNumericHyphenUnderbarType(target = "doNext1, doDecide")
    public Object field0;

    @AlphaNumericType(target = "doNext1, doDecide")
    public Object field1;

    @AlphaType(target = "doNext1, doDecide")
    public Object field2;

    @AsciiHalfKanaWin31jType(target = "doNext1, doDecide")
    public Object field3;

    @CapitalAlphaSpaceType(target = "doNext1, doDecide")
    public Object field4;

    @FullAlphaType(target = "doNext1, doDecide")
    public Object field5;

    @FullKanaType(target = "doNext1, doDecide")
    public Object field6;

    @FullType(target = "doNext1, doDecide")
    public Object field7;

    @FullWin31jType(target = "doNext1, doDecide")
    public Object field8;

    @HalfExclusiveHalfKanaType(target = "doNext1, doDecide")
    public Object field9;

    @HalfKanaType(target = "doNext1, doDecide")
    public Object field10;

    @HalfType(msg="errors.xxx",target = "doNext1, doDecide")
    public Object field11;

    @PositiveNumericHyphenType(msg="errors.xxx",target = "doNext1, doDecide")
    public Object field12;

    @PositiveNumericType(target = "doNext2, doDecide")
    public Object field13;

    @Fixlength(length = 10,target = "doNext2, doDecide")
    public Object field14;

    @ManageCodeExist(codeType = "0011", target = "doNext2, doDecide")
    public Object field15;

    @AlphaAndNumericType(target = "doNext1, doDecide")
    public Object field16;

    @DateFormat(format="yyyyMMdd",target = "doNext1, doDecide")
    public Object field17;

    @Required(target = "doNext1, doDecide")
    public Object field18;

    @Maxlength(target = "doNext1, doDecide")
    public Object field19;

    @LengthRange(target = "doNext1, doDecide")
    public Object filed20;

    @Maxbytelength(target = "doNext1, doDecide")
    public Object field21;

    @FullHalfType(target = "doNext1, doDecide")
    public Object field22;

    @Mask(mask = "\\d\\d-\\d\\d\\d\\d-\\d\\d\\d\\d", target = "doNext1, doDecide")
    public Object field23;

    @ByteLocale
    public Object field24;

    @FullHalfType(target = "doNext1, doDecide")
    @AsciiHalfKanaWin31jType(target = "doNext1, doDecide")
    public Object field25;

}

