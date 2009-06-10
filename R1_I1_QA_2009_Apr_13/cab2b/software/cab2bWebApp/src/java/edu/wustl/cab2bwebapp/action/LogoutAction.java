/**
 * 
 */
package edu.wustl.cab2bwebapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_pundhir
 * This action class logout the user by invalidating the session object and forwards the control to login page.
 *
 */

public class LogoutAction extends Action {

    org.apache.log4j.Logger logger = Logger.getLogger(LogoutAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        try {
            request.getSession().invalidate();
            ActionMessages messages = new ActionMessages();
            ActionMessage message = new ActionMessage("success.user.logout");
            messages.add(Constants.SUCCESS_USER_LOGOUT, message);
            saveMessages(request, messages);
            return mapping.findForward(Constants.FORWARD_HOME);
        } catch (Exception e) {
            logger.error(e.getMessage());
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.logout.failure");
            errors.add(Constants.FATAL_LOGOUT_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
    }
}
