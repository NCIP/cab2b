/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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
import edu.wustl.cab2b.common.queryengine.QueryType;
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
     * This method returns all the regular queries created by the given user.
     *
     * @param userName creator/owner of the queries
     * @return
     */
    public List<ICab2bQuery> getRegularQueriesByUserName(final String userName) {
        return getQueriesByTypeAndUserName(QueryType.ANDed, userName);
    }

    /**
     * This method returns all the keyword search queries created by the given user.
     *
     * @param userName creator/owner of the queries
     * @return
     */
    public List<ICab2bQuery> getKeywordQueriesByUserName(final String userName) {
        return getQueriesByTypeAndUserName(QueryType.ORed, userName);
    }

    /**
     * This method returns all the queries of the given type created by the given user.
     *
     * @param queryType type of the queries to be retrieved
     * @param userName creator/owner of the queries
     * @return
     */
    private List<ICab2bQuery> getQueriesByTypeAndUserName(QueryType queryType, final String userName) {
        List<Object> paramList = new ArrayList<Object>(2);
        paramList.add(queryType.toString());
        paramList.add(userName);

        List<ICab2bQuery> result = null;
        try {
            result = (List<ICab2bQuery>) Utility.executeHQL("getQueriesByTypeAndUserName", paramList);
        } catch (HibernateException e) {
            throw new RuntimeException("Error occured while executing the HQL:" + e.getMessage(), e);
        }
        return result;
    }

}
