/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.util.ServiceInstanceComparator;
import edu.wustl.cab2bwebapp.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gaurav_mehta
 * This is the Biz logic for loading application related things
 * It also gives Service Instances for given Application
 *
 */
public class ApplicationBizLogic {

    org.apache.log4j.Logger logger = Logger.getLogger(ApplicationBizLogic.class);

    private Collection<EntityGroupInterface> entityGroupCollection = new ArrayList<EntityGroupInterface>();;

    /**
     * This is the default constructor which initializes the EntityGroupCollection with all EntityGroup 
     * present in database. 
     */
    public ApplicationBizLogic() {
    }

    /**
     * This method also returns service Instances but only User is need to be given as input
     * It takes the first application loaded in database as default
     * @param user
     * @return List of service instances
     */
    public List<ServiceURLInterface> getApplicationInstances(UserInterface user) {
        if (entityGroupCollection.size() != 0) {
            String entityGroupName = entityGroupCollection.iterator().next().getName();
            return getApplicationInstances(user, entityGroupName);
        } else
            return getApplicationInstances(user, "");
    }

    /**
     * This method returns the list of service instances for given application and user
     * The input must be UserInterface object and EntityGroupName of application
     * @param user
     * @param modelGroupName
     * @return List of service instances
     */
    public List<ServiceURLInterface> getApplicationInstances(UserInterface user, String modelGroupName) {
        List<EntityGroupInterface> entityGrpList =
                new ModelGroupBizLogic().getEntityGroupsForModel(modelGroupName);
        List<ServiceURLInterface> serviceURLInstanceList = new ArrayList<ServiceURLInterface>();
        for (EntityGroupInterface entityGrp : entityGrpList) {
            String[] entity = entityGrp.getName().split("_v");
            List<ServiceURLInterface> serviceURLList =
                    new ServiceURLOperations().getInstancesByServiceName(entity[0], entity[1], user);
            for (ServiceURLInterface serviceInstance : serviceURLList) {
                if (serviceInstance.getHostingCenter() == null || serviceInstance.getHostingCenter().isEmpty()) {
                    serviceInstance.setHostingCenter(serviceInstance.getUrlLocation());
                }
            }
            serviceURLInstanceList.addAll(serviceURLList);
        }
        Collections.sort(serviceURLInstanceList, new ServiceInstanceComparator());
        return serviceURLInstanceList;
    }

    /**
     * @return Collection<EntityGroupInterface>
     */
    public Collection<EntityGroupInterface> getEntityGroups() {
        EntityCache cache = EntityCache.getInstance();
        Collection<EntityGroupInterface> groups = cache.getEntityGroups();
        if (groups.size() != 0) {
            for (EntityGroupInterface group : groups) {
                if (!Constants.CATEGORY_ENTITY_GROUP_NAME.equals(group.getName())) {
                    entityGroupCollection.add(group);
                }
            }
        }
        return entityGroupCollection;
    }
}
