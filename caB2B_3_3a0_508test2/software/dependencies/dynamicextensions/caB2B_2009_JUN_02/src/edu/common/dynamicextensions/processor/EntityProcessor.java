
package edu.common.dynamicextensions.processor;

import java.util.Collection;
import java.util.Iterator;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.EntityUIBeanInterface;
import edu.common.dynamicextensions.ui.util.SemanticPropertyBuilderUtil;
import edu.wustl.common.util.Utility;

/**
 *<p>Title: EntityProcessor</p>
 *<p>Description:  This class acts as a utility class which processes tne entity .
 *1 . Creates an Entity.
 *2 . Saves an Entity.
 *3 . Populates the cache object.
 *4. Populates the domain Object.
 *This processor class is a POJO and not a framework specific class so it can be used by
 *all types of presentation layers.  </p>
 *@author Vishvesh Mulay
 *@version 1.0
 */
public class EntityProcessor extends BaseDynamicExtensionsProcessor
{

	/**
	 * This is a singleton class so we have a protected constructor , We are providing getInstance method 
	 * to return the EntityProcessor's instance.
	 */
	protected EntityProcessor()
	{

	}

	/**
	 * this method gets the new instance of the entity processor to the caller.
	 * @return EntityProcessor EntityProcessor instance
	 */
	public static EntityProcessor getInstance()
	{
		return new EntityProcessor();
	}

	/**
	 * This method returns empty domain object of entityInterface.
	 * @return EntityInterface Returns new instance of EntityInterface from the domain object Factory.
	 */
	public EntityInterface createEntity()
	{
		return DomainObjectFactory.getInstance().createEntity();
	}

	/**
	 * This method creates a new instance of the EntityInterface from the domain object factory. After the creation
	 * of this instance it populates the entityInterface with the information that is provided through 
	 * the entityInformationInterface which is a parameter to the method.
	 * @param entityUIBeanInterface Implementation of entityInformationInterface 
	 * which has all the data required for the creation of the entity.
	 * @return EntityInterface Returns the unsaved instance of EntityInterface with populated values taken 
	 * from the entityInformationInterface.
	 * @throws DynamicExtensionsSystemException in case of system error
	 * @throws DynamicExtensionsApplicationException in case of application error.
	 */
	public EntityInterface createAndSaveEntity(EntityUIBeanInterface entityUIBeanInterface)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityInterface entityInterface = null;
		if (entityUIBeanInterface != null)
		{
			entityInterface = createAndPopulateEntity(entityUIBeanInterface);
			entityInterface = EntityManager.getInstance().persistEntity(entityInterface);
		}
		return entityInterface;
	}

	/**
	 * This method populates the given EntityInterface using the given entityInformationInterface.
	 * @param entityInterface Instance of EntityInterface which is populated using the informationInterface.
	 * @param entityUIBeanInterface Instance of EntityUIBeanInterface which is used to populate the entityInterface.
	 */
	public void populateEntity(EntityUIBeanInterface entityUIBeanInterface,
			EntityInterface entityInterface)
	{
		if (entityUIBeanInterface != null && entityInterface != null)
		{
			entityInterface.setName(entityUIBeanInterface.getFormName());
			entityInterface.setDescription(entityUIBeanInterface.getFormDescription());
			entityInterface.removeAllSemanticProperties();
			Collection collection = SemanticPropertyBuilderUtil
					.getSymanticPropertyCollection(entityUIBeanInterface.getConceptCode());
			if (collection != null && !collection.isEmpty())
			{
				Iterator iterator = collection.iterator();
				while (iterator.hasNext())
				{
					entityInterface
							.addSemanticProperty((SemanticPropertyInterface) iterator.next());
				}
			}
			if (entityUIBeanInterface.getIsAbstract() != null
					&& entityUIBeanInterface.getIsAbstract().equals("true"))
			{
				entityInterface.setAbstract(true);
			}
			else
			{
				entityInterface.setAbstract(false);
			}
		}

	}

	/**
	 * This method will populate the EntityUIBeanInterface using the EntityInterface so that the 
	 * information of the Entity can be shown on the user page using the EntityUIBeanInterface.
	 * @param entityInterface Instance of EntityInterface from which to populate the informationInterface.
	 * @param entityUIBeanInterface Instance of EntityUIBeanInterface which will be populated using 
	 * the first parameter that is EntityInterface.
	 */
	public void populateEntityUIBeanInterface(EntityInterface entityInterface,
			EntityUIBeanInterface entityUIBeanInterface)
	{
		if (entityInterface != null && entityUIBeanInterface != null)
		{
			entityUIBeanInterface.setFormName(Utility.toString(entityInterface.getName()));
			if (entityInterface.getDescription() != null)
			{
				entityUIBeanInterface.setFormDescription(Utility.toString(entityInterface
						.getDescription()));
			}
			if ((entityInterface.getSemanticPropertyCollection() != null)
					&& (!entityInterface.getSemanticPropertyCollection().isEmpty()))
			{
				entityUIBeanInterface.setConceptCode(SemanticPropertyBuilderUtil
						.getConceptCodeString(entityInterface));
			}

			if (entityInterface.isAbstract())
			{
				entityUIBeanInterface.setIsAbstract("true");
			}
			else
			{
				entityUIBeanInterface.setIsAbstract("");
			}

		}
	}

	/**
	 * This method creates a new instance of the EntityInterface from the domain object factory. After the creation
	 * of this instance it populates the entityInterface with the information that is provided through 
	 * the entityInformationInterface which is a parameter to the method.
	 * @param entityUIBeanInterface Implementation of entityInformationInterface 
	 * which has all the data required for the creation of the entity.
	 * @return EntityInterface Returns the unsaved instance of EntityInterface with populated values taken 
	 * from the entityInformationInterface.
	 * @throws DynamicExtensionsSystemException Exception
	 */
	public EntityInterface createAndPopulateEntity(EntityUIBeanInterface entityUIBeanInterface)
			throws DynamicExtensionsSystemException
	{
		EntityInterface entityInterface = DomainObjectFactory.getInstance().createEntity();
		populateEntity(entityUIBeanInterface, entityInterface);
		return entityInterface;
	}

}
