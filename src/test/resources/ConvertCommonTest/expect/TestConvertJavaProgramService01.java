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
public class TestConvertJavaProgramService01 implements Serializable {

    public List<EmployeeDto> selectBySQLFile() {
        List<EmployeeDto> results = UniversalDao.findAllBySqlFile(EmployeeDto.class,ParamFilter.sqlFileNameToKey( "test.sql"),new Object[0]);
        return results;
    }

}
