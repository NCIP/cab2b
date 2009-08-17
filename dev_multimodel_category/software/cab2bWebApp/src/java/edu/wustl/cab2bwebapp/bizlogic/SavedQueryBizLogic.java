package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.server.queryengine.QueryOperations;
import edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil;
import edu.wustl.cab2b.server.util.ServerProperties;
import edu.wustl.cab2bwebapp.util.Utility;

/**
 * This class fetches the saved searches.
 *
 * @author gaurav_mehta
 * @author chetan_patil
 * @author pallavi_mistry
 */
public class SavedQueryBizLogic {

    /** All keyword search queries:  map of modelgroupName and corresponding keyword query id */
    private Map<String, Long> modelVsKeywordQueryId = new HashMap<String, Long>();

    /** MAp of id vs its query    */
    private Map<Long, ICab2bQuery> idVsQuery = new HashMap<Long, ICab2bQuery>();

    /** All regular (ANDed) queries */
    private List<ICab2bQuery> regularQueries = new ArrayList<ICab2bQuery>();

    /**
     * Default Constructor
     * It loads all queries from database which are owned/created by the default user.
     */
    public SavedQueryBizLogic() {
        loadQueries();
    }

    /**
     * This method retrieves all the queries created/owned by the default user and sets them in 
     * appropriate collection.
     */
    private void loadQueries() {
        String defaultUser = ServerProperties.getDefaultUser();
        QueryOperations queryOperations = new QueryOperations();

        // putting all the Keyword queries id and the model group Name in the modelVsKeywordQueryId map
        List<KeywordQuery> keywordQueries = queryOperations.getKeywordQueriesByUserName(defaultUser);
        for (KeywordQuery keywordQuery : keywordQueries) {
            this.modelVsKeywordQueryId.put(keywordQuery.getApplicationGroup().getModelGroupName(), keywordQuery
                .getId());
        }
        //putting all the Keyword queries (ORed) in the idvsquery map
        for (ICab2bQuery query : keywordQueries) {
            query.getOutputUrls().clear();
            idVsQuery.put(query.getId(), query);
        }

        this.regularQueries = queryOperations.getRegularQueriesByUserName(defaultUser);
        // putting all the regular queries (ANDed) in the idvsquery map
        for (ICab2bQuery query : regularQueries) {
            query.getOutputUrls().clear();
            idVsQuery.put(query.getId(), query);
        }
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
        return (ICab2bQuery) DynamicExtensionsUtility.cloneObject(query);
    }

    /**
     * This method returns all the regular queries created by the default user.
     * @return
     */
    private List<ICab2bQuery> getRegularQueries() {
        return regularQueries;
    }

    /**
     * This method returns the corresponding keyword query id for the given ModelGroupName
     * @param ModelGroupName
     * @return
     */
    public Long getKeywordQueryId(String ModelGroupName) {
        return modelVsKeywordQueryId.get(ModelGroupName);
    }

    /**
     * This method returns the corresponding keyword queries for the given Model group names.
     * @param modelGroupNames
     * @return
     */
    public List<KeywordQuery> getKeywordQueries(String[] modelGroupNames) {
        List<KeywordQuery> keywordQueriesFor = new ArrayList<KeywordQuery>();
        for (String modelGroupName : modelGroupNames) {
            Set<Entry<String, Long>> modelVsKeywordQueryIdEntries = modelVsKeywordQueryId.entrySet();
            for (Entry<String, Long> modelVsKeywordQueryIdEntry : modelVsKeywordQueryIdEntries) {
                if (modelGroupName.equals(modelVsKeywordQueryIdEntry.getKey())) {
                    keywordQueriesFor.add((KeywordQuery) idVsQuery.get(modelVsKeywordQueryIdEntry.getValue()));
                }
            }
        }
        return keywordQueriesFor;
    }

    /**
     * This method returns all the regular queries created by the default user for the given set of 
     * entity groups.
     *
     * @param entityGroups
     * @return
     */
    public List<ICab2bQuery> getRegularQueries(Collection<EntityGroupInterface> entityGroups) {
        List<ICab2bQuery> regularQueriesFor = new ArrayList<ICab2bQuery>();

        for (ICab2bQuery query : getRegularQueries()) {
            Collection<EntityGroupInterface> queryEntityGroups = QueryExecutorUtil.getEntityGroups(query);
            if (entityGroups.containsAll(queryEntityGroups)) {
                regularQueriesFor.add(query);
            }
        }
        return regularQueriesFor;
    }
}
