/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

/**
 * @author pallavi_mistry
 *
 */
public class KeywordQueryExecutionHandler extends QueryExecutionHandler<KeywordQuery> {

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
        this.queryExecutorsList = new ArrayList<QueryExecutor>(query.getSubQueries().size());
        this.queryVsResultMap = new HashMap<ICab2bQuery, IQueryResult<? extends IRecord>>(query.getSubQueries().size());
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#executeQuery()
     */
    @Override
    protected void executeQuery() {
        // TODO Auto-generated method stub
        Collection<ICab2bQuery> subQueries = new ArrayList<ICab2bQuery>();
        subQueries.addAll(this.query.getSubQueries());
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
        for (QueryExecutor executor : queryExecutorsList) {
            IQueryResult<? extends IRecord> result = executor.getPartialResult();
            ICab2bQuery query = executor.getQuery();
            queryVsResultMap.put(query, result);
        }
        return null;
    }
    
    /**
     * @return
     */
    public Map<ICab2bQuery, IQueryResult<? extends IRecord>> getQueryVsResultMap() {
        getResult();
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
            QueryExecutorUtil.insertKeyword(query, this.keyword);
        }
        QueryExecutorUtil.insertURLConditions(queries, proxy, user, modelGroupNames);
    }
}