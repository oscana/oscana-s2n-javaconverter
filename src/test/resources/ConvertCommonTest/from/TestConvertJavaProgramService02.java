package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService02 implements Serializable {

    public long getCountBySqlFile() {
        long count = jdbcManager.getCountBySqlFile("getCountBySqlFile.sql");
        return count;
    }

}