package edu.wustl.cab2b.common.ejb.utility;

import java.rmi.RemoteException;
import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.BusinessInterface;

/**
 * @author Chandrakant Talele
 */
public interface UtilityBusinessInterface extends BusinessInterface {
    /**
     * @return
     * @throws RemoteException
     */
    Collection<EntityGroupInterface> getCab2bEntityGroups() throws RemoteException;

    /** 
     * @return associations with given entity as the target entity.
     * @throws RemoteException EJB specific exception.
     */
    public Collection<AssociationInterface> getIncomingIntramodelAssociations(Long entityId)
            throws RemoteException;
}
