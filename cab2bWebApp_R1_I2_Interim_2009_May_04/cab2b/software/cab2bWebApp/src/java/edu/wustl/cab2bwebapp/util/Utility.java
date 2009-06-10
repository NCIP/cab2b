/**
 *
 */
package edu.wustl.cab2bwebapp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
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
    public static EntityGroupInterface getEntityGroup(ICab2bQuery query) {
        EntityInterface outputEntity = query.getOutputEntity();
        if (edu.wustl.cab2b.common.util.Utility.isCategory(outputEntity)) {
            CategoryOperations categoryOperations = new CategoryOperations();
            Category category = categoryOperations.getCategoryByEntityId(outputEntity.getId());
            outputEntity = EntityCache.getInstance().getEntityById(category.getRootClass().getDeEntityId());
        }

        return edu.wustl.cab2b.common.util.Utility.getEntityGroup(outputEntity);
    }

    /**
     * This method sorts Service Instances based on 2 parameters.
     * 1. On Hosting Center Name
     * 2. Whether it is configured or not
     * @param serviceInstances
     * @return
     */
    public static List<ServiceURLInterface> sortServiceInstances(List<ServiceURLInterface> serviceInstances) {
        Collections.sort(serviceInstances, new ServiceNameSorter());
        Collections.sort(serviceInstances, new ServiceSorter());
        return serviceInstances;
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

}

/**
 * @author gaurav_mehta
 * This class sorts the Service instances based on HostingCenter Name
 */
class ServiceNameSorter implements Comparator<ServiceURLInterface> {

    /**
     * @param obj1
     * @param obj2
     * @return value depending on whether the 2 values are smaller or greater
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(ServiceURLInterface obj1, ServiceURLInterface obj2) {
        return obj1.getHostingCenter().compareTo(obj2.getHostingCenter());
    }

}

/**
 * @author gaurav_mehta
 * This class sorts the Service instances based on whether it is configured or not and if both are configured then on URL Location
 */
class ServiceSorter implements Comparator<ServiceURLInterface> {

    /**
     * @param obj1
     * @param obj2
     * @return value depending on whether the 2 values are smaller or greater
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(ServiceURLInterface obj1, ServiceURLInterface obj2) {

        if (obj1.isConfigured() && obj2.isConfigured()) {
            return obj1.getHostingCenter().compareTo(obj2.getHostingCenter());
        } else if (obj1.isConfigured() && !obj2.isConfigured()) {
            return -1;
        } else if (!obj1.isConfigured() && obj2.isConfigured()) {
            return 1;
        }
        return 0;
    }

}
