package jp.co.tis.s2n.javaConverter.convert.logic;

import java.util.List;

import jp.co.tis.s2n.javaConverter.convert.program.S2AbstractServiceFindAllHandler;
import jp.co.tis.s2n.javaConverter.convert.program.S2AbstractServiceFindByIdHandler;
import jp.co.tis.s2n.javaConverter.convert.program.S2AbstractServiceGetCountBySQLHandler;
import jp.co.tis.s2n.javaConverter.convert.program.S2AbstractServiceSelectBySQLHandler;
import jp.co.tis.s2n.javaConverter.convert.program.S2AbstractServiceSelectMethodHandler;
import jp.co.tis.s2n.javaConverter.convert.program.S2AbstractServiceUpdateBySQLFileHandler;
import jp.co.tis.s2n.javaConverter.convert.program.S2AbstractServiceUpdateMethodHandler;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;

/**
 * Serviceクラスの変換処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ConvertService extends ConvertBase {

    /**
     * Serviceクラス変換の主処理。
     * @param fileName ファイル名
     * @param topNode トップノード
     * @throws Exception 例外
     */
    @Override
    public void convertProc(String fileName, Node topNode) throws Exception {
        super.convertProc(fileName, topNode);
        List<Node> classNodeList = NodeUtil.findAllNode(topNode, Node.T_CLASS);
        if (classNodeList.size() == 0) {
            return;
        }
        Node classNode = classNodeList.get(0);
        //完全修飾クラス名特定
        String packageName = NodeUtil.getPackageName(topNode);
        //編集前の状態でクラスネームリゾルバを作成
        makeClassNameResolver(topNode, packageName);

        //ServiceならPrototype
        addScopeAnotationToClass(classNode, "@RequestScoped");

        //ファイルの種類によらない変換を実施
        convertCommon(topNode, fileName, classNode);

        convertJavaProgramForService(topNode, fileName, classNode);

        //追加import挿入
        insertStrutsImportInjectParts(topNode, "Others");

    }

    protected void convertJavaProgramForService(Node topNode, String fileName, Node classNode) {
        List<Node> nodeList = NodeUtil.findAllNode(topNode);
        for (Node lineNode : nodeList) {

            if ((lineNode.getType() == Node.T_NORMAL) || (lineNode.getType() == Node.T_RETURN)) {
                String line = lineNode.getString();
                if (!line.contains("UniversalDao")) {
                    if (line.contains("selectBySqlFile")) {
                        S2AbstractServiceSelectBySQLHandler handler = new S2AbstractServiceSelectBySQLHandler(classNode,
                                cnr);
                        handler.convert(lineNode, line, fileName);
                        if (handler.isGetId()) {
                            insertStrutsImportInjectParts(topNode, "GetId");
                        }
                    } else if (line.contains("updateBySqlFile")) {
                        new S2AbstractServiceUpdateBySQLFileHandler(cnr).convert(lineNode, line, fileName);
                    } else if (line.contains("getCountBySqlFile")) {
                        new S2AbstractServiceGetCountBySQLHandler(classNode, cnr).convert(lineNode, line, fileName);
                    } else if (line.contains("select")) {
                        new S2AbstractServiceSelectMethodHandler(classNode, cnr).convert(lineNode, line, fileName);
                    } else if (line.contains("update")) {
                        new S2AbstractServiceUpdateMethodHandler("update", cnr).convert(lineNode, line, fileName);
                    } else if (line.contains("insert")) {
                        new S2AbstractServiceUpdateMethodHandler("insert", cnr).convert(lineNode, line, fileName);
                    } else if (line.contains("delete")) {
                        new S2AbstractServiceUpdateMethodHandler("delete", cnr).convert(lineNode, line, fileName);
                    } else if (line.contains("findById")) {
                        new S2AbstractServiceFindByIdHandler(classNode, cnr).convert(lineNode, line, fileName);
                    } else if (line.contains("findAll")) {
                        new S2AbstractServiceFindAllHandler(classNode, cnr).convert(lineNode, line, fileName);
                    }
                }

            }
        }
    }

}
