/**
 * 
 */
package edu.wustl.cab2bwebapp.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2bwebapp.bizlogic.ApplicationBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * Action class for showing service instances
 * @author deepak_shingan
 *
 */
public class DisplayServiceInstancesAction extends Action {

    org.apache.log4j.Logger logger = Logger.getLogger(DisplayServiceInstancesAction.class);

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {

        try {

            logger.info("Inside ServiceInstanceAction");
            HttpSession session = request.getSession();
            UserInterface user = (UserInterface) session.getAttribute(Constants.USER);

            String modelGroupName = (String) request.getParameter(Constants.MODEL_GROUPS);
            ApplicationBizLogic appBizLogic = new ApplicationBizLogic();

            //Note :  getApplicationInstances() method is returning results from database 
            List<ServiceURLInterface> serviceInstancesForSingleModelGroup = appBizLogic.getApplicationInstances(
                                                                                                                user,
                                                                                                                modelGroupName);
            //Remember this collection is defined as HashSet in User class
            //For All service urls in this list configured=true
            Collection<ServiceURLInterface> allServiceInstanceFromUserObject = user.getServiceURLCollection();
            if (allServiceInstanceFromUserObject != null && allServiceInstanceFromUserObject.size() != 0) {
                if (user.getUserName().equals(Constants.ANONYMOUS)) {
                    for (ServiceURLInterface service : serviceInstancesForSingleModelGroup) {
                        if (allServiceInstanceFromUserObject.contains(service)) {
                            service.setConfigured(true);
                        } else {
                            service.setConfigured(false);
                        }
                    }
                }
            }

            //adding currently selected modelGroupName/serviceInstances to user session
            request.setAttribute(Constants.MODEL_GROUPS, modelGroupName);
            request.setAttribute(Constants.SERVICE_INSTANCES, serviceInstancesForSingleModelGroup);
            logger.info("Exiting ServiceInstanceAction");
            ActionForward actionForward = mapping.findForward(Constants.FORWARD_SERVICE_INSTANCES);
            ActionForward actionRedirect = new ActionForward(actionForward.getName(), actionForward.getPath(),
                    false);
            return actionRedirect;
        } catch (Exception e) {
            logger.error(e.getMessage());
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.displayserviceinstances.failure");
            errors.add(Constants.FATAL_DISPLAY_SERVICE_INSTANCES_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
    }
}
