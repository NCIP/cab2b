/**
 *
 */
package edu.wustl.cab2b.common.authentication.exception;

/**
 * @author chetan_patil
 *
 */
public class AuthenticationException extends RuntimeException {
    protected String errorCode;

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable t) {
        super(message, t);
    }

    public AuthenticationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthenticationException(String message, Throwable t, String errorCode) {
        super(message, t);
        this.errorCode = errorCode;
    }

    /**
     * @return Returns the errorCode.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode The errorCode to set.
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
