/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.webui.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.util.global.Constants;

/**
 * This class is used to redirect the user to the Home Page.
 * It also sets the cacheMap in session.
 * 
 * @author deepti_shelar
 */
public class ForwardAction extends BaseDynamicExtensionsAction
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
		HttpSession session = request.getSession();
		Map cacheMap = new HashMap();
		session.setAttribute(Constants.CACHE_MAP, cacheMap);
		return (mapping.findForward(Constants.SUCCESS));
	}

}