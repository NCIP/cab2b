/**
 *
 */

package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.ExecuteQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.IQueryUpdateBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * Action for executing query related operations.
 * @author deepak_shingan, chetan_patil
 */
public class ExecuteQueryAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExecuteQueryAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for retrieve the 
     * query results.
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
        String findForward = null;
        if (request.getSession().getAttribute(Constants.SEARCH_RESULTS) != null) {
            findForward = Constants.FORWARD_SEARCH_RESULTS;
        } else {
            String[] modelGroupNames = (String[]) request.getSession().getAttribute(Constants.MODEL_GROUPS);
            request.getSession().removeAttribute(Constants.MODEL_GROUPS);
            HttpSession session = request.getSession();
            SavedQueryBizLogic savedQueryBizLogic = (SavedQueryBizLogic) session.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
            ICab2bQuery query = savedQueryBizLogic.getQueryById(Long.parseLong(request.getParameter(Constants.QUERY_ID)));
            UserInterface user = (UserInterface) session.getAttribute(Constants.USER);

            try {
                String conditionstr = request.getParameter("conditionList");
                if (conditionstr == null) {
                    conditionstr = "";
                }

                //Update the IQuery according to parameterized conditions.
                String errorMessage = new IQueryUpdateBizLogic().setInputDataToQuery(conditionstr,
                                                                                     query.getConstraints(), null,
                                                                                     query);
                if (!errorMessage.equals("")) {
                    throw new Exception(errorMessage);
                } else {
                    Collection<ICab2bQuery> queries = new ArrayList<ICab2bQuery>();
                    queries.add(query);
                    GlobusCredential proxy = (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);
                    ExecuteQueryBizLogic executeQueryBizLogic = new ExecuteQueryBizLogic(queries, proxy, user,
                            modelGroupNames);
                    List<Map<AttributeInterface, Object>> finalResult = executeQueryBizLogic.getFinalResult();
                    if (!finalResult.isEmpty()) {
                        session.setAttribute(Constants.SEARCH_RESULTS_VIEW, finalResult);
                    }
                    Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = executeQueryBizLogic.getSearchResults();
                    Collection<ICab2bQuery> allQueries = searchResults.keySet();
                    List<String> queryNameList = new ArrayList<String>();

                    for (ICab2bQuery queryObj : allQueries) {
                        int count = searchResults.get(queryObj).getResultForAllUrls().size();
                        StringBuffer queryName = new StringBuffer(queryObj.getName()).append(" (").append(count).append(
                                                                                                                        ")");
                        queryNameList.add(queryName.toString());
                    }
                    int failedServicesCount = executeQueryBizLogic.getFailedSercives().size();
                    session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedServicesCount);
                    session.setAttribute(Constants.FAILED_SERVICES, executeQueryBizLogic.getFailedSercives());
                    session.setAttribute(Constants.SERVICE_INSTANCES,
                                         executeQueryBizLogic.getUrlsForSelectedQueries());
                    session.setAttribute(Constants.SAVED_QUERIES, queryNameList);
                    session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                    findForward = Constants.FORWARD_SEARCH_RESULTS;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                ActionErrors errors = new ActionErrors();
                ActionError error = new ActionError("fatal.executeQuerySearch.failure");
                errors.add(Constants.FATAL_KYEWORD_SEARCH_FAILURE, error);
                saveErrors(request, errors);
                findForward = Constants.FORWARD_FAILURE;
            }
        }
        return mapping.findForward(findForward);
    }
}
