package edu.wustl.cab2bwebapp.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2bwebapp.bizlogic.ApplicationBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_pundhir
 * @author gaurav_mehta
 * This Action class is called on change of drop down box on second page 
 * It loads the ServiceInstances and Saved Searches depending on the application selected 
 * 
 */
public class LoadModelRelatedDataAction extends Action {

    org.apache.log4j.Logger logger = Logger.getLogger(LoadModelRelatedDataAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {
        
        ApplicationBizLogic applicationBizLogic = new ApplicationBizLogic();
        UserInterface user = (UserInterface) request.getSession().getAttribute(Constants.USER_OBJECT);

        Long entityId = Long.parseLong(request.getParameter("modelId"));
        String entityGroupName = applicationBizLogic.getEntityName(entityId);
        EntityGroupInterface entityGroup = EntityCache.getInstance().getEntityGroupByName(entityGroupName);

        if (request.getParameter("task").equals("serviceinstance")) {
            List<ServiceURL> serviceInstances = applicationBizLogic.getApplicationInstances(user, entityGroupName);
            request.setAttribute(Constants.SERVICE_INSTANCES, serviceInstances);
            return mapping.findForward(Constants.FORWARD_SERVICE_INSTANCE);
        } else {
            Collection<ICab2bQuery> savedQueries = new SavedQueryBizLogic().getAllQueries(entityGroup);
            request.setAttribute(Constants.SAVED_QUERIES, savedQueries);
            return mapping.findForward(Constants.FORWARD_SAVED_QUERIES);
        }
        
    }
}