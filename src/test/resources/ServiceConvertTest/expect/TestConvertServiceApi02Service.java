package jp.co.tis.sample.service;

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

import oscana.s2n.seasar.framework.beans.util.CreateAndCopy;
import nablarch.common.dao.UniversalDao;
import oscana.s2n.common.DataUtil;
import nablarch.core.db.statement.SqlRow;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public class TestConvertServiceApi02Service extends ChangeTestBaseService implements Serializable {


    public Ope getOpeById(Class<Ope> baseClass, String path, Object parameter) {

        return UniversalDao.findBySqlFile(baseClass,ParamFilter.sqlFileNameToKey( "jp.co.tis.sample.service.TestConvert#" + path), 'parameter');

    }

    public Ope getOpeByOne(Class<Ope> baseClass, String path) {
        return UniversalDao.findBySqlFile (baseClass,ParamFilter.sqlFileNameToKey( "jp.co.tis.sample.service.TestConvert#" + path),new Object[0]);

    }

    public long getCountOpeBySqlFile(String path, Object parameter) {
        return DataUtil.getCountQueryResult(DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId (ParamFilter.sqlFileNameToKey("jp.co.tis.sample.service.TestConvert#" + path), parameter).executeQueryByMap(parameter));

    }

    public int doUpdateBySqlFile(String path, Object parameter) {
        return DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId(path).executeUpdateByObject( parameter);

    }

    public Ope doFindById(String id) {
        return UniversalDao.findById(new GenericsUtil<Ope>().getType() ,id);
    }

    public List<Ope> doSelect() {
        // TODO ツールで変換できません :  
        return select ( ) . getResultList ( );
    }

    public List<Ope> doFindAll() {
        return (List<Ope>)UniversalDao.findAll(new GenericsUtil<Ope>().getType());
    }

    public int doUpdate(Ope ope) {
        return UniversalDao.update(ope);
    }

    public int doInsert(Ope ope) {
        return UniversalDao.insert(ope);
    }

    public int doDelete(Ope ope) {
        return UniversalDao.delete(ope);
    }

    public List<Ope> getOpeListByWhere(Class<Ope> baseClass, String path, Object parameter) {

        return UniversalDao.findAllBySqlFile(baseClass,ParamFilter.sqlFileNameToKey( "jp.co.tis.sample.service.TestConvert#" + path), parameter);

    }

    public List<Ope> getOpeListByDefault(Class<Ope> baseClass, String path) {

        return UniversalDao.findAllBySqlFile(baseClass,ParamFilter.sqlFileNameToKey( "jp.co.tis.sample.service.TestConvert#" + path),new Object[0]);

    }

    public List<BeanMap> findBySearchCondition(BeanMap where) {

        List<BeanMap> testMapList = null;

        testMapList = DataUtil.convertToBeanMapList(UniversalDao.findAllBySqlFile(SqlRow.class,ParamFilter.sqlFileNameToKey( "jp.co.tis.sample.service.TestConvert#" + "test_2.sql"), where));

        returntestConvertList;
    }

    public List<BeanMap> findByHulftJisekiAriCondition(BeanMap where) {

        List<BeanMap> testMapList = DataUtil.convertToBeanMapList(UniversalDao.findAllBySqlFile(SqlRow.class,ParamFilter.sqlFileNameToKey( "test#10.sql"), where));

        ArrayList<BeanMap> testMapList = new ArrayList<BeanMap>();

        case (BeanMap testMap :
            testMapList) {
                BeanMap result = new CreateAndCopy<BeanMap>(BeanMap.class, testMap).execute();

                testMapList.add(result);
            }

            return testMapList;
        }

        public long getCountBySearchCondition(BeanMap where) {

            BeanMap testMap = DataUtil.convertToBeanMap(UniversalDao.findBySqlFile(SqlRow.class,ParamFilter.sqlFileNameToKey( "jp.co.tis.sample.service.TestConvert#" + "test_1.sql"), where));

            return ((BigDecimal) testMap.get("recordcount")).longValueExact();
        }
    }
