package jp.co.tis.s2n.javaConverter.convert.program;

import java.util.HashMap;
import java.util.Map;

import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.util.ClassNameResolver;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.token.Token;

/**
 *
 * 以下のような変換を行う。<br>
 * 変換前：} catch (SEntityExistsException e) {<br>
 * 変換後：} catch (DuplicateStatementException e) {<br>
 * <br>
 *
 * SQLRuntimeExceptionとIORuntimeExceptionも同じように対応する。<br>
 * 変換前：} catch (SQLRuntimeException e) {<br>
 * 変換後：} catch (SqlStatementException e) {<br>
 * <br>
 *
 * 変換前：} catch (IORuntimeException e) {<br>
 * 変換後：} catch (IOException e) {<br>
 * <br>
 *
 * 上記と同じように例外処理を変換したい場合、exceptionMapに変換前と変換後の例外クラスを定義する。<br>
 * また、importMapに変換後の例外クラスのフルパスを定義する。
 *
 * @author I Ko
 *
 */
public class ExceptionChangeHandler extends AbstractProgramConvertHandler implements AutoInstall {

    public ExceptionChangeHandler(ClassNameResolver tcnr) {
        super(tcnr);
    }

    private Token mainToken1;

    private Map<String, String> exceptionMap = new HashMap<>();

    {
        exceptionMap.put("SEntityExistsException", "DuplicateStatementException");
        exceptionMap.put("SQLRuntimeException", "SqlStatementException");
        exceptionMap.put("IORuntimeException", "IOException");
    }


    private Map<String, String> importMap = new HashMap<>();

    {
        importMap.put("SEntityExistsException", "nablarch.core.db.statement.exception.DuplicateStatementException");
        importMap.put("SQLRuntimeException", "nablarch.core.db.statement.exception.SqlStatementException");
        importMap.put("IORuntimeException", "java.io.IOException");
    }

    /**
     * ハンドラを実施するかどうか判定用メソッド。
     * @param line String型の行ソース
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return チェック結果 true:実行する、false:実行しない
     */
    @Override
    public boolean test(String line, Node lineNode) {
        return line.contains(KEY_CATCH_EXCEPTION);
    }

    /**
     * 変換主処理。
     * @param token 処理対象トークン
     * @param bracketLevel レベル
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return 処理結果
     */
    @Override
    protected boolean handle(Token token, BracketLevel bracketLevel, Node lineNode) {

        switch (step) {
        case 0:
            if ((token.getText().equals(KEY_CATCH_EXCEPTION))) {
                step++;
            }
            break;

        case 1:
            if (exceptionMap.get(token.getText()) != null) {
                step++;
                this.mainToken1 = token;
                addCommand(new ChangeToTxtCommand(mainToken1, exceptionMap.get(token.getText())));
                if(importMap.containsKey(token.getText())) {
                    ClassPathConvertUtil.getInstance().addImprt(importMap.get(token.getText()));
                }
                return true;
            }
            break;
        }
        return false;
    }

}
