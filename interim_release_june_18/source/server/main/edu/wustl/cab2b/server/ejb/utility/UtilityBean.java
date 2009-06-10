package edu.wustl.cab2b.server.ejb.utility;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.SessionBean;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;

public class UtilityBean extends AbstractStatelessSessionBean implements SessionBean, UtilityBusinessInterface {

    private static final long serialVersionUID = 1L;

    public Collection<EntityGroupInterface> getCab2bEntityGroups() throws RemoteException {
        return DynamicExtensionUtility.getCab2bEntityGroups();
    }
}
