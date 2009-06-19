package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * This method executes all the given regular queries
     *
     * @param keyword value to be searched
     * @param globusCredential prxoy certificate of the user
     * @return
     */
    public Map<ICab2bQuery, TransformedResultObjectWithContactInfo> execute(
                                                                            Collection<ICab2bQuery> regularQueries,
                                                                            GlobusCredential globusCredential) {
        for (ICab2bQuery query : regularQueries) {
            if (query.isKeywordSearch()) {
                throw new IllegalStateException(query.getName() + " is not a regular query.");
            }
        }

        return executeAll(regularQueries, globusCredential);
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
    public Map<ICab2bQuery, TransformedResultObjectWithContactInfo> execute(
                                                                            Collection<ICab2bQuery> keywordQueries,
                                                                            String keyword,
                                                                            GlobusCredential globusCredential) {
        for (ICab2bQuery query : keywordQueries) {
            if (!query.isKeywordSearch()) {
                throw new IllegalStateException(query.getName() + " is not a keyword search query.");
            }
            insertKeyword(query, keyword);
        }

        return executeAll(keywordQueries, globusCredential);
    }

    private Map<ICab2bQuery, TransformedResultObjectWithContactInfo> executeAll(Collection<ICab2bQuery> queries,
                                                                                GlobusCredential globusCredential) {
        Map<ICab2bQuery, IQueryResult<? extends IRecord>> result =
                new HashMap<ICab2bQuery, IQueryResult<? extends IRecord>>();
        for (ICab2bQuery query : queries) {
            IQueryResult<? extends IRecord> queryResult =
                    new QueryExecutor(query, globusCredential).executeQuery();
            result.put(query, queryResult);
        }

        return transformResult(result);
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
    Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformResult(
                                                                             Map<ICab2bQuery, IQueryResult<? extends IRecord>> rawResults) {
        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformedResult =
                new HashMap<ICab2bQuery, TransformedResultObjectWithContactInfo>();

        Set<Map.Entry<ICab2bQuery, IQueryResult<? extends IRecord>>> rawResultSet = rawResults.entrySet();
        for (Map.Entry<ICab2bQuery, IQueryResult<? extends IRecord>> rawResult: rawResultSet) {
            ICab2bQuery query = rawResult.getKey();
            IQueryResult<? extends IRecord> originalResult = rawResult.getValue();
            
            TransformedResultObjectWithContactInfo resultObj =
                    new SpreadSheetResultTransformer(query, originalResult).transResultToSpreadSheetView();
            transformedResult.put(query, resultObj);
        }
        return transformedResult;
    }
}
