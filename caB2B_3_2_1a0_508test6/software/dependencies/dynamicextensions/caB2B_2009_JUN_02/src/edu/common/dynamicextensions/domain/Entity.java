
package edu.common.dynamicextensions.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Constants.InheritanceStrategy;

/**
 * An entity is something that has a distinct, separate existence, though it need not be a material 
 * existence. In particular, abstractions and legal fictions are usually regarded as entities.
 * An entity can either have many attributes or many associations.
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_ENTITY"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @hibernate.cache  usage="read-write"
 */
public class Entity extends AbstractMetadata implements EntityInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -552600540977483821L;

	/**
	 * Collection of attributes in this entity.
	 */
	protected Collection<AbstractAttributeInterface> abstractAttributeCollection = new HashSet<AbstractAttributeInterface>();

	/**
	 * Table property for this entity.
	 */
	protected Collection<TablePropertiesInterface> tablePropertiesCollection = new HashSet<TablePropertiesInterface>();

	/**
	 * Collection of EntityGroup.
	 */
	protected Collection<EntityGroupInterface> entityGroupCollection = new HashSet<EntityGroupInterface>();

	/**
	 * state for data table creation
	 * 1 - data table created by DE
	 * 2 - data table is not created by DE and it does not exists
	 * 3 - data table is not created by DE but it is already present. calling application take the
	 *     responsibility of setting Table name and column name to the entity and attributes.
	 */
	protected int dataTableState = Constants.DATA_TABLE_STATE_CREATED;

	/**
	 * parent of this entity, null is no parent present. 
	 */
	protected EntityInterface parentEntity = null;

	/**
	 * indicates if this enitity is abstract or not. 
	 */
	protected boolean isAbstract = false;

	/**
	 * 
	 */
	protected boolean isProcessed = false;

	/*
	 * 
	 */
	protected int inheritStrategy = InheritanceStrategy.TABLE_PER_SUB_CLASS.getValue();

	/**
	 * 
	 */
	protected String discriminatorColumn;

	/**
	 * 
	 */
	protected String discriminatorValue;

	/**
	 * @hibernate.property name="dataTableState" type="int" column="DATA_TABLE_STATE"
	 * @return Returns the dataTableState.
	 */
	public int getDataTableState()
	{
		return dataTableState;
	}

	/**
	 * @param dataTableState The dataTableState to set.
	 */
	public void setDataTableState(int dataTableState)
	{
		this.dataTableState = dataTableState;
	}

	/**
	 * Empty Constructor.
	 */
	public Entity()
	{
	}

	/**
	 * This method returns the Collection of the EntityGroups.
	 * @hibernate.set name="entityGroupCollection" table="DYEXTN_ENTITY_GROUP_REL" 
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ENTITY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-many-to-many class="edu.common.dynamicextensions.domain.EntityGroup" column="ENTITY_GROUP_ID"
	 * @return the Collection of the Entities.
	 */
	public Collection<EntityGroupInterface> getEntityGroupCollection()
	{
		return entityGroupCollection;
	}

	/**
	 * This method sets the entityGroupCollection to the given Collection of the Entities.
	 * @param entityGroupCollection The entityGroupCollection to set.
	 */
	public void setEntityGroupCollection(Collection<EntityGroupInterface> entityGroupCollection)
	{
		this.entityGroupCollection = entityGroupCollection;
	}

	/**
	 * This method returns the Collection of TableProperties of this Entity.
	 * @hibernate.set name="tablePropertiesColletion" table="DYEXTN_TABLE_PROPERTIES" cascade="all-delete-orphan"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="ENTITY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.TableProperties"
	 * @return the Collection of TableProperties of this Entity.
	 */
	private Collection<TablePropertiesInterface> getTablePropertiesCollection()
	{
		return tablePropertiesCollection;
	}

	/**
	 * This method sets the tablePropertiesColletion to the given Collection of TableProperties. 
	 * @param tablePropertiesColletion Collection of TableProperties to be set.
	 */
	private void setTablePropertiesCollection(
			Collection<TablePropertiesInterface> tablePropertiesColletion)
	{
		this.tablePropertiesCollection = tablePropertiesColletion;
	}

	/**
	 * This method returns the TableProperties of the Entity.
	 * @return the TableProperties of the Entity.
	 */
	public TablePropertiesInterface getTableProperties()
	{
		TablePropertiesInterface tableProperties = null;
		if (tablePropertiesCollection != null && !tablePropertiesCollection.isEmpty())
		{
			Iterator tabletPropertiesIterator = tablePropertiesCollection.iterator();
			tableProperties = (TablePropertiesInterface) tabletPropertiesIterator.next();
		}
		return tableProperties;
	}

	/**
	 * This method sets the TableProperties of the Entity to the given TableProperties.
	 * @param tableProperties the TableProperties to be set.
	 */
	public void setTableProperties(TablePropertiesInterface tableProperties)
	{
		if (tablePropertiesCollection == null)
		{
			tablePropertiesCollection = new HashSet<TablePropertiesInterface>();
		}
		else
		{
			this.tablePropertiesCollection.clear();
		}
		this.tablePropertiesCollection.add(tableProperties);
	}

	/**
	 * This method adds an AbstractAttribute to the Entity's Collection of AbstractAttribute.
	 * @param abstractAttribute AbstractAttribute to be added.
	 */
	public void addAbstractAttribute(AbstractAttributeInterface abstractAttribute)
	{
		if (abstractAttribute == null)
		{
			return;
		}

		if (abstractAttributeCollection == null)
		{
			abstractAttributeCollection = new HashSet<AbstractAttributeInterface>();
		}
		abstractAttributeCollection.add(abstractAttribute);
		abstractAttribute.setEntity(this);

	}

	/**
	 * 
	 */
	public void addEntityGroupInterface(EntityGroupInterface entityGroupInterface)
	{
		if (this.entityGroupCollection == null)
		{
			entityGroupCollection = new HashSet<EntityGroupInterface>();
		}
		entityGroupCollection.add(entityGroupInterface);

	}

	/**
	 * 
	 */
	public void removeEntityGroupInterface(EntityGroupInterface entityGroupInterface)
	{
		if (entityGroupCollection != null)
		{
			if (entityGroupCollection.contains(entityGroupInterface))
			{
				entityGroupCollection.remove(entityGroupInterface);
				entityGroupInterface.removeEntity(this);

			}
		}

	}

	/**
	 * This method removes all entity groupa of the entity.
	 *
	 */
	public void removeAllEntityGroups()
	{
		if (entityGroupCollection != null)
		{
			Iterator<EntityGroupInterface> entityGroupIterator = entityGroupCollection.iterator();
			EntityGroupInterface entityGroupInterface;
			while (entityGroupIterator.hasNext())
			{
				entityGroupInterface = (EntityGroupInterface) entityGroupIterator.next();
				entityGroupInterface.removeEntity(this);
				entityGroupIterator.remove();

			}

		}
	}

	/**
	 * This method returns the Collection of AbstractAttribute.
	 * @hibernate.set name="abstractAttributeCollection" table="DYEXTN_ATTRIBUTE"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ENTIY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.AbstractAttribute" 
	 * @return the Collection of AbstractAttribute.
	 */
	public Collection<AbstractAttributeInterface> getAbstractAttributeCollection()
	{
		return abstractAttributeCollection;
	}

	/**
	 * This method sets the abstractAttributeCollection to the given Collection of AbstractAttribute.
	 * @param abstractAttributeCollection The abstractAttributeCollection to set.
	 */
	public void setAbstractAttributeCollection(
			Collection<AbstractAttributeInterface> abstractAttributeCollection)
	{
		this.abstractAttributeCollection = abstractAttributeCollection;
	}

	/**
	 * This method return the Collection of Attributes.
	 * @return the Collection of Attributes.
	 */
	public Collection<AttributeInterface> getAttributeCollection()
	{
		Collection<AttributeInterface> attributeCollection = new HashSet<AttributeInterface>();
		if (abstractAttributeCollection != null && !abstractAttributeCollection.isEmpty())
		{
			Iterator attributeIterator = abstractAttributeCollection.iterator();
			while (attributeIterator.hasNext())
			{
				Object object = attributeIterator.next();
				if (object instanceof AttributeInterface)
				{
					attributeCollection.add((AttributeInterface) object);
				}
			}
		}
		return attributeCollection;
	}

	/**
	 * This method return the Collection of Association.
	 * @return the Collection of Association.
	 */
	public Collection<AssociationInterface> getAssociationCollection()
	{
		Collection<AssociationInterface> associationCollection = new HashSet<AssociationInterface>();
		if (abstractAttributeCollection != null && !abstractAttributeCollection.isEmpty())
		{
			Iterator associationIterator = abstractAttributeCollection.iterator();
			while (associationIterator.hasNext())
			{
				Object object = associationIterator.next();
				if (object instanceof AssociationInterface)
				{
					associationCollection.add((AssociationInterface) object);
				}
			}
		}
		return associationCollection;
	}

	/**
	 * This method removes an AbstractAttribute from the Entity's Collection of AbstractAttribute.
	 * @param abstractAttribute an AbstractAttribute to be removed.
	 */
	public void removeAbstractAttribute(AbstractAttributeInterface abstractAttribute)
	{
		if (abstractAttributeCollection != null)
		{
			if (abstractAttributeCollection.contains(abstractAttribute))
			{
				abstractAttributeCollection.remove(abstractAttribute);
				abstractAttribute.setEntity(null);
			}
		}
	}

	/**
	 * This method removes all abstract attributes from the entity.
	 */
	public void removeAllAbstractAttributes()
	{
		if (abstractAttributeCollection != null)
		{
			abstractAttributeCollection.clear();
		}
	}

	/**
	 * This method returns the attribute for the given corresponding identifier.
	 * @param id identifier of the desired AbstractAttribute.
	 * @return the matched instance of AbstractAttribute.
	 */
	public AttributeInterface getAttributeByIdentifier(Long id)
	{
		AttributeInterface attribute = null;
		Collection<AttributeInterface> attributeCollection = getAttributeCollection();

		for (AttributeInterface attributeIterator : attributeCollection)
		{
			if (attributeIterator.getId() != null && attributeIterator.getId().equals(id))
			{
				attribute = attributeIterator;
				break;
			}
		}
		return attribute;
	}

	/**
	 * This method returns the attribute for the given corresponding identifier.
	 * @param id identifier of the desired AbstractAttribute.
	 * @return the matched instance of AbstractAttribute.
	 */
	public AssociationInterface getAssociationByIdentifier(Long id)
	{
		AssociationInterface association = null;
		Collection<AssociationInterface> associationCollection = getAssociationCollection();

		for (AssociationInterface associationIterator : associationCollection)
		{
			if (associationIterator.getId() != null && associationIterator.getId().equals(id))
			{
				association = associationIterator;
				break;
			}
		}
		return association;
	}

	/**
	 * This method adds attribute interface to the abstract attribute collection.
	 * @param attributeInterface
	 */
	public void addAttribute(AttributeInterface attributeInterface)
	{
		addAbstractAttribute(attributeInterface);

	}

	/** 
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#removeAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void removeAttribute(AttributeInterface attributeInterface)
	{
		removeAbstractAttribute(attributeInterface);

	}

	/**
	 * This method adds association interface to the abstract attribute collection.
	 * @param associationInterface
	 */
	public void addAssociation(AssociationInterface associationInterface)
	{
		addAbstractAttribute(associationInterface);
	}

	/** 
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#removeAssociation(edu.common.dynamicextensions.domaininterface.AssociationInterface)
	 */
	public void removeAssociation(AssociationInterface associationInterface)
	{
		removeAbstractAttribute(associationInterface);

	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#isAbstract()
	 * 
	 * @hibernate.property name="isAbstract" type="boolean" column="IS_ABSTRACT"
	 */
	public boolean isAbstract()
	{
		return isAbstract;
	}

	/**
	 * This private method is for hibernate to set parent
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#setAbstract(boolean)
	 */
	public void setAbstract(boolean isAbstract)
	{
		this.isAbstract = isAbstract;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#getParentEntity()
	 * @hibernate.many-to-one column="PARENT_ENTITY_ID" class="edu.common.dynamicextensions.domain.Entity" constrained="true" 
	 *                        cascade="none"    
	 */
	public EntityInterface getParentEntity()
	{
		return parentEntity;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#setParentEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
	 */
	public void setParentEntity(EntityInterface parentEntity)
	{
		this.parentEntity = parentEntity;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#getAllAssociations()
	 */
	public Collection<AssociationInterface> getAllAssociations()
	{
		Collection<AssociationInterface> associationCollection = new ArrayList<AssociationInterface>();
		associationCollection.addAll(getAssociationCollection());
		EntityInterface parentEntity = this.parentEntity;
		while (parentEntity != null)
		{
			associationCollection.addAll(parentEntity.getAssociationCollection());
			parentEntity = parentEntity.getParentEntity();
		}

		return associationCollection;
	}

	/** 
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#getAllAttributes()
	 */
	public Collection<AttributeInterface> getAllAttributes()
	{

		Collection<AttributeInterface> AttributeCollection = new ArrayList<AttributeInterface>();
		AttributeCollection.addAll(getAttributeCollection());
		EntityInterface parentEntity = this.parentEntity;
		while (parentEntity != null)
		{
			AttributeCollection.addAll(parentEntity.getAttributeCollection());
			parentEntity = parentEntity.getParentEntity();
		}

		return AttributeCollection;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#getAllAbstractAttributes()
	 */
	public Collection<AbstractAttributeInterface> getAllAbstractAttributes()
	{
		Collection<AbstractAttributeInterface> abstractAttributeCollection = new ArrayList<AbstractAttributeInterface>();
		abstractAttributeCollection.addAll(getAllAssociations());
		abstractAttributeCollection.addAll(getAllAttributes());
		return abstractAttributeCollection;
	}

	public boolean isProcessed()
	{
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed)
	{
		this.isProcessed = isProcessed;
	}

	/**
	 * @hibernate.property name="inheritStrategy" type="int" column="INHERITANCE_STRATEGY"
	 * @return Returns the inheritanceStrategy.
	 */
	private int getInheritStrategy()
	{
		return inheritStrategy;
	}

	/**
	 * @param inheritanceStrategy The inheritanceStrategy to set.
	 */
	private void setInheritStrategy(int inheritStrategy)
	{
		this.inheritStrategy = inheritStrategy;
	}

	/**
	 * @return Returns the inheritanceStrategy.
	 */
	public InheritanceStrategy getInheritanceStrategy()
	{
		return InheritanceStrategy.get(this.inheritStrategy);
	}

	/**
	 * @param inheritanceStrategy The inheritanceStrategy to set.
	 */
	public void setInheritanceStrategy(InheritanceStrategy inheritanceStrategy)
	{
		setInheritStrategy(inheritanceStrategy.getValue());
	}

	/**
	 * @hibernate.property name="discriminatorColumn" type="string" column="DISCRIMINATOR_COLUMN_NAME"
	 * @return Returns the discriminatorColumn.
	 */
	public String getDiscriminatorColumn()
	{
		return discriminatorColumn;
	}

	/**
	 * @param discriminatorColumn The discriminatorColumn to set.
	 */
	public void setDiscriminatorColumn(String discriminatorColumn)
	{
		this.discriminatorColumn = discriminatorColumn;
	}

	/**
	 * @hibernate.property name="discriminatorValue" type="string" column="DISCRIMINATOR_VALUE"
	 * @return Returns the discriminatorValue.
	 */
	public String getDiscriminatorValue()
	{
		return discriminatorValue;
	}

	/**
	 * @param discriminatorValue The discriminatorValue to set.
	 */
	public void setDiscriminatorValue(String discriminatorValue)
	{
		this.discriminatorValue = discriminatorValue;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.EntityInterface#getEntityAttributes()
	 */
	public Collection<AttributeInterface> getEntityAttributes()
	{
		Collection<AttributeInterface> AttributeCollection = new ArrayList<AttributeInterface>();
		AttributeCollection.addAll(getAttributeCollection());
		return AttributeCollection;
	}
    /**
     * This method returns a list of Attributes which includes attributes of an entity as well attributes 
     * of its parent entity. 
     * bug id 5546.
     * @return Collection<AttributeInterface> list of all attributes includeing attributes of it parents'.
     */
    public Collection<AttributeInterface> getEntityAttributesForQuery()
    { 
        Collection<AttributeInterface> theAttributeCollection = new ArrayList<AttributeInterface>();
        Set<String> attributeNames = new HashSet<String>();
        Collection<AttributeInterface> attributeCollection = getAttributeCollection();
		theAttributeCollection.addAll(attributeCollection);
		for(AttributeInterface attribute:attributeCollection)
		{
			attributeNames.add(attribute.getName());
			attribute.setEntity(this);
		}
        EntityInterface parentEntity = this.parentEntity;
        
        while (parentEntity != null)
        {
            Collection<AttributeInterface> parentAttributeCollection = parentEntity.getAttributeCollection();
            for (AttributeInterface parentAttribute: parentAttributeCollection)
            {
            	if (attributeNames.add(parentAttribute.getName()))
            	{
            		theAttributeCollection.add(parentAttribute);
            		boolean isDerivedTagPresent = false;
            		for (TaggedValueInterface tag : parentAttribute.getTaggedValueCollection()) {
			            if (tag.getKey().equals(edu.wustl.cab2b.common.util.Constants.TYPE_DERIVED)) {
			            	isDerivedTagPresent = true;
			            }
			        }
            		if (!isDerivedTagPresent)
            		{
	            		TaggedValueInterface tag = new TaggedValue();
	            		tag.setKey(edu.wustl.cab2b.common.util.Constants.TYPE_DERIVED);
	            		tag.setValue(edu.wustl.cab2b.common.util.Constants.TYPE_DERIVED);
	            		parentAttribute.addTaggedValue(tag);
            		}
            	}
            }
            parentEntity = parentEntity.getParentEntity();
        }
        return theAttributeCollection;
    }
}