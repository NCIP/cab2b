/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */

package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.io.Writer;

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

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * Action for executing query related operations.
 * @author pallavi_mistry
 * 
 */
public class ExecuteQueryAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExecuteQueryAction.class);

    /**
     * The execute method is called via ajax by searchresults.jsp, it calls the code to execute the query
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        try {
            HttpSession session = request.getSession();
            String[] modelGroupNames = (String[]) session.getAttribute(Constants.MODEL_GROUPS);
            SavedQueryBizLogic savedQueryBizLogic =
                    (SavedQueryBizLogic) session.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
            GlobusCredential proxy = (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);
            UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
            Long queryId = (Long) session.getAttribute(Constants.QUERY_ID);
            String keyword = (String) session.getAttribute(Constants.KEYWORD);
            String conditionstr = (String) session.getAttribute(Constants.CONDITION_LIST);
            ICab2bQuery query = savedQueryBizLogic.getQueryById(queryId);
            QueryBizLogic queryBizLogic =
                    new QueryBizLogic(query, conditionstr, keyword, user, proxy, modelGroupNames);
            session.setAttribute(Constants.QUERY_BIZ_LOGIC_OBJECT, queryBizLogic);

        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.home.failure", e.getMessage());
            errors.add(Constants.FATAL_KYEWORD_SEARCH_FAILURE, error);
            Writer writer = response.getWriter();
            response.setContentType("text/xml");
            writer.write("Exception");
        }
        return null;
    }
}
