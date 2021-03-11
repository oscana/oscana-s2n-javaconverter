package jp.co.tis.s2n.javaConverter.convert.logic;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.javaConverter.convert.validation.AbstractValidateHandler;
import jp.co.tis.s2n.javaConverter.convert.validation.sastruts.SAStrutsAbstractValidateHandler;
import jp.co.tis.s2n.javaConverter.convert.validation.struts.StrutsAbstractValidateHandler;
import jp.co.tis.s2n.javaConverter.node.AnnotationNodeUtil;

/**
 * Convertのインスタンスを取得する。
 *
 * @author I Ko
 *
 */
public abstract class ConvertFactory {

    /**
     * ConvertXのインスタンスを取得する。
     * @param className クラス名
     * @param fileName ファイル名
     * @return ConvertXのインスタンス
     */
    public static ConvertX getInstance(String className, String fileName) {
        ConvertX conv = null;
        try {
            Class<?> clazz = Class.forName(className);
            conv = (ConvertX) clazz.newInstance();
        } catch (Exception ex) {
            LogUtils.warn(fileName, className, "is not found", ex);
            System.err.println(className + " is not found.");
        }
        return conv;
    }

    /**
     * SAStrutsAbstractValidateHandlerのインスタンスを取得する。
     * @param aNode アノテーションノード
     * @throws InstantiationException 例外
     * @throws IllegalAccessException 例外
     */
    public static SAStrutsAbstractValidateHandler getSAStrutsAbstractValidateHandler(AnnotationNodeUtil aNode)
            throws InstantiationException, IllegalAccessException {

        Class<?> handlerClazz = null;
        String className = SAStrutsAbstractValidateHandler.class.getPackage().getName() + "."
                + StringUtils.capitalizeStr(aNode.getName()) + AbstractValidateHandler.HANDLER_SUFFIX;
        try {
            handlerClazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            handlerClazz = null;
        }
        if (handlerClazz != null) {
            SAStrutsAbstractValidateHandler handler = (SAStrutsAbstractValidateHandler) handlerClazz
                    .newInstance();
            return handler;
        } else {
            return null;
        }

    }

    /**
     * StrutsAbstractValidateHandlerのインスタンスを取得する。
     * @param depend イテレータの次の要素
     * @throws InstantiationException 例外
     * @throws IllegalAccessException 例外
     */
    public static StrutsAbstractValidateHandler getStrutsAbstractValidateHandler(String depend)
            throws InstantiationException, IllegalAccessException {

        Class<?> handlerClazz = null;
        String className = StrutsAbstractValidateHandler.class.getPackage().getName() + "."
                + StringUtils.capitalizeStr(depend) + AbstractValidateHandler.HANDLER_SUFFIX;
        try {
            handlerClazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            handlerClazz = null;
        }

        if (handlerClazz != null) {
            StrutsAbstractValidateHandler handler = (StrutsAbstractValidateHandler) handlerClazz
                    .newInstance();
            return handler;
        } else {
            return null;
        }

    }


}
