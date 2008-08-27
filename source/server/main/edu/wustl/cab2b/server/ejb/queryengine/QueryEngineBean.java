package edu.wustl.cab2b.server.ejb.queryengine;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import org.globus.gsi.GlobusCredential;
import org.hibernate.HibernateException;

import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;
import edu.wustl.common.hibernate.HibernateCleanser;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.util.dbManager.HibernateUtility;

/**
 * class for handling query related database operations
 * @author Chetan_patil
 */
public class QueryEngineBean extends AbstractStatelessSessionBean implements
		QueryEngineBusinessInterface {

	private static final long serialVersionUID = 8416841912609836063L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#executeQuery(edu.wustl.cab2b.common.queryengine.ICab2bQuery,
	 *      org.globus.gsi.GlobusCredential)
	 */
	public IQueryResult<? extends IRecord> executeQuery(ICab2bQuery query,
			GlobusCredential cred) {
		return new QueryExecutor(query, cred).executeQuery();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#saveQuery(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
	 */
	public void saveQuery(ICab2bQuery query)
			throws RemoteException {
		new HibernateCleanser(query).clean();
		new QueryBizLogic<ICab2bQuery>().saveQuery(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#updateQuery(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
	 */
	public void updateQuery(ICab2bQuery query)
			throws RemoteException {
		new QueryBizLogic<ICab2bQuery>().updateQuery(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#retrieveQueryById(java.lang.Long)
	 */
	public ICab2bQuery retrieveQueryById(Long queryId)
			throws RemoteException {
		ICab2bQuery query = (ICab2bQuery) new QueryBizLogic<ICab2bQuery>()
				.getQueryById(queryId);
		return query;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#retrieveAllQueries()
	 */
	public List<ICab2bQuery> retrieveAllQueries()
			throws RemoteException {
		return new QueryBizLogic<ICab2bQuery>().getAllQueries();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#getAllQueryNameAndDescription()
	 */
	public Collection<IParameterizedQuery> getAllQueryNameAndDescription()
			throws RemoteException {
		try {
			return HibernateUtility
					.executeHQL(HibernateUtility.GET_PARAMETERIZED_QUERIES_DETAILS);
		} catch (HibernateException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#isQueryNameDuplicate(java.lang.String)
	 */
	public boolean isQueryNameDuplicate(String queryName)
			throws RemoteException {
		boolean isDuplicate = false;
		try {
			Collection<IParameterizedQuery> queries = HibernateUtility
					.executeHQL(HibernateUtility.GET_PARAMETERIZED_QUERIES_DETAILS);
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