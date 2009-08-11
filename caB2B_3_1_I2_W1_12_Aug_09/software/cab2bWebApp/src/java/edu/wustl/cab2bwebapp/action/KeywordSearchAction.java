package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2bwebapp.actionform.KeywordSearchForm;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.ExecuteQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SavedQueryDVO;
import edu.wustl.cab2bwebapp.util.Utility;

/**
 * @author chetan_patil
 * @author chetan_pundhir
 *
 */
public class KeywordSearchAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(KeywordSearchAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code to retrieve the 
     * query results for keyword search.
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
        String actionForward = Constants.FORWARD_SEARCH_RESULTS;
        try {
            ICab2bQuery selectedQueryObj = null;
            Collection<ICab2bQuery> queries = null;
            ExecuteQueryBizLogic executeQueryBizLogic = null;
            Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = null;

            HttpSession session = request.getSession();

            if (request.getQueryString() == null) {
                KeywordSearchForm keywordSearchForm = (KeywordSearchForm) form;
                SavedQueryBizLogic savedQueryBizLogic =
                        (SavedQueryBizLogic) session.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
                GlobusCredential globusCredential =
                        (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);
                UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
                String[] modelGroupNames = keywordSearchForm.getModelGroups();
                queries = Utility.verifyModelGroups(modelGroupNames, globusCredential, savedQueryBizLogic);
                if (modelGroupNames == null) {
                    modelGroupNames = Utility.getModelGroups(globusCredential);
                }

                try {
                    executeQueryBizLogic =
                            new ExecuteQueryBizLogic(queries, globusCredential, keywordSearchForm.getKeyword(),
                                    user, modelGroupNames);
                } catch (RuntimeException e) {
                    logger.error(e.getMessage(), e);
                    request.setAttribute("error", "Incorrect service instance configured for query");
                    return mapping.findForward(Constants.FORWARD_HOME);
                }
                Thread.sleep(200);
                //As each executor is invoked in a thread SearchQueryExecutor.executeAll()   
                //the next call executeQueryBizLogic.isProcessingFinished() returns immediately as TRUE 
                //This is because query execution hasn't even started. 
                //Adding sleep to allow at least one query executing thread to run
                int transformationMaxLimit = Integer.MAX_VALUE;
                while (!executeQueryBizLogic.isProcessingFinished()) {
                    Thread.sleep(100); // to ensure next lines are not executed before server finishes keyword query.
                }

                searchResults = executeQueryBizLogic.getSearchResults(transformationMaxLimit);

                Collection<ICab2bQuery> allQueries = searchResults.keySet();

                //Collect query details to be shown on result page combo box 
                List<SavedQueryDVO> queryList = new ArrayList<SavedQueryDVO>();
                for (ICab2bQuery queryObj : allQueries) {
                    SavedQueryDVO savedQuery = new SavedQueryDVO();
                    savedQuery.setName(queryObj.getName());
                    TransformedResultObjectWithContactInfo res = searchResults.get(queryObj);
                    if (res != null) {
                        //It occurs in case of failed URLs
                        savedQuery.setResultCount(res.getResultForAllUrls().size());
                    }
                    queryList.add(savedQuery);
                }
                Collections.sort(queryList, new Comparator() {
                    public int compare(Object a, Object b) {
                        return (((SavedQueryDVO) b).getResultCount() - ((SavedQueryDVO) a).getResultCount());
                    }
                });
                session.setAttribute(Constants.SAVED_QUERIES, queryList);

                Iterator<ICab2bQuery> iter = allQueries.iterator();
                String highestResultCountQueryName = queryList.get(0).getName();

                //select the query object with highest number of results 
                while (iter.hasNext()) {
                    selectedQueryObj = iter.next();
                    if (selectedQueryObj.getName().equals(highestResultCountQueryName + "#")) {
                        break;
                    }
                }
                //TODO once the URL list is enabled, this should be uncommented.
                //List<String> urlsForSelectedQueries = selectedQueryObj.getOutputUrls();
                List<String> urlsForSelectedQueries =
                        new ArrayList<String>(1 + selectedQueryObj.getOutputUrls().size());
                urlsForSelectedQueries.add(Constants.ALL_HOSTING_INSTITUTIONS);
                urlsForSelectedQueries.addAll(selectedQueryObj.getOutputUrls());
                if (searchResults.get(selectedQueryObj) != null) {
                    TransformedResultObjectWithContactInfo selectedQueryResult =
                            searchResults.get(selectedQueryObj);
                    Collection<ServiceURLInterface> failedURLS =
                            ExecuteQueryBizLogic.getFailedServiceUrls(selectedQueryResult.getFailedServiceUrl());

                    if (failedURLS != null && failedURLS.size() == 0) {
                        failedURLS = null;
                    }
                    session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedURLS != null ? failedURLS.size()
                            : 0);
                    session.setAttribute(Constants.FAILED_SERVICES, failedURLS);
                    session.setAttribute(Constants.SERVICE_INSTANCES, urlsForSelectedQueries);
                    session.setAttribute(Constants.SEARCH_RESULTS_VIEW, ExecuteQueryBizLogic
                        .getSearchResultsView(selectedQueryResult.getResultForAllUrls(), selectedQueryResult
                            .getAllowedAttributes()));
                }
                session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
            } else {
                String selectedQueryName = request.getParameter(Constants.SAVED_QUERIES);
                if (selectedQueryName != null) {
                    if (request.getHeader(Constants.AJAX_CALL) == null) {
                        List<SavedQueryDVO> searchResult =
                                (List<SavedQueryDVO>) session.getAttribute(Constants.SAVED_QUERIES);
                        for (int i = 0; i < searchResult.size(); i++) {
                            SavedQueryDVO savedQuery = searchResult.get(i);
                            if (selectedQueryName.equals(savedQuery.getName())) {
                                savedQuery.setSelected(true);
                            } else {
                                savedQuery.setSelected(false);
                            }
                        }
                    } else {
                        searchResults =
                                (Map<ICab2bQuery, TransformedResultObjectWithContactInfo>) session
                                    .getAttribute(Constants.SEARCH_RESULTS);
                        queries = searchResults.keySet();
                        for (ICab2bQuery queryObj : queries) {
                            if (queryObj.isKeywordSearch() && queryObj.getName().equals(selectedQueryName + "#")) {
                                selectedQueryObj = queryObj;
                                break;
                            } else if (queryObj.getName().equals(selectedQueryName)) {
                                selectedQueryObj = queryObj;
                                break;
                            }
                        }
                        //TODO once the URL list is enabled, this should be uncommented.
                        //List<String> urlsForSelectedQueries = selectedQueryObj.getOutputUrls();
                        List<String> urlsForSelectedQueries =
                                new ArrayList<String>(1 + selectedQueryObj.getOutputUrls().size());
                        urlsForSelectedQueries.add(Constants.ALL_HOSTING_INSTITUTIONS);
                        urlsForSelectedQueries.addAll(selectedQueryObj.getOutputUrls());
                        TransformedResultObjectWithContactInfo selectedQueryResult =
                                searchResults.get(selectedQueryObj);
                        session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                        session.setAttribute(Constants.SERVICE_INSTANCES, urlsForSelectedQueries);
                        if (selectedQueryResult != null) {
                            Collection<ServiceURLInterface> failedURLS =
                                    ExecuteQueryBizLogic.getFailedServiceUrls(selectedQueryResult
                                        .getFailedServiceUrl());
                            if (failedURLS != null && failedURLS.size() == 0) {
                                failedURLS = null;
                            }
                            session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedURLS != null ? failedURLS
                                .size() : 0);
                            session.setAttribute(Constants.FAILED_SERVICES, failedURLS);
                            session.setAttribute(Constants.SEARCH_RESULTS_VIEW, ExecuteQueryBizLogic
                                .getSearchResultsView(selectedQueryResult.getResultForAllUrls(),
                                                      selectedQueryResult.getAllowedAttributes()));
                        } else {
                            session.setAttribute(Constants.SEARCH_RESULTS_VIEW, null);
                        }

                        actionForward = Constants.FORWARD_SEARCH_RESULTS_PANEL;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionMessage message =
                    new ActionMessage(
                            e.getMessage().equals(Constants.SERVICE_INSTANCES_NOT_CONFIGURED) ? "message.serviceinstancesnotconfigured"
                                    : "fatal.keywordsearch.failure", e.getMessage());
            errors.add(Constants.FATAL_KYEWORD_SEARCH_FAILURE, message);
            saveErrors(request, errors);
            actionForward =
                    e.getMessage().equals(Constants.SERVICE_INSTANCES_NOT_CONFIGURED) ? Constants.FORWARD_HOME
                            : Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(actionForward);
    }
}
