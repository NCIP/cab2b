package edu.wustl.cab2b.common.ejb.queryengine;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

public interface QueryEngineBusinessInterface extends BusinessInterface {
    /**
     * @param query the query to execute
     * @return the query result
     */
    IQueryResult executeQuery(ICab2bQuery query, GlobusCredential cred) throws RemoteException;

    /**
     * This method saves the Cab2bQuery object.
     * @throws RemoteException
     */
    void saveQuery(ICab2bParameterizedQuery query) throws RemoteException;

    /**
     * This method updates the Cab2bQuery object.
     * @throws RemoteException
     */
    void updateQuery(ICab2bParameterizedQuery query) throws RemoteException;

    ICab2bParameterizedQuery retrieveQueryById(Long query) throws RemoteException;

    List<ICab2bParameterizedQuery> retrieveAllQueries() throws RemoteException;

    boolean isQueryNameDuplicate(String queryName) throws RemoteException;

    public Collection<IParameterizedQuery> getAllQueryNameAndDescription() throws RemoteException;
}
