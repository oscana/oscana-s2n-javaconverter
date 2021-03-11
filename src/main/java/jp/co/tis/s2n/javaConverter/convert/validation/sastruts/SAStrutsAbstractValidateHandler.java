package jp.co.tis.s2n.javaConverter.convert.validation.sastruts;

import jp.co.tis.s2n.javaConverter.convert.validation.AbstractValidateHandler;
import jp.co.tis.s2n.javaConverter.convert.validation.annotation.ValidateAnnotation;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * バリデーションの書き換えロジックのAbstractクラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public abstract class SAStrutsAbstractValidateHandler extends AbstractValidateHandler {

    /**
     * 変換主処理。
     * @param memberAnnotationNode 変換対象のアノテーションノードユーティリティ
     * @return 変換結果
     */
    public abstract ValidateAnnotation handle(AnnotationNodeUtil memberAnnotationNode);
}
