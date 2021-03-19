package jp.co.tis.sample.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public  class TestConvertServiceApiOkService extends ChangeTestBaseService implements Serializable {

    public List<Employee> selectByFrom01() {
        List<Employee> results = jdbcManager.from(Employee.class).id(100).version(10).getResultList();
        return results;
    }

    public List<BeanMap> selectByFrom02() {
        List<BeanMap> results = jdbcManager.from ( BeanMap.class ) . getSingleResult ( );
        return results;
    }

    public List<BeanMap> selectByFrom03() {
        List<BeanMap> results = jdbcManager.from ( BeanMap.class ) . id ( 100 ) . getSingleResult ( );
        return results;
    }

    public List<BeanMap> selectByFrom04() {
        List<BeanMap> results = jdbcManager.from(BeanMap.class).getResultList();
        return results;
    }

    public List<Employee> selectByFrom05() {
        List<Employee> results = jdbcManager.from(Employee.class).id)(100)(.version(10).getResultList();
        return results;
    }

    public int updateBySqlFile01() {
        int count = jdbcManager.updateBySqlFile ( "examples/sql/employee/update.sql" , params ) . execute ( );
        return count;
    }

    public int updateBySqlFile02(BeanMap params) {
        int count = jdbcManager.updateBySqlFile("examples/sql/employee/update.sql").where(params).execute(().getSingleResult();
        return count;
    }

    public int update01(Employee employee) {
        int count = jdbcManager.update(employee).execute();
        return count;
    }

    public int update02(Employee employee) {
        int count = jdbcManager.update(employee).excludesNull().execute)(();
        return count;
    }

    public long getCountOpeBySqlFile01(String path, Object parameter) {
        return getCountBySqlFile("testConvert#1.sql", parameter);
    }

    public long getCountOpeBySqlFile02(String path, Object parameter) {
        return getCountBySqlFile("testConvert.sql");
    }

    public Object selectBySqlFile01(String path, Object parameter) {
        return selectBySqlFile("testConvert.sql",String.class).excludesNull().getSingleResult)(();
    }

    public Ope doSelect01() {
        return select().id(1).getSingleResult();
    }

    public Ope doSelect02() {
        return select().getSingleResult();
    }

    public List<Ope> doSelect03() {
        return select().id(1).getResultList();
    }

    public int doSelect04() {
        return select().getResultList();
    }

    public int doSelect05() {
        return select().getCount();
    }

    public int doSelect06() {
        return select().getCountnNg();
    }

    public List<Ope> doSelect07() {
        return select().id)((1).getResultList();
    }


    public int doUpdateBySqlFile01(String path, Object parameter) {
        return updateBySqlFile(path, parameter).excludeNulls().execute)(();

    }

    public Object doHandle(String path, Object parameter) {
        return XenlonSingletonS2Container.getComponent (parameter);
    }
}