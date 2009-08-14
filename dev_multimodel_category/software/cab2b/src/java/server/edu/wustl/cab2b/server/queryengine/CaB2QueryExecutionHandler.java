/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * Basic ICab2bQuery query handler 
 * @author deepak_shingan
 *
 */
public class CaB2QueryExecutionHandler extends QueryExecutionHandler {

    private QueryExecutor queryExecutor = null;

    CaB2QueryExecutionHandler(
            ICab2bQuery query,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames) {
        super(query, proxy, user, modelGroupNames);
        queryExecutor = new QueryExecutor(query, proxy);
    }

    @Override
    protected void executeQuery() {
        queryExecutor.executeQuery();
    }

    @Override
    public IQueryResult getResult() {
        return queryExecutor.getPartialResult();
    }

    @Override
    public QueryStatus getStatus() {
        return queryExecutor.getStatus();

    }

    @Override
    protected void postProcessResults() {
        //No need

    }

    @Override
    protected void preProcessQuery() {
        //No need
    }
}
