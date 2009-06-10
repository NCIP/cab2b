/**
 * 
 */
package edu.wustl.cab2bwebapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * @author chetan_pundhir
 * This Action class clears the session and user gets Logout
 *
 */

public class LogoutAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().invalidate();
		ActionMessages messages = new ActionMessages();
		ActionMessage message = new ActionMessage("success.user.logout");
		messages.add(Constants.SUCCESS_USER_LOGOUT, message);
		saveMessages(request, messages);		
		return mapping.findForward(Constants.HOME);
	}
}
