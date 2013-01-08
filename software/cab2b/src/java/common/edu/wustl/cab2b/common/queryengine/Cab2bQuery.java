/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;

/**
 *
 * @author chetan_patil
 *
 * @hibernate.joined-subclass table="CAB2B_QUERY" extends="edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class Cab2bQuery extends ParameterizedQuery implements ICab2bQuery {
    private static final long serialVersionUID = -3676549385071170949L;

    private List<String> outputClassUrls;
    
    private Collection<ServiceGroup> serviceGroups;

    private EntityInterface outputEntity;

    protected QueryType queryType = QueryType.ANDed;

    /**
     * Default constructor
     */
    public Cab2bQuery() {
        super();
    }

    /**
     * Parameterized constructor
     * @param query
     */
    public Cab2bQuery(ICab2bQuery query) {
        super(query);
        createdDate = query.getCreatedDate();
        createdBy = query.getCreatedBy();
        outputEntity = query.getOutputEntity();
        outputClassUrls = query.getOutputUrls();
    }

    /**
     * Parameterized constructor
     * @param query
     */
    public Cab2bQuery(Long id, String name, String description, Date createdDate) {
        super(id, name, description, createdDate);
    }

    /**
     * This method returns the list of output URLs
     * @return the outputClassUrls.
     *
     * @hibernate.list name="outputClassUrls" table="OUTPUT_CLASS_URLS" cascade="all-delete-orphan" inverse="false" lazy="false"
     * @hibernate.collection-key column="CAB2B_QUERY_ID"
     * @hibernate.collection-index column="POSITION" type="int"
     * @hibernate.collection-element type="string" column="OUTPUT_CLASS_URL"
     * @hibernate.cache usage="read-write"
     */
    public List<String> getOutputUrls() {
        return outputClassUrls;
    }

    /**
     * This method sets the list of output class URLs
     *
     * @param outputClassUrls the outputClassUrls to set.
     */
    public void setOutputUrls(List<String> outputClassUrls) {
        this.outputClassUrls = outputClassUrls;
    }

    /**
     * This method returns the output entity
     * @return output entity
     */
    public EntityInterface getOutputEntity() {
        return outputEntity;
    }

    /**
     * This method sets the output entity
     * @param outputEntity
     */
    public void setOutputEntity(EntityInterface outputEntity) {
        this.outputEntity = outputEntity;
    }

    /**
     * @return the entityId
     *
     * @hibernate.property name="entityId" column="ENTITY_ID" type="long" length="30" not-null="true"
     */
    @SuppressWarnings("unused")
    private Long getEntityId() {
        return outputEntity.getId();
    }

    /**
     * @param entityId the entityId to set
     */
    @SuppressWarnings("unused")
    private void setEntityId(Long entityId) {
        this.outputEntity = AbstractEntityCache.getCache().getEntityById(entityId);
    }

    /**
     * This method returns true if the query is marked for keyword search; false otherwise.
     * @return isKeywordSearch
     */
    public Boolean isKeywordSearch() {
        Boolean isKeywordSearch = Boolean.FALSE;
        if (QueryType.ORed == queryType) {
            isKeywordSearch = Boolean.TRUE;
        } else if (QueryType.ANDed == queryType) {
            isKeywordSearch = Boolean.FALSE;
        }
        return isKeywordSearch;
    }

    /**
     * This method marks whether query is for keyword search or not.
     * @param isKeywordSearch true if marked for keyword search; false if not.
     */
    public void setIsKeywordSearch(Boolean isKeywordSearch) {
        if (isKeywordSearch) {
            queryType = QueryType.ORed;
        } else {
            queryType = QueryType.ANDed;
        }
        type = queryType.toString();
    }

    /* This setter method is required for Hibernate */
    public void setType(String type) {
        this.type = type;
        if (QueryType.ORed.toString().equals(type)) {
            queryType = QueryType.ORed;
        } else if (QueryType.ANDed.toString().equals(type)) {
            queryType = QueryType.ANDed;
        }
    }

	public Collection<ServiceGroup> getServiceGroups() {
		return serviceGroups;
	}

	public void setServiceGroups(Collection<ServiceGroup> serviceGroups) {
		this.serviceGroups = serviceGroups;
	}
}
