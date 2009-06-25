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

    private Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = null;

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExecuteQueryAction.class);

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
        List<String> urls = Utility.getUserConfiguredUrls(user, modelGroupNames);
        for (ICab2bQuery regularQuery : queries) {
            regularQuery.setOutputUrls(urls);
        }
        executeQuery(queries, proxy, keyword);
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
     * @return the searchResults
     */
    public final Map<ICab2bQuery, TransformedResultObjectWithContactInfo> getSearchResults() {
        return searchResults;
    }

    /**
     * @return the finalResult
     */
    public final List<Map<AttributeInterface, Object>> getResultForAllUrls(ICab2bQuery selectedQuery) {
        return searchResults.get(selectedQuery).getResultForAllUrls();
    }

    /**
     * @param failedUrl
     * @return
     */
    public static final Collection<ServiceURLInterface> getFailedServiceUrls(Collection<FailedTargetURL> failedUrl) {
        Collection<ServiceURLInterface> failedServices = null;
        if (failedUrl != null) {
            failedServices = new HashSet<ServiceURLInterface>();
            for (FailedTargetURL failedurl : failedUrl) {
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
                failedServices.add(serviceurl);
            }
        }
        return failedServices;
    }

    /**
     * @param finalResult
     * @param orderList
     * @return List<SearchResultDVO>
     */
    public static final List<List<SearchResultDVO>> getSearchResultsView(
                                                                         List<Map<AttributeInterface, Object>> finalResult,
                                                                         Collection<AttributeInterface> orderedAttributeList) {
        List<List<SearchResultDVO>> searchResultsView = null;
        if (finalResult != null && finalResult.size() > 0) {
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
                    searchResultDVO.setValue(value == null ? null : value);
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