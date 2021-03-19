package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;

import oscana.s2n.common.DataUtil;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService02 implements Serializable {

    public long getCountBySqlFile() {
        long count = DataUtil.getCountQueryResult(DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId (ParamFilter.sqlFileNameToKey( "getCountBySqlFile.sql" )).executeQueryByMap( ));
        return count;
    }

}
