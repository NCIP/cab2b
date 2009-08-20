/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;

/**
 * Basic ICab2bQuery query handler 
 * @author deepak_shingan
 *
 */
public class CaB2QueryExecutionHandler extends QueryExecutionHandler<ICab2bQuery> {

    /**
     * @param query
     * @param proxy
     * @param user
     * @param modelGroupNames
     */
    public CaB2QueryExecutionHandler(
            ICab2bQuery query,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames) {
        super(query, proxy, user, modelGroupNames);
        this.queryExecutorsList = new ArrayList<QueryExecutor>(1);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#executeQuery()
     */
    @Override
    protected void executeQuery() {
        QueryExecutor queryExecutor = new QueryExecutor(query, proxy);
        queryExecutorsList.add(queryExecutor);
        queryExecutor.executeQuery();
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getResult()
     */
    @Override
    public IQueryResult getResult() {
        IQueryResult<? extends IRecord> result = null;
        QueryExecutor executor = queryExecutorsList.get(0);
        //when query is sent to execute in background, then only query status (count, etc) will be updated in database.
        //getResult() will update the DB, while getPartialResult() will only update the in-memory object.
        if (isExecuteInBackground()) {
            result = executor.getResult();
        } else {
            result = executor.getPartialResult();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getStatus()
     */
    @Override
    public QueryStatus getStatus() {
        QueryExecutor queryExecutor = (QueryExecutor) queryExecutorsList.get(0);
        return queryExecutor.getStatus();

    }

    @Override
    protected void postProcessResults() {
        //No need

    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#preProcessQuery()
     */
    @Override
    protected void preProcessQuery() {
        Collection<ICab2bQuery> queries = new ArrayList<ICab2bQuery>(1); //since it is a single query
        queries.add(this.query);
        QueryExecutorUtil.insertURLConditions(queries, proxy, user, modelGroupNames);

    }
    
    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getQuery()
     */
    public ICab2bQuery getQuery() {
        return query;
    }
}
