/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.Collection;
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
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.authentication.Authenticator;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.actionform.LoginForm;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * @author gaurav_mehta
 * @author chetan_pundhir
 * This Action class is called for authenticating user and to load entities required for next page.
 *
 */
public class LoginAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(LoginAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for login the user.
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
        LoginForm loginForm = (LoginForm) form;
        String userName = loginForm.getUserName();
        String password = loginForm.getPassword();

        if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute(Constants.LOGIN_PAGE, Constants.LOGIN_PAGE);
            return mapping.findForward(Constants.FORWARD_LOGIN);
        }
        HttpSession session = request.getSession();
        UserInterface user = (UserInterface) session.getAttribute(Constants.USER);

        if (user == null || user.getUserName().equals(Constants.ANONYMOUS)) {
            UserOperations userOperations = new UserOperations();
            try {
                GlobusCredential globusCredential = new Authenticator(userName).validateUser(password);

                user = userOperations.getUserByName(globusCredential.getIdentity());
                if (user == null) {
                    user = userOperations.insertUser(new User(globusCredential.getIdentity(), null, false));
                }

                session.setAttribute(Constants.GLOBUS_CREDENTIAL, globusCredential);
                session.setAttribute(Constants.USER, user);
                session.setAttribute(Constants.USER_NAME, userName);
            } catch (Exception e) {
                logger.error(e.getMessage());
                handleException(request, "error.login.invalid", Constants.ERROR_LOGIN_INVALID);
                request.setAttribute(Constants.LOGIN_PAGE, Constants.LOGIN_PAGE);
                return mapping.findForward(Constants.FORWARD_LOGIN);
            }
        }
        try {
            List<ModelGroupInterface> modelGroups = new ModelGroupBizLogic().getAllModelGroups();
            if (modelGroups != null && !modelGroups.isEmpty()) {
                ModelGroupInterface modelGroup = modelGroups.iterator().next();
                SavedQueryBizLogic savedQueryBizLogic =
                        (SavedQueryBizLogic) request.getSession().getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
                Collection<ICab2bQuery> savedSearches =
                        savedQueryBizLogic.getRegualarQueries(modelGroup.getEntityGroupList());
                request.setAttribute(Constants.MODEL_GROUPS, modelGroups);
                request.setAttribute(Constants.SAVED_SEARCHES, savedSearches);
            }
            return mapping.findForward(Constants.FORWARD_HOME);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            handleException(request, "fatal.login.failure", Constants.FATAL_LOGIN_FAILURE);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
    }

    private void handleException(HttpServletRequest request, String errorKey, String errorConstant) {
        ActionErrors errors = new ActionErrors();
        ActionError error = new ActionError(errorKey);
        errors.add(errorConstant, error);
        saveErrors(request, errors);
    }
}