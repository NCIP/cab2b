/**
 *
 */

package edu.wustl.cab2bwebapp.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import edu.wustl.cab2bwebapp.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_patil
 *
 */
public class KeywordSearchAction extends Action {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(KeywordSearchAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (request.getMethod().equals("POST")) {
            request.getSession().removeAttribute(Constants.SEARCH_RESULTS);
            request.getSession().removeAttribute(Constants.SEARCH_RESULTS_VIEW);
        }
        String findForward = null;
        HttpSession session = request.getSession();
        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = null;
        if (session.getAttribute(Constants.SEARCH_RESULTS) != null) {

            String queryName = request.getParameter(Constants.SAVED_QUERIES);
            String selectedQueryName = null;
            if (queryName != null) {
                String[] queryNameTokens = queryName.split(" \\(");
                selectedQueryName = queryNameTokens[0];

                searchResults = (Map<ICab2bQuery, TransformedResultObjectWithContactInfo>) session.getAttribute(Constants.SEARCH_RESULTS);

                List<String> urlsForSelectedQueries = null;

                List<Map<AttributeInterface, Object>> finalResult = null;
                List<AttributeInterface> orderedAttributeList = null;
                Collection<FailedTargetURL> failedServiceURLs = new ArrayList<FailedTargetURL>();
                Collection<ServiceURLInterface> failedSercives = new ArrayList<ServiceURLInterface>();
                Collection<ICab2bQuery> queries = searchResults.keySet();

                for (ICab2bQuery queryObj : queries) {
                    if (queryObj.getName().equals(selectedQueryName)) {
                        logger.info("Selected Query Name:" + selectedQueryName);
                        urlsForSelectedQueries = queryObj.getOutputUrls();
                        urlsForSelectedQueries.add(0, "All Hosting Institutions");
                        TransformedResultObjectWithContactInfo transformedResultObj = searchResults.get(queryObj);
                        finalResult = transformedResultObj.getResultForAllUrls();
                        orderedAttributeList = transformedResultObj.getAllowedAttributes();
                        Collection<FailedTargetURL> failedUrl = transformedResultObj.getFailedServiceUrl();
                        if (failedUrl != null) {
                            failedServiceURLs.addAll(failedUrl);
                        }
                        break;
                    }
                    if (!failedServiceURLs.isEmpty()) {
                        for (FailedTargetURL failedurl : failedServiceURLs) {
                            ServiceURLInterface serviceurl = null;
                            try {
                                serviceurl = new ServiceURLOperations().getServiceURLbyURLLocation(failedurl.getTargetUrl());
                                if (serviceurl.getHostingCenter().contains("http")) {
                                    serviceurl.setHostingCenter("No Hosting Center Name Available.");
                                }
                            } catch (RemoteException e) {
                                logger.info(e.getMessage(), e);
                                throw new RuntimeException(e);
                            }
                            failedSercives.add(serviceurl);
                        }
                    }
                }
                if (finalResult != null && !finalResult.isEmpty()) {
                    session.setAttribute(Constants.SEARCH_RESULTS_VIEW, finalResult);
                }
                session.setAttribute(Constants.FAILED_SERVICES, failedSercives);
                session.setAttribute(Constants.SERVICE_INSTANCES, urlsForSelectedQueries);
            }
            findForward = selectedQueryName == null ? Constants.FORWARD_SEARCH_RESULTS
                    : Constants.FORWARD_SEARCH_RESULTS_PANEL;
        } else {

            KeywordSearchForm keywordSearchForm = (KeywordSearchForm) form;

            SavedQueryBizLogic savedQueryBizLogic = (SavedQueryBizLogic) session.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
            GlobusCredential globusCredential = (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);
            UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
            String[] modelGroupNames = keywordSearchForm.getModelGroups();

            session.removeAttribute(Constants.SEARCH_RESULTS);
            session.removeAttribute(Constants.SEARCH_RESULTS_VIEW);
            session.removeAttribute(Constants.FAILED_SERVICES_COUNT);
            session.removeAttribute(Constants.FAILED_SERVICES);
            session.removeAttribute(Constants.SAVED_QUERIES);
            session.removeAttribute(Constants.SERVICE_INSTANCES);

            try {

                Collection<ICab2bQuery> queries = Utility.verifyModelGroups(modelGroupNames, globusCredential,
                                                                            savedQueryBizLogic);
                if (modelGroupNames == null) {
                    modelGroupNames = Utility.getModelGroups(globusCredential);
                }
                ExecuteQueryBizLogic executeQueryBizLogic = new ExecuteQueryBizLogic(queries, globusCredential,
                        keywordSearchForm.getKeyword(), user, modelGroupNames);

                List<Map<AttributeInterface, Object>> finalResult = executeQueryBizLogic.getFinalResult();
                if (finalResult != null && !finalResult.isEmpty()) {
                    session.setAttribute(Constants.SEARCH_RESULTS_VIEW, finalResult);
                }
                searchResults = executeQueryBizLogic.getSearchResults();
                Collection<ICab2bQuery> allQueries = searchResults.keySet();
                List<String> queryNameList = new ArrayList<String>();

                for (ICab2bQuery query : allQueries) {
                    int count = searchResults.get(query).getResultForAllUrls().size();
                    StringBuffer queryName = new StringBuffer(query.getName()).append(" (").append(count).append(
                                                                                                                 ")");
                    queryNameList.add(queryName.toString());
                }
                int failedServicesCount = executeQueryBizLogic.getFailedSercives().size();

                session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedServicesCount);
                session.setAttribute(Constants.FAILED_SERVICES, executeQueryBizLogic.getFailedSercives());
                session.setAttribute(Constants.SAVED_QUERIES, queryNameList);
                session.setAttribute(Constants.SERVICE_INSTANCES, executeQueryBizLogic.getUrlsForSelectedQueries());
                session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                findForward = Constants.FORWARD_SEARCH_RESULTS;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                ActionErrors errors = new ActionErrors();
                ActionError error = new ActionError("fatal.keywordsearch.failure");
                errors.add(Constants.FATAL_KYEWORD_SEARCH_FAILURE, error);
                saveErrors(request, errors);
                findForward = Constants.FORWARD_FAILURE;
            }
        }
        return mapping.findForward(findForward);
    }
}
