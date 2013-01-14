/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.util.AttributeInterfaceComparator;

/**
 * Skeletal implementation of a {@link DataListSaver}. A concrete
 * implementation need only implement the method
 * {@link #populateNewEntity(EntityInterface)} to add attributes and/or
 * associations to the newly created entity.
 * <p>
 * {@link #populateNewEntity()} is then called; subclasses are expected to
 * populate the new entity with attributes/associations appropriately. e.g. the
 * default saver merely copies non-virtual attributes from the old entity,
 * whereas FooBarSaver would create an additional virtual attribute in the new
 * entity called "foo".
 * 
 * @author srinath_k
 * 
 * @param <R> the type of record to be saved.
 */
public abstract class AbstractDataListSaver<R extends IRecord> implements DataListSaver<R> {
    /**
     * The new entity created during {@link #initialize(EntityInterface)}.
     */
    protected EntityInterface newEntity;

    /**
     * Tracks if this saver has been initialized by calling
     * {@link #initialize(EntityInterface)}.
     */
    private boolean initialized = false;

    /**
     * Creates a new entity and inits it with essential attributes and tags.
     * <p>
     * The new entity has tags for
     * {@link edu.wustl.cab2b.common.util.DataListUtil#ORIGIN_ENTITY_ID_KEY} and
     * {@link edu.wustl.cab2b.common.util.DataListUtil#SOURCE_ENTITY_ID_KEY}.
     * <p>
     * Two virtual attributes (for id and url of the record) are added to this
     * new entity using {@link DataListUtil}.
     * <p>
     * Then the method {@link #populateNewEntity(EntityInterface)} is called.
     * Finally, this saver is marked intialized by setting {@link #initialized}
     * to <code>true</code>.
     * 
     * @param oldEntity 
     * @see edu.wustl.cab2b.server.datalist.DataListSaver#initialize(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public void initialize(EntityInterface oldEntity) {
        this.newEntity = DataListUtil.createNewEntity(oldEntity);
        populateNewEntity(oldEntity);
        setInitialized(true);
    }

    /**
     * Status of initialized
     * @return
     */
    protected final boolean isInitialized() {
        return initialized;
    }

    /**
     * Set initialized status
     * @param initialized
     */
    protected final void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Transforms a given record to a DE specific representation. The records
     * map is created by calling {@link #transformToMap(IRecord)}. Then entries
     * for the record's id and url are added to this map by calling
     * {@link #putRecordIdInMap(RecordId, Map, EntityInterface)}.
     * 
     * @param record 
     * @return the DE representation of the given record.
     * @throws IllegalStateException if {@link #initialize(EntityInterface)} has
     *             not been called.
     * @see edu.wustl.cab2b.server.datalist.DataListSaver#getRecordAsMap(edu.wustl.cab2b.common.queryengine.result.IRecord)
     */
    public final Map<AbstractAttributeInterface, Object> getRecordAsMap(R record) {
        if (!isInitialized()) {
            throw new IllegalStateException();
        }
        Map<AbstractAttributeInterface, Object> recordsMap = transformToMap(record);
        putRecordIdInMap(record.getRecordId(), recordsMap, newEntity);

        return recordsMap;
    }

    /**
     * Returns the DE representation of the record's attribute-value map. Note
     * that this implies that only the values of non-virtual attributes are
     * added to the result map.
     * 
     * @param record
     * @return the DE representation of the record's attribute-value map.
     */
    protected Map<AbstractAttributeInterface, Object> transformToMap(R record) {
        List<AttributeInterface> recordAttributes = new ArrayList<AttributeInterface>(record.getAttributes());

        List<AttributeInterface> newEntityAttributes = new ArrayList<AttributeInterface>(
                newEntity.getAttributeCollection());

        for (Iterator<AttributeInterface> iter = newEntityAttributes.iterator(); iter.hasNext();) {
            if (DataListUtil.isVirtualAttribute(iter.next())) {
                iter.remove();
            }
        }
        // set of nonVirtual attributes in newEntity equals (by name) set of
        // attributes in
        // record (FOR THE DEFAULT CODE).
        if (!(recordAttributes.size() == newEntityAttributes.size())) {
            throw new IllegalArgumentException();
        }
        Collections.sort(recordAttributes, new AttributeInterfaceComparator());
        Collections.sort(newEntityAttributes, new AttributeInterfaceComparator());

        Map<AbstractAttributeInterface, Object> recordsMap = new HashMap<AbstractAttributeInterface, Object>();
        for (int i = 0; i < newEntityAttributes.size(); i++) {
            recordsMap.put(newEntityAttributes.get(i), record.getValueForAttribute(recordAttributes.get(i)));
        }
        return recordsMap;
    }

    /**
     * Returns the new entity created during the initialization process.
     * 
     * @see edu.wustl.cab2b.server.datalist.DataListSaver#getNewEntity()
     * @throws IllegalStateException if this saver is not yet completely
     *             intialized.
     */
    public final EntityInterface getNewEntity() {
        if (!isInitialized()) {
            throw new IllegalStateException();
        }
        return this.newEntity;
    }

  
    private Long getOriginEntityId(EntityInterface oldEntity) {
        return edu.wustl.cab2b.common.util.DataListUtil.getOriginEntity(oldEntity).getId();
    }

    /**
     * Subclasses should access the newly created entity using
     * {@link #newEntity}.
     * 
     * @param oldEntity
     */
    protected abstract void populateNewEntity(EntityInterface oldEntity);

    /**
     * Adds virtual attributes for id and url to the given entity.
     * 
     * @param entity
     */
    protected final void addVirtualAttributes(EntityInterface entity) {
        AttributeInterface idAttribute = getDomainObjectFactory().createStringAttribute();
        idAttribute.setName(DataListUtil.ID_ATTRIBUTE_NAME);
        DataListUtil.markVirtual(idAttribute);

        AttributeInterface urlAttribute = getDomainObjectFactory().createStringAttribute();
        urlAttribute.setName(DataListUtil.URL_ATTRIBUTE_NAME);
        DataListUtil.markVirtual(urlAttribute);

        entity.addAttribute(idAttribute);
        entity.addAttribute(urlAttribute);
    }

    /**
     * Puts the contents of given recordId (id and url) into the given map. The
     * id and url attributes of given entity are obtained using
     * {@link DataListUtil#getVirtualIdAttribute(EntityInterface)} and{@link DataListUtil#getVirtualUrlAttribute(EntityInterface)}
     * respectively.
     * 
     * @param recordId
     * @param map
     * @param entity
     */
    protected final void putRecordIdInMap(RecordId recordId, Map<AbstractAttributeInterface, Object> map,
                                          EntityInterface entity) {
        map.put(DataListUtil.getVirtualIdAttribute(entity), recordId.getId());
        map.put(DataListUtil.getVirtualUrlAttribute(entity), recordId.getUrl());
    }

    /**
     * Returns reference to DomainObjectFactory
     * @return Reference to DomainObjectFactory
     */
    protected final DomainObjectFactory getDomainObjectFactory() {
        return DomainObjectFactory.getInstance();
    }
}