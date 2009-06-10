/**
 * 
 */
package edu.wustl.cab2b.common.ejb.user;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * @author hrishikesh_rajpathak
 * 
 */
public interface UserBusinessInterface extends BusinessInterface {

    /**
     * Insert given user as a new user in database
     * 
     * @param user
     * @throws RemoteException
     */
    UserInterface insertUser(String dref, String idP) throws RemoteException;

    /**
     * Update present user in database
     * 
     * @param user
     * @throws RemoteException
     */
    void updateUser(UserInterface user) throws RemoteException;

    /**
     * Get a map of entity group name vs related list of service urls for the given user
     * 
     * @param user
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     * @throws RemoteException
     */
    Map<String, List<String>> getServiceUrlsForUser(UserInterface user) throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException, RemoteException;

    /**
     * Get user by user name. If user of that name not found in database, it returns null.
     * 
     * @param user
     * @return
     * @throws RemoteException
     */
    UserInterface getUserByName(String user, String idP) throws RemoteException;

    /**
     * Returns globus credential
     * @param dref
     * @param idP
     * @return
     * @throws RemoteException
     */
    GlobusCredential getGlobusCredential(String dref, String idP) throws RemoteException;
    
    /**
     * 
     * @return Anonymous User
     * @throws RemoteException
     */
    UserInterface getAnonymousUser()throws RemoteException;
    

}
