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

import edu.common.dynamicextensions.ui.webui.actionform.FormsIndexForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * This class will forward the request to LoadFormDefinitionAction.java.
 * @author deepti_shelar
 */
public class ApplyFormsIndexAction extends BaseDynamicExtensionsAction
{
	/**
	 * This mathod will forward the request to LoadFormDefinitionAction.java.
	 * 
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		FormsIndexForm formsIndexForm = (FormsIndexForm) form;
		ActionForward actionForward = null;
		String mode = formsIndexForm.getOperationMode();
		if (mode != null && mode.equalsIgnoreCase(Constants.ADD_NEW_FORM)
				&&	CacheManager.getObjectFromCache(request, Constants.CALLBACK_URL) == null)
		{
			CacheManager.clearCache(request);
		}
		else if (mode != null && mode.equalsIgnoreCase(Constants.INSERT_DATA)
				&& CacheManager.getObjectFromCache(request, Constants.CALLBACK_URL) == null)
		{
			CacheManager.clearCache(request);
		}
		
		
		if (mode != null && mode.equalsIgnoreCase(Constants.ADD_NEW_FORM))
		{
			actionForward = mapping.findForward(Constants.SUCCESS);
		}else if (mode != null && mode.equalsIgnoreCase(Constants.INSERT_DATA))
		{
			actionForward = mapping.findForward(Constants.INSERT_DATA);
		}
		if (mode != null && mode.equalsIgnoreCase(""))
		{
			CacheManager.clearCache(request);
			actionForward = mapping.findForward(Constants.SUCCESS);
		}
		return actionForward;
	}
}
