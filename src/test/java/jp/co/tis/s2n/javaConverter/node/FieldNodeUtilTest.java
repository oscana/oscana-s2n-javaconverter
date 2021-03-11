package jp.co.tis.s2n.javaConverter.node;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.co.tis.s2n.javaConverter.parser.JavaParser;

/**
 * {@link FieldNodeUtil}のテスト。
 */
public class FieldNodeUtilTest {

    /**
     * 正常系ケース(1):Javaソースコードをパースすること
     */
    @Test
    public void testParseStd() {

        FieldNodeUtil f;
        Node node;

        node = JavaParser.parse("private String[] datas;").getChildren().get(0);
        f = new FieldNodeUtil(node);

        assertEquals("String[]", f.getClassName());
        assertEquals("datas", f.getFieldName());

        node = JavaParser.parse("private List<DetailOfSalesDto> moneyUtilizationDetailBeforeLastList;").getChildren()
                .get(0);
        f = new FieldNodeUtil(node);

        assertEquals("List<DetailOfSalesDto>", f.getClassName());
        assertEquals("moneyUtilizationDetailBeforeLastList", f.getFieldName());

        node = JavaParser.parse("public String dataParam;").getChildren().get(0);
        f = new FieldNodeUtil(node);

        assertEquals("String", f.getClassName());
        assertEquals("dataParam", f.getFieldName());

        node = JavaParser.parse("public static String KEY_DATA = \"test\";").getChildren().get(0);
        f = new FieldNodeUtil(node);

        assertEquals("String", f.getClassName());
        assertEquals("KEY_DATA", f.getFieldName());

        node = JavaParser.parse("private List<Map<String,DetailOfSalesDto>> moneyUtilizationDetailBeforeLastList;")
                .getChildren().get(0);
        f = new FieldNodeUtil(node);

        assertEquals("List<Map<String,DetailOfSalesDto>>", f.getClassName());
        assertEquals("moneyUtilizationDetailBeforeLastList", f.getFieldName());

    }

    /**
     * 異常系、Javaソースコードをパースしないこと
     */
    @Test
    public void testParseErr() {
        Node node;

        node = JavaParser.parse("private List<DetailOfSalesDto<> moneyUtilizationDetailBeforeLastList;").getChildren()
                .get(0);
        try {
            new FieldNodeUtil(node);
            fail(); //エラーが出なかったのはFail
        } catch (Throwable e) {
            assertTrue(e instanceof RuntimeException);
        }

    }

    /**
     * 正常系ケース(2):Javaソースコードをパースすること
     */
    @Test
    public void testUtilMethod() {
        FieldNodeUtil f;
        Node node;

        node = JavaParser.parse("protected Map<T> data;").getChildren().get(0);
        f = FieldNodeUtil.parse(node);
        assertEquals("Map<T>", f.getClassName());
        assertEquals("data", f.getFieldName());

        node = JavaParser.parse("protected Object< data;").getChildren().get(0);
        f = FieldNodeUtil.parse(node);
        assertEquals(null, f);

    }

    /**
     * 正常系ケース(3):Javaソースコードをパースすること
     */
    @Test
    public void testEndNotSemicolon() {
        FieldNodeUtil f;
        Node node;

        node = JavaParser.parse("protected Map<T> data").getChildren().get(0);
        f = FieldNodeUtil.parse(node);
        assertEquals("Map<T>", f.getClassName());
        assertEquals("data", f.getFieldName());

        node = JavaParser.parse("Map<T> data").getChildren().get(0);
        f = FieldNodeUtil.parse(node);
        assertEquals("Map<T>", f.getClassName());
        assertEquals("data", f.getFieldName());

        node = JavaParser.parse("protected Object< data").getChildren().get(0);
        f = FieldNodeUtil.parse(node);
        assertEquals(null, f);

        node = JavaParser.parse("protected Object[ data").getChildren().get(0);
        f = FieldNodeUtil.parse(node);
        assertEquals(null, f);

    }

    /**
     * 正常系ケース(4):Javaソースコードをパースすること
     */
    @Test
    public void testChangeClass() {

        FieldNodeUtil f;
        Node node;

        node = JavaParser.parse("public String data").getChildren().get(0);
        f = FieldNodeUtil.parse(node);
        assertEquals("String", f.getClassName());
        assertEquals("data", f.getFieldName());

        FieldNodeUtil.setFieldClass(node, "Date");
        assertEquals("public Date data", node.getStringWithoutCRLF());

        node = JavaParser.parse("public int[] data").getChildren().get(0);
        f = FieldNodeUtil.parse(node);
        assertEquals("int[]", f.getClassName());
        assertEquals("data", f.getFieldName());

        FieldNodeUtil.setFieldClass(node, "String");
        assertEquals("public String data", node.getStringWithoutCRLF());

        node = JavaParser.parse("public int[] data = new Object();").getChildren().get(0);
        f = FieldNodeUtil.parse(node);
        assertEquals("int[]", f.getClassName());
        assertEquals("data", f.getFieldName());

        FieldNodeUtil.setFieldClass(node, "String");
        assertEquals("public String data = new Object();", node.getStringWithoutCRLF());

    }

}
