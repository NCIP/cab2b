/**
 * 
 */
package edu.wustl.cab2bwebapp.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
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
        EntityInterface outputEntity = getOutputEntity(query);
        EntityGroupInterface eg = edu.wustl.cab2b.common.util.Utility.getEntityGroup(outputEntity);
        return eg;
    }

    /**
     * This method returns the EntityInterface for given query
     * @param query
     * @return EntityInterfcae object of query
     */
    private static EntityInterface getOutputEntity(ICab2bQuery query) {
        EntityInterface outputEntity = query.getOutputEntity();
        if (edu.wustl.cab2b.common.util.Utility.isCategory(outputEntity)) {
            CategoryOperations categoryOperations = new CategoryOperations();
            Category category = categoryOperations.getCategoryByEntityId(outputEntity.getId());
            outputEntity = EntityCache.getInstance().getEntityById(category.getRootClass().getDeEntityId());
        }
        return outputEntity;
    }

    public static List<ServiceURLInterface> sortServiceInstances(List<ServiceURLInterface> serviceInstances) {
        Collections.sort(serviceInstances,new ServiceNameSorter());
        Collections.sort(serviceInstances, new ServiceSorter());
        return serviceInstances;
    }

}


class ServiceNameSorter implements Comparator<ServiceURLInterface> {
    
    public int compare(ServiceURLInterface obj1, ServiceURLInterface obj2) {
        int returnValue = 0;
        
        if (obj1.getHostingCenter().compareTo(obj2.getHostingCenter()) < 0)  {
            returnValue = -1;
        } else if (obj1.getHostingCenter().compareTo(obj2.getHostingCenter()) > 0) {
            returnValue = 1;
        } else if (obj1.getHostingCenter().compareTo(obj2.getHostingCenter()) == 0) {
            returnValue = 0;
        }
        return returnValue;
    }
}

class ServiceSorter implements Comparator<ServiceURLInterface> {

    public int compare(ServiceURLInterface obj1, ServiceURLInterface obj2) {

        if (obj1.isConfigured() && obj2.isConfigured()) {
            return obj1.getUrlLocation().compareTo(obj2.getUrlLocation());
        } else if (obj1.isConfigured() && !obj2.isConfigured()) {
            return -1;
        } else if (!obj1.isConfigured() && obj2.isConfigured()) {
            return 1;
        }
        return 0;
    }
}
