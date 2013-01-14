/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityRecordInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.RecordId;

/**
 * Skeletal implementation of a {@link DataListRetriever}. A concrete
 * implementation need only implement the method
 * {@link #createRecord(EntityInterface, Set, RecordId)} to create a record of
 * appropriate type.
 * <p>
 * A hook ({@link #copyOtherFields(IRecord, EntityRecordInterface, List, EntityInterface)})
 * is provided to copy more values from the DE record to the record created by
 * {@link #createRecord(EntityInterface, Set, RecordId)}.
 * 
 * @author srinath_k
 * 
 * @param <R> the type of record to be retrieved.
 */
public abstract class AbstractDataListRetriever<R extends IRecord> implements DataListRetriever<R> {
    /**
     * The entity whose data is to be retrieved.
     */
    private EntityInterface newEntity;

    /**
     * Tracks if this retriever has been initialized by calling
     * {@link #initialize(EntityInterface)}.
     */
    private boolean initialized = false;

    /**
     * Merely saves the provided entity in a member variable and marks this
     * retriever as initialized. The entity is the one whose records are to be
     * fetched.
     * 
     * @see edu.wustl.cab2b.server.datalist.DataListRetriever#initialize(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public void initialize(EntityInterface entity) {
        this.newEntity = entity;
        setInitialized(true);
    }

    protected final boolean isInitialized() {
        return initialized;
    }

    protected final void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Returns the entity that was provided at initialization time.
     */
    protected final EntityInterface getNewEntity() {
        if (!isInitialized()) {
            throw new IllegalStateException();
        }
        return this.newEntity;
    }

    /**
     * Checks if the retriever has been initialized and then calls
     * {@link #getEntityRecords(EntityInterface, List)}.
     * 
     * @see edu.wustl.cab2b.server.datalist.DataListRetriever#getEntityRecords(java.util.List)
     */
    public final List<R> getEntityRecords(List<Long> recordIds) {
        if (!isInitialized()) {
            throw new IllegalStateException();
        }
        return getEntityRecords(newEntity, recordIds);
    }

    /**
     * Obtains the specified records of the given entity using DE and transforms
     * the results to appropriate subtype of {@link IRecord}. Which
     * attributes'/associations' values are to be fetched is obtained by calling
     * {@link #getAttributesList(EntityInterface)}.
     * <p>
     * In this process, for each DE record,
     * <ol>
     * <li>A new caB2B record is created using
     * {@link #createRecord(EntityInterface, Set, RecordId)}. </li>
     * <li>The values of the non-virtual attributes of the entity are set in
     * the attribute-values map of this record.</li>
     * <li>The {@link RecordId} of this record is set.</li>
     * <li>{@link #copyOtherFields(IRecord, EntityRecordInterface, List, EntityInterface)}
     * is called.</li>
     * </ol>
     * So subclasses can
     * <ol>
     * <li>Specify additional associations/attributes by overriding
     * {@link #getAttributesList(EntityInterface)}.</li>
     * <li>Copy values of any additional fields as needed by overriding
     * {@link #copyOtherFields(IRecord, EntityRecordInterface, List, EntityInterface)}</li>.
     * </ol>
     * 
     * @param entity
     * @param recordIds
     * @return the list of caB2B records.
     */
    protected List<R> getEntityRecords(EntityInterface entity, List<Long> recordIds) {
        try {
            EntityRecordResultInterface recordResultInterface = EntityManager.getInstance().getEntityRecords(
                                                                                                             entity,
                                                                                                             getAttributesList(entity),
                                                                                                             recordIds);

            return transformToRecords(recordResultInterface, entity);
        } catch (DynamicExtensionsSystemException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DE_0004));
        }
    }

    private List<R> transformToRecords(EntityRecordResultInterface recordResultInterface, EntityInterface entity) {
        List<R> res = new ArrayList<R>();
        List<? extends AbstractAttributeInterface> attributesList = recordResultInterface.getEntityRecordMetadata().getAttributeList();
        int idIndex = attributesList.indexOf(DataListUtil.getVirtualIdAttribute(entity));
        int urlIndex = attributesList.indexOf(DataListUtil.getVirtualUrlAttribute(entity));

        Set<AttributeInterface> recordAttributes = getNonVirtualAttributes(attributesList);

        for (EntityRecordInterface recordInterface : recordResultInterface.getEntityRecordList()) {
            List values = recordInterface.getRecordValueList();
            String id = values.get(idIndex).toString();
            String url = values.get(urlIndex).toString();
            RecordId recordId = new RecordId(id, url);
            R record = createRecord(entity, recordAttributes, recordId);
            res.add(record);
            for (int i = 0; i < attributesList.size(); i++) {
                if (recordAttributes.contains(attributesList.get(i))) {
                    AttributeInterface attribute = (AttributeInterface) attributesList.get(i);
                    record.putStringValueForAttribute(attribute, values.get(i).toString());
                }
            }
            copyOtherFields(record, recordInterface, attributesList, entity);
        }
        return res;
    }

    protected List<AbstractAttributeInterface> getAttributesList(EntityInterface entity) {
        List<AbstractAttributeInterface> res = new ArrayList<AbstractAttributeInterface>();
        res.addAll(entity.getAttributeCollection());
        return res;
    }

    /**
     * A hook for subclasses.
     * 
     * @param record the record that was created using
     *            {@link #createRecord(EntityInterface, Set, RecordId)}.
     * @param recordInterface the DE record
     * @param attributesList the list of attributes/associations in the record.
     * @param entity the entity to which these records belong.
     */
    protected void copyOtherFields(R record, EntityRecordInterface recordInterface,
                                   List<? extends AbstractAttributeInterface> attributesList,
                                   EntityInterface entity) {
        // hook
    }

    protected abstract R createRecord(EntityInterface entity, Set<AttributeInterface> attributes, RecordId id);

    /**
     * Prunes the given list to remove associations and virtual attributes.
     * 
     * @param attributesList
     * @return set of non virtual attributes from given list.
     */
    private Set<AttributeInterface> getNonVirtualAttributes(
                                                            List<? extends AbstractAttributeInterface> attributesList) {
        Set<AttributeInterface> attributesSet = new HashSet<AttributeInterface>();
        for (int i = 0; i < attributesList.size(); i++) {
            AbstractAttributeInterface abstractAttribute = attributesList.get(i);
            if (abstractAttribute instanceof AttributeInterface) {
                AttributeInterface attribute = (AttributeInterface) abstractAttribute;
                if (!DataListUtil.isVirtualAttribute(attribute)) {
                    attributesSet.add(attribute);
                }
            }
        }
        return attributesSet;
    }

    protected final DomainObjectFactory getDomainObjectFactory() {
        return DomainObjectFactory.getInstance();
    }
}
