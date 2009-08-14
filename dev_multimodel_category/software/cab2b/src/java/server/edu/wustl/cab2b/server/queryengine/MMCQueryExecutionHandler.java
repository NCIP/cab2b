/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * @author chetan_patil
 *
 */
public class MMCQueryExecutionHandler extends QueryExecutionHandler<MultiModelCategoryQuery> {
    private MMCQueryResultConflator resultConflator;

    private Collection<QueryExecutor> queryExecutors;

    private IQueryResult<? extends IRecord> result;

    /**
     * @param queries
     * @param proxy
     * @param user
     * @param modelGroupNames
     */
    public MMCQueryExecutionHandler(
            MultiModelCategoryQuery query,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames) {
        super(query, proxy, user, modelGroupNames);
        resultConflator = new MMCQueryResultConflator(query);
        queryExecutors = new ArrayList<QueryExecutor>(query.getSubQueries().size());
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#executeQuery()
     */
    @Override
    protected void executeQuery() {
        Collection<ICab2bQuery> subQueries = query.getSubQueries();
        for (ICab2bQuery query : subQueries) {
            QueryExecutor queryExecutor = new QueryExecutor(query, proxy);
            queryExecutors.add(queryExecutor);
        }

        new Thread() {
            public void run() {
                for (QueryExecutor queryExecutor : queryExecutors) {
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
        postProcessResults();
        return result;
    }
  
    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getStatus()
     */
    @Override
    public QueryStatus getStatus() {
        for (QueryExecutor queryExecutor : queryExecutors) {

        }
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#postProcessResults()
     */
    @Override
    protected void postProcessResults() {
        Collection<IQueryResult<? extends IRecord>> queryResults =
                new ArrayList<IQueryResult<? extends IRecord>>();
        for (QueryExecutor queryExecutor : queryExecutors) {
            queryResults.add(queryExecutor.getPartialResult());
        }

        result = resultConflator.conflate(queryResults);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#preProcessQuery()
     */
    @Override
    protected void preProcessQuery() {
        new MultimodelCategoryQueryPreprocessor().preprocessQuery(query);
    }

}
