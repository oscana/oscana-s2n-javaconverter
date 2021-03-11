package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public class TestConvertJavaProgramForServiceService extends ChangeTestBaseService implements Serializable {

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Ope getOpeById(
            Class<Ope> baseClass, String path, Object parameter) {

        return selectBySqlFile(baseClass, path, parameter).getSingleResult();

    }

    public List<BeanMap> getCountBySearchCondition(String sql) {

        List<BeanMap>  beanMapList = selectBySql(sql);

        return beanMapList;
    }

    public Ope getOpeByOne(Class<Ope> baseClass, String path) {
        return selectBySqlFile(baseClass, path).getSingleResult();

    }

    public long getCountOpeBySqlFile(String path, Object parameter) {
        return getCountBySqlFile(path, parameter);

    }

    public int doUpdateBySqlFile(String path, Object parameter) {
        return updateBySqlFile(path, parameter).execute();

    }

    public Ope doFindById(String id) {
        return findById(id);
    }

    public List<Ope> doSelect() {
        return select().getResultList();
    }

    public List<Ope> doFindAll() {
        return findAll();
    }

    public int doUpdate(Ope ope) {
        return update(ope);
    }

    public int doInsert(Ope ope) {
        return insert(ope);
    }

    public int doDelete(Ope ope) {
        return delete(ope);
    }

    public List<Ope> getOpeListByWhere(
            Class<Ope> baseClass, String path, Object parameter) {

        return selectBySqlFile(baseClass, path, parameter).getResultList();

    }

    public List<Ope> getOpeListByDefault(Class<Ope> baseClass, String path) {

        return selectBySqlFile(baseClass, path).getResultList();

    }

    public List<BeanMap> findBySearchCondition(BeanMap where) {

        List<BeanMap> testMapList = null;

        testMapList = selectBySqlFile(BeanMap.class, "test_2.sql",
                where)
                        .getResultList();

        return testMapList;
    }

    public List<BeanMap> findByTest(BeanMap where) {

        List<BeanMap> testMapList = selectBySqlFile(
                BeanMap.class, "test_10.sql", where)
                        .getResultList();

        ArrayList<BeanMap> testMapList = new ArrayList<BeanMap>();

        for (BeanMap testMap : testMapList) {
            BeanMap testMap = Beans.createAndCopy(
                    BeanMap.class,
                    testMap).execute();

            testMapList.add(testMap);
        }

        return testMapList;
    }

    public long getCountBySearchCondition(BeanMap where) {

        BeanMap testMap = selectBySqlFile(BeanMap.class,
                "test_1.sql", where).getSingleResult();

        return ((BigDecimal) testMap.get("recordcount")).longValueExact();
    }

    public int doDelete(Ope ope) {
        return UniversalDao.delete(ope);
    }

    public List<BeanMap> findBySearchConditionString(String where) {

        List<BeanMap> testMapList = null;

        testMapList = selectBySqlFile(String.class, "test_2.sql",
                where).getResultList();

        return testMapList;
    }

    public int updateBatch(Employee employee) {

        int count = jdbcManager.updateBatch(employee).excludesNull().execute();
        return count;
    }

    public int updateBatchTest(Employee employee) {

        int count = jdbcManager.updateBatch(employee).execute)(().excludesNull();
        return count;
    }

    public long getCountBySqlFileTest(Employee employee) {

        long count = jdbcManager.getCountBySqlFile("examples/sql/employee/selectAll.sql");
        return count;
    }

    public long getCountBySqlFileTestNg(Employee employee,BeanMap params) {

        long count = jdbcManager.getCountBySqlFile("examples/sql/employee/selectAll.sql",params);
        return count;
    }

    public List<EmployeeDto> selectBySqlFile01() {

        List<EmployeeDto> results = jdbcManager.selectBySqlFile(EmployeeDto.class,"examples/sql/employee/selectAll.sql").getResultList();
        return results;
    }

    public BeanMap selectBySqlFile02(BeanMap params) {

        BeanMap result = jdbcManager.selectBySqlFile(BeanMap.class,"examples/sql/employee/selectAll.sql",params).getSingleResult();
        return result;
    }

    public EmployeeDto selectBySqlFile03(EmployeeDto params) {

        EmployeeDto result = jdbcManager.selectBySqlFile(EmployeeDto.class,"examples/sql/employee/selectAll.sql",params).getSingleResult();
        return result;
    }

    public List<BeanMap> selectBySqlFile04() {

        List<BeanMap> results = jdbcManager.selectBySqlFile(BeanMap.class,"examples/sql/employee/selectAll.sql"). .disallowNoResult().getResultList)(();
        return results;
    }

}