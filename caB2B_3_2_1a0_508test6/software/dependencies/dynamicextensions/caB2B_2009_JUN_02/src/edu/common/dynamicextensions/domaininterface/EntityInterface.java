
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.util.global.Constants.InheritanceStrategy;

/**
 * Entity object stores information of the entity.For each entity a dynamic table is generated using the metadata
 * information.
 * @author sujay_narkar
 *
 */
public interface EntityInterface extends AbstractMetadataInterface
{

	/**
	 * This method returns the Collection of AbstractAttribute.
	 * @return the Collection of AbstractAttribute.
	 */
	Collection<AbstractAttributeInterface> getAbstractAttributeCollection();

	/**
	 * This method return the Collection of Attributes.
	 * @return the Collection of Attributes.
	 */
	Collection<AttributeInterface> getAttributeCollection();

	/**
	 * This method return the Collection of Association.
	 * @return the Collection of Association.
	 */
	Collection<AssociationInterface> getAssociationCollection();

	/**
	 * The abstractAttributeInterface to be added 
	 * @param abstractAttributeInterface abstract attribute interface 
	 */
	void addAbstractAttribute(AbstractAttributeInterface abstractAttribute);

	/**
	 * Returns a collection of entity groups having this entity. 
	 * @return Returns the entityGroupCollection.
	 */
	Collection<EntityGroupInterface> getEntityGroupCollection();

	/**
	 * Adds an entity group to the entity 
	 * @param entityGroupInterface The entityGroupInterface to be added set.
	 * 
	 */
	void addEntityGroupInterface(EntityGroupInterface entityGroup);

	/**
	 * The table properties object contains name of the dynamically created table.
	 * @return
	 */
	TablePropertiesInterface getTableProperties();

	/**
	 * @param tableProperties
	 */
	void setTableProperties(TablePropertiesInterface tableProperties);

	/**
	 * This method removes an AbstractAttribute from the Entity's Collection of AbstractAttribute.
	 * @param abstractAttribute an AbstractAttribute to be removed.
	 */
	void removeAbstractAttribute(AbstractAttributeInterface abstractAttribute);

	/**
	 * This method adds attribute interface to the abstract attribute collection.
	 * @param attributeInterface
	 */
	void addAttribute(AttributeInterface attributeInterface);

	/**
	 * This method adds attribute interface to the abstract attribute collection.
	 * @param attributeInterface
	 */
	void removeAttribute(AttributeInterface attributeInterface);

	/**
	 * This method removes association interface from the abstract attribute collection.
	 * @param associationInterface
	 */
	void addAssociation(AssociationInterface associationInterface);

	/**
	 * This method removes association interface from the abstract attribute collection.
	 * @param associationInterface
	 */
	void removeAssociation(AssociationInterface associationInterface);

	/**
	 * This method removes all entity groupa of the entity.
	 *
	 */
	void removeEntityGroupInterface(EntityGroupInterface entityGroupInterface);

	/**
	 * This method removes all entity groupa of the entity.
	 *
	 */
	void removeAllEntityGroups();

	/**
	 * @return Returns the isAbstract.
	 */
	boolean isAbstract();

	/**
	 * @param isAbstract The isAbstract to set.
	 */
	void setAbstract(boolean isAbstract);

	/**
	 * @return Returns the parentEntity.
	 */
	EntityInterface getParentEntity();

	/**
	 * @param parentEntity The parentEntity to set.
	 */
	void setParentEntity(EntityInterface parentEntity);

	/**
	 * This method returns association for all the hierarchy
	 * @return
	 */
	Collection<AssociationInterface> getAllAssociations();

	/**
	 * This method returns attributs for all the hierarchy.
	 * @return  Collection of AttributeInterface
	 */
	Collection<AttributeInterface> getAllAttributes();
	/**
	 * This method returns attributs ONLY for the entity. It does not traverse the heirarchy
	 * @return  Collection of AttributeInterface
	 */
	Collection<AttributeInterface> getEntityAttributes();

	/**
	 * This method returns all the attributes and associations for all the hierarchy.
	 * @return Collection of AbstractAttributeInterface
	 */
	Collection<AbstractAttributeInterface> getAllAbstractAttributes();

	/**
	 * Method returns  attribute based on the id passed.
	 * @param id Long identifier of the abstract attribute
	 * @return 
	 */
	AttributeInterface getAttributeByIdentifier(Long id);

	/**
	 * Method returns  association based on the id passed.
	 * @param id Long identifier of the abstract attribute
	 * @return 
	 */
	AssociationInterface getAssociationByIdentifier(Long id);

	/**
	 * @return Returns the inheritanceStrategy.
	 */
	InheritanceStrategy getInheritanceStrategy();

	/**
	 *@param inheritanceStrategy The inheritanceStrategy to set.
	 */
	void setInheritanceStrategy(InheritanceStrategy inheritanceStrategy);

	/**
	 * 
	 *
	 */
	void removeAllAbstractAttributes();

	/**
	 * @return
	 */
	String getDiscriminatorColumn();

	/**
	 * @param discriminatorColumn
	 */
	void setDiscriminatorColumn(String discriminatorColumn);

	/**
	 * @return
	 */
	String getDiscriminatorValue();

	/**
	 * @param discriminatorValue
	 */
	void setDiscriminatorValue(String discriminatorValue);
    
    /**
     * Get all attributes for query
     */
    public Collection<AttributeInterface> getEntityAttributesForQuery();
}
