/**
 *
 */

package edu.wustl.cab2bwebapp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author gaurav_mehta
 *
 */
public class Utility {

    /**
     * This method returns the EntityGroupInterface for given query
     * @param query
     * @return EntityGroupInterfcae object of query
     */
    public static Collection<EntityGroupInterface> getEntityGroups(ICab2bQuery query) {
        Collection<EntityGroupInterface> entityGroups = new HashSet<EntityGroupInterface>();

        EntityInterface outputEntity = query.getOutputEntity();
        if (edu.wustl.cab2b.common.util.Utility.isCategory(outputEntity)) {
            entityGroups.add(getEntityGroupForCategory(outputEntity));
        } else if (edu.wustl.cab2b.common.util.Utility.isMultiModelCategory(outputEntity)) {
            MultiModelCategoryQuery mmcQuery = (MultiModelCategoryQuery) query;
            Collection<ICab2bQuery> subQueries = mmcQuery.getSubQueries();
            for (ICab2bQuery subQuery : subQueries) {
                outputEntity = subQuery.getOutputEntity();
                entityGroups.add(getEntityGroupForCategory(outputEntity));
            }
        } else {
            entityGroups.add(edu.wustl.cab2b.common.util.Utility.getEntityGroup(outputEntity));
        }

        return entityGroups;
    }

    private static EntityGroupInterface getEntityGroupForCategory(EntityInterface outputEntity) {
        CategoryOperations categoryOperations = new CategoryOperations();
        Category category = categoryOperations.getCategoryByEntityId(outputEntity.getId());
        outputEntity = EntityCache.getInstance().getEntityById(category.getRootClass().getDeEntityId());

        return edu.wustl.cab2b.common.util.Utility.getEntityGroup(outputEntity);
    }

    /**
     * This method returns a set of the entity groups that collectively forms one of the the given model groups names.
     *
     * @param modelGroupNames
     * @return
     */
    public static Set<EntityGroupInterface> getEntityGroupsForModelGroups(String[] modelGroupNames) {
        ModelGroupBizLogic modelGroupBizLogic = new ModelGroupBizLogic();
        final Set<EntityGroupInterface> entityGroups = new HashSet<EntityGroupInterface>();
        for (String modelGroupName : modelGroupNames) {
            entityGroups.addAll(modelGroupBizLogic.getEntityGroupsForModel(modelGroupName));
        }
        return entityGroups;
    }

    /**
     * This method returns list of query names.
     * @param queries
     * @return Collection<String>
     */
    public static List<String> getQueryNameList(Collection<ICab2bQuery> queries) {
        List<String> queryNameList = null;
        if (queries != null) {
            queryNameList = new ArrayList<String>();
            for (ICab2bQuery query : queries) {
                queryNameList.add(query.getName());
            }
        }
        return queryNameList;
    }

    /**
     * Method to verify model groups.
     * @param modelGroupNames
     * @param globusCredential
     * @param savedQueryBizLogic
     * @return Collection<ICab2bQuery>
     */
    public static Collection<ICab2bQuery> verifyModelGroups(String[] modelGroupNames,
                                                            GlobusCredential globusCredential,
                                                            SavedQueryBizLogic savedQueryBizLogic) {
        if (modelGroupNames == null || modelGroupNames.length == 0) {
            modelGroupNames = getModelGroups(globusCredential);
        }
        Set<EntityGroupInterface> entityGroups = Utility.getEntityGroupsForModelGroups(modelGroupNames);
        Collection<ICab2bQuery> keywordSearches = savedQueryBizLogic.getKeywordQueries(entityGroups);
        return keywordSearches;
    }

    /**
     * Method to get all model groups.
     * @param globusCredential
     * @return String[]
     */
    public static String[] getModelGroups(GlobusCredential globusCredential) {

        ModelGroupOperations modelGroupOperations = new ModelGroupOperations();
        List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
        if (globusCredential == null) {
            modelGroups.addAll(modelGroupOperations.getAllNonSecuredModelGroups());
        } else {
            modelGroups.addAll(modelGroupOperations.getAllModelGroups());
        }
        String[] modelGroupNames = new String[modelGroups.size()];
        for (int i = 0; i < modelGroups.size(); i++) {
            modelGroupNames[i] = modelGroups.get(i).getModelGroupName();
        }
        return modelGroupNames;
    }

    /**
     * This method returns the List of url's which are configured by user for that model group/entity group.
     * This method is required to set the url's in query
     * @param user
     * @param entityGroups
     * @return List<String>
     * @throws Exception
     */
    public static Map<EntityGroupInterface, List<String>> getUserConfiguredUrls(UserInterface user,
                                                                                String[] modelGroupNames)
            throws Exception {
        Map<EntityGroupInterface, List<String>> entityGroupVsSelectedUrls =
                new HashMap<EntityGroupInterface, List<String>>();

        if (modelGroupNames != null && modelGroupNames.length != 0) {
            Collection<ServiceURLInterface> userConfiguredUrls = user.getServiceURLCollection();

            if (userConfiguredUrls == null || userConfiguredUrls.size() == 0) {
                if (!user.isAdmin()) {
                    entityGroupVsSelectedUrls =
                            getUserConfiguredUrls(new UserOperations().getAdmin(), modelGroupNames);
                } else {
                    throw new Exception(Constants.SERVICE_INSTANCES_NOT_CONFIGURED);
                }
            } else {
                for (ServiceURLInterface serviceUrl : userConfiguredUrls) {
                    String entityGroupName = serviceUrl.getEntityGroupName();

                    EntityGroupInterface entityGroup =
                            EntityCache.getInstance().getEntityGroupByName(entityGroupName);
                    List<String> urls = entityGroupVsSelectedUrls.get(entityGroup);
                    if (urls == null) {
                        urls = new ArrayList<String>();
                        entityGroupVsSelectedUrls.put(entityGroup, urls);
                    }
                    urls.add(serviceUrl.getUrlLocation());
                }
            }
        }
        return entityGroupVsSelectedUrls;
    }
}