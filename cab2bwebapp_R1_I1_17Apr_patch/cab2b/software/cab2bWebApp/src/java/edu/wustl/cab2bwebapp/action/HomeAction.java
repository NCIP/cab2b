package edu.wustl.cab2bwebapp.action;

import java.util.ArrayList;
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

import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_pundhir
 * This action class is called to prepare for and forward to the home page of the application.
 * 
 */
public class HomeAction extends Action {

    org.apache.log4j.Logger logger = Logger.getLogger(HomeAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {

        try {
            HttpSession session = request.getSession();
            List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
            UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
            if (user == null) {
                user = new UserOperations().getUserByName(Constants.ANONYMOUS);
            } 
 
            if(user.getUserName().equals(Constants.ANONYMOUS)) {
                modelGroups = new ModelGroupBizLogic().getAllNonSecuredModelGroups();  
            } else {
                modelGroups = new ModelGroupBizLogic().getAllModelGroups();
            }
            SavedQueryBizLogic savedQueryBizLogic = (SavedQueryBizLogic) request.getSession().getAttribute(
                                                                                                           Constants.SAVED_QUERY_PROVIDER);
            if (savedQueryBizLogic == null) {
                savedQueryBizLogic = new SavedQueryBizLogic();
                request.getSession().setAttribute(Constants.SAVED_QUERY_PROVIDER, savedQueryBizLogic);
            }
            List<ICab2bQuery> savedSearches = savedQueryBizLogic.getAllNormalSearchQueries();

            request.setAttribute(Constants.HOME_ACTION, Constants.HOME_ACTION);
            request.setAttribute(Constants.MODEL_GROUPS, modelGroups);
            request.setAttribute(Constants.SAVED_SEARCHES, savedSearches);

            session.setAttribute(Constants.USER, user);
            return mapping.findForward(Constants.FORWARD_HOME);
        } catch (Exception e) {
            logger.error(e.getMessage());
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.home.failure");
            errors.add(Constants.FATAL_HOME_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
    }
}