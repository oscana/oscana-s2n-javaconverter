package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService03 implements Serializable {

    public int updateBySqlFile() {
        UpdateParam param = new UpdateParam();
        param.salary = new BigDecimal(1200);
        param.id = 10;
        int count = jdbcManager.updateBySqlFile("update.sql",param).execute();
        return count;
    }

}