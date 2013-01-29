/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.result;

import edu.wustl.common.querysuite.metadata.category.Category;

public interface ICategoryResult<C extends ICategorialClassRecord> extends
        IQueryResult<C> {
    Category getCategory();
}
