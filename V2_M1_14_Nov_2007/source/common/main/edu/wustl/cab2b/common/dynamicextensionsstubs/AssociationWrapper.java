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

public class AssociationWrapper implements AssociationInterface {

    private static final long serialVersionUID = -8189250865381555390L;

    private transient AssociationInterface association;

    public AssociationWrapper(AssociationInterface association) {
        this.association = association;
    }

    public AssociationDirection getAssociationDirection() {
        // TODO Auto-generated method stub
        return association.getAssociationDirection();
    }

    public ConstraintPropertiesInterface getConstraintProperties() {
        // TODO Auto-generated method stub
        return association.getConstraintProperties();
    }

    public Boolean getIsSystemGenerated() {
        // TODO Auto-generated method stub
        return association.getIsSystemGenerated();
    }

    public RoleInterface getSourceRole() {
        // TODO Auto-generated method stub
        return association.getSourceRole();
    }

    public EntityInterface getTargetEntity() {
        // TODO Auto-generated method stub
        return association.getTargetEntity();
    }

    public RoleInterface getTargetRole() {
        // TODO Auto-generated method stub
        return association.getTargetRole();
    }

    public void setAssociationDirection(AssociationDirection arg0) {
        // TODO Auto-generated method stub
    }

    public void setConstraintProperties(ConstraintPropertiesInterface arg0) {
        // TODO Auto-generated method stub
    }

    public void setIsSystemGenerated(Boolean arg0) {
        // TODO Auto-generated method stub
    }

    public void setSourceRole(RoleInterface arg0) {
        // TODO Auto-generated method stub
    }

    public void setTargetEntity(EntityInterface arg0) {
        // TODO Auto-generated method stub
    }

    public void setTargetRole(RoleInterface arg0) {
        // TODO Auto-generated method stub
    }

    public void addRule(RuleInterface arg0) {
        // TODO Auto-generated method stub
    }

    public EntityInterface getEntity() {
        // TODO Auto-generated method stub
        return association.getEntity();
    }

    public Collection<RuleInterface> getRuleCollection() {
        // TODO Auto-generated method stub
        return association.getRuleCollection();
    }

    public void removeRule(RuleInterface arg0) {
        // TODO Auto-generated method stub
    }

    public void setEntity(EntityInterface arg0) {
        // TODO Auto-generated method stub
    }

    public void setRuleCollection(Collection<RuleInterface> arg0) {
        // TODO Auto-generated method stub
    }

    public void addTaggedValue(TaggedValueInterface arg0) {
        // TODO Auto-generated method stub
    }

    public Date getCreatedDate() {
        // TODO Auto-generated method stub
        return association.getCreatedDate();
    }

    public String getDescription() {
        // TODO Auto-generated method stub
        return association.getDescription();
    }

    public Long getId() {
        // TODO Auto-generated method stub
        return association.getId();
    }

    public Date getLastUpdated() {
        // TODO Auto-generated method stub
        return association.getLastUpdated();
    }

    public String getName() {
        // TODO Auto-generated method stub
        return association.getName();
    }

    public String getPublicId() {
        // TODO Auto-generated method stub
        return association.getPublicId();
    }

    public Collection<TaggedValueInterface> getTaggedValueCollection() {
        // TODO Auto-generated method stub
        return association.getTaggedValueCollection();
    }

    public void setCreatedDate(Date arg0) {
        // TODO Auto-generated method stub

    }

    public void setDescription(String arg0) {
        // TODO Auto-generated method stub

    }

    public void setId(Long arg0) {
        // TODO Auto-generated method stub

    }

    public void setLastUpdated(Date arg0) {
        // TODO Auto-generated method stub

    }

    public void setName(String arg0) {
        // TODO Auto-generated method stub

    }

    public void setPublicId(String arg0) {
        // TODO Auto-generated method stub

    }

    public void setTaggedValueCollection(Collection<TaggedValueInterface> arg0) {
        // TODO Auto-generated method stub

    }

    public void addSemanticProperty(SemanticPropertyInterface arg0) {
        // TODO Auto-generated method stub

    }

    public List<SemanticPropertyInterface> getOrderedSemanticPropertyCollection() {
        // TODO Auto-generated method stub
        return association.getOrderedSemanticPropertyCollection();
    }

    public Collection<SemanticPropertyInterface> getSemanticPropertyCollection() {
        // TODO Auto-generated method stub
        return association.getSemanticPropertyCollection();
    }

    public void removeAllSemanticProperties() {
        // TODO Auto-generated method stub

    }

    public void removeSemanticProperty(SemanticPropertyInterface arg0) {
        // TODO Auto-generated method stub

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
