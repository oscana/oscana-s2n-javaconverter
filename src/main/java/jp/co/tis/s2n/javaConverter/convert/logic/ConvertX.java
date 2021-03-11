package jp.co.tis.s2n.javaConverter.convert.logic;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzeResult;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.Route;
import jp.co.tis.s2n.javaConverter.convert.profile.S2nProfile;
import jp.co.tis.s2n.javaConverter.node.Node;

/**
 * Velocityテンプレートを読む。
 *
 * @author Fumihiko Yamamoto
 *
 */
public abstract class ConvertX {

    protected StrutsAnalyzeResult[] strutsAnalyzeResult;
    protected VelocityEngine velocityEngine;
    protected S2nProfile activeProfile;
    protected Map<String, Route> allRoutes;

    /**
     * 設定ファイルを設定する。
     * @param activeProfile 設定ファイル
     */
    public void setActiveProfile(S2nProfile activeProfile) {
        this.activeProfile = activeProfile;
    }

    /**
     * Strutsの設定ファイルを構造化したクラス配列を設定する。
     * @param strutsAnalyzeResult Strutsの設定ファイルを構造化したクラス配列
     */
    public void setStrutsAnalyzeResult(StrutsAnalyzeResult[] strutsAnalyzeResult) {
        this.strutsAnalyzeResult = strutsAnalyzeResult;
    }

    /**
     * 主処理。
     * @param fileName ファイル名
     * @param topNode トップノード
     * @throws Exception 例外
     */
    public void convertProc(String fileName, Node topNode) throws Exception {

    }

    /**
     * Velocityエンジンの初期化。
     */
    public void initVelocityEngine() {
        VelocityEngine engine = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        p.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine.init(p);
        this.velocityEngine = engine;

    }

    /**
     * Velocityテンプレートを読む。
     * @param InjectPartsName 対象テンプレートファイル名
     * @param context コンテキスト
     * @return 取得した文字列
     */
    public String mergeVelocityTemplate(String InjectPartsName, VelocityContext context) {
        StringWriter sw = new StringWriter();

        //テンプレートの作成
        Template template = this.velocityEngine.getTemplate(InjectPartsName, "UTF-8");
        //テンプレートとマージ
        template.merge(context, sw);
        //マージしたデータはWriterオブジェクトであるswが持っているのでそれを文字列として出力
        sw.flush();
        return sw.toString();
    }

    /**
     * アノテーションのログを収集する。
     * @param topNode トップノード
     */
    public abstract void makeAnnotationLog(Node topNode) ;

    /**
     * すべてのルートを設定する。
     * @param allRoutes ルート
     */
    public void setAllRoutes(Map<String, Route> allRoutes) {
        this.allRoutes = allRoutes;
    }

}
