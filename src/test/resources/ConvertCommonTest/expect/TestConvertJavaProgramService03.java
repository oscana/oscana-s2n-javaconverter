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



/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService03 implements Serializable {

    public int updateBySqlFile() {
        UpdateParam param = new UpdateParam();
        param.salary = new BigDecimal(1200);
        param.id = 10;
        // TODO ツールで変換できません :   
        int count = jdbcManager.updateBySqlFile ( "update.sql", param ) . execute( );
        return count;
    }

}
