package edu.wustl.cab2b.common.queryengine;

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

    private EntityInterface outputEntity;
    
    public Cab2bQuery() {
        super();
    }
    
    public Cab2bQuery(ICab2bQuery query) {
        super(query);
        outputEntity = query.getOutputEntity();
        outputClassUrls = query.getOutputUrls();    
    }
    
    /**
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
     * @param outputClassUrls
     *            the outputClassUrls to set.
     */
    public void setOutputUrls(List<String> outputClassUrls) {
        this.outputClassUrls = outputClassUrls;
    }

    public EntityInterface getOutputEntity() {
        return outputEntity;
    }

    public void setOutputEntity(EntityInterface outputEntity) {
        this.outputEntity = outputEntity;
    }

    // for hibernate
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
}
