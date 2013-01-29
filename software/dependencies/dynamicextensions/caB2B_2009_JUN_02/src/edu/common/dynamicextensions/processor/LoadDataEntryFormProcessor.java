/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.actionForm.AbstractActionForm;

/**
 * @author sujay_narkar
 * @author chetan_patil
 *
 */
public class LoadDataEntryFormProcessor
{

	/**
	 * Empty Constructor
	 */
	protected LoadDataEntryFormProcessor()
	{
	}

	/**
	 * This method gets the new instance of the entity processor to the caller.
	 * @return EntityProcessor EntityProcessor instance
	 */
	public static LoadDataEntryFormProcessor getInstance()
	{
		return new LoadDataEntryFormProcessor();
	}

	/**
	 *
	 * @param actionForm
	 * @param containerInterface
	 * @param recordIdentifier
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException DynamicExtensionsApplicationException
	 */
	public ContainerInterface loadDataEntryForm(AbstractActionForm actionForm,
			ContainerInterface containerInterface,
			Map<AbstractAttributeInterface, Object> valueMap, String mode, String recordIdentifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		DataEntryForm dataEntryForm = (DataEntryForm) actionForm;

		if (mode != null && mode.equalsIgnoreCase(WebUIManagerConstants.VIEW_MODE))
		{
			containerInterface.setMode(mode);
		}

		if (valueMap != null && !valueMap.isEmpty())
		{
			containerInterface.setContainerValueMap(valueMap);
		}
		List processedContainersList = new ArrayList<ContainerInterface>();
		DynamicExtensionsUtility.setAllInContextContainers(containerInterface,
				processedContainersList);
		dataEntryForm.setContainerInterface(containerInterface);
		if (dataEntryForm.getErrorList() == null)
		{
			List<String> errorList = new ArrayList<String>();
			dataEntryForm.setErrorList(errorList);
		}
		if (dataEntryForm.getShowFormPreview() == null)
		{
			dataEntryForm.setShowFormPreview("");
		}
		if (recordIdentifier != null)
		{
			dataEntryForm.setRecordIdentifier(recordIdentifier);
		}
		else
		{
			dataEntryForm.setRecordIdentifier("");
		}
		return containerInterface;
	}

	/**
	 *
	 * @param entityInterface
	 * @param recordIdentifier
	 * @return
	 * @throws NumberFormatException
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Map<AbstractAttributeInterface, Object> getValueMapFromRecordId(
			EntityInterface entityInterface, String recordIdentifier) throws NumberFormatException,
			DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Map<AbstractAttributeInterface, Object> recordMap = new HashMap<AbstractAttributeInterface, Object>();
		if (recordIdentifier != null && !recordIdentifier.equals(""))
		{
			//Get corresponding Entity of the Container
			EntityManagerInterface entityManager = EntityManager.getInstance();
			recordMap = entityManager
					.getRecordById(entityInterface, Long.valueOf(recordIdentifier));

		}
		return recordMap;
	}
}
