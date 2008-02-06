package edu.wustl.cab2b.server.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

public class ServiceURLOperations extends DefaultBizLogic {

    public Collection<String> getAllApplicationNames() throws RemoteException {
        List<ServiceURL> serviceList = null;
        try {
            serviceList = (List<ServiceURL>) retrieve(ServiceURL.class.getName());
        } catch (DAOException e) {
            throw new RemoteException(e.getMessage());
        }

        Collection<String> applicationNames = new HashSet<String>();
        for (ServiceURL serviceURL : serviceList) {
            applicationNames.add(serviceURL.getEntityGroupName());
        }
        return applicationNames;
    }

}
