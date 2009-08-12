/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.ejb.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;

import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * @author chetan_patil
 *
 */
public class MMCQueryExecutionHandler extends QueryExecutionHandler<MultiModelCategoryQuery> {

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
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#executeQuery()
     */
    @Override
    protected void executeQuery() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#exportResultToCSVFile(java.lang.String)
     */
    @Override
    public void exportResultToCSVFile(String filePath) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getResult()
     */
    @Override
    public IQueryResult<? extends IRecord> getResult() {
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getResult(java.lang.String)
     */
    @Override
    public IQueryResult<? extends IRecord> getResult(String url) {
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.queryengine.QueryExecutionHandler#getStatus()
     */
    @Override
    public QueryStatus getStatus() {
        // TODO Auto-generated method stub
        return null;
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
    protected void preProcessQuery() {
        // TODO Auto-generated method stub
    }

}
