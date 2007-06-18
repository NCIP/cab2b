package edu.wustl.cab2b.server.ejb.utility;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface;
import edu.wustl.cab2b.server.cache.DatalistCache;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;

public class UtilityBean extends AbstractStatelessSessionBean implements SessionBean, UtilityBusinessInterface {

    private static final long serialVersionUID = 1L;

    public Collection<EntityGroupInterface> getCab2bEntityGroups() throws RemoteException {
        return DynamicExtensionUtility.getCab2bEntityGroups();
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
        super.setSessionContext(sessionContext);
        // TODO probably belongs in super;
        DatalistCache.getInstance();
    }
    /** 
     * @return associations with given entity as the target entity.
     * @throws RemoteException EJB specific exception.
     * @see edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface#getIncomingIntramodelAssociations(Long)
     */
    public Collection<AssociationInterface> getIncomingIntramodelAssociations(Long entityId)
            throws RemoteException {
        Collection<AssociationInterface> associations = DynamicExtensionUtility.getIncomingIntramodelAssociations(entityId);
        return associations;
    }

}
