/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;

/**
 * Basic ICab2bQuery query handler 
 * @author deepak_shingan
 *
 */
public class CaB2BQueryExecutionHandler extends QueryExecutionHandler<ICab2bQuery> {

    /**
     * @param query
     * @param proxy
     * @param user
     * @param modelGroupNames
     */
    public CaB2BQueryExecutionHandler(
            ICab2bQuery query,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames) {
        super(query, proxy, user, modelGroupNames);
        preProcessQuery();
        //Initilizing executor list.
        QueryExecutor queryExecutor = new QueryExecutor(query, proxy);
        this.queryExecutorsList = new ArrayList<QueryExecutor>(1);
        queryExecutorsList.add(queryExecutor);
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
            new QueryURLStatusOperations().updateQueryStatus(getStatus());
        } else {
            result = executor.getPartialResult();
        }
        updateStatus();
        return result;
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
        QueryExecutorUtil.setOutputURLs(query, proxy, user, modelGroupNames);
    }
}
