package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.ExecuteQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SavedQueryDVO;

/**
 * Action for executing query related operations.
 */
public class TransformQueryResultsAction extends Action {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(TransformQueryResultsAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the
     * code to retrieve the query results.
     * 
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
        String actionForward = Constants.FORWARD_SEARCH_RESULTS_PANEL;
        try {
            HttpSession session = request.getSession();
            Writer writer;
            System.out.println();
            if(session.getAttribute(Constants.STOP_AJAX) != null)
             {
                Boolean stopAjax = (Boolean) session.getAttribute(Constants.STOP_AJAX);
                if(stopAjax)
                {
                    writer = response.getWriter();
                    response.setContentType("text/xml");
                    writer.write("stopAjax");
                    return null;
                }
             }

            ExecuteQueryBizLogic executeQueryBizLogic =
                    (ExecuteQueryBizLogic) session.getAttribute(Constants.EXECUTE_QUERY_BIZ_LOGIC_OBJECT);
            if (executeQueryBizLogic == null) {
                writer = response.getWriter();
                response.setContentType("text/xml");
                writer.write("WaitingFor_ExecuteQueryBizLogicObject");
                return null;
            }

            SavedQueryBizLogic savedQueryBizLogic =
                    (SavedQueryBizLogic) session.getServletContext().getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
            ICab2bQuery query =
                    savedQueryBizLogic.getQueryById(Long.parseLong(request.getParameter(Constants.QUERY_ID)));
            int transformationMaxLimit = Integer.valueOf(request.getParameter("transformationMaxLimit"));
            Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults =
                    executeQueryBizLogic.getSearchResults(transformationMaxLimit);

            ICab2bQuery selectedQueryObj = null;
            Set<ICab2bQuery> queries = searchResults.keySet();
            for (ICab2bQuery queryObj : queries) {
                if (queryObj.getName().equals(query.getName())) {
                    selectedQueryObj = queryObj;
                    break;
                }
            }

            List<SavedQueryDVO> queryList = new ArrayList<SavedQueryDVO>();
            if (searchResults != null && searchResults.get(selectedQueryObj) != null) {
                SavedQueryDVO savedQuery = new SavedQueryDVO();
                savedQuery.setName(selectedQueryObj.getName());
                savedQuery.setResultCount(searchResults.get(selectedQueryObj).getResultForAllUrls().size());

                queryList.add(savedQuery);
                TransformedResultObjectWithContactInfo selectedQueryResult = searchResults.get(selectedQueryObj);
                Collection<ServiceURLInterface> failedURLS =
                        ExecuteQueryBizLogic.getFailedServiceUrls(selectedQueryResult.getFailedServiceUrl());
                session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedURLS != null ? failedURLS.size() : 0);
                session.setAttribute(Constants.FAILED_SERVICES, failedURLS);
                session.setAttribute(Constants.SEARCH_RESULTS_VIEW, ExecuteQueryBizLogic
                    .getSearchResultsView(selectedQueryResult.getResultForAllUrls(), selectedQueryResult
                        .getAllowedAttributes()));
                session.setAttribute(Constants.INFEASIBLE_URL, selectedQueryResult.getInFeasibleUrl());
                session.setAttribute(Constants.SAVED_QUERIES, queryList);
                session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                if (savedQuery.getResultCount() >= transformationMaxLimit
                        || executeQueryBizLogic.isProcessingFinished()) {
                    /* writer = response.getWriter();
                     response.setContentType("text/xml");
                     writer.write("StopAjax");
                     return null;*/
                    //response.setStatus(1000);
                    //System.out.println((Boolean) session.getAttribute("stopAjax"));
                    session.setAttribute(Constants.STOP_AJAX, true);
                }
            }

            session.setAttribute(Constants.SAVED_QUERIES, queryList);
            session.setAttribute(Constants.SEARCH_RESULTS, searchResults);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionMessage message =
                    new ActionMessage(
                            e.getMessage().equals(Constants.SERVICE_INSTANCES_NOT_CONFIGURED) ? "message.serviceinstancesnotconfigured"
                                    : "fatal.executequery.failure", e.getMessage());
            errors.add(Constants.FATAL_KYEWORD_SEARCH_FAILURE, message);
            saveErrors(request, errors);
            actionForward =
                    e.getMessage().equals(Constants.SERVICE_INSTANCES_NOT_CONFIGURED) ? Constants.FORWARD_HOME
                            : Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(actionForward);
    }
}
