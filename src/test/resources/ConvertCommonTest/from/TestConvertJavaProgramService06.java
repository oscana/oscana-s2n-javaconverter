package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService06 implements Serializable {

    public int insert(Employee employee) {
        int count = jdbcManager.insert(employee).execute();
        return count;
    }

}