/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.cab2bwebapp.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * @author chetan_pundhir
 * This action class logout the user by invalidating the session object and forwards the control to login page.
 *
 */

public class LogoutAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(LogoutAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for invalidating 
     * the user session and forward to the home page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        try {
            request.getSession().invalidate();
            ActionMessages messages = new ActionMessages();
            ActionMessage message = new ActionMessage("success.user.logout");
            messages.add(Constants.SUCCESS_USER_LOGOUT, message);
            saveMessages(request, messages);
            return mapping.findForward(Constants.FORWARD_HOME);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.logout.failure", e.getMessage());
            errors.add(Constants.FATAL_LOGOUT_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
    }
}