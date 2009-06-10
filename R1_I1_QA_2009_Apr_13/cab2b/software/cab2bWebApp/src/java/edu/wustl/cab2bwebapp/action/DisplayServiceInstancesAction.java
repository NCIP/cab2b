/**
 * 
 */
package edu.wustl.cab2bwebapp.action;

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
import edu.wustl.cab2b.server.user.UserOperations;
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
            UserInterface user = session.getAttribute(Constants.USER) == null ? new UserOperations().getUserByName(Constants.ANONYMOUS)
                    : (UserInterface) session.getAttribute(Constants.USER);

            logger.info("User Name:" + user.getUserName());
            String modelGroupName = (String) request.getParameter(Constants.MODEL_GROUPS);
            logger.info("modelGroupName:" + modelGroupName);
            ApplicationBizLogic appBizLogic = new ApplicationBizLogic();
            List<ServiceURLInterface> serviceInstances = appBizLogic.getApplicationInstances(user, modelGroupName);
            for (ServiceURLInterface serInstance : serviceInstances) {
                logger.info("model name:" + serInstance.getEntityGroupName());
                if (serInstance.isConfigured()) {
                    logger.info("Configured");
                }
            }
            request.setAttribute(Constants.SERVICE_INSTANCES, serviceInstances);
            logger.info("Exiting ServiceInstanceAction");
            return mapping.findForward(Constants.FORWARD_SERVICE_INSTANCES);
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
