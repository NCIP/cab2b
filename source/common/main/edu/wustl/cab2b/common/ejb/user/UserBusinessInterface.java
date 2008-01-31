/**
 * 
 */
package edu.wustl.cab2b.common.ejb.user;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.user.User;

/**
 * @author hrishikesh_rajpathak
 *
 */
public interface UserBusinessInterface extends BusinessInterface  {
	
	User getUserById(Long id) throws RemoteException;
	
	void insertUser(User user) throws RemoteException;
	
	void updateUser(User user) throws RemoteException;
	
	String[] getServiceURLs(String appName) throws RemoteException;
	
	

}
