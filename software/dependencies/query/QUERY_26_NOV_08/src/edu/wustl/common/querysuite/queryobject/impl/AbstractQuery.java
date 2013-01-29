/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import java.util.Date;

import edu.wustl.common.querysuite.queryobject.IAbstractQuery;

/**
 * @author vijay_pande
 * @author chetan_patil
 * Class created for the model changes for Composite Query.
 */
public class AbstractQuery extends BaseQueryObject implements IAbstractQuery {
    /** Default serial version id */
    private static final long serialVersionUID = 1L;

    /** Name of the query */
    protected String name;

    /** Type of the query */
    protected String type;

    /** Description of the query */
    protected String description;

    /** Date of creation */
    protected Date createdDate;

    /** The identifier of the creator/user */
    protected Long createdBy;

    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#getName()
     *
     * @hibernate.property name="name" column="QUERY_NAME" type="string"
     *                     length="255" unique="true"
     */
    public String getName() {
        return name;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#getDescription()
     *
     * @hibernate.property name="description" column="DESCRIPTION" type="string" not-null="true"
     */
    public String getDescription() {
        return description;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IDescribable#setDescription()
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the type
     *
     * @hibernate.property name="type" column="QUERY_TYPE" type="string" length="30"
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the createdDate
     *
     * @hibernate.property name="createdDate" column="CREATED_DATE" type="timestamp" not-null="true"
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the createdBy
     *
     * @hibernate.property name="createdBy" column="CREATED_BY" type="long" not-null="true"
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
