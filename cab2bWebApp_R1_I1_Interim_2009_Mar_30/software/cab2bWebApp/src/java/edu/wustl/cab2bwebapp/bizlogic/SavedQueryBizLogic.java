package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.queryengine.QueryOperations;
import edu.wustl.cab2bwebapp.util.ServerProperties;
import edu.wustl.cab2bwebapp.util.Utility;

/**
 * @author gaurav_mehta
 * This BIZ logic is for saved searches. It performs operations related to saved Searches
 *
 */
public class SavedQueryBizLogic {

    private Map<Long, ICab2bQuery> idVsSavedQuery = null;

    /**
     * Default Constructor;
     * It calls loadQueries function which loads all queries from database 
     */
    public SavedQueryBizLogic() {
        loadQueries();
    }

    private void loadQueries() {
        String defaultUser = ServerProperties.getDefaultUser();
        List<ICab2bQuery> savedQueries = new QueryOperations().retrieveAllQueriesByUser(defaultUser);

        idVsSavedQuery = new HashMap<Long, ICab2bQuery>(savedQueries.size());
        for (ICab2bQuery savedQuery : savedQueries) {
            idVsSavedQuery.put(savedQuery.getId(), savedQuery);
        }
    }

    /**
     * This method also returns all saved queries for first application loaded in database
     * @return All Saved Queries
     */
    public List<ICab2bQuery> getAllQueries() {
        Collection<EntityGroupInterface> entityGroup = EntityCache.getInstance().getEntityGroups();
        return getAllQueries(entityGroup.iterator().next());
    }

    /**
     * This method returns all saved queries for given entityGroup 
     * @param entityGroup
     * @return All saved queries
     */
    public List<ICab2bQuery> getAllQueries(EntityGroupInterface entityGroup) {
        List<ICab2bQuery> queries = new ArrayList<ICab2bQuery>();

        for (ICab2bQuery query : idVsSavedQuery.values()) {
            EntityGroupInterface eg = Utility.getEntityGroup(query);
            if (entityGroup.equals(eg)) {
                queries.add(query);
            }
        }
        return queries;
    }
}
