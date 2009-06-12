/**
 * 
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.FailedTargetURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.action.ExecuteQueryAction;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;
import edu.wustl.cab2bwebapp.util.Utility;

/**
 * Business logic for processing query results.
 * @author deepak_shingan 
 * @author chetan_pundhir
 */
public class ExecuteQueryBizLogic {

    private List<String> urlsForSelectedQueries = null;

    private List<Map<AttributeInterface, Object>> finalResult = null;

    private List<AttributeInterface> orderedAttributeList = null;

    private Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = null;

    private List<String> queryNameList = null;

    private Collection<ServiceURLInterface> failedServices = new HashSet<ServiceURLInterface>();

    private List<List<SearchResultDVO>> searchResultsView = null;

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExecuteQueryAction.class);

    /**
     * No argument constructor
     */
    public ExecuteQueryBizLogic() {

    }

    /**
     * Constructor
     * @param queries
     * @param proxy
     * @param user
     * @param modelGroupNames
     * @throws Exception
     */
    public ExecuteQueryBizLogic(
            Collection<ICab2bQuery> queries,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames) throws Exception {
        this(queries, proxy, null, user, modelGroupNames);
    }

    /**
     * @param queries
     * @param proxy
     * @param keyword
     * @param user
     * @param modelGroupNames
     * @throws Exception
     */
    public ExecuteQueryBizLogic(
            Collection<ICab2bQuery> queries,
            GlobusCredential proxy,
            String keyword,
            UserInterface user,
            String[] modelGroupNames) throws Exception {
        processResults(queries, proxy, keyword, user, modelGroupNames);
    }

    /**
     * @param queries
     * @param proxy
     * @param keyword
     * @param user
     * @param modelGroupNames
     * @throws Exception
     */
    private void processResults(Collection<ICab2bQuery> queries, GlobusCredential proxy, String keyword,
                                UserInterface user, String[] modelGroupNames) throws Exception {

        queryNameList = Utility.getQueryNameList(queries);
        List<String> urls = Utility.getUserConfiguredUrls(user, modelGroupNames);
        for (ICab2bQuery regularQuery : queries) {
            regularQuery.setOutputUrls(urls);
        }
        executeQuery(queries, proxy, keyword);
        Collection<FailedTargetURL> failedServiceURLs = new ArrayList<FailedTargetURL>();
        if (queryNameList != null && !queryNameList.isEmpty()) {
            String selectedQueryName = queryNameList.get(0);
            for (ICab2bQuery queryObj : queries) {
                if (queryObj.getName().equals(selectedQueryName)) {
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
            }
            if (!failedServiceURLs.isEmpty()) {
                for (FailedTargetURL failedurl : failedServiceURLs) {
                    ServiceURLInterface serviceurl = null;
                    try {
                        serviceurl =
                                new ServiceURLOperations().getServiceURLbyURLLocation(failedurl.getTargetUrl());
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
        }
    }

    /**
     * Method for executing query.
     * @param queries
     * @param proxy
     * @param keyword
     */
    private void executeQuery(Collection<ICab2bQuery> queries, GlobusCredential proxy, String keyword) {
        if (keyword == null || keyword.equals("")) {
            searchResults = new SearchQueryExecutor().execute(queries, proxy);
        } else {
            searchResults = new SearchQueryExecutor().execute(queries, keyword, proxy);
        }
    }

    /**
     * @return the urlsForSelectedQueries
     */
    public final List<String> getUrlsForSelectedQueries() {
        return urlsForSelectedQueries;
    }

    /**
     * @return the finalResult
     */
    public final List<Map<AttributeInterface, Object>> getFinalResult() {
        return finalResult;
    }

    /**
     * @return the orderedAttributeList
     */
    public final List<AttributeInterface> getOrderedAttributeList() {
        return orderedAttributeList;
    }

    /**
     * @return the failedSercives
     */
    public final Collection<ServiceURLInterface> getFailedServices() {
        return failedServices;
    }

    /**
     * @return the searchResults
     */
    public final Map<ICab2bQuery, TransformedResultObjectWithContactInfo> getSearchResults() {
        return searchResults;
    }

    /**
     * @return the queryNameList
     */
    public final List<String> getQueryNameList() {
        return queryNameList;
    }

    /**
     * @param finalResult
     * @param orderList
     * @return List<SearchResultDVO>
     */
    public final List<List<SearchResultDVO>> getSearchResultsView(List<Map<AttributeInterface, Object>> finalResult) {        
        if (finalResult != null) {
            searchResultsView = new ArrayList<List<SearchResultDVO>>();
            for (int i = 0; i < finalResult.size(); i++) {
                Map<AttributeInterface, Object> record = (Map<AttributeInterface, Object>) finalResult.get(i);
                Collection<AttributeInterface> keys =
                        orderedAttributeList == null ? record.keySet() : orderedAttributeList;
                ArrayList<SearchResultDVO> row = new ArrayList<SearchResultDVO>();
                for (AttributeInterface a : keys) {
                    SearchResultDVO searchResultDVO = new SearchResultDVO();
                    Object value = record.get(a);
                    String title = edu.wustl.cab2b.common.util.Utility.getFormattedString(a.getName());
                    searchResultDVO.setTitle(title);
                    searchResultDVO.setValue(value == null ? null : value.toString());
                    searchResultDVO
                        .setMedia((!title.equals("Point of Contact")
                                && !title.equals("Hosting Cancer Research Center") && !title
                            .equals("Contact e Mail")) ? "html"
                                + (!title.equals("Hosting Institution") ? " csv excel pdf" : "") : "csv excel pdf");
                    row.add(searchResultDVO);
                }
                searchResultsView.add(row);
            }
        }
        return searchResultsView;
    }
}