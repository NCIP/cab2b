/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

import java.sql.Date;

public interface IDateLiteral extends ILiteral {
    void setDate(Date date);

    Date getDate();
}
