package edu.wustl.cab2b.server.ejb.queryengine;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.authentication.util.AuthenticationUtility;
import edu.wustl.cab2b.common.domain.DCQL;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.category.PopularCategoryOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.queryengine.DCQLGenerator;
import edu.wustl.cab2b.server.queryengine.QueryConverter;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;
import edu.wustl.cab2b.server.queryengine.QueryOperations;
import edu.wustl.cab2b.server.util.UtilityOperations;
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
    public IQueryResult<? extends IRecord> executeQuery(ICab2bQuery query, String serializedDCR)
            throws RemoteException {
        GlobusCredential globusCredential = null;
        boolean hasAnySecureService = Utility.hasAnySecureService(query);
        if (hasAnySecureService) {
            globusCredential = AuthenticationUtility.getGlobusCredential(serializedDCR);
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
    public void saveQuery(ICab2bQuery query, String serializedDCR) throws RemoteException {
        Long userId = UtilityOperations.getLocalUserId(serializedDCR);
        query.setCreatedBy(userId);

        new HibernateCleanser(query).clean();
        if (query.isKeywordSearch()) { // First Save as keyword search
            saveAsKeywordQuery(query);
        }

        // Reset as regular query and save.
        query.setIsKeywordSearch(Boolean.FALSE);
        new QueryOperations().saveQuery(query);
    }

    /**
     * This method save the given regular query as keyword search query.
     *
     * @param query
     * @throws RemoteException
     */
    public void saveAsKeywordQuery(ICab2bQuery query) throws RemoteException {
        QueryConverter queryConverter = new QueryConverter();
        if (query.isKeywordSearch()) {
            ICab2bQuery oredQuery = queryConverter.convertToKeywordQuery(query);
            new QueryOperations().saveQuery(oredQuery);
        }
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
    public Collection<ICab2bQuery> getUsersQueriesDetail(String serializedDCR) throws RemoteException {
        String usersGridId = AuthenticationUtility.getUsersGridId(serializedDCR);
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
    public boolean isQueryNameDuplicate(String queryName, String serializedDCR) throws RemoteException {
        String usersGridId = AuthenticationUtility.getUsersGridId(serializedDCR);
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
     * This method returns all the regular queries created by the given user.
     *
     * @param serializedDCR End Point Reference for user's credential
     * @return
     * @throws RemoteException
     */
    public List<ICab2bQuery> getRegularQueriesByUserName(final String serializedDCR) throws RemoteException {
        String usersGridId = AuthenticationUtility.getUsersGridId(serializedDCR);
        return new QueryOperations().getRegularQueriesByUserName(usersGridId);
    }

    /**
     * This method returns all the keyword search queries created by the given user.
     *
     * @param serializedDCR End Point Reference for user's credential
     * @return
     * @throws RemoteException
     */
    public List<ICab2bQuery> getKeywordQueriesByUserName(final String serializedDCR) throws RemoteException {
        String usersGridId = AuthenticationUtility.getUsersGridId(serializedDCR);
        return new QueryOperations().getKeywordQueriesByUserName(usersGridId);
    }

}