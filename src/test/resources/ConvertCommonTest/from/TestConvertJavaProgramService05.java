package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService05 implements Serializable {

    public int[] insertBatch(List<Employee> employees) {
        int[] countArray = jdbcManager.insertBatch ( employees ) . execute( );
        return countArray;
    }

    public int[] updateBatch(List<Employee> employees) {
        int[] countArray = jdbcManager.updateBatch(employees).execute();
        return countArray;
    }

    public int[] deleteBatch(List<Employee> employees) {
        int[] countArray = jdbcManager.deleteBatch(employees).suppresOptimisticLockException().execute();
        return countArray;
    }

}