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
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.common.queryengine.QueryExecutorPropertes;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SavedQueryDVO;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;

/**
 * Action for retrieving the query results
 * @author pallavi_mistry
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
        HttpSession session = request.getSession();
        try {

            SavedQueryBizLogic savedQueryBizLogic =
                    (SavedQueryBizLogic) session.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
            Long queryId = (Long) session.getAttribute(Constants.QUERY_ID);
            ICab2bQuery query = savedQueryBizLogic.getQueryById(queryId);
            int transformationMaxLimit = QueryExecutorPropertes.getUiResultLimit();
            IQueryResult<? extends IRecord> queryResult = null;

            QueryBizLogic queryBizLogic = (QueryBizLogic) session.getAttribute(Constants.QUERY_BIZ_LOGIC_OBJECT);
            //While the variable queryBizLogic is not set in the session by executeQueryAction
            // Processing image will be seen on the UI
            Writer writer;
            if (queryBizLogic == null) {
                return null;
            }

            Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = null;
            searchResults = queryBizLogic.getSearchResults();

            Set<ICab2bQuery> queries = searchResults.keySet();
            ICab2bQuery selectedQueryObj = null;

            //selectedQueryName will only be available in request at: onChange of drop down of keyword Search Results.
            String selectedQueryName = request.getParameter(Constants.SELECTED_QUERY_NAME);
            if (selectedQueryName == null) {
                selectedQueryName = (String) session.getAttribute(Constants.SELECTED_QUERY_NAME);
                if (selectedQueryName == null) {
                    if (query instanceof KeywordQuery) { //i.e. its the first time,results page is been opened.Thus, set it as first KSubquery.
                        List<SavedQueryDVO> savedQueries =
                                (List<SavedQueryDVO>) session.getAttribute(Constants.SAVED_QUERIES);
                        if (savedQueries != null || savedQueries.size() != 0) {
                            selectedQueryName = savedQueries.get(0).getName();
                        } else {
                            throw new RuntimeException("No keyword queries saved for selected application group.");
                        }
                    } else { //MMC query or form based query
                        selectedQueryName = query.getName();
                    }
                }
            }
            session.setAttribute(Constants.SELECTED_QUERY_NAME, selectedQueryName);

            if (query instanceof KeywordQuery) {
                for (ICab2bQuery queryObj : queries) {
                    if (queryObj.getName().equals(selectedQueryName + "#")) {
                        selectedQueryObj = queryObj;
                        break;
                    }
                }
            } else {
                for (ICab2bQuery queryObj : queries) {
                    if (queryObj.getName().equals(selectedQueryName)) {
                        selectedQueryObj = queryObj;
                        break;
                    }
                }
            }
            List<SavedQueryDVO> updatedSavedQueries = new ArrayList<SavedQueryDVO>();
            if (searchResults.get(selectedQueryObj) != null) {

                SavedQueryDVO savedQuery = new SavedQueryDVO();
                savedQuery.setName(selectedQueryObj.getName());

                TransformedResultObjectWithContactInfo selectedQueryResult = searchResults.get(selectedQueryObj);

                Collection<ServiceURLInterface> failedURLS = queryBizLogic.getFailedServiceUrls();
                if (failedURLS != null && failedURLS.size() == 0) {
                    failedURLS = null;
                }
                session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedURLS != null ? failedURLS.size() : 0);
                session.setAttribute(Constants.FAILED_SERVICES, failedURLS);

                List<List<SearchResultDVO>> searchResultsView =
                        queryBizLogic.getSearchResultsView(selectedQueryResult.getResultForAllUrls(),
                                                           selectedQueryResult.getAllowedAttributes());
                // if no of records are more than 100, retain only 100 records in the list to be shown on UI.
                if (searchResultsView != null) //for zero records or for failed URLs, searchResultsView will be null
                {
                    if (searchResultsView.size() > transformationMaxLimit) {
                        searchResultsView = searchResultsView.subList(0, transformationMaxLimit);
                    }
                    savedQuery.setResultCount(searchResultsView.size());
                }
                updatedSavedQueries.add(savedQuery);

                // populateSessionVariables as per the condition achieved of query
                boolean uiGotEnoughRecords = savedQuery.getResultCount() >= transformationMaxLimit;
                boolean queryFinished = queryBizLogic.isProcessingFinished();
                if (queryFinished) {
                    request.setAttribute(Constants.TRANSFORMATION_MAX_LIMIT, transformationMaxLimit);
                }
                updateSessionVariables(uiGotEnoughRecords, queryFinished, searchResults, updatedSavedQueries,
                                       searchResultsView, session);
            }
        } catch (Exception e) {
            session.setAttribute(Constants.STOP_AJAX, true); //if some exception occurs, Ajax from UI should stop
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

    /**
     * @param uiGotEnoughRecords
     * @param queryFinished
     * @param searchResults
     * @param queryList
     * @param searchResultsView
     * @param session
     */
    private void updateSessionVariables(boolean uiGotEnoughRecords, boolean queryFinished,
                                        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults,
                                        List<SavedQueryDVO> updatedSavedQueries,
                                        List<List<SearchResultDVO>> searchResultsView, HttpSession session) {
        if (uiGotEnoughRecords && queryFinished) {
            session.setAttribute(Constants.STOP_AJAX, true);
            session.setAttribute(Constants.UI_POPULATION_FINISHED, true);
            session.setAttribute(Constants.SEARCH_RESULTS_VIEW, searchResultsView);
            //session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
            session.setAttribute(Constants.SAVED_QUERIES, updatedSavedQueries);
        } else if (uiGotEnoughRecords && !queryFinished) {
            Boolean flag = (Boolean) session.getAttribute(Constants.UI_POPULATION_FINISHED);
            if (flag != null && flag) {
                //session.setAttribute(Constants.UI_POPULATION_FINISHED, true);
            } else {
                session.setAttribute(Constants.SEARCH_RESULTS_VIEW, searchResultsView);
                session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                session.setAttribute(Constants.SAVED_QUERIES, updatedSavedQueries);
            }
            // session.setAttribute(Constants.UI_POPULATION_FINISHED, true);
        } else if (!uiGotEnoughRecords && queryFinished) {
            session.setAttribute(Constants.STOP_AJAX, true);
            session.setAttribute(Constants.UI_POPULATION_FINISHED, true);
            session.setAttribute(Constants.SEARCH_RESULTS_VIEW, searchResultsView);
            session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
            session.setAttribute(Constants.SAVED_QUERIES, updatedSavedQueries);
        } else if (!uiGotEnoughRecords && !queryFinished) {
            session.setAttribute(Constants.SEARCH_RESULTS_VIEW, searchResultsView);
            session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
            session.setAttribute(Constants.SAVED_QUERIES, updatedSavedQueries);
        }
    }
}
