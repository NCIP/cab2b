/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;
import edu.wustl.cab2b.server.util.UtilityOperations;

/**
 * Basic ICab2bQuery query handler 
 * @author deepak_shingan
 *
 */
public class CaB2BQueryExecutionHandler extends QueryExecutionHandler<ICab2bQuery> {

    /**
     * @param query
     * @param proxy
     * @param user
     * @param modelGroupNames
     */
    public CaB2BQueryExecutionHandler(
            ICab2bQuery query,
            GlobusCredential proxy,
            UserInterface user,
            String[] modelGroupNames) {
        super(query, proxy, user, modelGroupNames);
        preProcessQuery();
        //Initilizing executor list.
        QueryExecutor queryExecutor = new QueryExecutor(query, proxy);
        this.queryExecutorsList = new ArrayList<QueryExecutor>(1);
        queryExecutorsList.add(queryExecutor);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getResult()
     */
    @Override
    public IQueryResult getResult() {
        IQueryResult<? extends IRecord> result = null;
        QueryExecutor executor = queryExecutorsList.get(0);
        result = executor.getResult();
        updateStatus();
        return result;
    }

    @Override
    protected void postProcessResults() {
        //No need

    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#preProcessQuery()
     */
    @Override
    protected void preProcessQuery() {
        QueryExecutorUtil.setOutputURLs(query, proxy, user, modelGroupNames);
    }

    public void initializeQueryStatus() {
        String conditions = UtilityOperations.getStringRepresentationofConstraints(query.getConstraints());

        Set<URLStatus> queryURLStatusSet = new HashSet<URLStatus>();
        Set<QueryStatus> childrenQueryStatus = new HashSet<QueryStatus>(1);
        for (QueryExecutor queryExecutor : queryExecutorsList) {
            QueryStatus subQueryStatus = queryExecutor.getStatus();
            queryURLStatusSet.addAll(subQueryStatus.getUrlStatus());
            childrenQueryStatus.add(subQueryStatus);
            
            conditions = queryExecutor.getStatus().getQueryConditions();
        }
        status.setQuery(query);
        status.setUser(user);
        status.setVisible(Boolean.FALSE);
        status.setQueryConditions(conditions);
        status.setStatus(AbstractStatus.Processing);
        status.setUrlStatus(queryURLStatusSet);
        status.setQueryStartTime(new Date());
        status.setChildrenQueryStatus(childrenQueryStatus);

        //Saving to database
        new QueryURLStatusOperations().insertQueryStatus(status);
    }
}
