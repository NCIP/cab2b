/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.bizlogic;

import java.util.List;

import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.Query;

// TODO parameter is inappropriate
/**
 * This class processes the Query object before persisting and after retreival.
 * Note that this class assumes that {@link AbstractEntityCache} has already
 * been initialized.
 * 
 * @author chetan_patil
 * @created Aug 30, 2007, 11:31:45 AM
 * @param
 *          <Q>
 */
public class QueryBizLogic<Q extends IParameterizedQuery> {

    /**
     * Default Constructor
     */
    public QueryBizLogic() {

    }

    /**
     * This method persists the Query object
     * 
     * @param query @
     */
    public final void saveQuery(Q query) {
        dbOperations().insert(query);
    }

    /**
     * This method persists the Query object
     * 
     * @param query @
     */
    public final void updateQuery(Q query) {
        dbOperations().update(query);
    }

    /**
     * This method retrieves the query object given its identifier.
     * 
     * @param categoryId Id of the category
     * @return The Category for given id. @ EBJ specific Exception
     */
    public Q getQueryById(Long queryId) {
        List<Q> queryList = dbOperations().retrieve(Query.class.getName(), "id", queryId);

        Q query = null;
        if (queryList != null && !queryList.isEmpty()) {
            if (queryList.size() > 1) {
                throw new RuntimeException("Problem in code; probably database schema");
            } else {
                query = (Q) queryList.get(0);
            }
        }
        return query;
    }

    /**
     * This method retrieves all the query objects in the system. Returns all
     * the categories available in the system.
     * 
     * @return List of all categories.
     */
    public List<Q> getAllQueries() {
        List<Q> queryList = dbOperations().retrieve(Query.class.getName());
        return queryList;
    }

    public void delete(Q query) {
        dbOperations().delete(query);
    }

    private HibernateDatabaseOperations<Q> dbOperations() {
        // TODO probably new session is OK...
        return new HibernateDatabaseOperations<Q>(HibernateUtil.currentSession());
    }
}
