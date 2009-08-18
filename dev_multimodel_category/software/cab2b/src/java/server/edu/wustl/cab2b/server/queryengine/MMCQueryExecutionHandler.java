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
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;

/**
 * @author chetan_patil
 *
 */
public class MMCQueryExecutionHandler extends QueryExecutionHandler<MultiModelCategoryQuery> {
    private MMCQueryResultConflator resultConflator;

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
        this.queryExecutorsList = new ArrayList<QueryExecutor>(query.getSubQueries().size());
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#executeQuery()
     */
    @Override
    protected void executeQuery() {
        Collection<ICab2bQuery> subQueries = query.getSubQueries();
        for (ICab2bQuery query : subQueries) {
            QueryExecutor queryExecutor = new QueryExecutor(query, proxy);
            this.queryExecutorsList.add(queryExecutor);
        }
        //making it serial temporarily
        for (QueryExecutor queryExecutor : queryExecutorsList) {
            queryExecutor.executeQuery();
        }
        /* new Thread() {
             public void run() {
                 
             }
         }.start();*/
    }

    /**
     * It will PostProcess the results firstly (i.e. consolidate in a single result) and then will return single result. 
     * @return IQueryResult<? extends IRecord>
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
        for (QueryExecutor queryExecutor : queryExecutorsList) {

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
        for (QueryExecutor queryExecutor : queryExecutorsList) {
            IQueryResult<? extends IRecord> subQueryResult = null;
            //when query is sent to execute in background, then only query status (count, etc) will be updated in database.
            //getResult() will update the DB, while getPartialResult() will only update the in-memory object.
            if (isExecuteInBackground()) {
                subQueryResult = queryExecutor.getResult();
            } else {
                subQueryResult = queryExecutor.getPartialResult();
            }
            if (subQueryResult != null) {
                queryResults.add(subQueryResult);
            }
        }
        result = resultConflator.conflate(queryResults);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#preProcessQuery()
     */
    @Override
    protected void preProcessQuery() {
        new MultimodelCategoryQueryPreprocessor().preprocessQuery(query);
        QueryExecutorUtil.insertURLConditions(query, proxy, user, modelGroupNames);
    }
}
