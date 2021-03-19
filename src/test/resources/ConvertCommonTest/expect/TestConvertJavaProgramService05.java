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

import nablarch.common.dao.UniversalDao;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService05 implements Serializable {

    public int[] insertBatch(List<Employee> employees) {
        // TODO ツールで変換できません :  
        int[] countArray = jdbcManager.insertBatch ( employees ) . execute( );
        return countArray;
    }

    public int[] updateBatch(List<Employee> employees) {
        int[] countArray = UniversalDao.batchUpdate(ParamFilter.toList(employees));
        return countArray;
    }

    public int[] deleteBatch(List<Employee> employees) {
        // TODO ツールで変換できません : suppresOptimisticLockException().
        int[] countArray = jdbcManager.deleteBatch(employees).suppresOptimisticLockException().execute();
        return countArray;
    }

}
