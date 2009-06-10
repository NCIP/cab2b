package edu.wustl.cab2b.common.ejb.utility;

import java.rmi.RemoteException;
import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.BusinessInterface;

/**
 * @author Chandrakant Talele
 */
public interface UtilityBusinessInterface extends BusinessInterface {
    Collection<EntityGroupInterface> getCab2bEntityGroups() throws RemoteException;
}
