/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IRule;

/**
 * This class executes the queries in groups and returns the consolidated result.
 *
 * @author chetan_patil
 */
public class SearchQueryExecutor {

    private List<QueryExecutor> queryExecutorList = null;

    /**
     * This method executes all the given regular queries
     *
     * @param keyword value to be searched
     * @param globusCredential proxy certificate of the user
     * @return
     */
    public void execute(Collection<ICab2bQuery> regularQueries, GlobusCredential globusCredential) {
        for (ICab2bQuery query : regularQueries) {
            if (query.isKeywordSearch()) {
                throw new IllegalStateException(query.getName() + " is not a regular query.");
            }
        }
        executeAll(regularQueries, globusCredential);
    }

    /**
     * This method executes all the given keyword queries. Before execution, the provided keyword is replaced 
     * with the values of the conditions in all the queries.
     * 
     * @param keywordQueries
     * @param keyword
     * @param globusCredential
     * @return
     */
    public void execute(Collection<ICab2bQuery> keywordQueries, String keyword, GlobusCredential globusCredential) {
        for (ICab2bQuery query : keywordQueries) {
            if (!query.isKeywordSearch()) {
                throw new IllegalStateException(query.getName() + " is not a keyword search query.");
            }
            insertKeyword(query, keyword);
        }
        //executeAll(keywordQueries, globusCredential);
        queryExecutorList = new ArrayList<QueryExecutor>(keywordQueries.size());
        for (ICab2bQuery query : keywordQueries) {
            QueryExecutor queryExecutor = new QueryExecutor(query, globusCredential);
            queryExecutorList.add(queryExecutor);
            queryExecutor.executeQuery();
        }
        for(QueryExecutor queryExecutor : queryExecutorList) {
            queryExecutor.getCompleteResults();
        }
    }

    private void executeAll(Collection<ICab2bQuery> queries, GlobusCredential globusCredential) {
        queryExecutorList = new ArrayList<QueryExecutor>(queries.size());
        for (ICab2bQuery query : queries) {
            QueryExecutor queryExecutor = new QueryExecutor(query, globusCredential);
            queryExecutorList.add(queryExecutor);
        }
        new Thread() {
            public void run() {
                for (QueryExecutor queryExecutor : queryExecutorList) {
                    queryExecutor.executeQuery();
                    queryExecutor.getCompleteResults();
                }
            }
        }.start();
    }

    /**
     * This method replaces the values of the conditions of given query with the provided keyword.
     * @param query
     * @param keyword
     */
    void insertKeyword(ICab2bQuery query, final String keyword) {
        for (IExpression expression : query.getConstraints()) {
            for (IExpressionOperand operand : expression) {
                if (operand instanceof IRule) {
                    IRule rule = (IRule) operand;
                    for (ICondition condition : rule) {
                        List<String> values = new ArrayList<String>();
                        values.add(keyword);

                        condition.setValues(values);
                    }
                }
            }
        }
    }

    /**
     * Returns results for given query name in spreadsheet data model format.
     * If result is already transformed into spreadsheet date-model and available in
     * <code>queryToTransformedResultMap</code> format returns it directly.
     * If result is not transformed into spreadsheet data-model format then iterate,
     * find result for "queryName", transform to data-model format and add to queryToTransformedResultMap.
     * @param queryName
     * @return List<Map<AttributeInterface, Object>>
     */
    public synchronized Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformResult(int transformationMaxLimit) {
        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformedResult =
                new HashMap<ICab2bQuery, TransformedResultObjectWithContactInfo>();

        for (QueryExecutor executor : queryExecutorList) {
            ICab2bQuery query = executor.getQuery();
            IQueryResult<? extends IRecord> originalResult = executor.getPartialResult();
            TransformedResultObjectWithContactInfo resultObj =
                    new SpreadSheetResultTransformer(query, originalResult).transResultToSpreadSheetView(transformationMaxLimit);
            transformedResult.put(query, resultObj);

        }
        return transformedResult;
    }

    public boolean isProcessingFinished() {
        for (QueryExecutor e : queryExecutorList) {
            if (!e.isProcessingFinished()) {
                return false;
            }
        }
        return true;
    }

    public String exportToCSV(long queryId, String filePath) throws IOException {
        String filename = null;
        for (QueryExecutor executor : queryExecutorList) {
            ICab2bQuery query = executor.getQuery();
            if(queryId == query.getId()) {
                IQueryResult<? extends IRecord> originalResult = executor.getCompleteResults();
                SpreadSheetResultTransformer transformer = new SpreadSheetResultTransformer(query, originalResult);
                filename= transformer.writeToCSV(filePath);
            }
        }
        return filename;
    }
}
