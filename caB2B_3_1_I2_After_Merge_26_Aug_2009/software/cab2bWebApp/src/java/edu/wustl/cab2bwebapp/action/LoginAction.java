package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.Collection;
 ximport java.util.Iterator;
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
import edu.wustl.cab2b.common.authentication.exception.AuthenticationException;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.user.UserOperations;

import edu.wustl.cab2bwebapp.actionform.LoginForm;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

;

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
        try {
            LoginForm loginForm = (LoginForm) form;
            String userName = loginForm.getUserName();
            String password = loginForm.getPassword();

            if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
                return mapping.findForward(Constants.FORWARD_LOGIN);
            }

            HttpSession session = request.getSession();

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
            session.removeAttribute(Constants.QUERY_BIZ_LOGIC_OBJECT);
            session.removeAttribute(Constants.UI_POPULATION_FINISHED);
            session.removeAttribute(Constants.KEYWORD);
            session.removeAttribute(Constants.SELECTED_QUERY_NAME);

            UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
            if (user == null || user.getUserName().equals(Constants.ANONYMOUS)) {
                UserOperations userOperations = new UserOperations();
                GlobusCredential globusCredential = null;
                try {
                    globusCredential = new Authenticator(userName).validateUser(password);
                } catch (AuthenticationException e) {
                    logger.error(e.getMessage());
                    ActionErrors errors = new ActionErrors();
                    ActionError error = null;
                    if (e.getErrorCode().equals(ErrorCodeConstants.UR_0007)) {
                        error = new ActionError("error.login.invalid");
                    } else {
                        error = new ActionError("error.login.failure", e.getMessage());
                    }
                    errors.add(Constants.ERROR_LOGIN_INVALID, error);
                    saveErrors(request, errors);
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
            if (!user.getUserName().equals(Constants.ANONYMOUS)) {
                int inProgressQueryCount = 0;
                int completedQueryCount = 0;
                QueryURLStatusOperations opr = new QueryURLStatusOperations();
                Collection<QueryStatus> qsCollection = opr.getAllQueryStatusByUser(user);
                Iterator<QueryStatus> i = qsCollection.iterator();
                while (i.hasNext()) {
                    QueryStatus qs = i.next();
                    if (qs.getStatus().equals("Processing"))
                        inProgressQueryCount++;
                    else
                        completedQueryCount++;
                }
                session.setAttribute("completedQueryCount", completedQueryCount);
                session.setAttribute("inProgressQueryCount", inProgressQueryCount);
            }
            List<ModelGroupInterface> modelGroups = new ModelGroupBizLogic().getAllModelGroups();
            if (modelGroups != null && !modelGroups.isEmpty()) {
                ModelGroupInterface modelGroup = modelGroups.iterator().next();
                SavedQueryBizLogic savedQueryBizLogic =
                        (SavedQueryBizLogic) request.getSession().getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
                Collection<ICab2bQuery> savedSearches =
                        savedQueryBizLogic.getRegularQueries(modelGroup.getEntityGroupList());
                request.setAttribute(Constants.MODEL_GROUPS, modelGroups);
                request.setAttribute(Constants.SAVED_SEARCHES, savedSearches);
            }
            return mapping.findForward(Constants.FORWARD_HOME);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.login.failure", e.getMessage());
            errors.add(Constants.FATAL_LOGIN_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
    }
}
