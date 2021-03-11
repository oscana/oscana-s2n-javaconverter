package jp.co.tis.s2n.javaConverter.convert.logic;

import jp.co.tis.s2n.javaConverter.node.Node;

/**
 * Action、Dto、Entity、Form、Logic、Service以外のクラスの変換処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ConvertOthers extends ConvertBase {

    /**
     * Othersクラス変換の主処理。
     * @param fileName ファイル名
     * @param topNode トップノード
     * @throws Exception 例外
     */
    @Override
    public void convertProc(String fileName, Node topNode) throws Exception {
        super.convertProc(fileName, topNode);

        convertProcCommon(fileName, topNode, "Others");

    }

}
