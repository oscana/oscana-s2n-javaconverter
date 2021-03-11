package jp.co.tis.s2n.javaConverter.convert;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.co.tis.s2n.javaConverter.convert.common.TestSecurityManager;
import jp.co.tis.s2n.javaConverter.convert.common.TestSecurityManager.ExitException;

/**
 * Routeファイルの出力テスト。
 *
 */
public class RouteXmlGenerateTest {

    private PrintStream defaultPrintStream;
    private SecurityManager securityManager;
    ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        // SecurityManager退避
        securityManager = System.getSecurityManager();
        // エラー出力退避
        defaultPrintStream = System.err;

        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(new BufferedOutputStream(byteArrayOutputStream)));
        System.setSecurityManager(new TestSecurityManager());

    }

    /**
     * Route.Xmlファイルを生成すること
     */
    @Test
    public void testRouteXmlGenerate() {
        try {
            String[] args = new String[] { "src/test/resources/RouteXmlGenerateTest/RouteXmlGenerate.properties",
                    "src/test/resources/RouteXmlGenerateTest/RouteXmlGenerate.csv" };
            ConvertStruts2Nablarch.main(args);

        } catch (ExitException ite) {
            // 戻り値を確認
            assertEquals(0, ite.getStatus());

            List<String> expectFile = getFileNames(
                    new File("src/test/resources/RouteXmlGenerateTest/java/expect").getAbsolutePath());
            List<String> toFile = getFileNames(
                    new File("src/test/resources/RouteXmlGenerateTest/java/to").getAbsolutePath());

            // ファイルの数を比較する
            assertEquals(expectFile.size(), toFile.size());

            for (int i = 0; i < expectFile.size(); i++) {
                //　ファイルの中身を比較する
                assertTrue(fileCompare(expectFile.get(i), toFile.get(i)));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        System.setOut(defaultPrintStream);
        System.setSecurityManager(securityManager);
    }

    private boolean fileCompare(String fileA, String fileB) {
        boolean bRet = false;
        try {
            bRet = Arrays.equals(Files.readAllBytes(Paths.get(fileA)), Files.readAllBytes(Paths.get(fileB)));
        } catch (IOException e) {
            return false;
        }
        return bRet;
    }

    private List<String> getFileNames(String path) {
        File dir = new File(path);

        File[] files = dir.listFiles();
        if (files == null) {
            return null;
        }

        List<String> listFileNames = new ArrayList<>();

        for (File file : files) {
            if (file.isFile()) {

                listFileNames.add(file.getAbsolutePath());
            }
        }
        listFileNames.forEach(t -> {
            System.out.println(t);
        });
        return listFileNames;
    }
}
