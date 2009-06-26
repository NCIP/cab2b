package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.ModelGroupDVO;
import edu.wustl.cab2bwebapp.util.SavedSearchComparator;

/**
 * @author chetan_pundhir
 * @author gaurav_mehta
 * This action class is called on change of drop down box on second page.
 * It loads the saved searches depending on the application selected.
 *
 */
public class DisplaySavedSearchesAction extends Action {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(DisplaySavedSearchesAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for setting saved 
     * searches parameters required at the pages for displaying saved searches.
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
        SavedQueryBizLogic savedQueryBizLogic =
                (SavedQueryBizLogic) request.getSession().getServletContext()
                    .getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
        String modelGroupName = (String) request.getParameter(Constants.MODEL_GROUPS);
        List<ModelGroupDVO> modelGroupDVOList =
                (List<ModelGroupDVO>) request.getSession().getAttribute(Constants.MODEL_GROUP_DVO_LIST);
        for (int i = 0; i < modelGroupDVOList.size(); i++) {
            if (modelGroupDVOList.get(i).getModelGroupName().equals(modelGroupName)) {
                modelGroupDVOList.get(i).setSelected(true);
            } else {
                modelGroupDVOList.get(i).setSelected(false);
            }
        }
        request.getSession().setAttribute(Constants.MODEL_GROUP_DVO_LIST, modelGroupDVOList);
        String findForward = null;
        try {
            if (!modelGroupName.equals("")) {
                Collection<EntityGroupInterface> entityGroups =
                        new ModelGroupBizLogic().getEntityGroupsForModel(modelGroupName);
                Collection<ICab2bQuery> savedSearches = savedQueryBizLogic.getRegularQueries(entityGroups);
                List savedSearchesList = new java.util.ArrayList(savedSearches);
                Collections.sort(savedSearchesList, new SavedSearchComparator());
                if (request.getParameter(Constants.FORWARD_ADD_LIMIT) != null) {
                    request.setAttribute(Constants.FORWARD_ADD_LIMIT, Constants.FORWARD_ADD_LIMIT);
                }
                request.setAttribute(Constants.SAVED_SEARCHES, savedSearchesList);
            }
            findForward = Constants.FORWARD_SAVED_SEARCHES;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.displaysavedsearches.failure");
            errors.add(Constants.FATAL_DISPLAY_SAVED_SEARCHES_FAILURE, error);
            saveErrors(request, errors);

            findForward = Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(findForward);
    }
}