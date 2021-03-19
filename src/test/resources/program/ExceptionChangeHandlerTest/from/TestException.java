package jp.co.tis.test;

/**
 * 例外変換用テストクラス
 *
 */
public class TestException {

    public String index() {
        try {
            // 本番反映前のデータの追加を行なう
            Project.insert(test);
        } catch (SEntityExistsException e) {
            // 登録失敗
            return false;
        }

        try {
            // 本番反映前のデータの追加を行なう
            Project.insert(test2);
        } catch (IORuntimeException f) {
            // 登録失敗
            return false;
        }

        try {
            // 本番反映前のデータの追加を行なう
            Project.insert(test3);
        } catch (SEntityExistsException test) {
            // 登録失敗
            return false;
        }

        throw new SEntityExistsException(e);

        try {
            // 本番反映前のデータの追加を行なう
            Project.insert(test4);
        } catch (SQLRuntimeException sql) {
            // 登録失敗
            return false;
        }
        } catch (Exception e) {
            // 登録失敗
            return false;
        }
        return null;

    }
}
