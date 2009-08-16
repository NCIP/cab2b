package edu.wustl.cab2b.server.queryengine.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IRule;

/**
 * @author Deepak
 * @author pallavi_mistry
 */
public class QueryExecutorUtil {
    /**
     * It takes list of root ICRs for a URL and count the resulting number of Total spreadsheet records.
     * On the basis of max limit for total spreadsheet records, it returns 
     * true = if URL is feasible for conversion
     * or false = if its infeasible/or/ if num of records are 0 for failed URLs
     * A->B->D          A is the root categorial class and B,C,F are the child categorial classes for A
     *  ->C->G->H
     *  ->F
     * @param recordList
     * @param maxLimit
     * @return
     */
    public static boolean isURLFeasibleForConversion(List<ICategorialClassRecord> recordList, int maxLimit) {
        if (recordList != null) {
            if (recordList.size() > maxLimit) //if no of Root records exceed the limit, return false 
                return false;

            int finalSpreadSheetCount = 0;
            for (ICategorialClassRecord rootRecord : recordList) {
                int rootRecordCount = 1; // A1, A2,A3,A4
                Map<CategorialClass, List<ICategorialClassRecord>> categorialclassVsRecordsMap =
                        rootRecord.getChildrenCategorialClassRecords();
                for (CategorialClass categorialClass : categorialclassVsRecordsMap.keySet()) //B,C,F
                {
                    int childLeavesCount = 0;
                    for (ICategorialClassRecord categorialChildRecord : categorialclassVsRecordsMap
                        .get(categorialClass)) //B1,B2,C1,C2,F1,F2
                    {
                        childLeavesCount = childLeavesCount + countLeavesForEachChildRecord(categorialChildRecord);
                    }
                    rootRecordCount = rootRecordCount * childLeavesCount;
                }
                finalSpreadSheetCount = finalSpreadSheetCount + rootRecordCount;
            }

            if (finalSpreadSheetCount < maxLimit) {
                return true; // feasible URL good enough to be transformed
            } else {
                return false; // infeasible URL, can't invoke transformer
            }
        } else
            return false; //case of failed URL => num of records= 0, Transformer should not be invoked
    }

    /**
     * This method will take input as a CategoryRecord and just count the number of leaves of the tree.
     * It will return the total leaf count.
     * @param categorialChildRecord
     * @return
     */
    public static int countLeavesForEachChildRecord(ICategorialClassRecord categorialChildRecord)//B1,B2,C1,C2,F1,F2
    {
        int leavesCount = 0;
        Map<CategorialClass, List<ICategorialClassRecord>> categorialclassVsRecordsMap =
                categorialChildRecord.getChildrenCategorialClassRecords();

        if (categorialclassVsRecordsMap.isEmpty()) {
            return 1; //if leaf has come, return 1; D,H,F
        }

        for (CategorialClass categorialClass : categorialclassVsRecordsMap.keySet()) //G
        {
            for (ICategorialClassRecord childRecord : categorialclassVsRecordsMap.get(categorialClass)) {
                leavesCount = leavesCount + countLeavesForEachChildRecord(childRecord); //recursive call
            }
        }
        return leavesCount; //if its not a leaf, while returning in the called stack, return 0 for the interleaved nodes.
    }

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
     * @param mmcQuery
     * @return
     */
    public static Map<String, List<String>> getAttributeNameValuesMap(MultiModelCategoryQuery mmcQuery) {
        Map<String, List<String>> attributeNameValuesMap = new HashMap<String, List<String>>();

        IExpression expression = mmcQuery.getConstraints().getExpression(1);
        try {
            IRule rule = (IRule) expression.getOperand(0);
            for (ICondition condition : rule) {
                attributeNameValuesMap.put(condition.getAttribute().getName(), condition.getValues());
            }
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "The MultiModelCategoryQuery should contain only a single operand of type Rule", e);
        }

        return attributeNameValuesMap;
    }

    /**
     * @param queries
     * @param proxy
     * @param user
     * @param modelGroupNames
     * @throws RuntimeException
     */
    public static void insertURLConditions(Collection<? extends ICab2bQuery> queries, GlobusCredential proxy,
                                           UserInterface user, String[] modelGroupNames) throws RuntimeException {
        Map<EntityGroupInterface, List<String>> entityGroupURLsMap = getUserConfiguredUrls(user, modelGroupNames);
        for (ICab2bQuery query : queries) {
            Collection<EntityGroupInterface> queryEntityGroups = QueryExecutorUtil.getEntityGroups(query);
            for (EntityGroupInterface queryEntityGroup : queryEntityGroups) {
                List<String> urls = entityGroupURLsMap.get(queryEntityGroup);
                if (urls != null && !urls.isEmpty()) {
                    query.setOutputUrls(urls);
                } else if (query.getIsSystemGenerated()) {
                    query.setOutputUrls(new ArrayList<String>(0));
                } else {
                    StringBuffer errorMessage =
                            new StringBuffer("Incorrect service instance configured for query ");
                    errorMessage.append("having model as ").append(queryEntityGroup.getName());
                    throw new RuntimeException(errorMessage.toString(), ErrorCodeConstants.MG_008);
                }
            }
        }
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
    }

}
