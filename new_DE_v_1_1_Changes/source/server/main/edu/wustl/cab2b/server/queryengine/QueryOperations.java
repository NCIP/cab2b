/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

/**
 * @author chetan_patil
 *
 */
public class QueryOperations extends QueryBizLogic<ICab2bQuery> {

    /**
     * This method checks whether the given query name has already been used by the given user or not.
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#isQueryNameDuplicate(java.lang.String)
     * 
     * @param queryName name of the query that is to be verified 
     * @return true if the queryName is duplicate; false if not
     */
    public boolean isQueryNameDuplicate(String queryName, String userName) {
        boolean isDuplicate = false;

        Collection<ICab2bQuery> queries = getUsersQueriesDetail(userName);
        for (IParameterizedQuery query : queries) {
            if (query.getName().equalsIgnoreCase(queryName)) {
                isDuplicate = true;
                break;
            }
        }

        return isDuplicate;
    }

    /**
     * This method returns all the queries created by given user with 
     * only their name, description and created date populated.
     * 
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#getAllQueryNameAndDescription()
     * 
     * @param userName
     * 
     * @return list of IParameterizedQuery having only name, description and created date populated.
     */
    public Collection<ICab2bQuery> getUsersQueriesDetail(String userName) {
        List<Object> idList = new ArrayList<Object>(1);
        idList.add(userName);
        try {
            return (List<ICab2bQuery>) Utility.executeHQL("getUserQueriesDetails", idList);
        } catch (HibernateException e) {
            throw new RuntimeException("Error occured while executing the HQL:" + e.getMessage(), e);
        }
    }

    /**
     * This method returns all the queries created by the given user.
     * @param userName
     * @return
     */
    public List<ICab2bQuery> retrieveAllQueriesByUser(String userName) {
        List<Object> idList = new ArrayList<Object>(1);
        idList.add(userName);
        try {
            return (List<ICab2bQuery>) Utility.executeHQL("getQueriesByUserName", idList);
        } catch (HibernateException e) {
            throw new RuntimeException("Error occured while executing the HQL:" + e.getMessage(), e);
        }
    }

}
