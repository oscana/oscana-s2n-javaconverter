package jp.co.tis.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class TestConvertJavaProgramService04 implements Serializable {

    public Employee from() {
        Employee result = jdbcManager.from ( Employee.class ) . getSingleResult( );
        return result;
    }

}