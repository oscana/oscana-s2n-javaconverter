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

import nablarch.common.dao.UniversalDao;
import oscana.s2n.common.DataUtil;

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
import oscana.s2n.common.DataUtil;
import nablarch.core.db.statement.SqlRow;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public  class TestConvertServiceApiOkService extends ChangeTestBaseService implements Serializable {

    public List<Employee> selectByFrom01() {
        // TODO ツールで変換できません : version(10).
        List<Employee> results = jdbcManager.from(Employee.class).id(100).version(10).getResultList();
        return results;
    }

    public List<BeanMap> selectByFrom02() {
        // TODO ツールで変換できません :  
        List<BeanMap> results = jdbcManager.from ( BeanMap.class ) . getSingleResult ( );
        return results;
    }

    public List<BeanMap> selectByFrom03() {
        // TODO ツールで変換できません :   
        List<BeanMap> results = jdbcManager.from ( BeanMap.class ) . id ( 100 ) . getSingleResult ( );
        return results;
    }

    public List<BeanMap> selectByFrom04() {
        List<BeanMap> results = UniversalDao.findAll(BeanMap.class);
        return results;
    }

    public List<Employee> selectByFrom05() {
        List<Employee> results = UniversalDao.findById(Employee.class,100)(.version(10);
        return results;
    }

    public int updateBySqlFile01() {
        // TODO ツールで変換できません :   
        int count = jdbcManager.updateBySqlFile ( "examples/sql/employee/update.sql" , params ) . execute ( );
        return count;
    }

    public int updateBySqlFile02(BeanMap params) {
        int count = jdbcManager.updateBySqlFile("examples/sql/employee/update.sql").where(params).execute(().getSingleResult(); return count; }  
        public int update01(Employee employee) { int count = UniversalDao.update(employee); return count; }  
        // TODO ツールで変換できません : excludesNull().
        public int update02(Employee employee) { int count = jdbcManager.update(employee).excludesNull().execute)((); return count; }  
        public long getCountOpeBySqlFile01(String path, Object parameter) { return DataUtil.getCountQueryResult(DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId(ParamFilter.sqlFileNameToKey("testConvert#1.sql"), parameter).executeQueryByMap(parameter)); }  
        public long getCountOpeBySqlFile02(String path, Object parameter) { return DataUtil.getCountQueryResult(DbConnectionContext.getConnection().prepareParameterizedSqlStatementBySqlId(ParamFilter.sqlFileNameToKey("jp.co.tis.sample.service.TestConvert#" + "testConvert.sql")).executeQueryByMap(null)); }  
        // TODO ツールで変換できません : excludesNull().
        public Object selectBySqlFile01(String path, Object parameter) { return selectBySqlFile("testConvert.sql",String.class).excludesNull().getSingleResult)((); }  
        public Ope doSelect01() { return UniversalDao.findById(new GenericsUtil<null>().getType() ,1); }  
        public Ope doSelect02() { return UniversalDao.findAll(new GenericsUtil<null>().getType()); }  
        public List<Ope> doSelect03() { return UniversalDao.findById(new GenericsUtil<null>().getType() ,1); }  
        public int doSelect04() { return UniversalDao.findAll(new GenericsUtil<null>().getType()); }  
        public int doSelect05() { return select().getCount(); }  
        // TODO ツールで変換できません : getCountnNg
        public int doSelect06() { return select().getCountnNg(); }  
        public List<Ope> doSelect07() { return UniversalDao.findById(new GenericsUtil<null>().getType() ,(1); }   
        // TODO ツールで変換できません : excludeNulls().(
        public int doUpdateBySqlFile01(String path, Object parameter) { return updateBySqlFile(path, parameter).excludeNulls().execute)(();  }  
        public Object doHandle(String path, Object parameter) { return Containers.get().getComponent (parameter); } } 
