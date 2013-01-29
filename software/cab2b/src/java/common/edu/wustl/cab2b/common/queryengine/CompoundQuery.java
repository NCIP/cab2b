/*L
 * Copyright Georgetown University, Washington University.
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
 * @author chetan_patil
 *
 */
public interface CompoundQuery extends ICab2bQuery {
    
    /**
     * This method adds a single query to the collection of sub queries
     * @param query
     */
    void addSubQuery(ICab2bQuery query);
    
    /**
     * This method sets the given collections of queries 
     * @param queries
     */
    void setSubQueries(Collection<ICab2bQuery> queries);
        
    /**
     * This method returns respective the sub queries for all the child categories.
     * @return
     */
    Collection<ICab2bQuery> getSubQueries();

}
