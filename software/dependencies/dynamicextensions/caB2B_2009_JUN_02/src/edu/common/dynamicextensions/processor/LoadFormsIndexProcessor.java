/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.processor;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.actionform.FormsIndexForm;

/**
 * Populates  the actonForm with required data eg. entityList. 
 * @author deepti_shelar
 *
 */
public class LoadFormsIndexProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Default Constructor.
	 */
	protected LoadFormsIndexProcessor()
	{
	}

	/**
	 * returns the instance of LoadFormsIndexProcessor.
	 * @return LoadFormsIndexProcessor instance of LoadFormsIndexProcessor
	 */
	public static LoadFormsIndexProcessor getInstance()
	{
		return new LoadFormsIndexProcessor();
	}

	/**
	 * A call to EntityManager will return the entityList which will then added to actionForm.
	 * @param loadFormIndexForm FormsIndexForm
	 * @throws DynamicExtensionsApplicationException  DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	public void populateFormsIndex(FormsIndexForm loadFormIndexForm) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		Collection<ContainerInterface> containerCollection = null;
		EntityManagerInterface entityManager = EntityManager.getInstance();
		containerCollection = entityManager.getAllContainers();
		if (containerCollection == null)
		{
			containerCollection = new HashSet<ContainerInterface>();
		}
		loadFormIndexForm.setContainerCollection(containerCollection);
	}

}
