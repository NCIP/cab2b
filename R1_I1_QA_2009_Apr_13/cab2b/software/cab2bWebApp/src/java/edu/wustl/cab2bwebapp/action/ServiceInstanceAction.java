/**
 * 
 */
package edu.wustl.cab2bwebapp.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
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
 * @author deepak_shingan
 *
 */
public class ServiceInstanceAction extends Action {

    org.apache.log4j.Logger logger = Logger.getLogger(ServiceInstanceAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {
        System.out.println("Inside ServiceInstanceAction");
        UserInterface user = new UserOperations().getUserByName(Constants.ANONYMOUS);
        System.out.println("User Name:" + user.getUserName());
        String modelGroupName = (String) request.getParameter(Constants.MODEL_GROUPS);
        System.out.println("modelGroupName:" + modelGroupName);
        ApplicationBizLogic appBizLogic = new ApplicationBizLogic();
        List<ServiceURLInterface> serviceInstances = appBizLogic.getApplicationInstances(user, modelGroupName);
        for (ServiceURLInterface serInstance : serviceInstances) {
            System.out.println("model name:" + serInstance.getEntityGroupName());
        }
        request.setAttribute(Constants.SERVICE_INSTANCES, serviceInstances);
        logger.info("Exiting ServiceInstanceAction");
        return mapping.findForward(Constants.SERVICE_INSTANCES);
    }
}
