package edu.wustl.cab2b.common.locator;

import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.exception.RuntimeException;


/**
 * All exception occured in {@link edu.wustl.cab2b.common.locator.Locator} will be wrapped in this exception.
 * @author Chandrakant Talele
 */
public class LocatorException extends RuntimeException{

    /**
     * Default Constructor
     */
    public LocatorException() {
        super();
    }

    /**
     * @param message Message to set
     * @param cause Cause for exception
     */
    public LocatorException(String message, Throwable cause, String errorCode) {
        super(message, (Exception)cause, errorCode);
        cause.printStackTrace();
    }

    /**
     * @param message Message to set
     */
    public LocatorException(String message) {
        super(message);
    }

    /**
     * @param cause Cause for exception
     */
    public LocatorException(Throwable cause) {
        super((Exception)cause);
        cause.printStackTrace();
    }
}
