/**
 * 
 */

package edu.wustl.common.util;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.wustl.common.querysuite.EntityManagerMock;
import edu.wustl.common.querysuite.queryobject.util.InheritanceUtilsInterface;
import edu.wustl.common.util.logger.Logger;

/**
 * Mock implementation for InheritanceUtils method.
 * @author prafull_kadam
 */
public class InheritanceUtilMock implements InheritanceUtilsInterface
{

	/**
	 * TO check whether given attribute is inherited or not.
	 * @param abstractAttribute reference to AbstractAttributeInterface
	 * @return true if attribute is inherited.
	 */
	public boolean isInherited(AbstractAttributeInterface abstractAttribute)
	{
		if (abstractAttribute instanceof AttributeInterface)
		{
			return isInheritedAttribute((AttributeInterface) abstractAttribute);
		}
		else
		{
			return isInheritedAassociation((AssociationInterface) abstractAttribute);
		}
	}

	/**
	 * Returns actual attribute if passed attribute is a derieved one. Else
	 * returns the passed attribute
	 * @param attribute Attribute for which actual attribute is expected.
	 * @return The actual attribute
	 */
	public AttributeInterface getActualAttribute(AttributeInterface attribute)
	{
		EntityInterface parentEntity = attribute.getEntity().getParentEntity();
		if (parentEntity != null)
		{
			String attributeName = attribute.getName();
			Collection<AttributeInterface> attributes = parentEntity.getAttributeCollection();
			for (AttributeInterface attr : attributes)
			{
				if (attributeName.equals(attr.getName()))
				{
					if (attr.getEntity().getParentEntity() != null)
					{
						AttributeInterface attr1 = getActualAttribute(attr);
						if (attr1 != null)
						{
							return attr1;
						}
					}
					return attr;
				}
			}
		}

		return null;
	}

	/**
	 * Returns actual association if passed association is a derieved one. Else
	 * returns the passed association
	 * @param association Attribute for which actual association is expected.
	 * @return The actual association
	 */
	public AssociationInterface getActualAassociation(AssociationInterface association)
	{
		EntityManagerMock entityManagerMock = new EntityManagerMock();

		String sourceEntityName = association.getEntity().getName();
		String targetEntityName = association.getTargetEntity().getName();
		try
		{
			//Associations between parent & child specimen
			if (EntityManagerMock.specimenClasses.contains(sourceEntityName)
					&& EntityManagerMock.specimenClasses.contains(targetEntityName))
			{
				return entityManagerMock.createAssociation(EntityManagerMock.SPECIMEN_NAME,
						EntityManagerMock.SPECIMEN_NAME, AssociationDirection.BI_DIRECTIONAL,
						"childrenSpecimen", "collectionProtocolEvent", null, "PARENT_SPECIMEN_ID");
			}

			//Associations between SCG & specimen
			if (sourceEntityName.equals(EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME)
					&& EntityManagerMock.specimenClasses.contains(targetEntityName))
			{
				return entityManagerMock.createAssociation(
						EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME,
						EntityManagerMock.SPECIMEN_NAME, AssociationDirection.BI_DIRECTIONAL,
						"specimenCollectionGroup", "specimenCollection", null,
						"SPECIMEN_COLLECTION_GROUP_ID");
			}

			//Associations between specimen & event heirarchy
			if (EntityManagerMock.specimenClasses.contains(sourceEntityName)
					&& EntityManagerMock.eventClasses.contains(targetEntityName))
			{
				return entityManagerMock.createAssociation(EntityManagerMock.SPECIMEN_NAME,
						EntityManagerMock.SPECIMEN_EVT_NAME, AssociationDirection.BI_DIRECTIONAL,
						"specimen", "specimenEventCollection", null, "SPECIMEN_ID");
			}
			if (EntityManagerMock.specimenClasses.contains(sourceEntityName)
					&& targetEntityName.equals(EntityManagerMock.SPECIMEN_CHARACTERISTIC_NAME))
			{
				return entityManagerMock.createAssociation(sourceEntityName, targetEntityName,
						AssociationDirection.SRC_DESTINATION, "", "specimenCharacteristics",
						"SPECIMEN_CHARACTERISTICS_ID", null);
			}

		}
		catch (DynamicExtensionsSystemException e)
		{
			Logger.out.error("UnExpected Exception:" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * To check whether the attribute is inherited attribute or not.
	 * @param attribute The refrence to attribute
	 * @return true if that attribute is inherited.
	 */
	private boolean isInheritedAttribute(AttributeInterface attribute)
	{
		EntityInterface parentEntity = attribute.getEntity().getParentEntity();
		if (parentEntity != null)
		{
			String attributeName = attribute.getName();
			Collection<AttributeInterface> attributes = parentEntity.getAttributeCollection();
			for (AttributeInterface attr : attributes)
			{
				if (attributeName.equals(attr.getName()))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * To check whether the association is inherited association or not.
	 * @param association The refrence to association
	 * @return true if that association is inherited.
	 */
	private boolean isInheritedAassociation(AssociationInterface association)
	{
		String sourceEntityName = association.getEntity().getName();
		String targetEntityName = association.getTargetEntity().getName();
		// These associations are not inherited associations.
		if (sourceEntityName.equals(EntityManagerMock.SPECIMEN_NAME)
				&& targetEntityName.equals(EntityManagerMock.SPECIMEN_NAME))
		{
			return false;
		}
		else if (sourceEntityName.equals(EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME)
				&& targetEntityName.equals(EntityManagerMock.SPECIMEN_NAME))
		{
			return false;
		}
		else if (sourceEntityName.equals(EntityManagerMock.SPECIMEN_NAME)
				&& targetEntityName.equals(EntityManagerMock.SPECIMEN_CHARACTERISTIC_NAME))
		{
			return false;
		}
		else if (sourceEntityName.equals(EntityManagerMock.SPECIMEN_NAME)
				&& targetEntityName.equals(EntityManagerMock.SPECIMEN_EVT_NAME))
		{
			return false;
		}
		//Associations between parent & child specimen
		if (EntityManagerMock.specimenClasses.contains(sourceEntityName)
				&& EntityManagerMock.specimenClasses.contains(targetEntityName))
		{
			return true;
		}

		//Associations between SCG & specimen
		if (sourceEntityName.equals(EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME)
				&& EntityManagerMock.specimenClasses.contains(targetEntityName))
		{
			return true;
		}

		//Associations between specimen & event heirarchy
		if (EntityManagerMock.specimenClasses.contains(sourceEntityName)
				&& EntityManagerMock.eventClasses.contains(targetEntityName))
		{
			return true;
		}

		if (EntityManagerMock.specimenClasses.contains(sourceEntityName)
				&& targetEntityName.equals(EntityManagerMock.SPECIMEN_CHARACTERISTIC_NAME))
		{
			return true;
		}
		return false;
	}
}
