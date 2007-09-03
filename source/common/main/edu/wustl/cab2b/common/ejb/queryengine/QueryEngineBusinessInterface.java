package edu.wustl.cab2b.common.ejb.queryengine;

import java.rmi.RemoteException;
import java.util.List;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;

public interface QueryEngineBusinessInterface extends BusinessInterface {
    /**
     * @param query the query to execute
     * @return the query result
     */
    IQueryResult executeQuery(ICab2bQuery query) throws RemoteException;

    /**
     * This method saves the Cab2bQuery object.
     * @return the saved query populated with the ids
     * @throws RemoteException
     */
    void saveQuery(ICab2bQuery query) throws RemoteException;

    ICab2bQuery retrieveQueryById(Long query) throws RemoteException;

    List<ICab2bQuery> retrieveAllQueries() throws RemoteException;
}
