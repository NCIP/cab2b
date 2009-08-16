/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatusImpl;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
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

    private String keyword;
    private Map<KeywordQuery, IQueryResult<? extends IRecord>> queryVsResultMap;

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
        this.queryExecutorsList = new ArrayList<QueryExecutor>(query.getSubQueries().size());
        this.queryVsResultMap = new HashMap<KeywordQuery, IQueryResult<? extends IRecord>>(query.getSubQueries().size());
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#executeQuery()
     */
    @Override
    protected void executeQuery() {
        // TODO Auto-generated method stub
        Collection<ICab2bQuery> subQueries = this.query.getSubQueries();
        for (ICab2bQuery query : subQueries) {
            QueryExecutor queryExecutor = new QueryExecutor(query, proxy);
            queryExecutorsList.add(queryExecutor);
        }
        new Thread() {
            public void run() {
                for (QueryExecutor queryExecutor : queryExecutorsList) {
                    queryExecutor.executeQuery();
                }
            }
        }.start();
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getResult()
     */
    @Override
    public IQueryResult<? extends IRecord> getResult() {
        return null;
    }
    
    /**
     * @return
     */
    public Map<KeywordQuery, IQueryResult<? extends IRecord>> getQueryVsResultMap() {
        for (QueryExecutor executor : queryExecutorsList) {
            IQueryResult<? extends IRecord> result = executor.getPartialResult();
            KeywordQuery query = (KeywordQuery)executor.getQuery();
            queryVsResultMap.put(query, result);
        }
        return queryVsResultMap;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getStatus()
     */
    @Override
    public QueryStatusImpl getStatus() {
        // TODO Auto-generated method stub
        return null;
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
    protected void preProcessQuery() throws RuntimeException{
        // TODO Auto-generated method stub
        Collection<ICab2bQuery> queries = this.query.getSubQueries();
        for (ICab2bQuery query : queries) {
            insertKeyword(query, this.keyword);
        }
        QueryExecutorUtil.insertURLConditions(queries, proxy, user, modelGroupNames);
    }
    
    public KeywordQuery getQuery() {
        return query;
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
