package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService07 implements Serializable {

    public int update(Employee employee) {
        int count = jdbcManager.update(employee).execute();
        return count;
    }

}