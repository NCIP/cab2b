/**
 * 
 */
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
import edu.wustl.common.util.logger.Logger;

/**
 * @author gaurav_mehta
 * @author chetan_pundhir
 * This Action class is called for authenticating user credentials and to load things required for next page.
 * 
 */
public class LoginAction extends Action {

    org.apache.log4j.Logger logger = Logger.getLogger(LoginAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {

        try {

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

                GlobusCredential globusCredential = null;
                UserOperations userOperations = new UserOperations();

                try {
                    globusCredential = new Authenticator(userName).validateUser(password);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    ActionErrors errors = new ActionErrors();
                    ActionError error = new ActionError("error.login.invalid");
                    errors.add(Constants.ERROR_LOGIN_INVALID, error);
                    saveErrors(request, errors);
                    request.setAttribute(Constants.LOGIN_PAGE, Constants.LOGIN_PAGE);
                    return mapping.findForward(Constants.FORWARD_LOGIN);
                }
                user = userOperations.getUserByName(globusCredential.getIdentity());
                if (user == null) {
                    user = userOperations.insertUser(new User(globusCredential.getIdentity(), null, false));
                }
                session.setAttribute(Constants.GLOBUS_CREDENTIAL, globusCredential);
                session.setAttribute(Constants.USER, user);
                session.setAttribute(Constants.USER_NAME, userName);
            }
            List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
            List<ICab2bQuery> savedSearches = new ArrayList<ICab2bQuery>();

            modelGroups = new ModelGroupBizLogic().getAllModelGroups();
            savedSearches = ((SavedQueryBizLogic) request.getSession().getAttribute(Constants.SAVED_QUERY_PROVIDER)).getAllNormalSearchQueries();

            request.setAttribute(Constants.MODEL_GROUPS, modelGroups);
            request.setAttribute(Constants.SAVED_SEARCHES, savedSearches);

            return mapping.findForward(Constants.FORWARD_HOME);
        } catch (Exception e) {
            logger.error(e.getMessage());
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.login.failure");
            errors.add(Constants.FATAL_LOGIN_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
    }
}