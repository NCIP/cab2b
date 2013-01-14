/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.queryengine;

import java.util.Collection;

/**
 * MultiModelCategoryQuery class represents query object for MultiModelCategory.
 *  
 * @author chetan_patil
 *
 */
public interface MultiModelCategoryQuery extends ICab2bQuery {
    
    /**
     * This method adds a single query to the collection of sub queries
     * @param query
     */
    void addSubQueries(ICab2bQuery query);
    
    /**
     * This method returns respective the sub queries for all the child categories.
     * @return
     */
    Collection<ICab2bQuery> getSubQueries();

}
