package edu.wustl.cab2b.server.queryengine;

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
     * Returns results from all URLs 
     * @param url
     */
    public abstract IQueryResult<? extends IRecord> getResult(String url);

    public void execute() {
        if (!isAlive.getAndSet(Boolean.TRUE)) {
            preProcessQuery();
            executeQuery();
            postProcessResults();
        }
    }

    /**
     * writes query results to specified path
     * @param filePath
     */
    public abstract void exportResultToCSVFile(String filePath);

    /**
     * @return QueryStatus
     */
    public abstract QueryStatus getStatus();

}
