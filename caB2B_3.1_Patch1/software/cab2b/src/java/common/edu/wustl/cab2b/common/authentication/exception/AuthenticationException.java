/**
 *
 */
package edu.wustl.cab2b.common.authentication.exception;

import edu.wustl.cab2b.common.exception.RuntimeException;

/**
 * @author chetan_patil
 *
 */
public class AuthenticationException extends RuntimeException {

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
        super(message, errorCode);
    }

    public AuthenticationException(String message, Throwable t, String errorCode) {
        super(message, t, errorCode);
    }

}
