/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * Created on Jan 8, 2007
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestActionClass extends Action
{

	@Override
	public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception
	{
		super.execute(arg0, arg1, arg2, arg3);
		ServletContext otherContext = getServlet().getServletContext().getContext("/catissuecore");
		otherContext.setAttribute("a","abcd");
		String t = "http://" + arg2.getServerName() + ":" + arg2.getServerPort() + "/catissuecore/DefineAnnotations.do";
		arg3.sendRedirect(t);
		return null;
	}
	 
}
