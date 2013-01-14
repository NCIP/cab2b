/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.ModelGroupDVO;

/**
 * @author chetan_pundhir
 * This action class is called to prepare for and forward to the home page of the application.
 *
 */

public class HomeAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(HomeAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for setting 
     * parameters required at the home page.
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
        String actionForward = null;
        try {
            HttpSession session = request.getSession();
            SavedQueryBizLogic savedQueryBizLogic =
                    (SavedQueryBizLogic) session.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
            //Removing SEARCH_RESULTS attributes from session               
            session.removeAttribute(Constants.SEARCH_RESULTS);
            session.removeAttribute(Constants.SEARCH_RESULTS_VIEW);
            session.removeAttribute(Constants.FAILED_SERVICES_COUNT);
            session.removeAttribute(Constants.SAVED_QUERIES);
            session.removeAttribute(Constants.FAILED_SERVICES);
            session.removeAttribute(Constants.QUERY_ID);
            session.removeAttribute(Constants.SERVICE_INSTANCES);
            session.removeAttribute(Constants.MODEL_GROUPS);
            session.removeAttribute(Constants.CONDITION_LIST);
            session.removeAttribute(Constants.IS_FIRST_REQUEST);
            session.removeAttribute(Constants.STOP_AJAX);
            session.removeAttribute(Constants.EXECUTE_QUERY_BIZ_LOGIC_OBJECT);
            session.removeAttribute(Constants.UI_POPULATION_FINISHED);

            //delete the file(exported by the user) from server , when user navigates to the home page
            String filePath = (String) session.getAttribute(Constants.EXPORTED_FILE_PATH);
            if (filePath != null && !filePath.equals("")) {
                File file = new File(filePath);
                if (file.exists()) {
                    boolean isDeleted = file.delete();
                    if (!isDeleted) {
                        logger.info("Not able to delete file " + file.getName());
                    } else {
                        logger.info("File deleted successfully: " + file.getName());
                    }
                }
                session.removeAttribute(Constants.EXPORTED_FILE_PATH);
            }

            UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
            if (user == null) {
                user = new UserOperations().getUserByName(Constants.ANONYMOUS);
            }
            session.setAttribute(Constants.USER, user);
            if (savedQueryBizLogic == null) {
                savedQueryBizLogic = new SavedQueryBizLogic();
                request.getSession().setAttribute(Constants.SAVED_QUERY_BIZ_LOGIC, savedQueryBizLogic);
            }
            if (session.getAttribute(Constants.MODEL_GROUP_DVO_LIST) == null) {
                List<ModelGroupInterface> modelGroups = new ModelGroupBizLogic().getAllModelGroups();
                List<ModelGroupDVO> modelGroupDVOList = new ArrayList<ModelGroupDVO>();
                boolean singleSelected = false;
                for (int i = 0; i < modelGroups.size(); i++) {
                    ModelGroupDVO modelGroupDVO = new ModelGroupDVO();
                    modelGroupDVO.setModelGroupName(modelGroups.get(i).getModelGroupName());
                    modelGroupDVO.setSecured(modelGroups.get(i).isSecured());
                    if ((!singleSelected && session.getAttribute("userName") == null && !modelGroups.get(i)
                        .isSecured())
                            || (i == 0 && session.getAttribute("userName") != null)) {
                        modelGroupDVO.setSelected(true);
                        singleSelected = true;
                    }
                    modelGroupDVOList.add(modelGroupDVO);
                }
                session.setAttribute(Constants.MODEL_GROUP_DVO_LIST, modelGroupDVOList);

                List<EntityGroupInterface> entityGroups = new ArrayList<EntityGroupInterface>();
                if (!modelGroups.isEmpty()) {
                    ModelGroupInterface modelGroup = modelGroups.iterator().next();
                    entityGroups.addAll(modelGroup.getEntityGroupList());
                }
            }
            actionForward = Constants.FORWARD_HOME;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.home.failure", e.getMessage());
            errors.add(Constants.FATAL_HOME_FAILURE, error);
            saveErrors(request, errors);
            actionForward = Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(actionForward);
    }
}