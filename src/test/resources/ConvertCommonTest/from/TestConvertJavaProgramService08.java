package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService08 implements Serializable {

    public int delete(Employee employee) {
        int count = jdbcManager.delete ( employee ) . execute( );
        return count;
    }

}