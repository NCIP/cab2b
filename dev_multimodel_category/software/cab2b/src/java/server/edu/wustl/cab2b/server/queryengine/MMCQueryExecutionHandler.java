/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatusImpl;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;

/**
 * @author chetan_patil
 *
 */
public class MMCQueryExecutionHandler extends QueryExecutionHandler<MultiModelCategoryQuery> {
    private MMCQueryResultConflator resultConflator;

    private IQueryResult<? extends IRecord> result;

    private QueryStatus status = null;

    /**
     * @param queries
     * @param proxy
     * @param user
     * @param modelGroupNames
     */
    public MMCQueryExecutionHandler(
            MultiModelCategoryQuery query,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames) {
        super(query, proxy, user, modelGroupNames);
        resultConflator = new MMCQueryResultConflator(query);
        this.queryExecutorsList = new ArrayList<QueryExecutor>(query.getSubQueries().size());
        status = new QueryStatusImpl();
    }

    /**
     * 
     */
    private void updateStatus() {
      //  if (status.getStatus() == null || !status.getStatus().equals(AbstractStatus.Processing)) {
            String desc = "";
            boolean isProcessing = false;
            boolean isCompleteWithError = false;
            boolean isNull = true;
            Integer recCount = null;
            for (QueryExecutor queryExecutor : queryExecutorsList) {
                QueryStatus qs = queryExecutor.getStatus();
                desc = desc + "  " + qs.getDescription();
                Integer resCount = qs.getResultCount();
                if (resCount != null) {
                    recCount = +resCount;
                }
                String statusStr = qs.getStatus();
                if (statusStr != null) {
                    isNull = false;
                    if (statusStr.equals(AbstractStatus.Processing)) {
                        isProcessing = true;
                    } else if (statusStr.equals(AbstractStatus.Complete_With_Error)) {
                        isCompleteWithError = true;
                    }
                }
            }
            if (status.getQueryStartTime() == null) {
                status.setQueryStartTime(new Date());
            }
            status.setResultCount(recCount);
            if (isProcessing) {
                status.setStatus(AbstractStatus.Processing);
            } else {
                boolean isQueryExecutorListFinished = true;
                for (QueryExecutor queryExecutor : queryExecutorsList) {
                    if (!queryExecutor.isProcessingFinished()) {
                        isQueryExecutorListFinished = false;
                        break;
                    }
                }
                if (isQueryExecutorListFinished) {
                    if (isCompleteWithError) {
                        status.setStatus(AbstractStatus.Complete_With_Error);
                    } else if (!isNull) {
                        status.setStatus(AbstractStatus.Complete);
                        status.setQueryEndTime(new Date());
                    }
                }
            }
        }
   // }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#executeQuery()
     */
    @Override
    protected void executeQuery() {
        Collection<ICab2bQuery> subQueries = query.getSubQueries();
        for (ICab2bQuery query : subQueries) {
            QueryExecutor queryExecutor = new QueryExecutor(query, proxy);
            this.queryExecutorsList.add(queryExecutor);

        }
        new Thread() {
            public void run() {
                for (QueryExecutor queryExecutor : queryExecutorsList) {
                    queryExecutor.executeQuery();
                }
            }
        }.start();

        initializeQueryStatus();
        updateStatus();
        setStatusChild();
    }

    /**
     * Initializes main query status with Child query URL-status.
     * 
     */
    private void initializeQueryStatus() {
        Set<URLStatus> queryURLStatusSet = new HashSet<URLStatus>();
        for (QueryExecutor queryExecutor : queryExecutorsList) {
            Set<URLStatus> urlStatusSet = queryExecutor.getStatus().getUrlStatus();
            queryURLStatusSet.addAll(urlStatusSet);
        }
        status.setUrlStatus(queryURLStatusSet);
    }

    /**
     * Set status for all child/sub queries.
     */
    private void setStatusChild() {
        Set<QueryStatus> childrenQueryStatus = new HashSet<QueryStatus>(query.getSubQueries().size());
        for (QueryExecutor queryExecutor : queryExecutorsList) {
            childrenQueryStatus.add(queryExecutor.getStatus());
        }
        status.setChildrenQueryStatus(childrenQueryStatus);
    }

    /**
     * It will PostProcess the results firstly (i.e. consolidate in a single result) and then will return single result. 
     * @return IQueryResult<? extends IRecord>
     */
    @Override
    public IQueryResult<? extends IRecord> getResult() {
        postProcessResults();
        return result;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getStatus()
     */
    @Override
    public QueryStatus getStatus() {
        updateStatus();
        return status;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#postProcessResults()
     */
    @Override
    protected void postProcessResults() {
        Collection<IQueryResult<? extends IRecord>> queryResults =
                new ArrayList<IQueryResult<? extends IRecord>>();
        for (QueryExecutor queryExecutor : queryExecutorsList) {
            IQueryResult<? extends IRecord> subQueryResult = null;
            //when query is sent to execute in background, then only query status (count, etc) will be updated in database.
            //getResult() will update the DB, while getPartialResult() will only update the in-memory object.
            if (isExecuteInBackground()) {
                subQueryResult = queryExecutor.getResult();
            } else {
                subQueryResult = queryExecutor.getPartialResult();
            }
            if (subQueryResult != null) {
                queryResults.add(subQueryResult);
            }
        }
        result = resultConflator.conflate(queryResults);
        updateStatus();
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#preProcessQuery()
     */
    @Override
    protected void preProcessQuery() {
        new MultimodelCategoryQueryPreprocessor().preprocessQuery(query);
        QueryExecutorUtil.insertURLConditions(query, proxy, user, modelGroupNames);
    }
}
