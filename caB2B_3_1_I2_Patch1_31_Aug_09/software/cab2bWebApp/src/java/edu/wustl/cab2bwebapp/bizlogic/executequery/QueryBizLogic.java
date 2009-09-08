package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.QueryExecutorPropertes;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.CaB2BQueryExecutionHandler;
import edu.wustl.cab2b.server.queryengine.KeywordQueryExecutionHandler;
import edu.wustl.cab2b.server.queryengine.MMCQueryExecutionHandler;
import edu.wustl.cab2b.server.queryengine.QueryExecutionHandler;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.bizlogic.UserBackgroundQueries;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;

/**
 * @author pallavi_mistry
 *
 */
public class QueryBizLogic {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryBizLogic.class);

    private QueryExecutionHandler queryExecutionHandler = null;

    private int transformationMaxLimit;

    private UserInterface user;

    /**
     * @param query
     * @param conditionstr
     * @param keyword
     * @param user
     * @param proxy
     * @param modelGroupNames
     */
    public QueryBizLogic(
            ICab2bQuery query,
            String conditionstr,
            String keyword,
            UserInterface user,
            GlobusCredential proxy,
            String[] modelGroupNames) {
        this.user = user;
        this.transformationMaxLimit = QueryExecutorPropertes.getUiResultLimit();
        initializeQueryExecutionHandler(query, conditionstr, keyword, user, proxy, modelGroupNames);
    }

    /**
     * @param queryId
     * @param conditionstr
     * @param keyword
     * @param savedQueryBizLogic
     * @param user
     * @param proxy
     * @param modelGroupNames
     */
    private void initializeQueryExecutionHandler(ICab2bQuery query, String conditionstr, String keyword,
                                                 UserInterface user, GlobusCredential proxy,
                                                 String[] modelGroupNames) {
        if (query instanceof KeywordQuery) {//KeyWord Query
            queryExecutionHandler =
                    new KeywordQueryExecutionHandler((KeywordQuery) query, proxy, user, modelGroupNames, keyword);
        } else if (query instanceof MultiModelCategoryQuery) {//MMC Query
            setInputDataToQuery((MultiModelCategoryQuery) query, conditionstr);
            queryExecutionHandler =
                    new MMCQueryExecutionHandler((MultiModelCategoryQuery) query, proxy, user, modelGroupNames);
        } else {//Form Based
            setInputDataToQuery(query, conditionstr);
            queryExecutionHandler = new CaB2BQueryExecutionHandler(query, proxy, user, modelGroupNames);
        }
        queryExecutionHandler.execute();
    }

    /**
     * @return
     */
    public boolean isProcessingFinished() {
        return queryExecutionHandler.isProcessingFinished();
    }

    /**
     * Sets conditionString to the query. It is called before query is sent to execution handler for execution.
     * @param query
     * @param session
     * @throws RuntimeException
     */
    public void setInputDataToQuery(ICab2bQuery query, String conditionstr) throws RuntimeException {
        if (conditionstr == null) {
            conditionstr = "";
        }
        String errorMessage =
                new QueryUpdateBizLogic().setInputDataToQuery(conditionstr, query.getConstraints(), null, query);
        if (!errorMessage.equals("")) {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * @return
     * @throws IOException 
     */
    public Map<ICab2bQuery, TransformedResultObjectWithContactInfo> getSearchResults() throws IOException {

        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformedResult = null;
        if (queryExecutionHandler instanceof KeywordQueryExecutionHandler) {
            transformedResult = getKeywordSearchResults(transformationMaxLimit);
        } else {
            transformedResult = getSearchResults(transformationMaxLimit);
        }
        QueryStatus qs = getStatus();
        if (queryExecutionHandler.isExecuteInBackground()
                && (qs.getStatus().equals(AbstractStatus.Complete) || qs.getStatus()
                    .equals(AbstractStatus.Complete_With_Error)) && isProcessingFinished()) {
            exportToCSV();
        }
        return transformedResult;
    }

    /**
     * @param transformationMaxLimit
     * @return
     */
    private Map<ICab2bQuery, TransformedResultObjectWithContactInfo> getKeywordSearchResults(
                                                                                             int transformationMaxLimit) {
        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformedResult =
                new HashMap<ICab2bQuery, TransformedResultObjectWithContactInfo>();
        Map<ICab2bQuery, IQueryResult<? extends IRecord>> queryVsResultMap =
                ((KeywordQueryExecutionHandler) queryExecutionHandler).getQueryVsResultMap();
        for (ICab2bQuery keywordSubQuery : queryVsResultMap.keySet()) {
            TransformedResultObjectWithContactInfo resultObj =
                    new SpreadSheetResultTransformer(keywordSubQuery, queryVsResultMap.get(keywordSubQuery))
                        .transResultToSpreadSheetView(transformationMaxLimit);
            transformedResult.put(keywordSubQuery, resultObj);
        }
        return transformedResult;
    }

    /**
     * @param transformationMaxLimit
     * @return
     */
    private Map<ICab2bQuery, TransformedResultObjectWithContactInfo> getSearchResults(int transformationMaxLimit) {
        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformedResult =
                new HashMap<ICab2bQuery, TransformedResultObjectWithContactInfo>();
        TransformedResultObjectWithContactInfo resultObj =
                new SpreadSheetResultTransformer(queryExecutionHandler.getQuery(), queryExecutionHandler
                    .getResult()).transResultToSpreadSheetView(transformationMaxLimit);
        transformedResult.put(queryExecutionHandler.getQuery(), resultObj);
        return transformedResult;
    }

    /**
     * @param finalResult
     * @param orderList
     * @return List<SearchResultDVO>
     */
    public static final List<List<SearchResultDVO>> getSearchResultsView(
                                                                         List<Map<AttributeInterface, Object>> finalResult,
                                                                         List<AttributeInterface> orderedAttributeList) {
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
                    searchResultDVO.setMedia((!title.equals(Constants.POINT_OF_CONTACT)
                            && !title.equals(Constants.HOSTING_CANCER_RESEARCH_CENTER)
                            && !title.equals(edu.wustl.cab2b.common.util.Utility
                                .getFormattedString(Constants.CONTACT_EMAIL)) && !title
                        .equals(Constants.MODEL_NAME)) ? "html"
                            + (!title.equals(Constants.HOSTING_INSTITUTION) ? " csv excel pdf" : "")
                            : "csv excel pdf");
                    row.add(searchResultDVO);
                }
                searchResultsView.add(row);
            }
        }
        return searchResultsView;
    }

    /**
     * Returns failed URS for the query.
     * @return
     */
    public final Collection<ServiceURLInterface> getFailedServiceUrls() {
        Collection<ServiceURLInterface> failedServices = null;
        Set<String> failedurls = queryExecutionHandler.getFailedUrls();
        if (failedurls != null) {
            failedServices = new HashSet<ServiceURLInterface>();
            for (String failedurl : failedurls) {
                ServiceURLInterface serviceurl = new ServiceURLOperations().getServiceURLbyURLLocation(failedurl);
                if (serviceurl.getHostingCenter().contains("http")) {
                    serviceurl.setHostingCenter("No Hosting Center Name Available.");
                }
                failedServices.add(serviceurl);
            }
        }
        return failedServices;
    }

    /**
     * @param filePath
     * @return
     * @throws IOException
     */
    public String exportToCSV() throws IOException {
        Set<String> fileNames = null;
        if (queryExecutionHandler instanceof KeywordQueryExecutionHandler) {
            fileNames = keyWordExportToCSV();
        } else {
            fileNames = new HashSet<String>(1);
            SpreadSheetResultTransformer transformer =
                    new SpreadSheetResultTransformer(queryExecutionHandler.getQuery(), queryExecutionHandler
                        .getResult());
            String fileName = transformer.writeToCSV();
            queryExecutionHandler.getStatus().setFileName(fileName);
            CsvWriter.addMetadataToFile(queryExecutionHandler.getStatus());
            fileNames.add(fileName);
        }

        String fileName = zipAndSave(fileNames);
        updateDatabaseWithFileName(fileName, queryExecutionHandler.getStatus());
        return fileName;
    }

    /**
     * Updates Query Status properties in database with file name.  
     * @param fileName
     */
    private void updateDatabaseWithFileName(String fileName, QueryStatus qStatus) {
        qStatus.setFileName(fileName);
        new QueryURLStatusOperations().updateQueryStatus(qStatus);
    }

    /**
     * Method for keyword query export. 
     * @param filePath
     * @return
     * @throws IOException 
     */
    private Set<String> keyWordExportToCSV() throws IOException {
        Set<String> fileNames = new HashSet<String>();
        KeywordQueryExecutionHandler handler = (KeywordQueryExecutionHandler) queryExecutionHandler;
        Map<ICab2bQuery, IQueryResult<? extends IRecord>> qVsResultMap = handler.getQueryVsResultMap();
        for (Map.Entry<ICab2bQuery, IQueryResult<? extends IRecord>> keyValuePair : qVsResultMap.entrySet()) {
            ICab2bQuery subQuery = keyValuePair.getKey();
            SpreadSheetResultTransformer transformer =
                    new SpreadSheetResultTransformer(keyValuePair.getKey(), keyValuePair.getValue());
            String fileName = null;
            try {
                fileName = transformer.writeToCSV();
                Set<QueryStatus> subQueryStatus = queryExecutionHandler.getStatus().getChildrenQueryStatus();
                for (QueryStatus queryStatus : subQueryStatus) {
                    if (queryStatus.getQuery().equals(subQuery)) {
                        updateDatabaseWithFileName(fileName, queryStatus);
                        CsvWriter.addMetadataToFile(queryStatus);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error while saving CSV file.", ErrorCodeConstants.IO_0001);
            }
            fileNames.add(fileName);
        }
        return fileNames;
    }

    /**
     * Method which will add the existing file/files and zip them. 
     * @param fileNames
     * @return
     * @throws IOException 
     */
    private String zipAndSave(Set<String> fileNames) throws IOException {

        String zipFileName = System.currentTimeMillis() + ".zip";
        try {
            ZipOutputStream out =
                    new ZipOutputStream(new FileOutputStream(UserBackgroundQueries.EXPORT_CSV_DIR + File.separator
                            + zipFileName));
            // Add ZIP entry to output stream.
            for (String inFilename : fileNames) {
                FileInputStream in =
                        new FileInputStream(UserBackgroundQueries.EXPORT_CSV_DIR + File.separator + inFilename);
                out.putNextEntry(new ZipEntry(inFilename));
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving CSV ZIP file.", ErrorCodeConstants.IO_0001);
        }
        return zipFileName;
    }

    /**
     * @return QueryStatus
     */
    public QueryStatus getStatus() {
        return queryExecutionHandler.getStatus();
    }

    /**
     * Adds currently running query for background execution for the current user and updating properties in database.
     */
    public void addQueryForExecuteInBackground() {
        queryExecutionHandler.setExecuteInBackground(true);
        UserBackgroundQueries.getInstance().addBackgroundQuery(user, this);
    }
}