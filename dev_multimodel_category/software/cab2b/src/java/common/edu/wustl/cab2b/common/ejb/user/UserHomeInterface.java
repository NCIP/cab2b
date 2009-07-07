/**
 * 
 */
package edu.wustl.cab2b.common.ejb.user;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author hrishikesh_rajpathak
 * 
 */
public interface UserHomeInterface extends EJBHome {
	public UserRemoteInterface create() throws RemoteException, CreateException;
}
