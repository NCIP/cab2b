/**
 *
 */
package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

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

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2bwebapp.bizlogic.AddLimitHTMLGeneratorBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.utils.QueryUtility;

/**
 * @author chetan_pundhir
 * This action class is used to call code to generate dynamic UI for defining conditions on query 
 * and forward to the page for adding limits via the generated UI.
 *
 */
public class AddLimitAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AddLimitAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for generating 
     * dynamic HTML for add limit page.
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

        
        try {
            SavedQueryBizLogic savedQueryProvider =
                    (SavedQueryBizLogic) request.getSession().getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
            if (request.getParameterValues(Constants.MODEL_GROUPS) != null) {
                actionForward = Constants.FORWARD_ADD_LIMIT;
                ActionForward forward = mapping.findForward(actionForward);
                return new ActionForward(forward.getName(), forward.getPath(), false);
            } else {
                Long queryId = Long.parseLong(request.getParameter(Constants.QUERY_ID));
                ICab2bQuery query = savedQueryProvider.getQueryById(queryId);
                Collection<ICondition> nonPara = QueryUtility.getAllNonParameteriedConditions(query);
                Collection<ICondition> paraCond = QueryUtility.getAllParameterizedConditions(query);
                response.setContentType("text/html");
                PrintWriter writer = response.getWriter();
                String html =
                        (nonPara != null && !nonPara.isEmpty() && (paraCond == null || paraCond.isEmpty())) ? ""
                                : new AddLimitHTMLGeneratorBizLogic(servlet.getServletContext()
                                    .getRealPath(Constants.ADD_LIMIT_XML_FILE_PATH)).getHTMLForSavedQuery(query);
                writer.write(html);
                writer.close();
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.addlimit.failure", e.getMessage());
            errors.add(Constants.FATAL_ADD_LIMIT_FAILURE, error);
            saveErrors(request, errors);
            actionForward = Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(actionForward);
    }
}