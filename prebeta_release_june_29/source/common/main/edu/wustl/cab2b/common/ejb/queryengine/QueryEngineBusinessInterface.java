package edu.wustl.cab2b.common.ejb.queryengine;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;

public interface QueryEngineBusinessInterface extends BusinessInterface {
    /**
     * @param query the query to execute
     * @return the query result
     */
    IQueryResult executeQuery(ICab2bQuery query) throws RemoteException;
}
