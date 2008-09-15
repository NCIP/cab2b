package edu.wustl.cab2b.common.exception;

/**
 * @author gautam_shetty
 */
public class CheckedException extends Exception implements Cab2bExceptionInterface {
    static final long serialVersionUID = 123456L;

    protected String errorCode;

    public CheckedException() {
        super();
    }

    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(Exception exp) {
        super(exp);
    }

    public CheckedException(String message, Exception exp, String errorCode) {
        super(message, exp);
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
