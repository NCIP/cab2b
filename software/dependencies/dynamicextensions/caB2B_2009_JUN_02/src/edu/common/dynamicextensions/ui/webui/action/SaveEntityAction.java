/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * Created on Oct 18, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.webui.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.ContainerProcessor;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SaveEntityAction extends BaseDynamicExtensionsAction
{

	/**
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ActionForward actionForward = null;
		try
		{
			//Get container interface from cache
			ContainerInterface containerInterface = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
			ControlsForm controlsForm = (ControlsForm) form;
			ContainerInterface currentContainerInterface = WebUIManager.getCurrentContainer(request);
			if((controlsForm!=null)&&(currentContainerInterface!=null))
			{
				ControlsUtility.reinitializeSequenceNumbers(currentContainerInterface.getControlCollection(),controlsForm.getControlsSequenceNumbers());
			}

			//Call container processor save method
			ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
			String formName = "";

			containerInterface = containerProcessor.saveContainer(containerInterface);
			if ((containerInterface != null) && (containerInterface.getEntity() != null))
			{
				formName = containerInterface.getEntity().getName();
			}
			saveMessages(request, getSuccessMessage(formName));
			String callbackURL = (String) CacheManager.getObjectFromCache(request, Constants.CALLBACK_URL);
			if (callbackURL != null && !callbackURL.equals(""))
			{
				List<Long> deletedIdList = (List<Long>) CacheManager.getObjectFromCache(request,
						WebUIManagerConstants.DELETED_ASSOCIATION_IDS);
				String associationIds = "";
				if (deletedIdList != null)
				{
					for (int i = 0; i < deletedIdList.size(); i++)
					{
						associationIds += deletedIdList.get(i);
						if (i < deletedIdList.size() - 1)
						{
							associationIds += "_";
						}
					}
				}
				callbackURL = callbackURL + "?" + WebUIManager.getOperationStatusParameterName()
						+ "=" + WebUIManagerConstants.SUCCESS + "&"
						+ WebUIManager.getContainerIdentifierParameterName() + "="
						+ containerInterface.getId().toString() + "&"
						+ WebUIManagerConstants.DELETED_ASSOCIATION_IDS + "=" + associationIds;

				CacheManager.clearCache(request);
				response.sendRedirect(callbackURL);
				return null;
			}
			actionForward = mapping.findForward(Constants.SUCCESS);
		}
		catch (Exception e)
		{
			String actionForwardString = catchException(e, request);
			if ((actionForwardString == null) || (actionForwardString.equals("")))
			{
				return mapping.getInputForward();
			}
			actionForward = mapping.findForward(actionForwardString);
		}
		return actionForward;
	}

	/**
	 * Get messages for successful save of entity
	 * @param formName formname
	 * @return ActionMessages messages
	 */
	private ActionMessages getSuccessMessage(String formName)
	{
		ActionMessages actionMessages = new ActionMessages();
		actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("app.entitySaveSuccessMessage", formName));
		return actionMessages;
	}
}