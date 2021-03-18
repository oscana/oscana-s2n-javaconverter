package jp.co.tis.s2n.javaConverter.convert;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.converterCommon.struts.analyzer.ClassPathConvertUtil;
import jp.co.tis.s2n.converterCommon.struts.analyzer.NablarchRoutingFileGenerator;
import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzeResult;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.Route;
import jp.co.tis.s2n.javaConverter.convert.logic.ConvertFactory;
import jp.co.tis.s2n.javaConverter.convert.logic.ConvertX;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.convert.sqlfile.SqlFileConverter;
import jp.co.tis.s2n.javaConverter.convert.statistics.ActionStatistics;
import jp.co.tis.s2n.javaConverter.convert.statistics.AnnotationStatistics;
import jp.co.tis.s2n.javaConverter.convert.statistics.OtherStatistics;
import jp.co.tis.s2n.javaConverter.convert.statistics.S2JDBCStatistics;
import jp.co.tis.s2n.javaConverter.convert.statistics.SQLResultStatistics;
import jp.co.tis.s2n.javaConverter.keyword.JavaKeyword;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;
import jp.co.tis.s2n.javaConverter.node.Node;
import jp.co.tis.s2n.javaConverter.node.NodeUtil;
import jp.co.tis.s2n.javaConverter.parser.JavaParser;

