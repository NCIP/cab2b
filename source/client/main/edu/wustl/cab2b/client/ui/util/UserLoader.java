/**
 * 
 */
package edu.wustl.cab2b.client.ui.util;

import java.rmi.RemoteException;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.user.UserBusinessInterface;
import edu.wustl.cab2b.common.ejb.user.UserHomeInterface;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.util.Utility;

/**
 * @author hrishikesh_rajpathak
 * 
 */
public class UserLoader {

	/**
	 * Returns all the URLs of the data services which are exposing given entity
	 * 
	 * @param entity
	 *            Entity to check
	 * @return Returns the List of URLs
	 */
	public static String[] getServiceURLS(EntityInterface entity) {
		EntityGroupInterface eg = Utility.getEntityGroup(entity);
		String longName = eg.getLongName();

		UserBusinessInterface userBusinessInterface = (UserBusinessInterface) CommonUtils
				.getBusinessInterface(EjbNamesConstants.USER_BEAN, UserHomeInterface.class, null);
		String[] serviceUrls = null;
		try {
			serviceUrls = userBusinessInterface.getServiceURLs(longName);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serviceUrls;
	}

	/**
	 * @param id
	 * @return
	 */
	public static User getUser(Long id) {
		UserBusinessInterface userBusinessInterface = (UserBusinessInterface) CommonUtils
				.getBusinessInterface(EjbNamesConstants.USER_BEAN, UserHomeInterface.class, null);
		User user = null;
		try {
			user = userBusinessInterface.getUserById(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return user;
	}
}
