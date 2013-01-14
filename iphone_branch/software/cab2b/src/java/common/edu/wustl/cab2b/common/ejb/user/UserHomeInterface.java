/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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
