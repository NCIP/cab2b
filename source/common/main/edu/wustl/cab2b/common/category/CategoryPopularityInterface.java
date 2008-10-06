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
     * Get entity id
     * 
     * @return
     */
    long getEntityId();

    /** 
     * Increment popularity count by one 
     */
    void incPopularity();

}
