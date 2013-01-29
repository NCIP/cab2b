/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

import java.util.Date;

/**
 * @author vijay_pande
 *
 */
public interface IAbstractQuery extends IBaseQueryObject, INameable, IDescribable {
    /**
     * This method returns the type of the query object
     * @return
     */
    String getType();

    /**
     * This method sets the type of the query object
     * @param type
     */
    void setType(String type);

    /**
     * This method returns the date of creation
     * @return
     */
    Date getCreatedDate();

    /**
     * This method sets the date of creation
     * @param createdDate
     */
    void setCreatedDate(Date createdDate);

    /**
     * This method returns the identifier of the creator/user/owner
     * @return
     */
    Long getCreatedBy();

    /**
     * This method sets the identifier of the creator/user/owner
     * @param createdBy
     */
    void setCreatedBy(Long createdBy);
}
