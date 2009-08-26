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
     * @return AssociationDirection AssociationDirection
     */
    public AssociationDirection getAssociationDirection() {
        return association.getAssociationDirection();
    }

    /**
     * @return ConstraintPropertiesInterface
     */
    public ConstraintPropertiesInterface getConstraintProperties() {
        return association.getConstraintProperties();
    }

    /**
     * @return whether it is system generated or not
     */
    public Boolean getIsSystemGenerated() {
        return association.getIsSystemGenerated();
    }

    /**
     * @return source role
     */
    public RoleInterface getSourceRole() {
        return association.getSourceRole();
    }

    /**
     * @return target
     */
    public EntityInterface getTargetEntity() {
        return association.getTargetEntity();
    }

    /**
     * @return target role
     */
    public RoleInterface getTargetRole() {
        return association.getTargetRole();
    }

    /**
     * @param arg0 AssociationDirection
     */
    public void setAssociationDirection(AssociationDirection arg0) {
    }

    /**
     * @param arg0 ConstraintPropertiesInterface
     */
    public void setConstraintProperties(ConstraintPropertiesInterface arg0) {
    }

    /**
     * @param arg0 Boolean
     */
    public void setIsSystemGenerated(Boolean arg0) {
    }

    /**
     * @param arg0 source role
     */
    public void setSourceRole(RoleInterface arg0) {
    }

    /**
     * @param arg0 target
     */
    public void setTargetEntity(EntityInterface arg0) {
    }

    /**
     * @param arg0 target role
     */
    public void setTargetRole(RoleInterface arg0) {
    }

    /**
     * @param arg0 rule
     */
    public void addRule(RuleInterface arg0) {
    }

    /**
     * @return Entity
     */
    public EntityInterface getEntity() {
        return association.getEntity();
    }

    /**
     * @return All the rules
     */
    public Collection<RuleInterface> getRuleCollection() {
        return association.getRuleCollection();
    }

    /**
     * @param arg0 rule to remove
     */
    public void removeRule(RuleInterface arg0) {
    }

    /**
     * @param arg0 Source entity
     */
    public void setEntity(EntityInterface arg0) {
    }

    /**
     * @param arg0 rule collection
     */
    public void setRuleCollection(Collection<RuleInterface> arg0) {
    }

    /**
     * @param arg0 tagged value
     */
    public void addTaggedValue(TaggedValueInterface arg0) {
    }

    /**
     * @return created date
     */
    public Date getCreatedDate() {
        return association.getCreatedDate();
    }

    /**
     * @return description
     */
    public String getDescription() {
        return association.getDescription();
    }

    /**
     * @return identifier
     */
    public Long getId() {
        return association.getId();
    }

    /**
     * @return last update date
     */
    public Date getLastUpdated() {
        return association.getLastUpdated();
    }

    /**
     * @return name
     */
    public String getName() {
        return association.getName();
    }

    /**
     * @return public id
     */
    public String getPublicId() {
        return association.getPublicId();
    }

    /**
     * @return all tagged values
     */
    public Collection<TaggedValueInterface> getTaggedValueCollection() {
        return association.getTaggedValueCollection();
    }

    /**
     * @param arg0 created date
     */
    public void setCreatedDate(Date arg0) {
    }

    /**
     * @param arg0 description
     */
    public void setDescription(String arg0) {
    }

    /**
     * @param arg0 identifier
     */
    public void setId(Long arg0) {
    }

    /**
     * @param arg0 last update date
     */
    public void setLastUpdated(Date arg0) {
    }

    /**
     * @param arg0 name
     */
    public void setName(String arg0) {
    }

    /**
     * @param arg0 public id
     */
    public void setPublicId(String arg0) {
    }

    /**
     * @param tagged values
     */
    public void setTaggedValueCollection(Collection<TaggedValueInterface> arg0) {
    }

    /**
     * @param arg0 semantic properties
     */
    public void addSemanticProperty(SemanticPropertyInterface arg0) {
    }

    /**
     * @return list of SemanticProperties
     */
    public List<SemanticPropertyInterface> getOrderedSemanticPropertyCollection() {
        return association.getOrderedSemanticPropertyCollection();
    }

    /**
     * @return collection of SemanticProperties 
     */
    public Collection<SemanticPropertyInterface> getSemanticPropertyCollection() {
        return association.getSemanticPropertyCollection();
    }

    /**
     * Removes All Semantic Properties
     */
    public void removeAllSemanticProperties() { 
    }

    /**
     * @param arg0 property to remove
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