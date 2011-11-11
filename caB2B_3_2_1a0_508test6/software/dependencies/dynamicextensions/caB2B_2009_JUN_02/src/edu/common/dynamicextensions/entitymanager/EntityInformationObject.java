package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;
import java.util.HashSet;

public class EntityInformationObject implements NameInformationInterface {
	
	/**
	 * 
	 */
	String name;
	
	/**
	 * 
	 */
	Long identifier;
	/**
	 * 
	 */
	Collection<AttributeInformationObject> attributeInformationObjectCollection;
	
	/**
	 * 
	 */
	Collection<AssociationInformationObject> associationInformationObjectCollection;

	/**
	 * 
	 * @param name
	 * @param identifier
	 */
	public EntityInformationObject(String name, Long identifier) {
		super();
		this.name = name;
		this.identifier = identifier;
	}

	/**
	 * 
	 * @return
	 */
	public Long getIdentifier() {
		return identifier;
	}

	/**
	 * 
	 * @param identifier
	 */
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<AssociationInformationObject> getAssociationInformationObjectCollection() {
		return associationInformationObjectCollection;
	}
	
	/**
	 * 
	 *
	 */
	public void addAssociationInformationObject(AssociationInformationObject associationInformationObject)
	{
		if(this.associationInformationObjectCollection == null)
		{
			associationInformationObjectCollection = new HashSet<AssociationInformationObject>();
		}
		associationInformationObjectCollection.add(associationInformationObject);
	}
	/**
	 * 
	 * @return
	 */
	public Collection<AttributeInformationObject> getAttributeInformationObjectCollection() {
		return attributeInformationObjectCollection;
	}
	
	/**
	 * 
	 *
	 */
	public void addAttributeInformationObject(AttributeInformationObject attributeInformationObject)
	{
		if(this.attributeInformationObjectCollection == null)
		{
			attributeInformationObjectCollection = new HashSet<AttributeInformationObject>();
		}
		attributeInformationObjectCollection.add(attributeInformationObject);
		
	}

	/**
	 * 
	 * @param associationInformationObjectCollection
	 */
	public void setAssociationInformationObjectCollection(
			Collection<AssociationInformationObject> associationInformationObjectCollection) {
		this.associationInformationObjectCollection = associationInformationObjectCollection;
	}

	/**
	 * 
	 * @param attributeInformationObjectCollection
	 */
	public void setAttributeInformationObjectCollection(
			Collection<AttributeInformationObject> attributeInformationObjectCollection) {
		this.attributeInformationObjectCollection = attributeInformationObjectCollection;
	}
}
