/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.common.dynamicextensions.processor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This Class populates the DataEntryForm and saves the same into the Database.
 * @author chetan_patil
 */
public class ApplyDataEntryFormProcessor extends BaseDynamicExtensionsProcessor
{

	/**
	 * Default Constructor
	 */
	public ApplyDataEntryFormProcessor()
	{
	}

	/**
	 * This method returns the instance of ApplyDataEntryFormProcessor.
	 * @return ApplyDataEntryFormProcessor Instance of ApplyDataEntryFormProcessor
	 */
	public static ApplyDataEntryFormProcessor getInstance()
	{
		return new ApplyDataEntryFormProcessor();
	}

	/**
	 * 
	 * @param attributeValueMap
	 * @return
	 */
	public Map<AbstractAttributeInterface, Object> removeNullValueEntriesFormMap(
			Map<AbstractAttributeInterface, Object> attributeValueMap)
	{
		Set<Map.Entry<AbstractAttributeInterface, Object>> attributeValueSet = attributeValueMap
				.entrySet();
		Iterator attributeValueSetIterator = attributeValueSet.iterator();
		while (attributeValueSetIterator.hasNext())
		{
			Map.Entry<AbstractAttributeInterface, Object> attributeValueEntry = (Map.Entry<AbstractAttributeInterface, Object>) attributeValueSetIterator
					.next();

			Object value = attributeValueEntry.getValue();
			if (value == null)
			{
				attributeValueSetIterator.remove();
			}
			else if (value instanceof List && ((List) value).isEmpty())
			{
				attributeValueSetIterator.remove();

			}
		}
		return attributeValueMap;
	}

	/**
	 * This method will pass the values entered into the controls to EntityManager to insert them in Database.
	 * @param containerInterface The container of who's value of Control are to be populated. 
	 * @param attributeValueMap The Map of Attribute and their corresponding values from controls.
	 * @throws DynamicExtensionsApplicationException on Application exception
	 * @throws DynamicExtensionsSystemException on System exception
	 * @return recordIdentifier Record identifier of the last saved record. 
	 */
	public String insertDataEntryForm(ContainerInterface container,
			Map<AbstractAttributeInterface, Object> attributeValueMap)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		EntityInterface entity = container.getEntity();
		Long recordIdentifier = entityManager.insertData(entity, attributeValueMap);
		return recordIdentifier.toString();
	}

	/**
	 * This method will pass the changed (modified) values entered into the controls to EntityManager to update them in Database.
	 * @param container
	 * @param attributeValueMap
	 * @param recordIdentifier
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public Boolean editDataEntryForm(ContainerInterface container,
			Map<AbstractAttributeInterface, Object> attributeValueMap, Long recordIdentifier)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		EntityInterface entity = container.getEntity();
		Boolean edited = entityManager.editData(entity, attributeValueMap, recordIdentifier);
		return edited;
	}

}
