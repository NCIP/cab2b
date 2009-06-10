package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.server.queryengine.QueryOperations;
import edu.wustl.cab2bwebapp.util.ServerProperties;
import edu.wustl.cab2bwebapp.util.Utility;
import edu.wustl.common.hibernate.HibernateCleanser;

/**
 * This class fetches the saved searches.
 *
 * @author gaurav_mehta
 * @author chetan_patil
 */
public class SavedQueryBizLogic {

    /** All keyword search queries */
    private Collection<ICab2bQuery> keywordQueries = new ArrayList<ICab2bQuery>();

    /** All regular queries */
    private Collection<ICab2bQuery> regularQueries = new ArrayList<ICab2bQuery>();

    private Map<Long, ICab2bQuery> idVsQuery = new HashMap<Long, ICab2bQuery>();

    /**
     * Default Constructor
     * It loads all queries from database which are owned/created by the default user.
     */
    public SavedQueryBizLogic() {
        loadQueries();
    }

    /**
     * This method retrieves all the queries created/owned by the default user and sets them in appropriate collection.
     */
    private void loadQueries() {
        String defaultUser = ServerProperties.getDefaultUser();
        QueryOperations queryOperations = new QueryOperations();

        keywordQueries.addAll(queryOperations.getKeywordQueriesByUserName(defaultUser));
        regularQueries.addAll(queryOperations.getRegularQueriesByUserName(defaultUser));

        for (ICab2bQuery query : regularQueries) {
            idVsQuery.put(query.getId(), query);
        }
    }

    /**
     * This method returns all the regular queries created by the default user.
     * The original queries are cloned as these queries are modified before they can be executed.
     *
     * @return
     */
    public Collection<ICab2bQuery> getRegularQueries() {
        Collection<ICab2bQuery> queries = new HashSet<ICab2bQuery>(regularQueries.size());
        for (ICab2bQuery query : regularQueries) {
            ICab2bQuery newQuery = (ICab2bQuery) DynamicExtensionsUtility.cloneObject(query);
            queries.add(newQuery);
        }
        return queries;
    }


    /**
     * This method returns all the keyword queries created by the default user.
     * The original queries are cloned as these queries are modified before they can be executed.
     *
     * @return
     */
    public Collection<ICab2bQuery> getKeywordQueries() {
        Collection<ICab2bQuery> queries = new HashSet<ICab2bQuery>(keywordQueries.size());
        for (ICab2bQuery query : keywordQueries) {
            ICab2bQuery newQuery = (ICab2bQuery) DynamicExtensionsUtility.cloneObject(query);
            new HibernateCleanser(newQuery).clean();

            queries.add(newQuery);
        }
        return queries;
    }

    /**
     * This method returns all the regular queries created by the default user for the given set of entity groups.
     *
     * @param entityGroups
     * @return
     */
    public Collection<ICab2bQuery> getRegualarQueries(Collection<EntityGroupInterface> entityGroups) {
        Set<ICab2bQuery> allRegualarQueries = new HashSet<ICab2bQuery>(getRegularQueries());

        Set<ICab2bQuery> regualarQueries = new HashSet<ICab2bQuery>();
        for (EntityGroupInterface entityGroup : entityGroups) {
            Iterator<ICab2bQuery> iterator = allRegualarQueries.iterator();
            while (iterator.hasNext()) {
                ICab2bQuery query = iterator.next();

                if (entityGroup.equals(Utility.getEntityGroup(query))) {
                    regualarQueries.add(query);
                    iterator.remove();
                }
            }
        }

        return regualarQueries;
    }

    /**
     * This method returns all the keyword serach queries created by the default user for the given set of
     * entity groups.
     *
     * @param entityGroups
     * @return
     */
    public Collection<ICab2bQuery> getKeywordQueries(Set<EntityGroupInterface> entityGroups) {
        Set<ICab2bQuery> allKeywordQueries = new HashSet<ICab2bQuery>(getKeywordQueries());

        Set<ICab2bQuery> keywordQueries = new HashSet<ICab2bQuery>();
        for (EntityGroupInterface entityGroup : entityGroups) {
            Iterator<ICab2bQuery> iterator = allKeywordQueries.iterator();
            while (iterator.hasNext()) {
                ICab2bQuery query = iterator.next();

                if (entityGroup.equals(Utility.getEntityGroup(query))) {
                    keywordQueries.add(query);
                    iterator.remove();
                }
            }
        }

        return keywordQueries;
    }

    /**
     * This method returns query corresponding to a particular query id
     * @param query id
     * @return query object
     */
    public ICab2bQuery getQueryById(Long id) {
        ICab2bQuery query = idVsQuery.get(id);
        if (query == null) {
            throw new RuntimeException("No query present for id: " + id);
        }
        return query;
    }
}
