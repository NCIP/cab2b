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
import edu.wustl.cab2b.common.queryengine.QueryExecutorPropertes;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.ExecuteQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SavedQueryDVO;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;

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
    public synchronized ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                              HttpServletResponse response) throws IOException, ServletException {
        String actionForward = Constants.FORWARD_SEARCH_RESULTS_PANEL;
        try {
            HttpSession session = request.getSession();
            Writer writer;

            ExecuteQueryBizLogic executeQueryBizLogic =
                    (ExecuteQueryBizLogic) session.getAttribute(Constants.EXECUTE_QUERY_BIZ_LOGIC_OBJECT);
            if (executeQueryBizLogic == null) {
                writer = response.getWriter();
                response.setContentType("text/xml");
                String processingImage =
                        "<TABLE style='width:100%;'><TR><TD style='text-align:center;vertical-align:middle;'><IMG style='position:relative;top:-20' src='images/PageLoading.gif'/></TD></TR></TABLE>";
                writer.write(processingImage);
                return null;
            }

            SavedQueryBizLogic savedQueryBizLogic =
                    (SavedQueryBizLogic) session.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
            ICab2bQuery query =
                    savedQueryBizLogic.getQueryById(Long.parseLong(request.getParameter(Constants.QUERY_ID)));
            int transformationMaxLimit = QueryExecutorPropertes.getUiResultLimit();

            Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = null;
            //if UI_finished is true, transformer should not be invoked for re-transforming the same 100 records.Results should be taken from session
            //if (session.getAttribute(Constants.UI_POPULATION_FINISHED) == null) {
                searchResults = executeQueryBizLogic.getSearchResults(transformationMaxLimit);
            /*} else {
                searchResults =
                        (Map<ICab2bQuery, TransformedResultObjectWithContactInfo>) session
                            .getAttribute(Constants.SEARCH_RESULTS);
            }*/

            ICab2bQuery selectedQueryObj = null;
            Set<ICab2bQuery> queries = searchResults.keySet();
            for (ICab2bQuery queryObj : queries) {
                if (queryObj.getName().equals(query.getName())) {
                    selectedQueryObj = queryObj;
                    break;
                }
            }

            List<SavedQueryDVO> queryList = new ArrayList<SavedQueryDVO>();
            if (searchResults.get(selectedQueryObj) != null) {
                SavedQueryDVO savedQuery = new SavedQueryDVO();
                savedQuery.setName(selectedQueryObj.getName());
               // savedQuery.setResultCount(searchResults.get(selectedQueryObj).getResultForAllUrls().size());

                queryList.add(savedQuery);
                TransformedResultObjectWithContactInfo selectedQueryResult = searchResults.get(selectedQueryObj);
                Collection<ServiceURLInterface> failedURLS =
                        ExecuteQueryBizLogic.getFailedServiceUrls(selectedQueryResult.getFailedServiceUrl());

                if (failedURLS != null && failedURLS.size() == 0) {
                    failedURLS = null;
                }
                session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedURLS != null ? failedURLS.size() : 0);
                session.setAttribute(Constants.FAILED_SERVICES, failedURLS);

                session.setAttribute(Constants.INFEASIBLE_URL, selectedQueryResult.getInFeasibleUrl());

                List<List<SearchResultDVO>> val =
                        ExecuteQueryBizLogic.getSearchResultsView(selectedQueryResult.getResultForAllUrls(),
                                                                  selectedQueryResult.getAllowedAttributes());
                
                
                // if no of records are more than 100, retain only 100 records in the list to be shown on UI.
                if(val != null) //for zero records or for failed URLs, val will be null
                {
                    if(val.size()> transformationMaxLimit)
                    {
                        val = val.subList(0, transformationMaxLimit);
                    }
                    savedQuery.setResultCount(val.size());
                }
                
                boolean uiGotEnoughRecords = savedQuery.getResultCount() >= transformationMaxLimit;
                boolean queryFinished = executeQueryBizLogic.isProcessingFinished();

                if (uiGotEnoughRecords && queryFinished) {
                    session.setAttribute(Constants.STOP_AJAX, true);
                    session.setAttribute(Constants.UI_POPULATION_FINISHED, true);
                    //session.setAttribute(Constants.SEARCH_RESULTS_VIEW, val);
                    //session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                    //session.setAttribute(Constants.SAVED_QUERIES, queryList);
                } else if (uiGotEnoughRecords && !queryFinished) {
                    Boolean flag = (Boolean) session.getAttribute(Constants.UI_POPULATION_FINISHED);
                    if (flag != null && flag) {
                        //session.setAttribute(Constants.UI_POPULATION_FINISHED, true);
                    } else {
                        session.setAttribute(Constants.SEARCH_RESULTS_VIEW, val);
                        session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                        session.setAttribute(Constants.SAVED_QUERIES, queryList);
                    }
                   // session.setAttribute(Constants.UI_POPULATION_FINISHED, true);
                } else if (!uiGotEnoughRecords && queryFinished) {
                    session.setAttribute(Constants.STOP_AJAX, true);
                    session.setAttribute(Constants.UI_POPULATION_FINISHED, true);
                    session.setAttribute(Constants.SEARCH_RESULTS_VIEW, val);
                    session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                    session.setAttribute(Constants.SAVED_QUERIES, queryList);
                } else if (!uiGotEnoughRecords && !queryFinished) {
                    session.setAttribute(Constants.SEARCH_RESULTS_VIEW, val);
                    session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                    session.setAttribute(Constants.SAVED_QUERIES, queryList);
                }
            } else {
                session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                session.setAttribute(Constants.SAVED_QUERIES, queryList);
            }
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
