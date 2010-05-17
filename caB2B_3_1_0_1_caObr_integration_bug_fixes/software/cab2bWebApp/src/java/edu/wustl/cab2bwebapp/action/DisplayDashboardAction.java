package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2bwebapp.bizlogic.UserBackgroundQueries;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.DashBoardDvoUtility;
import edu.wustl.cab2bwebapp.dvo.QueryStatusDVO;

/**
 * @author chetan_pundhir
 * This action class is called to prepare for and forward to the application dash board.
 *
 */

public class DisplayDashboardAction extends Action {

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
            Set<QueryStatus> queryStatusSet = new HashSet<QueryStatus>();
            Set<QueryStatusDVO> queryStatusDVOSet = null;
            UserInterface user = (UserInterface) request.getSession().getAttribute(Constants.USER);

            if (request.getHeader(Constants.AJAX_CALL) == null) {
                //First call take value from database.
                queryStatusDVOSet = new HashSet<QueryStatusDVO>();
                QueryURLStatusOperations opr = new QueryURLStatusOperations();
                Collection<QueryStatus> queryStatusFromDB = opr.getAllQueryStatusByUser(user);
                if (queryStatusFromDB != null) {
                    queryStatusSet.addAll(queryStatusFromDB);
                }
            } else {
                queryStatusDVOSet =
                        (Set<QueryStatusDVO>) request.getSession().getAttribute(Constants.QUERY_STATUS_DVO_SET);
            }
            //This is to ensure that we are showing query status that was added into Memory after our first DB call.
            Set<QueryStatus> queryStatusFromMemory =
                    UserBackgroundQueries.getInstance().getBackgroundQueriesForUser(user);

            //As a property of JAVA set interface if you want to update the object 
            //we have to first remove the object from set and add again with its modified value.
            queryStatusSet.removeAll(queryStatusFromMemory);
            queryStatusSet.addAll(queryStatusFromMemory);

            DashBoardDvoUtility.updateStatusDVO(queryStatusSet, queryStatusDVOSet);
            int inProgressQueryCount =
                    DashBoardDvoUtility.getQueryStatusDVOProcessingResultCount(queryStatusDVOSet);
            int completedQueryCount = queryStatusDVOSet.size() - inProgressQueryCount;

            request.getSession().setAttribute("completedQueryCount", completedQueryCount);
            request.getSession().setAttribute("inProgressQueryCount", inProgressQueryCount);
            request.getSession().setAttribute(Constants.QUERY_STATUS_DVO_SET, queryStatusDVOSet);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.displaydashboard.failure", e.getMessage());
            errors.add(Constants.FATAL_DISPLAY_DASHBOARD_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
        ActionForward actionForward =
                mapping.findForward(request.getHeader(Constants.AJAX_CALL) == null ? Constants.FORWARD_DASHBOARD
                        : Constants.FORWARD_DASHBOARD_PANEL);
        ActionForward actionRedirect = new ActionForward(actionForward.getName(), actionForward.getPath(), false);
        return actionRedirect;
    }
}