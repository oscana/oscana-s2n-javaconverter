package jp.co.tis.s2n.javaConverter.convert.program;

import jp.co.tis.s2n.javaConverter.node.Node;

/**
 * このインターフェースを継承しておれば自動的にインストールされる。
 *
 * @author Fumihiko Yamamoto
 *
 */
public interface AutoInstall {

    /**
     * このハンドラを発動させるか確認する。
     * @param line String型の行ソース
     * @param lineNode 処理対象トークンを含む行全体を表すノード
     * @return チェック結果 true:実行する、false:実行しない
     */
    public boolean test(String line, Node lineNode);

}
