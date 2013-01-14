/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.locator;

import edu.wustl.cab2b.common.exception.RuntimeException;

/**
 * All exception occurred in {@link edu.wustl.cab2b.common.locator.Locator} will be wrapped in this exception.
 * @author Chandrakant Talele
 */
public class LocatorException extends RuntimeException {
    private static final long serialVersionUID = 8682893414728832937L;

    /**
     * @param message Message to set
     * @param cause Cause for exception
     */
    public LocatorException(String message, Exception cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
