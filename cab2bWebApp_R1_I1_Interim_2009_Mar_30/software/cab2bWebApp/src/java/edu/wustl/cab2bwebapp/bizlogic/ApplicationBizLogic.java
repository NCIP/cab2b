/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.bean.ModelBean;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.util.ServerProperties;
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

    /**
     * This method gives list of application loaded in database
     * The list is of type ModelBean which has Display name and Database Id of Application
     * @return List of Applications
     */
    public List<ModelBean> getApplicationNames() {
        Collection<EntityGroupInterface> groups = getEntityGroups();
        List<ModelBean> modelNames = new ArrayList<ModelBean>();
        for (EntityGroupInterface group : groups) {
            if (!Constants.CATEGORY_ENTITY_GROUP_NAME.equals(group.getName())) {
                String displayName = ServerProperties.getDisplayName(group.getName());
                modelNames.add(new ModelBean(group.getId(), displayName));
            }
        }
        return modelNames;
    }

    /**
     * This method also returns service Instances but only User is need to be given as input
     * It takes the first application loaded in database as default
     * @param user
     * @return List of service instances
     */
    public List<ServiceURL> getApplicationInstances(UserInterface user) {
        String entityGroupName = getEntityGroups().iterator().next().getName();
        return getApplicationInstances(user, entityGroupName);
    }

    /**
     * This method returns the list of service instances for given application and user
     * The input must be UserInterface object and EntityGroupName of application
     * @param user
     * @param entityGroupName
     * @return List of service instances
     */
    public List<ServiceURL> getApplicationInstances(UserInterface user, String entityGroupName) {
        String[] entity = entityGroupName.split("_v");
        List<ServiceURL> serviceURLInstanceList = new ServiceURLOperations().getInstancesByServiceName(entity[0],
                                                                                                       entity[1],
                                                                                                       user);
        for (ServiceURL serviceInstance : serviceURLInstanceList) {
            if (serviceInstance.getHostingResearchCenter() == null
                    || serviceInstance.getHostingResearchCenter().isEmpty()) {
                serviceInstance.setHostingCenter(serviceInstance.getUrlLocation());
            }
        }
        serviceURLInstanceList = Utility.sortServiceInstances(serviceURLInstanceList);
        return serviceURLInstanceList;
    }

    private Collection<EntityGroupInterface> getEntityGroups() {
        EntityCache cache = EntityCache.getInstance();
        Collection<EntityGroupInterface> groups = cache.getEntityGroups();
        return groups;
    }

    /**
     * This method returns the entitiy name for given entity ID
     * @param id
     * @return
     */
    public String getEntityName(Long id) {
        EntityCache cache = EntityCache.getInstance();
        Collection<EntityGroupInterface> entityGroupInterface = cache.getEntityGroups();
        for (EntityGroupInterface eg : entityGroupInterface) {
            if (eg.getId().equals(id)) {
                return eg.getName();
            }
        }
        throw new RuntimeException("No Entity present for entity ID:" + id);
    }
}
