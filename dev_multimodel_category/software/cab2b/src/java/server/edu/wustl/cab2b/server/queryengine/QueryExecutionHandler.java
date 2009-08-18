package edu.wustl.cab2b.server.queryengine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;

/**
 * @author deepak_shingan, chetan_patil
 *
 * @param <T>
 */
public abstract class QueryExecutionHandler<T extends ICab2bQuery> {
    private AtomicBoolean isAlive = new AtomicBoolean(Boolean.FALSE);

    protected IQueryResult<? extends IRecord> result;

    protected T query;

    protected GlobusCredential proxy;

    protected UserInterface user;

    protected String[] modelGroupNames;

    protected List<QueryExecutor> queryExecutorsList;
    
    protected boolean isExecuteInBackground ;

    public QueryExecutionHandler(T query, GlobusCredential proxy, UserInterface user, String[] modelGroupNames) {
        this.query = query;
        this.proxy = proxy;
        this.user = user;
        this.modelGroupNames = modelGroupNames;
        this.isExecuteInBackground = false;
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
            // postProcessResults();
        }
    }

    /**
     * @return QueryStatus
     */
    public abstract QueryStatus getStatus();

    /**
     * This method returns the query set for execution
     * @return
     */
    public T getQuery() {
        return query;
    }

    /**
     * Set query for background execution.
     * @param executeInBackground
     */
    public void executeInBackground() {
        if (!getStatus().isVisible()) {
            QueryStatus qStatus = getStatus();
            qStatus.setVisible(true);
            saveQueryStatus();
        }
    }

    /**
     * Save query status.
     */
    public void saveQueryStatus() {
        QueryURLStatusOperations qso = new QueryURLStatusOperations();
        qso.insertQueryStatus(getStatus());
    }

    /**
     * Returns set of failed urls.
     * @return
     */
    public Set<String> getFailedUrls() {
        Set<URLStatus> urlStatusSet = getStatus().getUrlStatus();
        Set<String> failedUrls = new HashSet<String>();
        for (URLStatus urlStatus : urlStatusSet) {
            if (urlStatus.getStatus().equals(AbstractStatus.Complete_With_Error)) {
                failedUrls.add(urlStatus.getUrl());
            }
        }
        return failedUrls;
    }

    /**
     * Returns true if processing of each and every query is finished.
     * @return
     */
    public boolean isProcessingFinished() {
        List<QueryExecutor> queryExecutorsList = this.queryExecutorsList;
        for (QueryExecutor e : queryExecutorsList) {
            if (!e.isProcessingFinished()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param isExecuteInBackground
     */
    public void setExecuteInBackground(boolean isExecuteInBackground) {
        this.isExecuteInBackground = isExecuteInBackground;
    }

    /**
     * @return
     */
    public boolean isExecuteInBackground() {
        return isExecuteInBackground;
    }
}
