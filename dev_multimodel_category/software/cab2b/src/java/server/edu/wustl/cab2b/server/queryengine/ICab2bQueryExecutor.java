package edu.wustl.cab2b.server.queryengine;

import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatusImpl;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

public interface ICab2bQueryExecutor {

    /**
     * Method for executing query.
     * @param query
     * @param globusCredential
     */
    void executeQuery();

    /**
     * @return IQueryResult<? extends IRecord> query results
     */
    IQueryResult<? extends IRecord> getResult();

    /**
     * Returns status of the query.
     * @return QueryStatus 
     */
    QueryStatusImpl getStatus();

}
