package jp.co.tis.s2n.javaConverter.node;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.co.tis.s2n.javaConverter.parser.JavaParser;

/**
 * {@link AnnotationNodeUtil}のテスト。
 */
public class AnnotationNodeUtilTest {

    /**
     * Javaソースコードをパースすること
     */
    @Test
    public void testParseStd() {

        AnnotationNodeUtil an1 = new AnnotationNodeUtil(JavaParser
                .parse("@Required(arg0=@Arg(key = \"パラメータ1\", resource = false),data1=\"size\", data2=\"あああ\")=")
                .getChildren().get(0));
        assertEquals("Required", an1.getName());

        AnnotationNodeUtil an1s = (AnnotationNodeUtil) an1.getValue("arg0");
        assertEquals("パラメータ1", an1s.getStringValueWithoutQuote("key"));
        assertEquals("false", an1s.getStringValueWithoutQuote("resource"));
        assertEquals("size", an1.getStringValueWithoutQuote("data1"));
        assertEquals("あああ", an1.getStringValueWithoutQuote("data2"));

        AnnotationNodeUtil an2 = new AnnotationNodeUtil(JavaParser.parse(
                "@Generated(value = {\"S2JDBC-Gen 2.4.46\", \"org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl\"}, date = \"2013/08/13 10:28:31\")")
                .getChildren().get(0));
        assertEquals("Generated", an2.getName());
        assertEquals("{\"S2JDBC-Gen 2.4.46\",\"org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl\"}",
                an2.getStringValueWithoutQuote("value"));
        assertEquals("2013/08/13 10:28:31", an2.getStringValueWithoutQuote("date"));

        AnnotationNodeUtil an3 = new AnnotationNodeUtil(
                JavaParser.parse("@ValuesAnnotation(\"Test\")").getChildren().get(0));
        assertEquals("ValuesAnnotation", an3.getName());
        assertEquals("Test", an3.getStringValueWithoutQuote("value"));

        AnnotationNodeUtil an4 = new AnnotationNodeUtil(JavaParser.parse(
                "@JoinColumns( { @JoinColumn(name = \"customer_system_id\", referencedColumnName = \"customer_system_id\"), @JoinColumn(name = \"order_serial_no\", referencedColumnName = \"order_serial_no\") })")
                .getChildren().get(0));
        assertEquals("JoinColumns", an4.getName());

    }
}
