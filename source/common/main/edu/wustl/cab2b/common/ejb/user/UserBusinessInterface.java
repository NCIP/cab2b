/**
 * 
 */
package edu.wustl.cab2b.common.ejb.user;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * @author hrishikesh_rajpathak
 * 
 */
public interface UserBusinessInterface extends BusinessInterface {

	void insertUser(User user) throws RemoteException;

	void updateUser(User user) throws RemoteException;

	Map<String, List<String>> getServiceUrlsForUser(UserInterface user) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, RemoteException;

	UserInterface getUserByName(String user) throws RemoteException;
}
