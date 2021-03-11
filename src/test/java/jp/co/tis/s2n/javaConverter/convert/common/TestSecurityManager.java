package jp.co.tis.s2n.javaConverter.convert.common;

import java.security.Permission;

/**
* JUnit用SecuriyManager。<br>
* System.Exitが呼ばれた際に、ExitExceptionにステータスを設定し、スローする。
*/
public class TestSecurityManager extends SecurityManager {
    public TestSecurityManager() {
    }

    public void checkPermission(Permission permission) {

    }

    public void checkExit(int status) {
        super.checkExit(status);
        throw new ExitException(status);
    }

    public class ExitException extends SecurityException {
        private int status = 0;

        public int getStatus() {
            return status;
        }

        public ExitException(int status) {
            this.status = status;
        }
    }
}
