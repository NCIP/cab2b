package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
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
import edu.wustl.cab2b.common.authentication.util.CagridPropertyLoader;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.actionform.RegisterForm;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * @author gaurav_mehta
 * @author chetan_pundhir
 * This action class is called for authenticating user and to load entities required for next page.
 *
 */
public class RegisterAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(RegisterAction.class);

    /**
     * Register
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
            
            RegisterForm registerForm = (RegisterForm) form;
            String userName = registerForm.getUserName();
            String email = registerForm.getEmail();
            String institution = registerForm.getInstitution();
            String phone = registerForm.getPhone();

           logger.info("JJJ RegisterAction: UserName="+userName);

            if (userName == null || userName.isEmpty()){
                return mapping.findForward(Constants.FORWARD_REGISTER);
            }


           return mapping.findForward(Constants.FORWARD_REGISTER);
      } catch (Exception e){
           logger.error("Error"+e);
           return mapping.findForward(Constants.FORWARD_FAILURE);
      }

    }
}
