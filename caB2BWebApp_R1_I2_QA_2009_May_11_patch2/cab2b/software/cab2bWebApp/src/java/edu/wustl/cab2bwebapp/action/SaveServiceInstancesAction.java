/**
 *
 */
package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2bwebapp.bizlogic.ApplicationBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SaveServiceInstancesBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * Action class for executing save-service operation for current user
 * @author deepak_shingan
 */
public class SaveServiceInstancesAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(SaveServiceInstancesAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for generating 
     * dynamic HTML for add limit page.
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

        HttpSession session = request.getSession();
        UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
        String modelGroupName = (String) request.getParameter(Constants.MODEL_GROUP_NAME);
        String[] urlArray = request.getParameterValues(Constants.SERVICE_INSTANCES);

        try {
            List<String> userSelectedURLs = Arrays.asList(urlArray);
            List<ServiceURLInterface> serviceInstances = new ApplicationBizLogic().getApplicationInstances(user,
                                                                                                           modelGroupName);
            user = new SaveServiceInstancesBizLogic().updateServiceInstanceSettings(user, userSelectedURLs,
                                                                                    serviceInstances,
                                                                                    modelGroupName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.addlimit.failure");
            errors.add(Constants.FATAL_ADD_LIMIT_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);

        }
        //updating user in session
        session.setAttribute(Constants.USER, user);
        ActionForward actionForward = mapping.findForward(Constants.FORWARD_HOME);
        ActionForward actionRedirect = new ActionForward(actionForward.getName(), actionForward.getPath(), false);
        return actionRedirect;
    }
}
