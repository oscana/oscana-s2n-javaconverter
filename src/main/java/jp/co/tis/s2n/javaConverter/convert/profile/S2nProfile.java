package jp.co.tis.s2n.javaConverter.convert.profile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzeResult;
import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzer;

/**
 * ツール実行用設定ファイルを読込む。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class S2nProfile {

    public static final int CONVERT_MODE_STRUTS = 1;
    public static final int CONVERT_MODE_SASTRUTS = 2;
    public static final int CONVERT_MODE_XENLON = 3;

    private String projectPath;
    private String basePackage;
    private String testDeployPath;
    private String savePathForRoutexml;
    private StrutsAnalyzeResult[] strutsAnalyzeResultList;
    private String fileEncording;
    private String onDoubleSubmissionPath;
    private int convertMode;
    private int tableNameCase;
    private String lineSeparator="\r\n";// 改行コードのデフォルドは、\r\n

    public S2nProfile(String profileName) throws Exception {
        this.init(profileName);
    }

    public S2nProfile() {
    }

    /**
     * 初期化。
     * @param profileName 設定ファイル
     */
    private void init(String profileName) throws Exception {
        try (InputStream in = new BufferedInputStream(new FileInputStream(profileName))) {

            Properties prop = new Properties();
            prop.load(in);
            /**設定値をチェックして、正常であれば、S2nProfileに設定*/
            // projectPath
            if (StringUtils.isEmpty(prop.getProperty("projectPath"))) {
                System.err.print("エラー:projectPathは必須です。");
                System.exit(-1);
            } else {
                this.setProjectPath(prop.getProperty("projectPath"));
            }
            // basePackage
            if (StringUtils.isEmpty(prop.getProperty("basePackage"))) {
                System.err.print("エラー:basePackageは必須です。");
                System.exit(-1);
            } else {
                this.setBasePackage(prop.getProperty("basePackage"));
            }
            // fileEncording
            if (StringUtils.isEmpty(prop.getProperty("fileEncording"))) {
                System.err.print("エラー:fileEncordingは必須です。");
                System.exit(-1);
            } else {
                this.setFileEncording(prop.getProperty("fileEncording"));
            }
            // savePathForRoutexml
            if (!StringUtils.isEmpty(prop.getProperty("savePathForRoutexml"))) {
                this.setSavePathForRoutexml(prop.getProperty("savePathForRoutexml"));
            }
            // convertMode
            if (StringUtils.isEmpty(prop.getProperty("convertMode"))) {
                System.err.print("エラー:convertModeは必須です。");
                System.exit(-1);
            } else if (!StringUtils.contains("123", prop.getProperty("convertMode"))) {
                System.err.print("エラー:convertModeは「1,2,3」しか設定出来ません");
                System.exit(-1);
            } else {
                this.setConvertMode(Integer.parseInt(prop.getProperty("convertMode")));
            }

            // onDoubleSubmissionPath
            if (convertMode == CONVERT_MODE_XENLON) {
                if (StringUtils.isEmpty(prop.getProperty("onDoubleSubmissionPath"))) {
                    System.err.print("エラー:onDoubleSubmissionPathは必須です。");
                    System.exit(-1);
                } else {
                    onDoubleSubmissionPath = prop.getProperty("onDoubleSubmissionPath");
                }
            }

            // tableNameCase
            if (StringUtils.isEmpty(prop.getProperty("tableNameCase"))) {
                this.setTableNameCase(0);
            } else if (!StringUtils.contains("01", prop.getProperty("tableNameCase"))) {
                System.err.print("エラー:tableNameCaseは「0,1」しか設定出来ません");
                System.exit(-1);
            } else {
                this.setTableNameCase(Integer.parseInt(prop.getProperty("tableNameCase")));
            }

            // lineSeparator
            if (!StringUtils.isEmpty(prop.getProperty("lineSeparator"))) {
                this.setLineSeparator(prop.getProperty("lineSeparator"));
            }

            // testDeployPath
            this.setTestDeployPath(prop.getProperty("testDeployPath"));

            //StrutsResource
            String[] strutsConfigFiles = prop.getProperty("strutsAnalyze.strutsConfigFile").split(",");
            String[] validationFile = prop.getProperty("strutsAnalyze.validationFile").split(",");
            String[] module = prop.getProperty("strutsAnalyze.module").split(",");
            if (strutsConfigFiles.length != validationFile.length) {
                System.err.print("エラー:strutsConfigFileとvalidationFileに指定したファイル数が異なります。（strutsConfigFile:" + strutsConfigFiles.length + ", validationFile:" + validationFile.length + ")");
                System.exit(-1);
            } else if (strutsConfigFiles.length != module.length) {
                if (module.length == 1 && StringUtils.isEmpty(module[0])) {
                    module = new String[strutsConfigFiles.length];
                } else {
                    System.err.print("エラー:strutsConfigFileに指定したファイル数とmoduleに指定したモジュール数が異なります。（strutsConfigFile:" + strutsConfigFiles.length + ", module:" + module.length + ")");
                    System.exit(-1);
                }
            }

            if (this.getConvertMode() == 1) {

                StrutsAnalyzeResult[] strutsAnalyzeResultList = new StrutsAnalyzeResult[strutsConfigFiles.length];
                for (int i = 0; i < strutsConfigFiles.length; i++) {
                    if (StringUtils.isEmpty(strutsConfigFiles[i])) {
                        continue;
                    }
                    strutsAnalyzeResultList[i] = StrutsAnalyzer.analyzeStrutsResource(
                            new File(this.getProjectPath() + strutsConfigFiles[i]),
                            (StringUtils.isEmpty(validationFile[i])) ? null
                                    : new File(this.getProjectPath() + validationFile[i]),
                            StringUtils.isEmpty(module[i]) ? "" : module[i],
                            this.getBasePackage());
                }
                setStrutsAnalyzeResultList(strutsAnalyzeResultList);
            }
        }
    }

    /**
     * プロジェクトパスを取得する。
     * @return プロジェクトパス
     */
    public String getProjectPath() {
        return projectPath;
    }

    /**
     * プロジェクトパスを設定する。
     * @param projectPath プロジェクトパス
     */
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    /**
     * ベースパッケージを取得する。
     * @return ベースパッケージ
     */
    public String getBasePackage() {
        return basePackage;
    }

    /**
     * ベースパッケージを設定する。
     * @param basePackage ベースパッケージ
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * デプローイ先のパスを取得する。
     * @return デプローイ先のパス
     */
    public String getTestDeployPath() {
        return testDeployPath;
    }

    /**
     * デプローイ先のパスを設定する。
     * @param testDeployPath デプローイ先のパス
     */
    public void setTestDeployPath(String testDeployPath) {
        this.testDeployPath = testDeployPath;
    }

    /**
     * Routeファイルパスを取得する。
     * @return Routeファイルパス
     */
    public String getSavePathForRoutexml() {
        return savePathForRoutexml;
    }

    /**
     * Routeファイルパスを設定する。
     * @param savePathForRoutexml Routeファイルパス
     */
    public void setSavePathForRoutexml(String savePathForRoutexml) {
        this.savePathForRoutexml = savePathForRoutexml;
    }

    /**
     * struts分析結果のリストを取得する。
     * @return struts分析結果のリスト
     */
    public StrutsAnalyzeResult[] getStrutsAnalyzeResultList() {
        return strutsAnalyzeResultList;
    }

    /**
     * struts分析結果のリストを設定する。
     * @param strutsAnalyzeResultList struts分析結果のリスト
     */
    public void setStrutsAnalyzeResultList(StrutsAnalyzeResult[] strutsAnalyzeResultList) {
        this.strutsAnalyzeResultList = strutsAnalyzeResultList;
    }

    /**
     * ファイルエンコーディングを取得する。
     * @return ファイルエンコーディング
     */
    public String getFileEncording() {
        return fileEncording;
    }

    /**
     * ファイルエンコーディングを設定する。
     * @param fileEncording ファイルエンコーディング
     */
    public void setFileEncording(String fileEncording) {
        this.fileEncording = fileEncording;
    }

    /**
     * 二重サブミットと判定した場合の遷移先を取得する。
     * @return 二重サブミットと判定した場合の遷移先
     */
    public String getOnDoubleSubmissionPath() {
        return onDoubleSubmissionPath;
    }

    /**
     * 二重サブミットと判定した場合の遷移先を設定する。
     * @param onDoubleSubmissionPath 二重サブミットと判定した場合の遷移先
     */
    public void setOnDoubleSubmissionPath(String onDoubleSubmissionPath) {
        this.onDoubleSubmissionPath = onDoubleSubmissionPath;
    }

    /**
     * 変換モードを取得する。<br>
     * 1...Struts<br>
     * 2...SAStruts<br>
     * 3...Other
     * @return 変換モード
     */
    public int getConvertMode() {
        return convertMode;
    }

    /**
     * 変換モードを設定する。
     * @param convertMode 変換モード
     */
    public void setConvertMode(int convertMode) {
        this.convertMode = convertMode;
    }

    /**
     * テーブルの文字制御（大文字・小文字）を取得する。<br>
     * 0...大文字<br>
     * 1...小文字
     * @return テーブルの文字制御（大文字・小文字）
     */
    public int getTableNameCase() {
        return tableNameCase;
    }

    /**
     * テーブルの文字制御（大文字・小文字）を設定する。
     * @param tableNameCase テーブルの文字制御（大文字・小文字）
     */
    public void setTableNameCase(int tableNameCase) {
        this.tableNameCase = tableNameCase;
    }

    /**
     * 改行コードを取得する。
     * @return 改行コード
     */
    public String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * 改行コードを設定する。
     * @param lineSeparator 改行コード
     */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

}
