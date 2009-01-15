package edu.wustl.cab2b.common.queryengine;

import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.user.UserInterface;
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

    private EntityInterface outputEntity;

    private Long userId;

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
     * This method returns the user id
     * @hibernate.property name="userId" column="USER_ID" type="long" length="30" not-null="true" 
     * @return user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method sets the user id
     * @param userId 
     * 
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method sets the user id of the given user
     * @param user
     */
    public void setUserId(UserInterface user) {
        userId = user.getUserId();
    }
}
