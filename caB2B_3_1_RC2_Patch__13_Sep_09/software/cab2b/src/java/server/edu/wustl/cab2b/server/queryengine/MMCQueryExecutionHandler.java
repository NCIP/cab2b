/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;

/**
 * @author chetan_patil
 *
 */
public class MMCQueryExecutionHandler extends QueryExecutionHandler<MultiModelCategoryQuery> {
    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(MMCQueryExecutionHandler.class);

    private MMCQueryResultConflator resultConflator;

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
        preProcessQuery();

        // Initilizing executor list.
        Collection<ICab2bQuery> subQueries = this.query.getSubQueries();
        this.queryExecutorsList = new ArrayList<QueryExecutor>(subQueries.size());
        for (ICab2bQuery subQuery : subQueries) {
            QueryExecutor queryExecutor = new QueryExecutor(subQuery, proxy);
            queryExecutorsList.add(queryExecutor);
        }
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
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#postProcessResults()
     */
    @Override
    protected void postProcessResults() {
        Collection<IQueryResult<? extends IRecord>> queryResults =
                new ArrayList<IQueryResult<? extends IRecord>>();
        for (QueryExecutor queryExecutor : queryExecutorsList) {
            IQueryResult<? extends IRecord> subQueryResult = queryExecutor.getResult();
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
        QueryExecutorUtil.setOutputURLs(query, proxy, user, modelGroupNames);
    }
}
