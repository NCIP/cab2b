/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IRule;

/**
 * @author pallavi_mistry
 *
 */
public class KeywordQueryExecutionHandler extends QueryExecutionHandler<KeywordQuery> {
    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(KeywordQueryExecutionHandler.class);

    private String keyword;

    private Map<ICab2bQuery, IQueryResult<? extends IRecord>> queryVsResultMap;

    /**
     * @param queries
     * @param proxy
     * @param user
     * @param modelGroupNames
     */
    public KeywordQueryExecutionHandler(
            KeywordQuery query,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames,
            String keyword) {
        super(query, proxy, user, modelGroupNames);
        this.keyword = keyword;
        preProcessQuery();

        // Initilizing executor list.
        Collection<ICab2bQuery> subQueries = this.query.getSubQueries();
        this.queryExecutorsList = new ArrayList<QueryExecutor>(subQueries.size());
        for (ICab2bQuery subQuery : subQueries) {
            QueryExecutor queryExecutor = new QueryExecutor(subQuery, proxy);
            queryExecutorsList.add(queryExecutor);
        }

        this.queryVsResultMap = new HashMap<ICab2bQuery, IQueryResult<? extends IRecord>>(subQueries.size());
    }

    /**
     * For keyword query, one needs to call getQueryVsResultMap() instead of getResult()
     * because, we dont have consolidated result available for keyword query.
     *  getResult() will return null.
     */
    @Override
    public IQueryResult<? extends IRecord> getResult() {
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#execute()
     */
    public void execute() {
        super.execute();
        status.setQueryConditions("Keyword(Search for)" + keyword);
    }

    /**
     * For keyword query, one needs to call getQueryVsResultMap() instead of getResult()
     * because, we dont have consolidated result available for keyword query.
     * @return
     */
    public Map<ICab2bQuery, IQueryResult<? extends IRecord>> getQueryVsResultMap() {
        for (QueryExecutor executor : queryExecutorsList) {
            IQueryResult<? extends IRecord>  result = executor.getResult();
            ICab2bQuery query = (ICab2bQuery) executor.getQuery();
            queryVsResultMap.put(query, result);
        }
        updateStatus();
        return queryVsResultMap;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#postProcessResults()
     */
    @Override
    protected void postProcessResults() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#preProcessQuery()
     */
    @Override
    protected void preProcessQuery() throws RuntimeException {
        Collection<ICab2bQuery> queries = this.query.getSubQueries();
        for (ICab2bQuery query : queries) {
            insertKeyword(query, this.keyword);
        }
        QueryExecutorUtil.setOutputURLs(query, proxy, user, modelGroupNames);
    }

    /**
     * This method replaces the values of the conditions of given query with the provided keyword.
     * @param query
     */
    private void insertKeyword(ICab2bQuery query, String keyword) {
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

}
