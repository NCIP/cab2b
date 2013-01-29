/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine;

import java.util.Date;

/**
 * @author chetan_patil
 *
 */
public class MultiModelCategoryQueryImpl extends CompoundQueryImpl implements MultiModelCategoryQuery {
    private static final long serialVersionUID = -7969272734716913783L;

    /**
     * Default constructor
     */
    public MultiModelCategoryQueryImpl() {
        super();
    }

    /**
     * Parameterized constructor
     */
    public MultiModelCategoryQueryImpl(ICab2bQuery query) {
        super(query);
    }

    /**
     * Parameterized constructor
     * @param id
     * @param name
     * @param description
     * @param createdDate
     */
    public MultiModelCategoryQueryImpl(Long id, String name, String description, Date createdDate) {
        super(id, name, description, createdDate);
    }

}
