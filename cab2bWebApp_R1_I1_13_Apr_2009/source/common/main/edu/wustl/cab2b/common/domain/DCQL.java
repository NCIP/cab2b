/**
 * 
 */
package edu.wustl.cab2b.common.domain;

import java.io.Serializable;

/**
 * @author chetan_patil
 *
 */
public class DCQL implements Serializable, Cloneable {

    private String entityName;

    private String dcqlQuery;

    /**
     * @param entityName
     * @param dcqlQuery
     */
    public DCQL(String entityName, String dcqlQuery) {
        super();
        this.entityName = entityName;
        this.dcqlQuery = dcqlQuery;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return the dcqlQiery
     */
    public String getDcqlQuery() {
        return dcqlQuery;
    }

    /**
     * @param dcqlQiery the dcqlQiery to set
     */
    public void setDcqlQuery(String dcqlQuery) {
        this.dcqlQuery = dcqlQuery;
    }

    public String toString() {
        return entityName.substring(entityName.lastIndexOf('.') + 1, entityName.length());
    }

    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (null != obj && obj instanceof DCQL) {
            DCQL dcql = (DCQL) obj;
            if (entityName.equals(dcql.getEntityName()) && dcqlQuery.equals(dcql.getDcqlQuery())) {
                isEqual = true;
            }
        }
        return isEqual;
    }

    public int hashCode() {
        return entityName.hashCode() + dcqlQuery.hashCode();
    }

}
