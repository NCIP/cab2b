/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;

/**
 * Basic ICab2bQuery query handler 
 * @author deepak_shingan
 *
 */
public class CaB2QueryExecutionHandler extends QueryExecutionHandler {

    public CaB2QueryExecutionHandler(
            ICab2bQuery query,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames) {
        super(query, proxy, user, modelGroupNames);
        this.queryExecutorsList = new ArrayList<QueryExecutor>(1);
    }

    @Override
    protected void executeQuery() {
        ((CaB2QueryExecutionHandler) queryExecutorsList.get(0)).executeQuery();
    }

    @Override
    public IQueryResult getResult() {
        return ((QueryExecutor) queryExecutorsList.get(0)).getPartialResult();
    }

    @Override
    public QueryStatus getStatus() {
        QueryExecutor queryExecutor = (QueryExecutor) queryExecutorsList.get(0);
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
