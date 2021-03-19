package jp.co.tis.test;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;

import nablarch.core.db.statement.exception.DuplicateStatementException;
import java.io.IOException;
import nablarch.core.db.statement.exception.SqlStatementException;


/**
 * 例外変換用テストクラス
 *
 */
public class TestException {

    public String index() {
        try {
            // 本番反映前のデータの追加を行なう
            Project.insert(test);
        } catch (DuplicateStatementException e) {
            // 登録失敗
            return false;
        }

        try {
            // 本番反映前のデータの追加を行なう
            Project.insert(test2);
        } catch (IOException f) {
            // 登録失敗
            return false;
        }

        try {
            // 本番反映前のデータの追加を行なう
            Project.insert(test3);
        } catch (DuplicateStatementException test) {
            // 登録失敗
            return false;
        }

        throw new SEntityExistsException(e);

        try {
            // 本番反映前のデータの追加を行なう
            Project.insert(test4);
        } catch (SqlStatementException sql) {
            // 登録失敗
            return false;
        }
    } catch (Exception e) {
        // 登録失敗
        return false;
    }
    return null;

}
