/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.queryengine;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.domain.DCQL;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.category.PopularCategoryOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.queryengine.DCQLGenerator;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;
import edu.wustl.cab2b.server.queryengine.QueryOperations;
import edu.wustl.cab2b.server.util.UserUtility;
import edu.wustl.common.hibernate.HibernateCleanser;

/**
 * Class for handling query related operations
 * @author Chetan_patil
 */
public class QueryEngineBean extends AbstractStatelessSessionBean implements QueryEngineBusinessInterface {

    private static final long serialVersionUID = 8416841912609836063L;

    /**
     * This method executes the given query if the given credential is authentic. 
     *  
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#executeQuery(edu.wustl.cab2b.common.queryengine.ICab2bQuery,
     *      org.globus.gsi.GlobusCredential)
     * 
     * @param query ICab2bQuery to be executed
     * @param serializedDCR
     * @param idP
     * @return query result
     * @throws Exception if authentication fails or query execution fails.
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public IQueryResult<? extends IRecord> executeQuery(ICab2bQuery query, String serializedDCR, String gridType)
            throws RemoteException {
        GlobusCredential globusCredential = null;
        boolean hasAnySecureService = Utility.hasAnySecureService(query);
        if (hasAnySecureService) {
            globusCredential = UserUtility.getGlobusCredential(serializedDCR, gridType);
        }
        new PopularCategoryOperations().setPopularity(query);
        return new QueryExecutor(query, globusCredential).executeQuery();
    }

    /**
     * This method saves the given ICab2bQuery object.
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#saveQuery(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
     *  
     * @param query
     * @param serializedDCR
     * @param gridType
     * 
     * @throws RemoteException if authentication fails or save process fails.
     */
    public void saveQuery(ICab2bQuery query, String serializedDCR, String gridType) throws RemoteException {
        Long userId = UserUtility.getLocalUserId(serializedDCR, gridType);
        query.setUserId(userId);

        new HibernateCleanser(query).clean();
        new QueryOperations().saveQuery(query);
    }

    /**
     * This method updates the given ICab2bQuery object.
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#updateQuery(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
     * 
     * @param query
     * 
     * @throws RemoteException if updating fails
     */
    public void updateQuery(ICab2bQuery query) throws RemoteException {
        new QueryOperations().updateQuery(query);
    }

    /**
     * This method retrieves the query which has the given identifier. 
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#retrieveQueryById(java.lang.Long)
     * 
     * @param queryId identifier of the query
     * @return ICab2bQuery object
     * @throws RemoteException if retrieving fails
     */
    public ICab2bQuery retrieveQueryById(Long queryId) throws RemoteException {
        return (ICab2bQuery) new QueryOperations().getQueryById(queryId);
    }

    /**
     * This method returns all the queries with only their name and description populated.
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#getAllQueryNameAndDescription()
     * 
     * @param serializedDCR
     * @param idP
     * 
     * @return list of IParameterizedQuery having only their name and description populated
     *
     * @throws RemoteException if authentication fails or retreiving fails
     */
    public Collection<ICab2bQuery> getUsersQueriesDetail(String serializedDCR, String gridType)
            throws RemoteException {
        String usersGridId = UserUtility.getUsersGridId(serializedDCR, gridType);
        return new QueryOperations().getUsersQueriesDetail(usersGridId);
    }

    /**
     * This method checks whether the given query name has already been used or not.
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#isQueryNameDuplicate(java.lang.String)
     * 
     * @param queryName name of the query that is to be verified 
     * @return true if the queryName is duplicate; false if not
     * 
     * @throws RemoteException if checking fails
     */
    public boolean isQueryNameDuplicate(String queryName, String serializedDCR, String gridType)
            throws RemoteException {
        String usersGridId = UserUtility.getUsersGridId(serializedDCR, gridType);
        return new QueryOperations().isQueryNameDuplicate(queryName, usersGridId);
    }

    /**
     * This method returns the DCQL in tree format for the given query. Tree nodes consists of DCQL object
     * that encapsulates Category/Entity name and its corresponding DCQL.
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#getDCQL(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
     * 
     * @param query query for which DCQl is to be generated
     * @return TreeNode<DCQL>
     * @throws RemoteException
     */
    public DCQL getDCQL(ICab2bQuery query) throws RemoteException {
        return new DCQLGenerator(query).generateDCQL();
    }

    /**
     * This method retrieves all the the queries created by the given user. 
     * 
     * @return list of ICab2bQuery objects
     * @throws RemoteException if retrieving fails
     */
    public List<ICab2bQuery> retrieveAllQueriesByUser(String serializedDCR, String gridType)
            throws RemoteException {
        String usersGridId = UserUtility.getUsersGridId(serializedDCR, gridType);
        return new QueryOperations().retrieveAllQueriesByUser(usersGridId);
    }
}