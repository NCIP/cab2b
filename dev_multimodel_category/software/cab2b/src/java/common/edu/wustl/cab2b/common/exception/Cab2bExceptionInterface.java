/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.exception;

/**
 * @author chandrakant_talele
 */
public interface Cab2bExceptionInterface {
    /**
     * @return Returns the errorCode.
     */
    String getErrorCode();

    /**
     * @param errorCode The errorCode to set.
     */
    void setErrorCode(String errorCode);
}
