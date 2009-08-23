package edu.wustl.cab2b.server.queryengine;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.cagrid.fqp.results.metadata.ProcessingStatus;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.CompoundQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatusImpl;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.util.UtilityOperations;

/**
 * @author deepak_shingan, chetan_patil
 *
 * @param <T>
 */
public abstract class QueryExecutionHandler<T extends ICab2bQuery> {
    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(QueryExecutionHandler.class);

    private AtomicBoolean isAlive = new AtomicBoolean(Boolean.FALSE);

    protected IQueryResult<? extends IRecord> result;

    protected T query;

    protected GlobusCredential proxy;

    protected UserInterface user;

    protected String[] modelGroupNames;

    protected List<QueryExecutor> queryExecutorsList;

    protected boolean isExecuteInBackground;

    protected QueryStatus status = null;

    public QueryExecutionHandler(T query, GlobusCredential proxy, UserInterface user, String[] modelGroupNames) {
        this.query = query;
        this.proxy = proxy;
        this.user = user;
        this.modelGroupNames = modelGroupNames;
        this.isExecuteInBackground = false;
        status = new QueryStatusImpl();

    }

    protected abstract void preProcessQuery();

    protected abstract void postProcessResults();

    /**
     * return results 
     */
    public abstract IQueryResult<? extends IRecord> getResult();

    /**
     *  Starts query execution process if execution is not yet started.
     */
    public void execute() {
        if (!isAlive.getAndSet(Boolean.TRUE)) {
            initializeQueryStatus();
            new Thread() {
                public void run() {
                    for (QueryExecutor queryExecutor : queryExecutorsList) {
                        logger.info("Execution handler...Running executor for :"
                                + queryExecutor.getQuery().getName() + "Query");
                        queryExecutor.executeQuery();
                    }
                    updateStatus();
                }
            }.start();
        }
    }

    /**
     * @return QueryStatus
     */
    public QueryStatus getStatus() {
        updateStatus();
        return status;

    }

    /**
     * This method returns the query set for execution
     * @return
     */
    public T getQuery() {
        return query;
    }

    /**
     * Returns set of failed urls.
     * @return
     */
    public Set<String> getFailedUrls() {
        Set<URLStatus> urlStatusSet = status.getUrlStatus();
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
        boolean isProcessingFinished = true;
        List<QueryExecutor> queryExecutorsList = this.queryExecutorsList;
        for (QueryExecutor e : queryExecutorsList) {
            if (!e.isProcessingFinished()) {
                isProcessingFinished = false;
            }
        }
        logger.info("Queryhandler isProcessingFinished:" + isProcessingFinished);
        return isProcessingFinished;
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

    /**
     * Initializes main query status with Child query URL-status and other properties.
     * 
     */
    public void initializeQueryStatus() {
        Set<URLStatus> queryURLStatusSet = new HashSet<URLStatus>();
        Set<QueryStatus> childrenQueryStatus = null;
        //if query is compound query then only set child query status values
        if (query instanceof CompoundQuery) {
            childrenQueryStatus = new HashSet<QueryStatus>(((CompoundQuery) query).getSubQueries().size());
        }
        for (QueryExecutor queryExecutor : queryExecutorsList) {
            QueryStatus subQueryStatus = queryExecutor.getStatus();
            queryURLStatusSet.addAll(subQueryStatus.getUrlStatus());
            if (childrenQueryStatus != null) {
                childrenQueryStatus.add(subQueryStatus);
            }
        }

        status.setQuery(query);
        status.setUser(user);
        status.setVisible(Boolean.FALSE);
        status.setQueryConditions(UtilityOperations.getStringRepresentationofConstraints(query.getConstraints()));
        status.setDescription("Testing : Execution handler query status for '" + query.getName() + "' query.");
        status.setStatus(AbstractStatus.Processing);
        status.setUrlStatus(queryURLStatusSet);
        status.setQueryStartTime(new Date());
        status.setChildrenQueryStatus(childrenQueryStatus);

        //Saving to database
        new QueryURLStatusOperations().insertQueryStatus(status);
    }

    /**
     * Method to update url status properties. It will update only the in-memory query status.
     */
    public void updateStatus() {

        logger.info("Updaing Query handler query status.");
        String statusStr = status.getStatus();
        if (statusStr != null
                && (statusStr.equals(AbstractStatus.Complete) || statusStr
                    .equals(AbstractStatus.Complete_With_Error))) {
            return;
        }
        int resultCount = 0;
        boolean resultAvailable = false;
        for (QueryExecutor subQueryExecutor : queryExecutorsList) {
            Integer childResultCount = subQueryExecutor.getStatus().getResultCount();
            if (childResultCount != null) {
                resultAvailable = true;
                resultCount = resultCount + childResultCount.intValue();
            }
        }
        if (resultAvailable) {
            status.setResultCount(new Integer(resultCount));
        }
        status.setStatus(AbstractStatus.Processing);
        Set<QueryStatus> childQueryStatus = status.getChildrenQueryStatus();
        if (isProcessingFinished() && areAllSubQueriesFinished(childQueryStatus)) {
            status.setQueryEndTime(new Date());
            boolean isEveryUrlWorked = true;
            int failedUrlCount = 0;
            for (QueryStatus subQueryStatus : childQueryStatus) {
                String urlStatus = subQueryStatus.getStatus();
                if (urlStatus.equals(ProcessingStatus._Complete_With_Error)) {
                    status.setStatus(AbstractStatus.Complete_With_Error);
                    isEveryUrlWorked = false;
                    failedUrlCount++;
                }
            }
            if ((failedUrlCount != 0 && failedUrlCount == childQueryStatus.size())
                    || (childQueryStatus.size() == 0 && getFailedUrls().size() == status.getUrlStatus().size())) {
                //Every URL is failed. Set status as failed.
                status.setStatus(AbstractStatus.FAILED);
            } else if (isEveryUrlWorked && failedUrlCount == 0) {
                status.setStatus(AbstractStatus.Complete);
            }
            //Query finished update in database.
            new QueryURLStatusOperations().updateQueryStatus(status);
        }
        logger.info("Execution handler query  status.:" + status.getStatus());
        logger.info("Execution handler query record count.:" + status.getResultCount());
    }

    /**
     * @param ChildQueryStatus
     * @return
     */
    private boolean areAllSubQueriesFinished(Collection<QueryStatus> ChildQueryStatus) {
        boolean res = true;
        for (QueryStatus fqpUrl : ChildQueryStatus) {
            String urlStatus = fqpUrl.getStatus();
            if (urlStatus.equals(ProcessingStatus._Processing)
                    || urlStatus.equals(ProcessingStatus._Waiting_To_Begin)) {
                res = false;
                break;
            }
        }
        return res;
    }
}
