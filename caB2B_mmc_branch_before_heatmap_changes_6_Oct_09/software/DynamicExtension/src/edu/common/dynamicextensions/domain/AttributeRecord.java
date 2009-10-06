package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This Class represents the a single record for multi select attribute.
 * 
 * @author Rahul Ner 
 * @hibernate.class  table="DYEXTN_ATTRIBUTE_RECORD"
 */
public class AttributeRecord extends DynamicExtensionBaseDomainObject {

    /**
     * Empty Constructor.
     */
    public AttributeRecord() {
    }

    /**
     * Serial Version Unique Identifier
     */
    protected static final long serialVersionUID = 1234567890L;

    /**
     * Entity to which this collectionRecord belongs
     */
    protected EntityInterface entity;

    /**
     * Attribute to which this collectionRecord belongs
     */
    protected AttributeInterface attribute;

    /**
     * a record Id to which this collectionRecord belongs
     */
    protected Long recordId;

    /**
     * value of this collectionRecord.
     */
    protected Collection<CollectionAttributeRecordValue> valueCollection = new HashSet<CollectionAttributeRecordValue>();

    /**
     * 
     */
    protected Collection<FileAttributeRecordValue> fileRecordCollection = new HashSet<FileAttributeRecordValue>();

    /**
     * 
     */
    protected Collection<ObjectAttributeRecordValue> objectRecordCollection = new HashSet<ObjectAttributeRecordValue>();

    /**
     * This method returns the unique identifier of the AbstractMetadata.
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DE_ATTR_REC_SEQ"
     * @return the identifier of the AbstractMetadata.
     */
    public Long getId() {
        return id;
    }

    /**
     * This method returns the Entity associated with this collectionRecord.
     * @hibernate.many-to-one column="ENTITY_ID" class="edu.common.dynamicextensions.domain.Entity" constrained="true"
     * @return EntityInterface the Entity associated with the collectionRecord.
     */
    public EntityInterface getEntity() {
        return entity;
    }

    /**
     * This method sets the Entity associated with this collectionRecord.
     * @param entityInterface The entity to be set.
     */
    public void setEntity(EntityInterface entityInterface) {
        if (entityInterface != null) {
            this.entity = (Entity) entityInterface;
        }
    }

    /**
     * This method returns the Attribute associated with this collectionRecord.
     * @hibernate.many-to-one column="ATTRIBUTE_ID" class="edu.common.dynamicextensions.domain.Attribute" constrained="true"
     * @return AttributeInterface the Attribut associated with the collectionRecord.
     */
    public AttributeInterface getAttribute() {
        return attribute;
    }

    /**
     * @param attribute The attribute to set.
     */
    public void setAttribute(AttributeInterface attribute) {
        this.attribute = attribute;
    }

    /**
     * This method returns the record associated with this collectionRecord.
     * @hibernate.property column="RECORD_ID" class="long" constrained="true"
     * @return AttributeInterface the Attribut associated with the collectionRecord.
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * @param recordId The recordId to set.
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * This method returns the Collection of AbstractAttribute.
     * @hibernate.set name="valueCollection" table="DE_COLL_ATTR_RECORD_VALUES"
     * cascade="all-delete-orphan" inverse="false" lazy="false"
     * @hibernate.collection-key column="COLLECTION_ATTR_RECORD_ID"
     * @hibernate.cache  usage="read-write"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.CollectionAttributeRecordValue" 
     * @return the Collection of AbstractAttribute.
     */
    public Collection<CollectionAttributeRecordValue> getValueCollection() {
        return valueCollection;
    }

    /**
     * @param valueCollection The valueCollection to set.
     */
    public void setValueCollection(Collection<CollectionAttributeRecordValue> valueCollection) {
        this.valueCollection = valueCollection;
    }

    /**
     * This method return Returns the fileRecord.
     * @hibernate.set name="fileRecordCollection" table="DE_FILE_ATTR_RECORD_VALUES"
     * cascade="all-delete-orphan" inverse="false" lazy="false"
     * @hibernate.collection-key column="RECORD_ID"
     * @hibernate.cache  usage="read-write"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.FileAttributeRecordValue" 
     * @return Returns the fileRecord.
     */
    private Collection<FileAttributeRecordValue> getFileRecordCollection() {
        return fileRecordCollection;
    }

    /**
     * @param fileRecord The fileRecord to set.
     */
    private void setFileRecordCollection(Collection<FileAttributeRecordValue> fileRecordCollection) {
        this.fileRecordCollection = fileRecordCollection;
    }

    /**
     * @return Returns the fileRecord.
     */
    public FileAttributeRecordValue getFileRecord() {
        if (fileRecordCollection != null && !fileRecordCollection.isEmpty()) {
            return fileRecordCollection.iterator().next();
        }
        return null;
    }

    /**
     * @param fileRecord The fileRecord to set.
     */
    public void setFileRecord(FileAttributeRecordValue fileRecord) {
        if (fileRecordCollection == null) {
            fileRecordCollection = new HashSet<FileAttributeRecordValue>();
        }
        this.fileRecordCollection.clear();
        this.fileRecordCollection.add(fileRecord);
    }

    /**
     * This method return Returns the objectRecord.
     * @hibernate.set name="objectRecordCollection" table="DE_OBJECT_ATTR_RECORD_VALUES"
     * cascade="all-delete-orphan" inverse="false" lazy="false"
     * @hibernate.collection-key column="RECORD_ID"
     * @hibernate.cache  usage="read-write"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.ObjectAttributeRecordValue" 
     * @return Returns the objectRecordCollection.
     */
    private Collection<ObjectAttributeRecordValue> getObjectRecordCollection() {
        return objectRecordCollection;
    }

    /**
     * @param objectRecordCollection The objectRecordCollection to set.
     */
    private void setObjectRecordCollection(Collection<ObjectAttributeRecordValue> objectRecordCollection) {
        this.objectRecordCollection = objectRecordCollection;
    }

    public void setObjectRecord(ObjectAttributeRecordValue objectRecord) {
        if (objectRecordCollection == null) {
            objectRecordCollection = new HashSet<ObjectAttributeRecordValue>();
        }
        this.objectRecordCollection.clear();
        this.objectRecordCollection.add(objectRecord);

    }

    /**
     * @return Returns the fileRecord.
     */
    public ObjectAttributeRecordValue getObjectRecord() {
        if (objectRecordCollection != null && !objectRecordCollection.isEmpty()) {
            return objectRecordCollection.iterator().next();
        }
        return null;
    }

}
