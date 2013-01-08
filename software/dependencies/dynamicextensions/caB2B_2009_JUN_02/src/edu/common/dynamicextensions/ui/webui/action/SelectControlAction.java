/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * Created on Nov 14, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadFormControlsProcessor;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelectControlAction extends BaseDynamicExtensionsAction
{

	/**
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 * @throws DynamicExtensionsApplicationException 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws DynamicExtensionsApplicationException
	{
		try
		{
			//Get controls form
			ControlsForm controlsForm = (ControlsForm) form;
			ContainerInterface containerInterface = WebUIManager.getCurrentContainer(request);
			if((controlsForm!=null)&&(containerInterface!=null))
			{
				ControlsUtility.reinitializeSequenceNumbers(containerInterface.getControlCollection(),controlsForm.getControlsSequenceNumbers());
			}
			//Action can be either add sub-form or add control to form
			if (isAddSubFormAction(controlsForm.getUserSelectedTool()))
			{
				String operationMode = request.getParameter("operationMode");
				request.setAttribute("operationMode", operationMode);
				return mapping.findForward(Constants.ADD_SUB_FORM);
			}
			else
			{
				//Add form control
				addControlToForm(containerInterface, controlsForm);
				request.setAttribute("controlsList",controlsForm.getChildList());
				return mapping.findForward(Constants.SUCCESS);
			}
		}
		catch (Exception e)
		{
			String actionForwardString = catchException(e, request);
			if((actionForwardString==null)||(actionForwardString.equals("")))
			{
				return mapping.getInputForward(); 
			}
			return (mapping.findForward(actionForwardString));
		}
	}

	/**
	 * @param controlsForm
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private void addControlToForm(ContainerInterface containerInterface, ControlsForm controlsForm) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		String oldControlOperation = controlsForm.getControlOperation();
		controlsForm.setControlOperation(ProcessorConstants.OPERATION_ADD);
		LoadFormControlsProcessor loadFormControlsProcessor = LoadFormControlsProcessor.getInstance();
		loadFormControlsProcessor.loadFormControls(controlsForm, containerInterface);
		controlsForm.setControlOperation(oldControlOperation);
	}

	/**
	 * @param userSelectedTool
	 * @return
	 */
	private boolean isAddSubFormAction(String userSelectedTool)
	{
		if ((userSelectedTool != null) && (userSelectedTool.equals(ProcessorConstants.ADD_SUBFORM_CONTROL)))
		{
			return true;
		}
		return false;
	}
}