package edu.wustl.cab2b.common.experiment;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ExperimentGroupHome extends EJBHome {
	
	/**
     * This method creates the EJB Object.
     * @return The newly created EJB Object.
     */
    public ExperimentGroupRemoteInterface create() throws RemoteException, CreateException;
}
