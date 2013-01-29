/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.ui.webui.actionform.PreviewForm;

/**
 * This is the Processor class for Preview Form
 * @author chetan_patil
 * @version 1.0
 */
public class LoadFormPreviewProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Default constructor
	 */
	protected LoadFormPreviewProcessor()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method returns the instance of LoadFormPreviewProcessor
	 * @return LoadFormPreviewProcessor instance
	 */
	public static LoadFormPreviewProcessor getInstance()
	{
		return new LoadFormPreviewProcessor();
	}

	/**
	 * This method populates the data from the PrewiewForm action
	 * @param containerInterface The Container Interface
	 * @param previewForm The PreviewForm action form 
	 * @throws DynamicExtensionsApplicationException : if Conatainer does not exists.
	 */
	public void populatePreviewForm(ContainerInterface containerInterface, PreviewForm previewForm) throws DynamicExtensionsApplicationException
	{
		previewForm.setContainerInterface(containerInterface);
	}
}