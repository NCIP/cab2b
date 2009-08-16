package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2bwebapp.bizlogic.executequery.ExecuteQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * @author chetan_pundhir
 * This action class is called to prepare for and forward to the application dash board.
 *
 */

public class BackgroundQueryAction extends Action {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(DisplayDashboardAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for setting 
     * parameters required at the dash board page.
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
            ExecuteQueryBizLogic executeQueryBizLogic =
                    (ExecuteQueryBizLogic) session.getAttribute(Constants.EXECUTE_QUERY_BIZ_LOGIC_OBJECT);
            UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
            Map<UserInterface, Set<ExecuteQueryBizLogic>> userToBackgroundQueries =
                    (Map<UserInterface, Set<ExecuteQueryBizLogic>>) session
                        .getAttribute(Constants.USER_VS_EXECUTE_QUERY_BIZ_LOGIC_OBJECT);
            Set<ExecuteQueryBizLogic> queryBizLogic = userToBackgroundQueries.get(user);
            if (queryBizLogic == null) {
                queryBizLogic = new HashSet<ExecuteQueryBizLogic>();
                userToBackgroundQueries.put(user, queryBizLogic);
            }
            queryBizLogic.add(executeQueryBizLogic);            

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.displaydashboard.failure", e.getMessage());
            errors.add(Constants.FATAL_DISPLAY_DASHBOARD_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
        ActionForward actionForward = mapping.findForward(Constants.FORWARD_HOME);
        ActionForward actionRedirect = new ActionForward(actionForward.getName(), actionForward.getPath(), false);
        return actionRedirect;
    }
}
