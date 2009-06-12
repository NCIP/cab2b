/**
 *
 */

package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.FailedTargetURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.actionform.KeywordSearchForm;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.ExecuteQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SavedQueryDVO;
import edu.wustl.cab2bwebapp.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_patil
 * @author chetan_pundhir
 *
 */
public class KeywordSearchAction extends Action {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(KeywordSearchAction.class);

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
            HttpSession session = request.getSession();
            Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = null;
            if (request.getQueryString() == null) {
                KeywordSearchForm keywordSearchForm = (KeywordSearchForm) form;

                SavedQueryBizLogic savedQueryBizLogic =
                        (SavedQueryBizLogic) session.getServletContext().getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
                GlobusCredential globusCredential =
                        (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);
                UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
                String[] modelGroupNames = keywordSearchForm.getModelGroups();

                Collection<ICab2bQuery> queries =
                        Utility.verifyModelGroups(modelGroupNames, globusCredential, savedQueryBizLogic);
                if (modelGroupNames == null) {
                    modelGroupNames = Utility.getModelGroups(globusCredential);
                }
                ExecuteQueryBizLogic executeQueryBizLogic =
                        new ExecuteQueryBizLogic(queries, globusCredential, keywordSearchForm.getKeyword(), user,
                                modelGroupNames);
                List<Map<AttributeInterface, Object>> finalResult = executeQueryBizLogic.getFinalResult();
                session.setAttribute(Constants.SEARCH_RESULTS_VIEW, executeQueryBizLogic
                    .getSearchResultsView(finalResult));
                searchResults = executeQueryBizLogic.getSearchResults();
                Collection<ICab2bQuery> allQueries = searchResults.keySet();
                List<SavedQueryDVO> queryList = new ArrayList<SavedQueryDVO>();

                for (ICab2bQuery queryObj : allQueries) {
                    SavedQueryDVO savedQuery = new SavedQueryDVO();
                    savedQuery.setName(queryObj.getName());
                    savedQuery.setResultCount(searchResults.get(queryObj).getResultForAllUrls().size());
                    queryList.add(savedQuery);
                }
                session.setAttribute(Constants.FAILED_SERVICES, executeQueryBizLogic.getFailedServices());
                session.setAttribute(Constants.FAILED_SERVICES_COUNT, executeQueryBizLogic.getFailedServices()
                    .size());
                session.setAttribute(Constants.SAVED_QUERIES, queryList);
                session
                    .setAttribute(Constants.SERVICE_INSTANCES, executeQueryBizLogic.getUrlsForSelectedQueries());
                session.setAttribute(Constants.SEARCH_RESULTS, searchResults);

            } else {
                String selectedQueryName = request.getParameter(Constants.SAVED_QUERIES);
                if (selectedQueryName != null) {
                    if (request.getHeader("Ajax-Call") == null) {
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
                        List<String> urlsForSelectedQueries = null;
                        List<Map<AttributeInterface, Object>> finalResult = null;
                        Collection<FailedTargetURL> failedServiceURLs = new ArrayList<FailedTargetURL>();
                        Collection<ServiceURLInterface> failedServices = new ArrayList<ServiceURLInterface>();
                        Collection<ICab2bQuery> queries = searchResults.keySet();
                        for (ICab2bQuery queryObj : queries) {
                            if (queryObj.getName().equals(selectedQueryName + "#")) {
                                urlsForSelectedQueries = queryObj.getOutputUrls();
                                urlsForSelectedQueries.add(0, "All Hosting Institutions");
                                TransformedResultObjectWithContactInfo transformedResultObj =
                                        searchResults.get(queryObj);
                                finalResult = transformedResultObj.getResultForAllUrls();
                                Collection<FailedTargetURL> failedUrl = transformedResultObj.getFailedServiceUrl();
                                if (failedUrl != null) {
                                    failedServiceURLs.addAll(failedUrl);
                                }
                                break;
                            }
                        }
                            if (!failedServiceURLs.isEmpty()) {
                                for (FailedTargetURL failedurl : failedServiceURLs) {
                                    ServiceURLInterface serviceurl = null;
                                    try {
                                        serviceurl =
                                                new ServiceURLOperations().getServiceURLbyURLLocation(failedurl
                                                    .getTargetUrl());
                                        if (serviceurl.getHostingCenter().contains("http")) {
                                            serviceurl.setHostingCenter("No Hosting Center Name Available.");
                                        }
                                    } catch (RemoteException e) {
                                        logger.info(e.getMessage(), e);
                                        throw new RuntimeException(e);
                                    }
                                    failedServices.add(serviceurl);
                                }
                            }
                        
                        session.setAttribute(Constants.SEARCH_RESULTS_VIEW, new ExecuteQueryBizLogic()
                            .getSearchResultsView(finalResult));
                        session.setAttribute(Constants.FAILED_SERVICES, failedServices);
                        session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedServices.size());
                        session.setAttribute(Constants.SERVICE_INSTANCES, urlsForSelectedQueries);
                        actionForward = Constants.FORWARD_SEARCH_RESULTS_PANEL;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.keywordsearch.failure");
            errors.add(Constants.FATAL_KYEWORD_SEARCH_FAILURE, error);
            saveErrors(request, errors);
            actionForward = Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(actionForward);
    }
}
