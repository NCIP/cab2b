package edu.wustl.cab2b.common.ejb.queryengine;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.domain.DCQL;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

/**
 * 
 * Interface for handling query related data operations.
 * @author Chetan_Patil
 *
 */
public interface QueryEngineBusinessInterface extends BusinessInterface {
    /**
     * This method executes the given query if the given credential is authentic. 
     * 
     * @param query ICab2bQuery to be executed
     * @param cred user crendential to be authenticated
     * @return query result
     * @throws RemoteException if authentication fails or query execution fails.
     */
    IQueryResult<? extends IRecord> executeQuery(ICab2bQuery query, GlobusCredential cred) throws RemoteException;

    /**
     * This method saves the given ICab2bQuery object.
     * 
     * @throws RemoteException if save process fails
     */
    void saveQuery(ICab2bQuery query) throws RemoteException;

    /**
     * This method updates the given ICab2bQuery object.
     * 
     * @throws RemoteException if updating fails
     */
    void updateQuery(ICab2bQuery query) throws RemoteException;

    /**
     * This method retrieves the query which has the given identifier. 
     * 
     * @param queryId identifier of the query
     * @return ICab2bQuery object
     * @throws RemoteException if retrieving fails
     */
    ICab2bQuery retrieveQueryById(Long queryId) throws RemoteException;

    /**
     * This method retrieves all the the queries. 
     * 
     * @return list of ICab2bQuery objects
     * @throws RemoteException if retrieving fails
     */
    List<ICab2bQuery> retrieveAllQueries() throws RemoteException;

    /**
     * This method checks whether the given query name has already been used or not.
     * 
     * @param queryName name of the query that is to be verified 
     * @return true if the queryName is duplicate; false if not
     * 
     * @throws RemoteException if checking fails
     */
    boolean isQueryNameDuplicate(String queryName) throws RemoteException;

    /**
     * This method returns all the queries with only their name and description populated.
     * 
     * @return list of IParameterizedQuery having only their name and description populated
     * @throws RemoteException if retrieving fails
     */
    Collection<IParameterizedQuery> getAllQueryNameAndDescription() throws RemoteException;

    /**
     * This method returns the DCQL in tree format for the given query. Tree nodes consists of DCQL object
     * that encapsulates Category/Entity name and its corresponding DCQL.
     * 
     * @param query query for which DCQl is to be generated
     * @return TreeNode<DCQL>
     */
    DCQL getDCQL(ICab2bQuery query) throws RemoteException;
}
