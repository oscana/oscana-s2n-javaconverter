package jp.co.tis.s2n.javaConverter.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.javaConverter.keyword.JavaKeyword;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;
import jp.co.tis.s2n.javaConverter.token.Token;
import jp.co.tis.s2n.javaConverter.token.TokenMgr;

/**
 * Nodeツリー組み立て<br>
 * ファイルを読み込み、Tokenに分解したうえで、Nodeツリーを組み立てて返す。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class JavaParser implements JavaKeyword {

    /**
     * Stringとして入力したJavaソースコードをパースし、Nodeを取得する。<br>
     * 文字列はテンポラリファイルと同等の前処理が完了したものを入力する。
     * @param lineStr Javaソース文字列
     * @return ノード
     */
    public static Node parse(String lineStr) {
        return parse(lineStr, true);
    }

    /**
     * Stringとして入力したJavaソースコードをパースし、Nodeを取得する。<br>
     * 文字列はテンポラリファイルと同等の前処理が完了したものを入力する。
     * @param lineStr Javaソース文字列
     * @param clearFlag コメント・改行を削除するならtrue
     * @return ノード
     */
    public static Node parse(String lineStr, boolean clearFlag) {

        String fileName = "TempForStr.java";

        String clazzName = fileName.replace(".java", "");

        JavaParseCtrl parseCtrl = new JavaParseCtrl(clazzName);

        StringReader sr = new StringReader(lineStr);

        try {
            parseMain(sr, parseCtrl, fileName);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        if (clearFlag) {
            NodeUtil.clearCrLfAndComment1(parseCtrl.tNode);
        }

        return parseCtrl.tNode;

    }

    /**
     * ファイルとして入力したJavaソースコードをパースし、Nodeを取得する。<br>
     * ファイルはテンポラリファイルとして前処理が完了したものを入力する。
     * @param inputPath 対象ファイルのあるディレクトリ
     * @param fileName 読み込むファイル（テンポラリファイルとして標準化されたもの）
     * @param codeName ファイルのエンコード
     * @return ノード
     */
    public static Node parse(String inputPath, String fileName, String codeName) {
        return parse(inputPath, null, fileName, codeName, true);
    }

    /**
     * ファイルとして入力したJavaソースコードをパースし、Nodeを取得する。<br>
     * ファイルはテンポラリファイルとして前処理が完了したものを入力する。
     * @param inputPath 対象ファイルのあるディレクトリ
     * @param tmpPath テンポラリファイル作成ディレクトリ
     * @param fileName ファイル名
     * @param codeName 文字コード
     * @param clearFlag コメント・改行を削除するならtrue
     * @return ノード
     */
    public static Node parse(String inputPath, String tmpPath, String fileName, String codeName, boolean clearFlag) {

        File tmpFile = null;

        try {
            tmpFile = preParse(inputPath, tmpPath, fileName, codeName);
        } catch (Exception ex) {
            LogUtils.warn(fileName, "パースの準備処理(preParse)に失敗しました", ex);
            return null;
        }

        JavaParseCtrl parseCtrl = new JavaParseCtrl(fileName.replace(".java", ""));

        try (Reader reader = new InputStreamReader(new FileInputStream(tmpFile.getAbsolutePath()), codeName)) {
            parseMain(reader, parseCtrl, fileName);
        } catch (Exception ex) {
            LogUtils.warn(fileName, "パースに失敗しました", ex);
        }

        if (clearFlag) {
            NodeUtil.clearCrLfAndComment1(parseCtrl.tNode);
        }

        return parseCtrl.tNode;

    }

    /**
     * 入力ファイルに対して、事前処理を行う。
     * @param inputPath 対象ファイルのあるディレクトリ
     * @param tmpPath テンポラリファイル作成ディレクトリ
     * @param fileName ファイル名
     * @param codeName 文字コード
     * @return 入力ファイル
     */

    protected static File preParse(String inputPath, String tmpPath, String fileName, String codeName)
            throws IOException {

        String inFilePath = inputPath + File.separator + fileName;

        File file = new File(inFilePath);
        List<String> allLines = null;
        try {
            allLines = Files.readAllLines(Paths.get(file.getPath()), Charset.forName(codeName));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            throw ex;
        }

        File tmpFile = null;

        String tmpFilePath = null;

        if (tmpPath == null) {
            tmpFile = File.createTempFile("temp", ".java");
            tmpFile.deleteOnExit();
            tmpFilePath = tmpFile.getAbsolutePath();
        } else {
            tmpFilePath = tmpPath + File.separator + fileName;
            tmpFile = new File(tmpFilePath);
        }

        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tmpFilePath), codeName)) {
            for (String line : allLines) {
                String trimedLine = line.trim();
                if (trimedLine.length() == 0) {
                    osw.write("XenCrLf\r\n");
                } else {
                    osw.write(trimedLine + "\r\n");
                }
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            throw ex;
        }

        return tmpFile;

    }

    protected static void parseMain(Reader reader, JavaParseCtrl parseCtrl, String fileName) throws Exception {

        try (PushbackReader pbr = new PushbackReader(reader, 3)) {

            //入力ソースを解析しNodeツリーを組み立てる
            parseMainLoop(pbr, parseCtrl, fileName);

        } catch (Exception ex) {
            String errStr = (parseCtrl.eNode != null) ? parseCtrl.eNode.getStringWithoutCRLF() + "-" : "";
            LogUtils.warn(fileName, errStr + "パースに失敗しました", ex);
        }

        //半角空白の除去
        NodeUtil.cleanAll(parseCtrl.tNode);

        //T_NORMALノードのノードタイプの再精査
        NodeUtil.coordinate(parseCtrl.tNode);

        //ノードに行番号を付与
        NodeUtil.calcLineNumber(parseCtrl.tNode);

        //ノードに階層番号を付与
        NodeUtil.calcDepth(parseCtrl.tNode);

    }

    protected static void parseMainLoop(PushbackReader pbr, JavaParseCtrl parseCtrl, String fileName) throws Exception {

        TokenMgr mgr = new TokenMgr(pbr, fileName);

        for (;;) {
            Token token = mgr.getToken();

            if (token.getType() == Token.EOF) {
                break;
            }

            if (token.getType() == Token.CRLF) {
                procCRLF(parseCtrl, token);
                parseCtrl.prevCRLF = true;
            } else {
                if (token.getType() == Token.TAB || token.getType() == Token.SPACE) {
                    procTabSpace(parseCtrl, token);
                } else if (token.getType() == Token.SEMICOLON) {
                    procSemiColon(parseCtrl, token);
                } else if (token.getType() == Token.COLON) {
                    procColon(parseCtrl, token);
                } else if (token.getType() == Token.COMMENT1) {
                    procComment1(parseCtrl, token);
                } else if (token.getType() == Token.COMMENT2) {
                    procComment2(parseCtrl, token, mgr);
                } else if (token.getType() == Token.SYMBOL) {
                    procSymbol(parseCtrl, token);
                } else {
                    procOther(parseCtrl, token);
                }
                parseCtrl.prevCRLF = false;
            }

        }

    }

    /**
     * タブと半角空白の処理を行う。
     * @param parseCtrl パース
     * @param token トークン
     */
    public static void procTabSpace(JavaParseCtrl parseCtrl, Token token) {
        parseCtrl.addParam(token);
    }

    /**
     * 改行の処理を行う。<br>
     * 改行自体はノード終了にはならないが、アノテーションの場合に限りノード終了と判断する。<br>
     * アノテーションであっても()内の改行の場合はノード終了とは判断しない。
     * @param parseCtrl パース
     * @param token トークン
     */
    public static void procCRLF(JavaParseCtrl parseCtrl, Token token) {

        if (parseCtrl.eNode.getName().startsWith("@") == true) {
            //アノテーションは()内に改行を含めることが可能である。()内の改行の場合はノードを切らない
            if (parseCtrl.kakkoCount == 0) {
                parseCtrl.initEnd = false;
            } else {
                parseCtrl.addParam(token);
            }
        } else {
            if (parseCtrl.initEnd == true) {
                parseCtrl.addParam(token);
            }
        }

    }

    /**
     * セミコロンの処理を行う。<br>
     * セミコロンは()内を除きノード終了と判断する。
     * @param parseCtrl パース
     * @param token トークン
     */
    public static void procSemiColon(JavaParseCtrl parseCtrl, Token token) {
        parseCtrl.addParam(token);
        if (parseCtrl.kakkoCount == 0) {
            parseCtrl.initEnd = false;
        }
    }

    /**
     * コロンの処理を行う。<br>
     * SWITCH1文中のCASE, DEFAULTと組み合わせて登場した場合にインデントを下げる。
     * @param parseCtrl パース
     * @param token トークン
     */
    public static void procColon(JavaParseCtrl parseCtrl, Token token) {
        parseCtrl.addParam(token);
        if (parseCtrl.eNode.getName().equals(CASE) || parseCtrl.eNode.getName().equals(DEFAULT)) {
            parseCtrl.indentDown();
            parseCtrl.initEnd = false;
        }
    }

    /**
     * 行コメントトークンの処理を行う。<br>
     * 改行の直後に限りノードに分割する。それ以外は現在のノードに含める。
     * @param parseCtrl パース
     * @param token トークン
     */
    public static void procComment1(JavaParseCtrl parseCtrl, Token token) {
        //改行の直後の//だけ新規ノード
        if (parseCtrl.prevCRLF) {
            if (parseCtrl.kakkoCount == 0) {
                Node newNode = Node.create(Node.T_COMMENT1, token);
                parseCtrl.cNode.add(newNode);
                parseCtrl.eNode = newNode;
                parseCtrl.initEnd = false;
            } else {
                parseCtrl.addParam(token);
            }
        } else {
            parseCtrl.addParam(token);
        }

    }

    /**
     * ブロックコメントトークンの処理を行う。<br>
     * 他のノードに含めるか、ノードとして切り出すかを判断する。
     * @param parseCtrl パース
     * @param token トークン
     * @param mgr トークン
     */
    public static void procComment2(JavaParseCtrl parseCtrl, Token token, TokenMgr mgr) throws IOException {
        if (parseCtrl.kakkoCount == 0) {
            if (parseCtrl.eNode.getName().equals("}")) {
                Token token1 = mgr.getToken();
                if (token1.getType() == Token.CRLF) {
                    Token token2 = mgr.getToken();
                    if (token2.getText().equals("else")) {
                        parseCtrl.addParam(token, true);
                    } else {
                        parseCtrl.addNewNode(Node.create(Node.T_COMMENT2, token), false);
                    }
                    mgr.pushToken(token2);
                    mgr.pushToken(token1);
                } else {
                    if (token1.getText().equals("else")) {
                        parseCtrl.addParam(token, true);
                    } else {
                        parseCtrl.addNewNode(Node.create(Node.T_COMMENT2, token), false);
                    }
                    mgr.pushToken(token1);
                }
            } else {
                parseCtrl.addNewNode(Node.create(Node.T_COMMENT2, token), false);
            }
        } else {
            parseCtrl.addParam(token, false);
        }
    }

    /**
     * {}による階層化と()の処理を行う。
     * @param parseCtrl パース
     * @param token トークン
     */
    public static void procSymbol(JavaParseCtrl parseCtrl, Token token) {

        if (token.getText().equals("{")) {

            if (parseCtrl.kakkoCount == 0) {
                if (parseCtrl.eNode.getName().equals(SWITCH)) {
                    parseCtrl.addParam(token, false);
                } else {
                    Token lastToken = parseCtrl.getLastTokenWithoutSpace();
                    if (lastToken != null && (lastToken.getText().equals("{")||lastToken.getText().equals(":") ) ) {
                        parseCtrl.addNewNode(Node.create(token), false);
                        parseCtrl.indentDown();
                    } else {

                        parseCtrl.addParam(token, false);
                        parseCtrl.indentDown();
                    }
                }
            } else {
                parseCtrl.addParam(token);
            }

        } else if (token.getText().equals("}")) {

            if (parseCtrl.kakkoCount == 0) {
                parseCtrl.indentUp();
                parseCtrl.addNewNode(Node.create(token), false);
            } else {
                parseCtrl.addParam(token);
            }

        } else if (token.getText().equals("(")) {
            parseCtrl.kakkoCount++;
            parseCtrl.addParam(token);
        } else if (token.getText().equals(")")) {
            parseCtrl.kakkoCount--;
            parseCtrl.addParam(token);
        } else {
            if (( token.getText().equals("."))&&(parseCtrl.eNode.getType()==Node.T_COMMENT1)) {
                //行コメントの次の行にあるピリオドは次の行のノードに含める
                parseCtrl.addNewNode(Node.create(token));
            }else {
                parseCtrl.addParam(token);
            }
        }

    }

    /**
     * 一般の文字を処理する。
     * @param parseCtrl パース
     * @param token トークン
     */
    public static void procOther(JavaParseCtrl parseCtrl, Token token) {

        if (parseCtrl.initEnd == false) {

            if (token.getText().equals(ELSE) == true
                    || token.getText().equals(CATCH) == true
                    || token.getText().equals(FINALLY) == true) {

                if(parseCtrl.prevCRLF) {
                    parseCtrl.addNewNodeWithoutInitEnd(Node.create(token));
                }else {
                    parseCtrl.addParam(token, true);
                }

            } else if (token.getText().equals(CASE)
                    || token.getText().equals(DEFAULT)) {

                if (parseCtrl.cNode.getName().equals(CASE)) {
                    parseCtrl.indentUp();
                }
                parseCtrl.addNewNode(Node.create(token));

            } else if (token.getText().equals(WHILE)) {

                boolean bDoBlock = parseCtrl.existDoBlock();
                if (bDoBlock) {
                    parseCtrl.addParam(token, true);
                } else {
                    parseCtrl.addNewNode(Node.create(token));
                }
            } else if (token.getText().equals("XenCrLf")) {
                parseCtrl.addNewNodeWithoutInitEnd(Node.create(token));

            } else if(token.getText().equals(IF) == true && !parseCtrl.prevCRLF){
                parseCtrl.addParam(token, true);
            }else {
                parseCtrl.addNewNode(Node.create(token));
            }

        } else {

            if (token.getText().startsWith("@") && !token.getText().equals("@interface")) {
                if (parseCtrl.kakkoCount == 0) {
                    parseCtrl.addNewNode(Node.create(token));
                } else {
                    parseCtrl.addParam(token);
                }
            } else if (token.getText().equals(PUBLIC)
                    || token.getText().equals(PROTECTED)
                    || token.getText().equals(PRIVATE)) {
                parseCtrl.addNewNode(Node.create(token));
            } else if (token.getText().equals("XenCrLf")) {
                parseCtrl.addParam(token);
            } else {
                if (parseCtrl.eNode.getHead().getType() == Token.NAME && parseCtrl.eNode.getParams().size() == 0) {
                    parseCtrl.addParam(new Token(Token.SPACE, " "));
                    parseCtrl.addParam(token);
                } else {
                    parseCtrl.addParam(token);
                }
            }

        }

    }

}
