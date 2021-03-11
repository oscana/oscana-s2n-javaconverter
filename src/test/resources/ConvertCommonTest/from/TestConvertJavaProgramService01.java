package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService01 implements Serializable {

    public List<EmployeeDto> selectBySQLFile() {
        List<EmployeeDto> results = jdbcManager.selectBySqlFile(
                        EmployeeDto.class,
                        "test.sql").getResultList();
        return results;
    }

}