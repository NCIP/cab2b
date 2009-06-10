package edu.wustl.cab2bwebapp.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
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
public class DisplaySavedSearchesAction extends Action {

    org.apache.log4j.Logger logger = Logger.getLogger(DisplaySavedSearchesAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {

        try {
            ModelGroupBizLogic modelGroupBizLogic = new ModelGroupBizLogic();
            SavedQueryBizLogic savedQueryBizLogic = (SavedQueryBizLogic) request.getSession().getAttribute(
                                                                                                           Constants.SAVED_QUERY_PROVIDER);
            List<ICab2bQuery> savedSearches = new ArrayList<ICab2bQuery>();

            String modelGroupName = (String) request.getParameter(Constants.MODEL_GROUPS);
            List<EntityGroupInterface> entityGroupName = modelGroupBizLogic.getEntityGroupsForModel(modelGroupName);
            for (EntityGroupInterface entityGroup : entityGroupName) {
                savedSearches.addAll(savedQueryBizLogic.getAllNormalSearchQueries(entityGroup));
            }

            request.setAttribute(Constants.SAVED_SEARCHES, savedSearches);
            return mapping.findForward(Constants.FORWARD_SAVED_SEARCHES);

        } catch (Exception e) {
            logger.error(e.getMessage());
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.displaysavedsearches.failure");
            errors.add(Constants.FATAL_DISPLAY_SAVED_SEARCHES_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
    }
}