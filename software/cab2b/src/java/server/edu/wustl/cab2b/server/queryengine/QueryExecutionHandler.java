/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatusImpl;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.QueryStatusUtil;
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

    protected boolean isQueryStatusCompleteAndSavedInDB = false;

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
        Set<QueryStatus> childrenQueryStatus = new HashSet<QueryStatus>(queryExecutorsList.size());
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
        if (QueryStatusUtil.isStatusProcessingDone(status)) {
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
        if (isProcessingFinished() && QueryStatusUtil.areAllSubQueriesFinished(status)) {
            String statusString = AbstractStatus.Complete_With_Error;
            if (QueryStatusUtil.isEveryChildStatusEqualsTo(AbstractStatus.FAILED, status)) {
                statusString = AbstractStatus.FAILED;
            } else if (QueryStatusUtil.isEveryChildStatusEqualsTo(AbstractStatus.Complete, status)) {
                statusString = AbstractStatus.Complete;
            }
            status.setStatus(statusString);
            status.setQueryEndTime(new Date());
            if (!isQueryStatusCompleteAndSavedInDB) {
                //Query finished update in database.
                new QueryURLStatusOperations().updateQueryStatus(status);
                isQueryStatusCompleteAndSavedInDB = true;
            }
        }
    }

    /**
     * Returns set of failed urls.
     * @return
     */
    public Set<String> getFailedUrls() {
        return QueryStatusUtil.getFailedURLs(status);
    }
}
