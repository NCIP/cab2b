/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author rahul_ner
 *
 */
public class DeleteRecordProcessor extends BaseDynamicExtensionsProcessor
{

	/**
	 * Default Constructor.
	 */
	protected DeleteRecordProcessor()
	{
	}

	/**
	 * @return
	 */
	public static DeleteRecordProcessor getInstance()
	{
		return new DeleteRecordProcessor();
	}

	/**
	 * @param container
	 * @param recordId
	 */
	public void deleteRecord(ContainerInterface container, Long recordIdentifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityManager.getInstance().deleteRecord(container.getEntity(), recordIdentifier);
	}

}
