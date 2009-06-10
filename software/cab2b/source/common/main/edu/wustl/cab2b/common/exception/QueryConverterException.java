/**
 *
 */
package edu.wustl.cab2b.common.exception;

/**
 * @author chetan_patil
 *
 */
public class QueryConverterException extends RuntimeException {

    /**
     *
     */
    public QueryConverterException() {
        super();
    }

    /**
     * @param throwable
     */
    public QueryConverterException(Throwable throwable) {
        super(throwable);
    }

    /**
     * @param message
     */
    public QueryConverterException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param errorCode
     */
    public QueryConverterException(String message, String errorCode) {
        super(message, errorCode);
    }

    /**
     * @param message
     * @param throwable
     */
    public QueryConverterException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * @param message
     * @param throwable
     * @param errorCode
     */
    public QueryConverterException(String message, Throwable throwable, String errorCode) {
        super(message, throwable, errorCode);
    }

}
