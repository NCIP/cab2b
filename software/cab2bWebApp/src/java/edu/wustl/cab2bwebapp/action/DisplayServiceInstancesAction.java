/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.cab2bwebapp.action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.util.ServiceInstanceComparator;

/**
 * Action class for showing service instances
 * @author deepak_shingan
 *
 */
public class DisplayServiceInstancesAction extends Action {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(DisplayServiceInstancesAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for identifying 
     * service instances to be shown.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {
        ActionForward actionForward = null;
        try {
            UserInterface user = (UserInterface) request.getSession().getAttribute(Constants.USER);
            String modelGroupName = request.getParameter(Constants.MODEL_GROUPS);
            ApplicationBizLogic appBizLogic = new ApplicationBizLogic();
            //Note:  getApplicationInstances() method is returning results from database.
            List<ServiceURLInterface> servicesForSingleModelGroup =
                    appBizLogic.getApplicationInstances(user, modelGroupName);
            //For All service URLs in this list, configured is equal to true.
            Collection<ServiceURLInterface> allServiceInstanceFromUserObject = user.getServiceURLCollection();

            if (allServiceInstanceFromUserObject != null && !allServiceInstanceFromUserObject.isEmpty()) {
                for (ServiceURLInterface service : servicesForSingleModelGroup) {
                    if (allServiceInstanceFromUserObject.contains(service)) {
                        service.setConfigured(true);
                    } else {
                        service.setConfigured(false);
                    }
                }
            }
            Collections.sort(servicesForSingleModelGroup, new ServiceInstanceComparator());
            //Adding currently selected modelGroupName/serviceInstances to user session.
            request.setAttribute(Constants.MODEL_GROUP_NAME, modelGroupName);
            request.setAttribute(Constants.SERVICE_INSTANCES, servicesForSingleModelGroup);
            ActionForward forward = mapping.findForward(Constants.FORWARD_SERVICE_INSTANCES);
            actionForward = new ActionForward(forward.getName(), forward.getPath(), false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.displayserviceinstances.failure", e.getMessage());
            errors.add(Constants.FATAL_DISPLAY_SERVICE_INSTANCES_FAILURE, error);
            saveErrors(request, errors);
            actionForward = mapping.findForward(Constants.FORWARD_FAILURE);
        }
        return actionForward;
    }
}
