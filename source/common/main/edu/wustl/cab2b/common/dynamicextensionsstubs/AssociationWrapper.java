package edu.wustl.cab2b.common.dynamicextensionsstubs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;

/**
 * Wrapper around DE Association
 * @author srinath_k
 * @author Chandrakant Talele
 */
public class AssociationWrapper implements AssociationInterface {

    private static final long serialVersionUID = -8189250865381555390L;

    private transient AssociationInterface association;

    /**
     * Constructor
     * @param association DE association
     */
    public AssociationWrapper(AssociationInterface association) {
        this.association = association;
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#getAssociationDirection()
     * @return AssociationDirection AssociationDirection
     */
    public AssociationDirection getAssociationDirection() {
        return association.getAssociationDirection();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#getConstraintProperties()
     * @return ConstraintPropertiesInterface
     */
    public ConstraintPropertiesInterface getConstraintProperties() {
        return association.getConstraintProperties();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#getIsSystemGenerated()
     * @return whether it is system generated or not
     */
    public Boolean getIsSystemGenerated() {
        return association.getIsSystemGenerated();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#getSourceRole()
     * @return source role
     */
    public RoleInterface getSourceRole() {
        return association.getSourceRole();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#getTargetEntity()
     * @return target
     */
    public EntityInterface getTargetEntity() {
        return association.getTargetEntity();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#getTargetRole()
     * @return target role
     */
    public RoleInterface getTargetRole() {
        return association.getTargetRole();
    }

    /**
     * @param arg0 AssociationDirection
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#setAssociationDirection(edu.common.dynamicextensions.util.global.Constants.AssociationDirection)
     */
    public void setAssociationDirection(AssociationDirection arg0) {
    }

    /**
     * @param arg0 ConstraintPropertiesInterface
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#setConstraintProperties(edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface)
     */
    public void setConstraintProperties(ConstraintPropertiesInterface arg0) {
    }

    /**
     * @param arg0 Boolean
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#setIsSystemGenerated(java.lang.Boolean)
     */
    public void setIsSystemGenerated(Boolean arg0) {
    }

    /**
     * @param arg0 source role
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#setSourceRole(edu.common.dynamicextensions.domaininterface.RoleInterface)
     */
    public void setSourceRole(RoleInterface arg0) {
    }

    /**
     * @param arg0 target
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#setTargetEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public void setTargetEntity(EntityInterface arg0) {
    }

    /**
     * @param arg0 target role
     * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#setTargetRole(edu.common.dynamicextensions.domaininterface.RoleInterface)
     */
    public void setTargetRole(RoleInterface arg0) {
    }

    /**
     * @param arg0 rule
     * @see edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface#addRule(edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface)
     */
    public void addRule(RuleInterface arg0) {
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface#getEntity()
     * @return Entity
     */
    public EntityInterface getEntity() {
        return association.getEntity();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface#getRuleCollection()
     * @return All the rules
     */
    public Collection<RuleInterface> getRuleCollection() {
        return association.getRuleCollection();
    }

    /**
     * @param arg0 rule to remove
     * @see edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface#removeRule(edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface)
     */
    public void removeRule(RuleInterface arg0) {
    }

    /**
     * @param arg0 Source entity
     * @see edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface#setEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public void setEntity(EntityInterface arg0) {
    }

    /**
     * @param arg0 rule collection
     * @see edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface#setRuleCollection(java.util.Collection)
     */
    public void setRuleCollection(Collection<RuleInterface> arg0) {
    }

    /**
     * @param arg0 tagged value
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#addTaggedValue(edu.common.dynamicextensions.domaininterface.TaggedValueInterface)
     */
    public void addTaggedValue(TaggedValueInterface arg0) {
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getCreatedDate()
     * @return created date
     */
    public Date getCreatedDate() {
        return association.getCreatedDate();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getDescription()
     * @return description
     */
    public String getDescription() {
        return association.getDescription();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getId()
     * @return identifier
     */
    public Long getId() {
        return association.getId();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getLastUpdated()
     * @return last update date
     */
    public Date getLastUpdated() {
        return association.getLastUpdated();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getName()
     * @return name
     */
    public String getName() {
        return association.getName();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getPublicId()
     * @return public id
     */
    public String getPublicId() {
        return association.getPublicId();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getTaggedValueCollection()
     * @return all tagged values
     */
    public Collection<TaggedValueInterface> getTaggedValueCollection() {
        return association.getTaggedValueCollection();
    }

    /**
     * @param arg0 created date
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#setCreatedDate(java.util.Date)
     */
    public void setCreatedDate(Date arg0) {
    }

    /**
     * @param arg0 description
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#setDescription(java.lang.String)
     */
    public void setDescription(String arg0) {
    }

    /**
     * @param arg0 identifier
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#setId(java.lang.Long)
     */
    public void setId(Long arg0) {
    }

    /**
     * @param arg0 last update date
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#setLastUpdated(java.util.Date)
     */
    public void setLastUpdated(Date arg0) {
    }

    /**
     * @param arg0 name
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#setName(java.lang.String)
     */
    public void setName(String arg0) {
    }

    /**
     * @param arg0 public id
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#setPublicId(java.lang.String)
     */
    public void setPublicId(String arg0) {
    }

    /**
     * @param tagged values
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#setTaggedValueCollection(java.util.Collection)
     */
    public void setTaggedValueCollection(Collection<TaggedValueInterface> arg0) {
    }

    /**
     * @param arg0 semantic properties
     * @see edu.common.dynamicextensions.domain.SemanticAnnotatableInterface#addSemanticProperty(edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface)
     */
    public void addSemanticProperty(SemanticPropertyInterface arg0) {
    }

    /**
     * @see edu.common.dynamicextensions.domain.SemanticAnnotatableInterface#getOrderedSemanticPropertyCollection()
     * @return list of SemanticProperties 
     */
    public List<SemanticPropertyInterface> getOrderedSemanticPropertyCollection() {
        return association.getOrderedSemanticPropertyCollection();
    }

    /**
     * @see edu.common.dynamicextensions.domain.SemanticAnnotatableInterface#getSemanticPropertyCollection()
     * @return collection of SemanticProperties 
     */
    public Collection<SemanticPropertyInterface> getSemanticPropertyCollection() {
        return association.getSemanticPropertyCollection();
    }

    /**
     * @see edu.common.dynamicextensions.domain.SemanticAnnotatableInterface#removeAllSemanticProperties()
     */
    public void removeAllSemanticProperties() { 
    }

    /**
     * @param arg0 property to remove
     * @see edu.common.dynamicextensions.domain.SemanticAnnotatableInterface#removeSemanticProperty(edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface)
     */
    public void removeSemanticProperty(SemanticPropertyInterface arg0) {
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeLong(association.getEntity().getId());
        s.writeLong(association.getId());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        AbstractEntityCache cache = AbstractEntityCache.getCache();
        EntityInterface entity = cache.getEntityById(s.readLong());
        association = entity.getAssociationByIdentifier(s.readLong());
    }
}