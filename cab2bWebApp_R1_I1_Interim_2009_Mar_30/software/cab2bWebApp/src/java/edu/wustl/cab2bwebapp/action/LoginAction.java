/**
 * 
 */
package edu.wustl.cab2bwebapp.action;

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
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.actionform.LoginForm;
import edu.wustl.cab2bwebapp.bean.ModelBean;
import edu.wustl.cab2bwebapp.bizlogic.ApplicationBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gaurav_mehta
 * This Action class is called for authenticating user credentials and to load things required for next page.
 * 
 */
public class LoginAction extends Action {

    org.apache.log4j.Logger logger = Logger.getLogger(LoginAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {
        LoginForm loginForm = (LoginForm) form;
        String userName = null;
        String password = null;

        userName = loginForm.getUserName();
        password = loginForm.getPassword();

        HttpSession session = request.getSession();

        GlobusCredential globusCredential = null;
        try {
            UserOperations userOperations = new UserOperations();
            UserInterface user = null;

            if (userName != null) {
                globusCredential = new Authenticator(userName).validateUser(password);
                user = userOperations.getUserByName(globusCredential.getIdentity());
                if (user == null) {
                    user = userOperations.insertUser(new User(globusCredential.getIdentity(), null, false));
                }
            } else {
                user = userOperations.getUserByName("Anonymous");
                userName = Constants.GUEST_USER;
            }

            ApplicationBizLogic applicationBizLogic = new ApplicationBizLogic();
            List<ModelBean> modelNames = applicationBizLogic.getApplicationNames();
            List<ServiceURL> serviceInstance = applicationBizLogic.getApplicationInstances(user);
            List<ICab2bQuery> savedQueries = new SavedQueryBizLogic().getAllQueries();

            request.setAttribute(Constants.MODEL_NAMES, modelNames);
            request.setAttribute(Constants.SERVICE_INSTANCES, serviceInstance);
            request.setAttribute(Constants.SAVED_QUERIES, savedQueries);

            session.setAttribute(Constants.GLOBUS_CREDENTIAL, globusCredential);
            session.setAttribute(Constants.USER_OBJECT, user);
            session.setAttribute(Constants.USER_NAME, userName);

            return mapping.findForward(Constants.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("error.login.invalid");
            errors.add(Constants.ERROR_LOGIN, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FAILURE);
        }
    }
}