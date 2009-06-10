package edu.wustl.cab2b.server.ejb.user;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.ejb.user.UserBusinessInterface;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * This bean handles user based operations
 * 
 * @author hrishikesh_rajpathak
 *
 */
public class UserBean extends AbstractStatelessSessionBean implements UserBusinessInterface {
    private static final long serialVersionUID = -9088505042791608055L;
    
    Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UserBean.class);

    public UserInterface insertUser(String dref) throws RemoteException {
        String userId = getUserIdentifier(dref);
        User user = new User(userId, null, false);
        return new UserOperations().insertUser(user);
    }

    public void updateUser(UserInterface user) throws RemoteException {
        new UserOperations().updateUser(user);
    }

    public Map<String, List<String>> getServiceUrlsForUser(UserInterface user)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, RemoteException {
        return new UserOperations().getServiceURLsForUser(user);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.ejb.user.UserBusinessInterface#getUserByName(java.lang.String)
     * If user not found, it returns null
     */
    public UserInterface getUserByName(String dref) throws RemoteException {
        String userId = getUserIdentifier(dref);
        return new UserOperations().getUserByName(userId);
    }

    public GlobusCredential getGlobusCredential(String dref) throws RemoteException {
        try {
            return UserOperations.getGlobusCredential(dref);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RemoteException(e.getMessage(), e);
        }
    }

    private String getUserIdentifier(String dref) throws RemoteException {
        String userId = null;
        try {
            userId = UserOperations.getGlobusCredential(dref).getIdentity();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RemoteException(e.getMessage(), e);
        }
        return userId;
    }
}
