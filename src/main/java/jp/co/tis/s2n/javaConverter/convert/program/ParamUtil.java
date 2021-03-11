package jp.co.tis.s2n.javaConverter.convert.program;

import java.util.ArrayList;
import java.util.List;

import jp.co.tis.s2n.javaConverter.token.Token;

/**
 * パラメータを構成するToken群をJavaパラメータ単位に分割し、グルーピングする。<br>
 * ※グルーピング結果にはトークン間のデリミタとなるカンマは含まれない。
 *
 * 使い方：<br>
 * 1...ParamUtil()をnewする。<br>
 * 2...token処理の中で取得したトークンを p.add(token)でユーティリティに取り込む。<br>
 * 3...p.getParamTokenGroups()でグルーピング結果を取り出す。<br>
 * ※getParamListで取り出した配列はReadOnlyである。これを操作しても実態には反映されない。<br>
 * ※getParamListで取り出した後に、p.add(token)で追加登録することは可能であるがこの場合、先にgetParamListで取り出したリストは変化しない。再度getParamListで取り出したリストに反映される。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ParamUtil {

    //トークンをパラメータ毎にグルーピングした結果
    private List<ParamTokenGroup> paramTokenGroups = new ArrayList<ParamTokenGroup>();

    //抽出途中のパラメータに対するトークン
    private ParamTokenGroup tmpParamTokenGroup = new ParamTokenGroup();

    //()のレベル
    private int parenthesisLevel;
    //{}のレベル
    private int curlyBrachetLevel;
    //[]のレベル
    private int squareBracketLevel;

    //""の中ならtrue
    private boolean quote1Mode;
    //''の中ならtrue
    private boolean quote2Mode;

    /**
     * トークンを登録すると、パラメータ配列が整備される。<br>
     * シングルクオート、ダブルクオートや各種括弧を判断するので、通常のJavaと同等のパラメータ分解を実施する。
     * @param token 処理対象トークン
     */
    public void add(Token token) {

        boolean isAddTmpParam = true;

        switch (token.getText()) {
        case "\"":
            if (quote2Mode == false) {
                quote1Mode ^= true;
            }
            break;
        case "'":
            if (quote1Mode == false) {
                quote2Mode ^= true;
            }
            break;
        }

        if ((quote1Mode == false) && (quote2Mode == false)) {
            switch (token.getText()) {
            case ";":
                //(),{},[]の外にあるセミコロンなら項目デリミタ兼終了文字として処理する
                if (isOutBrache()) {
                    isAddTmpParam = false;
                    paramTokenGroups.add(tmpParamTokenGroup);
                    tmpParamTokenGroup = new ParamTokenGroup();
                }
                break;
            case ",":
                //(),{},[]の外にあるカンマなら項目デリミタとして処理する
                if (isOutBrache()) {
                    isAddTmpParam = false;
                    paramTokenGroups.add(tmpParamTokenGroup);
                    tmpParamTokenGroup = new ParamTokenGroup();

                }
                break;
            case "(":
                parenthesisLevel++;
                break;
            case ")":
                parenthesisLevel--;
                break;
            case "{":
                curlyBrachetLevel++;
                break;
            case "}":
                curlyBrachetLevel--;
                break;
            case "[":
                squareBracketLevel++;
                break;
            case "]":
                squareBracketLevel--;
                break;
            default:
                break;
            }
        }
        if (isAddTmpParam) {
            tmpParamTokenGroup.add(token);
        }
    }

    /**
     * 括弧の外かどうかをチェックする。
     * @return チェック結果 true:括弧の外、false:括弧内
     */
    public boolean isOutBrache() {
        return (parenthesisLevel == 0) && (curlyBrachetLevel == 0) && (squareBracketLevel == 0);
    }

    /**
     * トークンのグルーピング結果を応答する。
     * @return トークングループのリスト
     */
    public List<ParamTokenGroup> getParamTokenGroups() {
        List<ParamTokenGroup> ret = new ArrayList<>();
        for (ParamTokenGroup data : this.paramTokenGroups) {
            ret.add(data);
        }
        if (tmpParamTokenGroup.size() > 0) {
            ret.add(tmpParamTokenGroup);
        }
        return ret;
    }

    /**
     * 1つのJavaパラメータを構成するトークンのグループ。
     * @author Fumihiko Yamamoto
     */
    public class ParamTokenGroup {
        List<Token> tokenGroup = new ArrayList<Token>();

        /**
         * １つのJavaパラメータを構成するトークンに対する文字列表現を取得する。
         * @return 対象文字列
         */
        public String getString() {
            StringBuffer sb = new StringBuffer();
            for (Token token : tokenGroup) {
                sb.append(token.getText());
            }
            return sb.toString();

        }

        /**
         * このグループを構成するトークンの個数。
         * @return 個数
         */
        public int size() {
            return this.tokenGroup.size();
        }

        /**
         * このグループにトークンを追加する。
         * @param token 処理対象トークン
         */
        public void add(Token token) {
            this.tokenGroup.add(token);
        }

        /**
         * このグループを構成するトークンのうち最初のものを返す。
         * @return 処理対象トークン
         */
        public Token getFirstToken() {
            return this.tokenGroup.get(0);
        }

        /**
         * このグループを構成するトークンのうち最後のものを返す。
         * @return 処理対象トークン
         */
        public Token getLastToken() {
            return this.tokenGroup.get(this.tokenGroup.size() - 1);
        }

    }
}
