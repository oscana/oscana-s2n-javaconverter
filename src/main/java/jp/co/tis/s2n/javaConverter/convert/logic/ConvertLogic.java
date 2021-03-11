package jp.co.tis.s2n.javaConverter.convert.logic;

import java.util.List;

import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;

/**
 * Logicクラスの変換処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ConvertLogic extends ConvertBase {

    /**
     * Logicクラス変換の主処理。
     * @param fileName ファイル名
     * @param topNode トップノード
     * @throws Exception 例外
     */
    @Override
    public void convertProc(String fileName, Node topNode) throws Exception {
        super.convertProc(fileName, topNode);

    	List<Node> classNodeList = NodeUtil.findAllNode(topNode, Node.T_CLASS);
    	Node classNode = classNodeList.get(0);
    	// 付与したクラスがSerializeableでなければimplementsを追加する
    	addScopeAnotationToClass(classNode, "@RequestScoped");
        convertProcCommon(fileName, topNode, "Logic");
    }

}
