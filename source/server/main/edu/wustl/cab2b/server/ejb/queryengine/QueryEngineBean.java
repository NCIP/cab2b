package edu.wustl.cab2b.server.ejb.queryengine;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import net.sf.hibernate.HibernateException;

import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;
import edu.wustl.cab2b.server.queryengine.Cab2bQueryBizLogic;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.util.dbManager.HibernateUtility;

public class QueryEngineBean extends AbstractStatelessSessionBean implements QueryEngineBusinessInterface {

    private static final long serialVersionUID = 8416841912609836063L;

    public IQueryResult executeQuery(ICab2bQuery query) {
        return new QueryExecutor(query).executeQuery();
    }

    public void saveQuery(ICab2bParameterizedQuery query) throws RemoteException {
        new Cab2bQueryBizLogic().saveQuery(query);
    }

    public void updateQuery(ICab2bParameterizedQuery query) throws RemoteException {
        new Cab2bQueryBizLogic().updateQuery(query);
    }

    public ICab2bParameterizedQuery retrieveQueryById(Long queryId) throws RemoteException {
        return (ICab2bParameterizedQuery) new Cab2bQueryBizLogic().getQueryById(queryId);
    }

    public List<ICab2bParameterizedQuery> retrieveAllQueries() throws RemoteException {
        return new Cab2bQueryBizLogic().getAllQueries();
    }

    public boolean isQueryNameDuplicate(String queryName) throws RemoteException {
        boolean isDuplicate = false;

        try {
            Collection<IParameterizedQuery> queries = HibernateUtility.executeHQL(HibernateUtility.GET_PARAMETERIZED_QUERIES_DETAILS);
            for (IParameterizedQuery query : queries) {
                if (query.getName().equalsIgnoreCase(queryName)) {
                    isDuplicate = true;
                    break;
                }
            }
        } catch (HibernateException e) {
            throw new RemoteException(e.getMessage());
        }

        return isDuplicate;
    }

}