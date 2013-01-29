/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.category;

/**
 * Interface for CategoryPopularity
 * 
 * @author hrishikesh_rajpathak
 *
 */
public interface CategoryPopularityInterface {

    /**
     * Method to get popularity
     * 
     * @return popularity count
     */
    long getPopularity();

    /**
     * Set Popularity
     * 
     * @param popularity
     */
    void setPopularity(long popularity);

    /**
     * Set entity id
     * 
     * @param entityId
     */
    void setEntityId(long entityId);

    /**
     * This method returns the entity id
     * 
     * @return entity identifier
     */
    long getEntityId();

    /** 
     * Increment popularity count by one 
     */
    void incPopularity();

}