/**
 * S2N JavaConverterの主処理。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ConvertStruts2Nablarch extends AbstractJavaParser implements JavaKeyword {

    private StrutsAnalyzeResult[] strutsAnalyzeResultList;
    private Map<String, Route> allRoutes;

    //プロファイル
    private S2nProfile activeProfile;
    private static String sp = System.getProperty("file.separator");

    /**
     * JavaConverterの主処理。
     * @param args 引数
     * @throws Exception 例外
     */
    public static void main(String[] args) throws Exception {
        LogUtils.init();

        // パラメータ必須チェック
        if (args.length < 1) {
            System.err.print("usage:java -jar javaconverter.jar [設定ファイル]" );
            System.exit(-1);
        }
        //設定ファイルを読み取り
        String profileName = args[0];

        // 設定ファイル存在チェック
        if (!new File(profileName).exists()) {
            System.err.print(profileName + " - エラー:設定ファイルが見つかりません。");
            System.exit(-1);
        }
        S2nProfile activeProfile = new S2nProfile(profileName);

        //パッケージマッピングファイル
        String mappingFileName = null;
        if (args.length > 1) {
            mappingFileName = args[1];
            // マッピングファイル存在チェック
            if (!new File(mappingFileName).exists()) {
                System.err.print(mappingFileName + " - エラー:パッケージマッピングファイルが見つかりません。");
                System.exit(-1);
            } else if (!"csv".equals(mappingFileName.substring(mappingFileName.lastIndexOf('.') + 1))) {
                System.err.print(mappingFileName + " - エラー:パッケージマッピングファイルをCSVフォーマットにしてください。");
                System.exit(-1);
            }
        }

        ClassPathConvertUtil.loadMappingFile(mappingFileName);
        /**StrutsにおけるFormのBaseクラス、置換前、置換後map*/
        ClassPathConvertUtil.loadBaseClassnameConvertMap("BaseClassnameConvertMap.csv");
        /**StrutsにおけるServlet関連クラス、置換前、置換後map*/
        ClassPathConvertUtil.loadServletClassnameConvertMap("ServletClassnameConvertMap.csv");

        String projectPath = activeProfile.getProjectPath();

        ConvertStruts2Nablarch parserJava = new ConvertStruts2Nablarch();
        parserJava.setActiveProfile(activeProfile);

        parserJava.setInPath(projectPath + "java" + sp + "from");
        parserJava.setOutPath(projectPath + "java" + sp + "to");
        parserJava.setTmpPath(projectPath + "java" + sp + "tmp");
        parserJava.setCodeName(activeProfile.getFileEncoding());

        parserJava.setStrutsAnalyzeResultList(activeProfile.getStrutsAnalyzeResultList());

        parserJava.createRoutes(projectPath);
        parserJava.execute();

        //テストアプリにも直接デプロイ
        if (!StringUtils.isEmpty(activeProfile.getTestDeployPath())) {
            parserJava.setOutPath(activeProfile.getTestDeployPath());
            parserJava.execute();

        }
        if (activeProfile.getSavePathForRoutexml() != null) {
            NablarchRoutingFileGenerator.saveRoutingFile(parserJava.allRoutes,
                    new File(activeProfile.getSavePathForRoutexml()), activeProfile.getConvertMode());
        }

        SqlFileConverter sqlFileConv = new SqlFileConverter();

        sqlFileConv.setInPath(projectPath + "sql" + sp + "from");
        sqlFileConv.setOutPath(projectPath + "sql" + sp + "to");
        sqlFileConv.setTmpPath(projectPath + "sql" + sp + "tmp");
        sqlFileConv.setCodeName(activeProfile.getFileEncoding());
        sqlFileConv.setActiveProfile(activeProfile);

        sqlFileConv.execute();

        AnnotationStatistics.getInstance().exportData();
        S2JDBCStatistics.getInstance().exportData();
        ActionStatistics.getInstance().exportData();
        OtherStatistics.getInstance().exportData();
        SQLResultStatistics.exportData();

    }

    /**
     * プロファイルを設定する。
     * @param activeProfile プロファイル
     */
    public void setActiveProfile(S2nProfile activeProfile) {
        this.activeProfile = activeProfile;

    }

    /**
     * Strutsの設定ファイルからRoutes.xmlを構築する。
     * @param projectPath プロジェクトパス
     * @throws XPathExpressionException 例外
     * @throws SAXException 例外
     * @throws IOException 例外
     * @throws ParserConfigurationException 例外
     */
    protected void createRoutes(String projectPath)
            throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {

        Map<String, Route> routes = new LinkedHashMap<>();
        this.allRoutes = routes;
        if (getStrutsAnalyzeResultList() == null) {
            return;
        }

        //Routeに書き出すために合成
        for (StrutsAnalyzeResult cResult : this.getStrutsAnalyzeResultList()) {
            Map<String, Route> cRoutes = cResult.getRoutes();
            for (Route val : cRoutes.values()) {
                routes.put(val.getPath(), val);
            }
        }

    }

    protected void executeFile(String inFilePath, String outPath, String fileName){
        println("<<File:" + inFilePath);
        String inputPath = new File(inFilePath).getParent();
        Node topNode = JavaParser.parse(inputPath, fileName, this.codeName);

        modify(inFilePath, fileName, topNode);

        postProc(outPath, fileName, topNode, activeProfile);

    }

    protected void modify(String inFilePath, String fileName, Node topNode) {

        String className = "jp.co.tis.s2n.javaConverter.convert.logic.Convert";

        if (fileName.endsWith("Form.java")) {
            className += "Form";
        } else if (fileName.endsWith("Action.java")) {
            className += "Action";
        } else if (fileName.endsWith("Logic.java")) {
            className += "Logic";
        } else if (fileName.endsWith("Service.java")) {
            className += "Service";
        } else if (fileName.endsWith("Dto.java")) {
            className += "Dto";
        } else {
            //ファイルの中身を見て判別する
            String fileType = determine(topNode);
            className += fileType;
        }

        if (className != null) {

            ConvertX conv = ConvertFactory.getInstance(className, fileName);

            try {
                ClassPathConvertUtil.getInstance().resetMap();
                conv.setActiveProfile(this.activeProfile);
                conv.initVelocityEngine();
                conv.setStrutsAnalyzeResult(this.getStrutsAnalyzeResultList());
                conv.setAllRoutes(this.allRoutes);
                conv.makeAnnotationLog(topNode);
                conv.convertProc(fileName, topNode);
            } catch (Exception ex) {
                LogUtils.warn(fileName, className, "例外が発生しました。", ex);
                ex.printStackTrace();
            }
        }

    }

    /**
     * ファイルの中身を見て種類の判別を実施する。
     * @param topNode トップノード
     * @return ファイルの種別
     */
    private String determine(Node topNode) {
        List<Node> annotationNodes = NodeUtil.findAllNode(topNode, "@Generated");
        if(annotationNodes == null || annotationNodes.size() == 0) {
            List<Node> annotationNodesEntity = NodeUtil.findAllNode(topNode, "@Entity");
            if(annotationNodesEntity != null && annotationNodesEntity.size() > 0) {
               return "Entity";
            }
        }
        for (Node annotationNode : annotationNodes) {
            AnnotationNodeUtil anode = new AnnotationNodeUtil(annotationNode);
            String value = anode.getStringValueWithoutQuote("value");
            if ((value != null) && (value.contains("S2JDBC-Gen"))) {
                if (value.contains("EntityModelFactoryImpl")) {
                    //エンティティで確定
                    return "Entity";
                } else if (value.contains("ServiceModelFactoryImpl")) {
                    //サービスで確定だがServlce専用処理はないのでOthersで返す
                    return "Others";
                }
            }
        }

        return "Others";

    }

    /**
     * Strutsの分析結果を取得する。
     * @return Strutsの分析結果
     */
    public StrutsAnalyzeResult[] getStrutsAnalyzeResultList() {
        return strutsAnalyzeResultList;
    }

    /**
     * Strutsの分析結果を設定する。
     * @param strutsAnalyzeResultList Strutsの分析結果
     */
    public void setStrutsAnalyzeResultList(StrutsAnalyzeResult[] strutsAnalyzeResultList) {
        this.strutsAnalyzeResultList = strutsAnalyzeResultList;
    }

}
