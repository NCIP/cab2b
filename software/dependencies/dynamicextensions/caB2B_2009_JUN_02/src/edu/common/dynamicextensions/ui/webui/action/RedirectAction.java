/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * Created on Nov 15, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author preeti_munot
 *
 */
public class RedirectAction extends BaseDynamicExtensionsAction
{

	/** (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String callbackURL = (String) CacheManager.getObjectFromCache(request,
				Constants.CALLBACK_URL);
		if (callbackURL != null && !callbackURL.equals(""))
		{
			callbackURL = callbackURL + "?" + WebUIManager.getOperationStatusParameterName() + "="
					+ "cancel";
			CacheManager.clearCache(request);
			response.sendRedirect(callbackURL);
			return null;
		}
		else
		{
			return mapping.findForward(Constants.SUCCESS);
		}
	}
}