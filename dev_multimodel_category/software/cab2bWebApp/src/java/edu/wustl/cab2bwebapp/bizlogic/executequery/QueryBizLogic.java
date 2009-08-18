package edu.wustl.cab2bwebapp.bizlogic.executequery;

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
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.CaB2QueryExecutionHandler;
import edu.wustl.cab2b.server.queryengine.KeywordQueryExecutionHandler;
import edu.wustl.cab2b.server.queryengine.MMCQueryExecutionHandler;
import edu.wustl.cab2b.server.queryengine.QueryExecutionHandler;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.UserBackgroundQueries;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;

/**
 * @author pallavi_mistry
 *
 */
public class QueryBizLogic {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryBizLogic.class);

    private QueryExecutionHandler queryExecutionHandler = null;

    private  int transformationMaxLimit;

    private UserInterface user;

    /**
     * @param queryId
     * @param conditionstr
     * @param keyword
     * @param savedQueryBizLogic
     * @param user
     * @param proxy
     * @param modelGroupNames
     */
    public QueryBizLogic(
            Long queryId,
            String conditionstr,
            String keyword,
            SavedQueryBizLogic savedQueryBizLogic,
            UserInterface user,
            GlobusCredential proxy,
            String[] modelGroupNames) {
        this.user = user;
        this.transformationMaxLimit = QueryExecutorPropertes.getUiResultLimit();
        initializeQueryExecutionHandler(queryId, conditionstr, keyword, savedQueryBizLogic, user, proxy,
                                        modelGroupNames);
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
    private void initializeQueryExecutionHandler(Long queryId, String conditionstr, String keyword,
                                                 SavedQueryBizLogic savedQueryBizLogic, UserInterface user,
                                                 GlobusCredential proxy, String[] modelGroupNames) {
        ICab2bQuery query = savedQueryBizLogic.getQueryById(queryId);
        //KeyWord Query
        if (query instanceof KeywordQuery) {
            queryExecutionHandler =
                    new KeywordQueryExecutionHandler((KeywordQuery) query, proxy, user, modelGroupNames, keyword);
            queryExecutionHandler.execute();
        }
        //MMC Query
        else if (query instanceof MultiModelCategoryQuery) {
            setInputDataToQuery((MultiModelCategoryQuery) query, conditionstr);
            queryExecutionHandler =
                    new MMCQueryExecutionHandler((MultiModelCategoryQuery) query, proxy, user, modelGroupNames);
            queryExecutionHandler.execute();
        }
        //Form Based
        else {
            setInputDataToQuery(query, conditionstr);
            queryExecutionHandler = new CaB2QueryExecutionHandler(query, proxy, user, modelGroupNames);
            queryExecutionHandler.execute();
        }
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
     */
    public Map<ICab2bQuery, TransformedResultObjectWithContactInfo> getSearchResults() {

        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformedResult = null;
        if (queryExecutionHandler instanceof KeywordQueryExecutionHandler) {
            transformedResult = getKeywordSearchResults(transformationMaxLimit);
        } else {
            transformedResult = getSearchResults(transformationMaxLimit);
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
        Map<KeywordQuery, IQueryResult<? extends IRecord>> queryVsResultMap =
                ((KeywordQueryExecutionHandler) queryExecutionHandler).getQueryVsResultMap();
        for (KeywordQuery keywordQuery : queryVsResultMap.keySet()) {
            TransformedResultObjectWithContactInfo resultObj =
                    new SpreadSheetResultTransformer(keywordQuery, queryVsResultMap.get(keywordQuery))
                        .transResultToSpreadSheetView(transformationMaxLimit);
            transformedResult.put(keywordQuery, resultObj);
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
    public String exportToCSV(String filePath) throws IOException {
        String fileName = null;
        if (queryExecutionHandler instanceof KeywordQueryExecutionHandler) {
            fileName = keyWordExportToCSV(filePath);
        } else {
            SpreadSheetResultTransformer transformer =
                    new SpreadSheetResultTransformer(queryExecutionHandler.getQuery(), queryExecutionHandler
                        .getResult());
            fileName = transformer.writeToCSV(filePath);
        }
        updateDatabaseWithFileName(fileName);
        logger.info("File saved at location:" + fileName);
        return fileName;
    }

    /**
     * Updates Query Status properties in database with file name.  
     * @param fileName
     */
    private void updateDatabaseWithFileName(String fileName) {
        QueryStatus qStatus = queryExecutionHandler.getStatus();
        qStatus.setFileName(fileName);
        new QueryURLStatusOperations().updateQueryStatus(qStatus);
    }

    /**
     * Method for keyword query export. 
     * @param filePath
     * @return
     * @throws IOException 
     */
    private String keyWordExportToCSV(String filePath) throws IOException {
        Set<String> fileNames = new HashSet<String>();
        KeywordQueryExecutionHandler handler = (KeywordQueryExecutionHandler) queryExecutionHandler;
        Map<KeywordQuery, IQueryResult<? extends IRecord>> qVsResultMap = handler.getQueryVsResultMap();
        for (Map.Entry<KeywordQuery, IQueryResult<? extends IRecord>> keyValuePair : qVsResultMap.entrySet()) {
            SpreadSheetResultTransformer transformer =
                    new SpreadSheetResultTransformer(keyValuePair.getKey(), keyValuePair.getValue());
            String fileName = null;
            try {
                fileName = transformer.writeToCSV(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error while saving CSV file.", ErrorCodeConstants.IO_0001);
            }
            fileNames.add(fileName);
        }
        return zipAndSave(fileNames);
    }

    /**
     * Method which will add the existing file/files and zip them. 
     * @param fileNames
     * @return
     * @throws IOException 
     */
    private String zipAndSave(Set<String> fileNames) throws IOException {

        String zipFileName = this.user.getUserName() + "_" + System.currentTimeMillis() + ".zip";
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
            // Add ZIP entry to output stream.
            for (String inFilename : fileNames) {
                FileInputStream in = new FileInputStream(inFilename);
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
        logger.info("File saved at location:" + zipFileName);
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
        UserBackgroundQueries.getInstance().addBackgroundQuery(user, this);
    }
}
