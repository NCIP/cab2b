package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.server.queryengine.QueryOperations;
import edu.wustl.cab2bwebapp.util.ServerProperties;
import edu.wustl.cab2bwebapp.util.Utility;

/**
 * @author gaurav_mehta
 * This BIZ logic is for saved searches. It performs operations related to saved Searches
 *
 */
public class SavedQueryBizLogic {

    private Map<Long, ICab2bQuery> idVsSavedQuery = new HashMap<Long, ICab2bQuery>();

    private List<ICab2bQuery> keywordSearchQueries = new ArrayList<ICab2bQuery>();

    private List<ICab2bQuery> normalSearchQueries = new ArrayList<ICab2bQuery>();

    /**
     * Default Constructor;
     * It calls loadQueries function which loads all queries from database 
     */
    public SavedQueryBizLogic() {
        loadQueries();
    }

    private void loadQueries() {
        String defaultUser = ServerProperties.getDefaultUser();
        List<ICab2bQuery> allSavedQueries = new ArrayList<ICab2bQuery>();
        allSavedQueries = new QueryOperations().retrieveAllQueriesByUser(defaultUser);

        if (allSavedQueries.size() != 0) {
            for (ICab2bQuery savedQuery : allSavedQueries) {
                if (savedQuery.isKeywordSearch()) {
                    keywordSearchQueries.add(savedQuery);
                } else {
                    normalSearchQueries.add(savedQuery);
                }
            }
            idVsSavedQuery = new HashMap<Long, ICab2bQuery>(normalSearchQueries.size());
            for (ICab2bQuery savedQuery : normalSearchQueries) {
                idVsSavedQuery.put(savedQuery.getId(), savedQuery);
            }
        }
    }

    /**
     * This method also returns all saved queries for first application loaded in database
     * @return All Saved Queries
     */
    public List<ICab2bQuery> getAllNormalSearchQueries() {
        List<ModelGroupInterface> modelGroup = new ArrayList<ModelGroupInterface>();
        List<ICab2bQuery> savedSearches = new ArrayList<ICab2bQuery>();
        ModelGroupBizLogic modelGroupBizLogic = new ModelGroupBizLogic();

        modelGroup = modelGroupBizLogic.getAllModelGroups();
        if (modelGroup.size() != 0) {
            ModelGroupInterface modelGroupInterface = modelGroup.iterator().next();
            List<EntityGroupInterface> entityGroup = modelGroupBizLogic.getEntityGroupsForModel(modelGroupInterface.getModelGroupName());
            for (EntityGroupInterface entity : entityGroup) {
                savedSearches.addAll(getAllNormalSearchQueries(entity));
            }
            return savedSearches;
        } else {
            return null;
        }
    }

    /**
     * This method returns all saved queries for given entityGroup 
     * @param entityGroup
     * @return All saved queries
     */
    public List<ICab2bQuery> getAllNormalSearchQueries(EntityGroupInterface entityGroup) {
        List<ICab2bQuery> queries = new ArrayList<ICab2bQuery>();

        if (idVsSavedQuery.size() != 0) {
            for (ICab2bQuery query : idVsSavedQuery.values()) {
                EntityGroupInterface eg = Utility.getEntityGroup(query);
                if (entityGroup.equals(eg)) {
                    queries.add(query);
                }
            }
        }
        return queries;
    }
}
