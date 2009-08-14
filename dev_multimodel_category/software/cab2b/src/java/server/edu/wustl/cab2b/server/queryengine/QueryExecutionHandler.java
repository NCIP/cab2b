package edu.wustl.cab2b.server.queryengine;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;

public abstract class QueryExecutionHandler<T extends ICab2bQuery> {
    private AtomicBoolean isAlive = new AtomicBoolean(Boolean.FALSE);

    protected IQueryResult<? extends IRecord> result;

    protected T query;

    protected GlobusCredential proxy;

    protected UserInterface user;

    protected String[] modelGroupNames;
    
    protected List<QueryExecutor> queryExecutorsList;

    public QueryExecutionHandler(T query, GlobusCredential proxy, UserInterface user, String[] modelGroupNames) {
        this.query = query;
        this.proxy = proxy;
        this.user = user;
        this.modelGroupNames = modelGroupNames;
    }

    protected abstract void preProcessQuery();

    protected abstract void postProcessResults();

    /**
     * Executes query
     * @param query
     */
    protected abstract void executeQuery();

    /**
     * return results 
     */
    public abstract IQueryResult<? extends IRecord> getResult();

    /**
     *  Starts query execution process if execution is not yet started.
     */
    public void execute() {
        if (!isAlive.getAndSet(Boolean.TRUE)) {
            preProcessQuery();
            executeQuery();
            postProcessResults();
        }
    }

    /**
     * @return QueryStatus
     */
    public abstract QueryStatus getStatus();

}
