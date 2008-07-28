package edu.wustl.cab2b.server.ejb.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;

import edu.wustl.cab2b.common.ejb.serviceurl.ServiceURLBusinessInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;

public class ServiceURLBean implements ServiceURLBusinessInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Collection<String> getAllApplicationNames() throws RemoteException {
        return new ServiceURLOperations().getAllApplicationNames();
    }

}
