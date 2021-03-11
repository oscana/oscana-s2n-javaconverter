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

import oscana.s2n.seasar.framework.beans.util.CreateAndCopy;
import nablarch.common.dao.UniversalDao;
import oscana.s2n.common.DataUtil;
import nablarch.core.db.statement.SqlRow;

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
import nablarch.core.db.statement.SqlRow;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public class TestConvertJavaProgramForServiceService extends ChangeTestBaseService implements Serializable {

    // TODO ツールで変換できません
    // @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Ope getOpeById(Class<Ope> baseClass, String path, Object parameter) {

        return UniversalDao.findBySqlFile(baseClass,ParamFilter.sqlFileNameToKey( "jp.co.tis.service.TestConvertJavaProgramFor#" + path), parameter);

    }

    public List<BeanMap> getCountBySearchCondition(String sql) {

        // TODO ツールで変換できません
        // List<BeanMap>  beanMapList = selectBySql(sql);

        return beanMapList;
    }

    public Ope getOpeByOne(Class<Ope> baseClass, String path) {
        return UniversalDao.findBySqlFile(baseClass,ParamFilter.sqlFileNameToKey( "jp.co.tis.service.TestConvertJavaProgramFor#" + path),new Object[0]);

    }

    public long getCountOpeBySqlFile(String path, Object parameter) {
        return DataUtil.getCountQueryResult(DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId(ParamFilter.sqlFileNameToKey("jp.co.tis.service.TestConvertJavaProgramFor#" + path), parameter).executeQueryByMap(parameter));

    }

    public int doUpdateBySqlFile(String path, Object parameter) {
        return DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId(path).executeUpdateByObject( parameter);

    }

    public Ope doFindById(String id) {
        return UniversalDao.findById(new GenericsUtil<Ope>().getType() ,id);
    }

    public List<Ope> doSelect() {
        return UniversalDao.findAll(new GenericsUtil<Ope>().getType());
    }

    public List<Ope> doFindAll() {
        return UniversalDao.findAll(new GenericsUtil<Ope>().getType());
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

        return UniversalDao.findAllBySqlFile(baseClass,ParamFilter.sqlFileNameToKey( "jp.co.tis.service.TestConvertJavaProgramFor#" + path), parameter);

    }

    public List<Ope> getOpeListByDefault(Class<Ope> baseClass, String path) {

        return UniversalDao.findAllBySqlFile(baseClass,ParamFilter.sqlFileNameToKey( "jp.co.tis.service.TestConvertJavaProgramFor#" + path),new Object[0]);

    }

    public List<BeanMap> findBySearchCondition(BeanMap where) {

        List<BeanMap> testMapList = null;

        testMapList = DataUtil.convertToBeanMapList(UniversalDao.findAllBySqlFile(SqlRow.class,ParamFilter.sqlFileNameToKey( "jp.co.tis.service.TestConvertJavaProgramFor#" + "test_2.sql"), where));

        return testMapList;
    }

    public List<BeanMap> findByTest(BeanMap where) {

        List<BeanMap> testMapList = DataUtil.convertToBeanMapList(UniversalDao.findAllBySqlFile(SqlRow.class,ParamFilter.sqlFileNameToKey( "jp.co.tis.service.TestConvertJavaProgramFor#" + "test_10.sql"), where));

        ArrayList<BeanMap> testMapList = new ArrayList<BeanMap>();

        for (BeanMap testMap : testMapList) {
            BeanMap testMap = new CreateAndCopy<BeanMap>(BeanMap.class, testMap).execute();

            testMapList.add(testMap);
        }

        return testMapList;
    }

    public long getCountBySearchCondition(BeanMap where) {

        BeanMap testMap = DataUtil.convertToBeanMap(UniversalDao.findBySqlFile(SqlRow.class,ParamFilter.sqlFileNameToKey( "jp.co.tis.service.TestConvertJavaProgramFor#" + "test_1.sql"), where));

        return ((BigDecimal) testMap.get("recordcount")).longValueExact();
    }

    public int doDelete(Ope ope) {
        return UniversalDao.delete(ope);
    }

    public List<BeanMap> findBySearchConditionString(String where) {

        List<BeanMap> testMapList = null;

        testMapList = UniversalDao.findAllBySqlFile(SqlRow.class,ParamFilter.sqlFileNameToKey( "jp.co.tis.service.TestConvertJavaProgramFor#" + "test_2.sql"), where);

        return testMapList;
    }

    public int updateBatch(Employee employee) {

        // TODO ツールで変換できません : excludesNull().
        int count = jdbcManager.updateBatch(employee).excludesNull().execute();
        return count;
    }

    public int updateBatchTest(Employee employee) {

        int count = UniversalDao.batchUpdate(ParamFilter.toList(employee)).excludesNull();
        return count;
    }

    public long getCountBySqlFileTest(Employee employee) {

        long count = DataUtil.getCountQueryResult(DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId(ParamFilter.sqlFileNameToKey("examples/sql/employee/selectAll.sql")).executeQueryByMap(null));
        return count;
    }

    public long getCountBySqlFileTestNg(Employee employee,BeanMap params) {

        long count = DataUtil.getCountQueryResult(DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId(ParamFilter.sqlFileNameToKey("examples/sql/employee/selectAll.sql"),params).executeQueryByMap(params));
        return count;
    }

    public List<EmployeeDto> selectBySqlFile01() {

        List<EmployeeDto> results = UniversalDao.findAllBySqlFile(EmployeeDto.class,ParamFilter.sqlFileNameToKey("examples/sql/employee/selectAll.sql"),new Object[0]);
        return results;
    }

    public BeanMap selectBySqlFile02(BeanMap params) {

        BeanMap result = DataUtil.convertToBeanMap(UniversalDao.findBySqlFile(SqlRow.class,ParamFilter.sqlFileNameToKey("examples/sql/employee/selectAll.sql"),params));
        return result;
    }

    public EmployeeDto selectBySqlFile03(EmployeeDto params) {

        EmployeeDto result = UniversalDao.findBySqlFile(EmployeeDto.class,ParamFilter.sqlFileNameToKey("examples/sql/employee/selectAll.sql"),params);
        return result;
    }

    public List<BeanMap> selectBySqlFile04() {

        // TODO ツールで変換できません :  .disallowNoResult().
        List<BeanMap> results = jdbcManager.selectBySqlFile(BeanMap.class,"examples/sql/employee/selectAll.sql"). .disallowNoResult().getResultList)(();
        return results;
    }

}
