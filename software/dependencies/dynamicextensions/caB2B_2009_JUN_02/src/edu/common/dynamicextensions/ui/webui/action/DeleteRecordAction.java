/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.DeleteRecordProcessor;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

/**
 * 
 * @author Rahul Ner 
 */
public class DeleteRecordAction extends BaseDynamicExtensionsAction
{

	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		String target = null;
		try
		{
			String containerIdentifier = request.getParameter("containerIdentifier");
			Long recordIdentifier = new Long(request.getParameter("recordIdentifier"));

			ContainerInterface container = DynamicExtensionsUtility
					.getContainerByIdentifier(containerIdentifier);

			DeleteRecordProcessor.getInstance().deleteRecord(container, recordIdentifier);

			saveMessages(request, getSuccessMessage(container.getCaption(), recordIdentifier
					.toString()));

			target = "success";
		}
		catch (Exception e)
		{
			target = catchException(e, request);
			if ((target == null) || (target.equals("")))
			{
				return mapping.getInputForward();
			}
		}
		return mapping.findForward(target);
	}

	/**
	 * 
	 * @param formDefinitionForm actionform
	 * @return ActionMessages Messages
	 */
	private ActionMessages getSuccessMessage(String entityName, String recordId)
	{
		ActionMessages actionMessages = new ActionMessages();
		actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"app.deleteRecord.success", recordId, entityName));
		return actionMessages;
	}

}
