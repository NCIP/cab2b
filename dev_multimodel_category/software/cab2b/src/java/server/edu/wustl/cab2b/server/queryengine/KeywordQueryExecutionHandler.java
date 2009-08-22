/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatusImpl;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IRule;

/**
 * @author pallavi_mistry
 *
 */
public class KeywordQueryExecutionHandler extends QueryExecutionHandler<KeywordQuery> {
    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(KeywordQueryExecutionHandler.class);

    private String keyword;

    private Map<ICab2bQuery, IQueryResult<? extends IRecord>> queryVsResultMap;

    private QueryStatus status = null;

    /**
     * @param queries
     * @param proxy
     * @param user
     * @param modelGroupNames
     */
    public KeywordQueryExecutionHandler(
            KeywordQuery query,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames,
            String keyword) {
        super(query, proxy, user, modelGroupNames);
        this.keyword = keyword;
        this.queryExecutorsList = new ArrayList<QueryExecutor>(query.getSubQueries().size());
        this.queryVsResultMap =
                new HashMap<ICab2bQuery, IQueryResult<? extends IRecord>>(query.getSubQueries().size());
        status = new QueryStatusImpl();
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
     * Method to update url status properties. It will update only the in-memory query status.
     */
    private void updateStatus() {
    	logger.info("--------Updating Keyword query status.");
        if (status.getStatus() == null || !status.getStatus().equals(AbstractStatus.Processing)) {
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
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#executeQuery()
     */
    @Override
    protected void executeQuery() {
        Collection<ICab2bQuery> subQueries = this.query.getSubQueries();
        for (ICab2bQuery query : subQueries) {
            QueryExecutor queryExecutor = new QueryExecutor(query, proxy);
            queryExecutorsList.add(queryExecutor);
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
     * For keyword query, one needs to call getQueryVsResultMap() instead of getResult()
     * because, we dont have consolidated result available for keyword query.
     *  getResult() will return null.
     */
    @Override
    public IQueryResult<? extends IRecord> getResult() {
        return null;
    }

    /**
     * For keyword query, one needs to call getQueryVsResultMap() instead of getResult()
     * because, we dont have consolidated result available for keyword query.
     * @return
     */
    public Map<ICab2bQuery, IQueryResult<? extends IRecord>> getQueryVsResultMap() {
        for (QueryExecutor executor : queryExecutorsList) {
            IQueryResult<? extends IRecord> result = null;
            //when query is sent to execute in background, then only query status (count, etc) will be updated in database.
            //getResult() will update the DB, while getPartialResult() will only update the in-memory object.
            if (isExecuteInBackground()) {
                result = executor.getResult();
            } else {
                result = executor.getPartialResult();
            }
            ICab2bQuery query = (ICab2bQuery) executor.getQuery();
            queryVsResultMap.put(query, result);
        }
        updateStatus();
        return queryVsResultMap;
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
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#preProcessQuery()
     */
    @Override
    protected void preProcessQuery() throws RuntimeException {
        Collection<ICab2bQuery> queries = this.query.getSubQueries();
        for (ICab2bQuery query : queries) {
            insertKeyword(query, this.keyword);
        }
        QueryExecutorUtil.insertURLConditions(query, proxy, user, modelGroupNames);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getQuery()
     */
    public KeywordQuery getQuery() {
        return query;
    }

    /**
     * This method replaces the values of the conditions of given query with the provided keyword.
     * @param query
     */
    private void insertKeyword(ICab2bQuery query, String keyword) {
        for (IExpression expression : query.getConstraints()) {
            for (IExpressionOperand operand : expression) {
                if (operand instanceof IRule) {
                    IRule rule = (IRule) operand;
                    for (ICondition condition : rule) {
                        List<String> values = new ArrayList<String>();
                        values.add(keyword);
                        condition.setValues(values);
                    }
                }
            }
        }
    }

}
