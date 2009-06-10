/**
 * 
 */
package edu.wustl.cab2bwebapp.action;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
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
public class SaveServiceInstanceAction extends Action {
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {

        HttpSession session = request.getSession();
        UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
        String modelGroupName = (String) request.getParameter(Constants.MODEL_GROUPS);
        String[] urlArray = request.getParameterValues(Constants.SERVICE_INSTANCES);
        List<String> userSelectedURLs = Arrays.asList(urlArray);
        List<ServiceURLInterface> serviceInstances = new ApplicationBizLogic().getApplicationInstances(user,
                                                                                                       modelGroupName);
        user = new SaveServiceInstancesBizLogic().updateServiceInstanceSettings(user, userSelectedURLs,
                                                                                serviceInstances, modelGroupName);
        //updating user in session
        session.setAttribute(Constants.USER, user);
        ActionForward actionForward = mapping.findForward(Constants.FORWARD_SERVICE_INSTANCES);
        ActionForward actionRedirect = new ActionForward(actionForward.getName(), actionForward.getPath(), false);
        return actionRedirect;
    }
}
