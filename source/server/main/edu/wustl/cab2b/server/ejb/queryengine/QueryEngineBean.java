package edu.wustl.cab2b.server.ejb.queryengine;

import java.rmi.RemoteException;
import java.util.List;

import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;
import edu.wustl.cab2b.server.queryengine.Cab2bQueryProcessor;

public class QueryEngineBean extends AbstractStatelessSessionBean implements QueryEngineBusinessInterface {

    private static final long serialVersionUID = 8416841912609836063L;

    public IQueryResult executeQuery(ICab2bQuery query) {
        return new QueryExecutor(query).executeQuery();
    }

    public void saveQuery(ICab2bQuery query) throws RemoteException {
        new Cab2bQueryProcessor().saveQuery(query);
    }

    public ICab2bQuery retrieveQueryById(Long queryId) throws RemoteException {
        return (ICab2bQuery) new Cab2bQueryProcessor().getQueryById(queryId);
    }

    public List<ICab2bQuery> retrieveAllQueries() throws RemoteException {
        return new Cab2bQueryProcessor().getAllQueries();
    }

}