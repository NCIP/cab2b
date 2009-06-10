/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * Bizlogic for updating service instance.    
 * @author deepak_shingan
 *
 */
public class SaveServiceInstancesBizLogic {

    /**
     * Updating selected service instances in User object as configured.
     * If User==ANONYMOUS do not persist in database else for valid user persist changes.    
     * @param user
     * @param userSelectedURLs
     * @param serviceInstances
     */
    public UserInterface updateServiceInstanceSettings(UserInterface user, List<String> userSelectedURLs,
                                                       List<ServiceURLInterface> serviceInstances,
                                                       String modelGroupName) {

        //Preparing list of only those urls that user has configured through UI
        List<ServiceURLInterface> selectedServiceURLs = new ArrayList<ServiceURLInterface>();
        for (ServiceURLInterface serviceURL : serviceInstances) {
            if (userSelectedURLs.contains(serviceURL.getUrlLocation())) {
                serviceURL.setConfigured(true);
                selectedServiceURLs.add(serviceURL);
            }
        }

        ServiceURLOperations serviceOperations = new ServiceURLOperations();
        List<EntityGroupInterface> entityGroupInterfaceList = new ModelGroupBizLogic().getEntityGroupsForModel(modelGroupName);

        for (EntityGroupInterface entityGroupInterface : entityGroupInterfaceList) {
            user = serviceOperations.saveServiceInstances(entityGroupInterface.getName(), selectedServiceURLs,
                                                          user);
        }

        //IF user is not anonymous then only save the settings
        if (!user.getUserName().equals(Constants.ANONYMOUS)) {
            try {
                updateUser(user);
            } catch (RemoteException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return user;
    }

    /**
     * Method to update User object with new configured URLS  
     * @param user
     * @return
     * @throws RemoteException
     */
    private UserInterface updateUser(UserInterface user) throws RemoteException {
        if (user.getUserId() != null)
            (new UserOperations()).updateUser(user);
        return user;
    }
}
