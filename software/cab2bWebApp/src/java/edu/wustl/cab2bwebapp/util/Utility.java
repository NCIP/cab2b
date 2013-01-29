/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */

package edu.wustl.cab2bwebapp.util;

import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;

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
    //Moved to edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil
    /*   public static Collection<EntityGroupInterface> getEntityGroups(ICab2bQuery query) {
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
       }*/

    //Moved to edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil
    /*   private static EntityGroupInterface getEntityGroupForCategory(EntityInterface outputEntity) {
           CategoryOperations categoryOperations = new CategoryOperations();
           Category category = categoryOperations.getCategoryByEntityId(outputEntity.getId());
           outputEntity = EntityCache.getInstance().getEntityById(category.getRootClass().getDeEntityId());

           return edu.wustl.cab2b.common.util.Utility.getEntityGroup(outputEntity);
       }*/

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
    /*   public static List<String> getQueryNameList(Collection<ICab2bQuery> queries) {
           List<String> queryNameList = null;
           if (queries != null) {
               queryNameList = new ArrayList<String>();
               for (ICab2bQuery query : queries) {
                   queryNameList.add(query.getName());
               }
           }
           return queryNameList;
       }*/

    /**
     * Method to get all model groups.
     * @param isSecureMode
     * @return String[]
     */
    //Commented as it was not used from anywhere.
    /*   public static String[] getModelGroups(Boolean isSecureMode) {
           ModelGroupOperations modelGroupOperations = new ModelGroupOperations();
           List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
           if (isSecureMode) {
               modelGroups.addAll(modelGroupOperations.getAllModelGroups());
           } else {
               modelGroups.addAll(modelGroupOperations.getAllNonSecuredModelGroups());
           }
           String[] modelGroupNames = new String[modelGroups.size()];
           for (int i = 0; i < modelGroups.size(); i++) {
               modelGroupNames[i] = modelGroups.get(i).getModelGroupName();
           }
           return modelGroupNames;
       }*/

    //Moved to edu.wustl.cab2b.server.queryengine.utils.QueryExecutorUtil
    /*    *//**
         * This method returns the List of url's which are configured by user for that model group/entity group.
         * This method is required to set the url's in query
         * @param user
         * @param entityGroups
         * @return List<String>
         * @throws Exception
         */
    /*
        public static Map<EntityGroupInterface, List<String>> getUserConfiguredUrls(UserInterface user,
                                                                                    String[] modelGroupNames) {
            Map<EntityGroupInterface, List<String>> entityGroupVsSelectedUrls =
                    new HashMap<EntityGroupInterface, List<String>>();

            if (modelGroupNames != null && modelGroupNames.length != 0) {
                Collection<ServiceURLInterface> userConfiguredUrls = user.getServiceURLCollection();

                if (userConfiguredUrls == null || userConfiguredUrls.size() == 0) {
                    if (!user.isAdmin()) {
                        entityGroupVsSelectedUrls =
                                getUserConfiguredUrls(new UserOperations().getAdmin(), modelGroupNames);
                    } else {
                        throw new RuntimeException(Constants.SERVICE_INSTANCES_NOT_CONFIGURED);
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
        }*/

    /**
     * This method verifies whether all queries have services instances set or not.
     * @param queries
     * @param user
     * @param modelGroupNames
     * @return
     */
    //Commented as it was not used from anywhere.
    /*    public static Boolean verifyServiceInstances(Collection<ICab2bQuery> queries, UserInterface user,
                                                     String[] modelGroupNames) {
            Map<EntityGroupInterface, List<String>> entityGroupURLsMap =
                    Utility.getUserConfiguredUrls(user, modelGroupNames);

            Boolean isValid = Boolean.TRUE;
            for (ICab2bQuery query : queries) {
                Collection<EntityGroupInterface> queryEntityGroups = Utility.getEntityGroups(query);
                for (EntityGroupInterface queryEntityGroup : queryEntityGroups) {
                    List<String> urls = entityGroupURLsMap.get(queryEntityGroup);
                    if (urls == null || urls.isEmpty()) {
                        isValid = Boolean.FALSE;
                    }
                }
            }
            return isValid;
        }*/
}