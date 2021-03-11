package jp.co.tis.s2n.javaConverter.convert.sqlfile;

import java.util.ArrayList;

/**
 * SQLの改行位置を記録し、先頭からのオフセットに対する行番号を計算する。
 *
 * @author Fumihiko Yamamoto
 *
 */
class SqlLineAnalyzer {

    //各行の先頭位置
    private Integer[] linePos;

    /**
     * SQLを読み、改行位置を記録する。
     * @param sql sqlデータ
     */
    public SqlLineAnalyzer(String sql) {
        ArrayList<Integer> linePosList = new ArrayList<>();
        linePosList.add(-1); //文字列の先頭という意味
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (c == '\n') {
                linePosList.add(i);
            }
        }
        linePos = linePosList.toArray(new Integer[0]);
    }

    /**
     * オフセットに対する行番号を計算する。
     * @param pos 先頭からのオフセット
     * @return 行番号
     */
    public int getLineOfPos(int pos) {
        for (int i = 0; i < linePos.length; i++) {
            Integer cPos = linePos[i];
            if ((cPos > pos)) {
                return i - 1;
            }
        }
        return linePos.length - 1;
    }

}