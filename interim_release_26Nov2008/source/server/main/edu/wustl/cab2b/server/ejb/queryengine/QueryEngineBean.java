package edu.wustl.cab2b.server.ejb.queryengine;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.globus.gsi.GlobusCredential;
import org.hibernate.HibernateException;

import edu.wustl.cab2b.common.domain.DCQL;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.category.PopularCategoryOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.queryengine.DCQLGenerator;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.hibernate.HibernateCleanser;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.util.dbManager.HibernateUtility;

/**
 * Class for handling query related operations
 * @author Chetan_patil
 */
public class QueryEngineBean extends AbstractStatelessSessionBean implements QueryEngineBusinessInterface {

    private static final long serialVersionUID = 8416841912609836063L;

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#executeQuery(edu.wustl.cab2b.common.queryengine.ICab2bQuery,
     *      org.globus.gsi.GlobusCredential)
     */
    /**
     * This method executes the given query if the given credential is authentic. 
     * 
     * @param query ICab2bQuery to be executed
     * @param dref
     * @param idP
     * @return query result
     * @throws Exception if authentication fails or query execution fails.
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public IQueryResult<? extends IRecord> executeQuery(ICab2bQuery query, String dref, String idP)
            throws GeneralSecurityException, IOException, Exception {
        GlobusCredential cred = null;
        boolean hasAnySecureService = Utility.hasAnySecureService(query);
        if (hasAnySecureService) {
            cred = UserOperations.getGlobusCredential(dref, idP);
        }
        new PopularCategoryOperations().setPopularity(query);
        return new QueryExecutor(query, cred).executeQuery();

    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#saveQuery(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
     */
    /**
     * This method saves the given ICab2bQuery object.
     *  
     * @param query
     * @param dref
     * @param idP
     * 
     * @throws Exception if authentication fails or query execution fails.
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws RemoteException if save process fails
     */
    public void saveQuery(ICab2bQuery query, String dref, String idP) throws RemoteException,
            GeneralSecurityException, IOException, Exception {
        Long userId = null;
        UserOperations uop = new UserOperations();
        try {

            userId = uop.getUserByName(uop.getCredentialUserName(dref, idP)).getUserId();
        } catch (GeneralSecurityException ge) {
            throw new RuntimeException("General Security Exception", ge.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());
        }
        query.setUserId(userId);

        new HibernateCleanser(query).clean();
        new QueryBizLogic<ICab2bQuery>().saveQuery(query);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#updateQuery(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
     */
    /**
     * This method updates the given ICab2bQuery object.
     * @param query
     * 
     * @throws RemoteException if updating fails
     */
    public void updateQuery(ICab2bQuery query) throws RemoteException {
        new QueryBizLogic<ICab2bQuery>().updateQuery(query);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#retrieveQueryById(java.lang.Long)
     */
    /**
     * This method retrieves the query which has the given identifier. 
     * 
     * @param queryId identifier of the query
     * @return ICab2bQuery object
     * @throws RemoteException if retrieving fails
     */
    public ICab2bQuery retrieveQueryById(Long queryId) throws RemoteException {
        ICab2bQuery query = (ICab2bQuery) new QueryBizLogic<ICab2bQuery>().getQueryById(queryId);
        return query;

    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#retrieveAllQueries()
     */
    /**
     * This method retrieves all the the queries. 
     * 
     * @return list of ICab2bQuery objects
     * @throws RemoteException if retrieving fails
     */
    public List<ICab2bQuery> retrieveAllQueries() throws RemoteException {
        return new QueryBizLogic<ICab2bQuery>().getAllQueries();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#getAllQueryNameAndDescription()
     */
    /**
     * This method returns all the queries with only their name and description populated.
     * @param dref
     * @param idP
     * 
     * @return list of IParameterizedQuery having only their name and description populated
     * @throws Exception if authentication fails or query execution fails.
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws RemoteException if retrieving fails
     */
    public Collection<IParameterizedQuery> getAllQueryNameAndDescription(String dref, String idP)
            throws RemoteException, IOException, GeneralSecurityException, Exception {

        String userName = null;
        try {
            userName = new UserOperations().getCredentialUserName(dref, idP);
            List idList = new ArrayList(1);
            idList.add(userName);
            return (List<IParameterizedQuery>) Utility.executeHQL("getQueriesByUserName", idList);

        } catch (HibernateException e) {
            throw new RemoteException(e.getMessage());
        } catch (GeneralSecurityException ge) {
            throw new RuntimeException("General Security Exception", ge.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#isQueryNameDuplicate(java.lang.String)
     */
    /**
     * This method checks whether the given query name has already been used or not.
     * 
     * @param queryName name of the query that is to be verified 
     * @return true if the queryName is duplicate; false if not
     * 
     * @throws RemoteException if checking fails
     */
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

    /*
     * (non-Javadoc)
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#getDCQL(edu.wustl.cab2b.common.queryengine.ICab2bQuery)
     */
    /**
     * This method returns the DCQL in tree format for the given query. Tree nodes consists of DCQL object
     * that encapsulates Category/Entity name and its corresponding DCQL.
     * 
     * @param query query for which DCQl is to be generated
     * @return TreeNode<DCQL>
     * @throws RemoteException
     */
    public DCQL getDCQL(ICab2bQuery query) throws RemoteException {
        return new DCQLGenerator(query).generateDCQL();
    }
}