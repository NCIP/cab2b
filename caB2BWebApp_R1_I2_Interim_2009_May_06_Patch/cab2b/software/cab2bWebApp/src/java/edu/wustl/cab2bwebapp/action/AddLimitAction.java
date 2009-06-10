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
        SavedQueryBizLogic savedQueryProvider = (SavedQueryBizLogic) request.getSession().getAttribute(
                                                                                                       Constants.SAVED_QUERY_BIZ_LOGIC);

        String queryID = request.getParameter(Constants.QUERY_ID);
        if (request.getParameter(Constants.MODEL_GROUPS)==null) {
            try {
                Long queryId = Long.parseLong(queryID);
                ICab2bQuery query = savedQueryProvider.getQueryById(queryId);

                Collection<ICondition> nonPara = QueryUtility.getAllNonParameteriedConditions(query);
                Collection<ICondition> paraCond = QueryUtility.getAllParameterizedConditions(query);

                response.setContentType("text/html");
                PrintWriter writer = response.getWriter();

                if (nonPara != null && !nonPara.isEmpty() && (paraCond == null || paraCond.isEmpty())) {
                    writer.write("");
                    writer.close();
                    actionForward = "";
                } else {
                    String html = new AddLimitHTMLGeneratorBizLogic(
                            servlet.getServletContext().getRealPath(Constants.ADD_LIMIT_XML_FILE_PATH)).getHTMLForSavedQuery(query);
                    writer.write(html);
                    writer.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                ActionErrors errors = new ActionErrors();
                ActionError error = new ActionError("fatal.addlimit.failure");
                errors.add(Constants.FATAL_ADD_LIMIT_FAILURE, error);
                saveErrors(request, errors);
                actionForward = Constants.FORWARD_FAILURE;
            }
        } else {
            actionForward = Constants.FORWARD_ADD_LIMIT;
            ActionForward forward = mapping.findForward(actionForward);
            return new ActionForward(forward.getName(), forward.getPath(), false);
        }
        return mapping.findForward(actionForward);
    }
}